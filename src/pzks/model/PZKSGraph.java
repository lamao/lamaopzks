package pzks.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

import pzks.model.listeners.PZKSModelListener;

/**
 * This is the model class and implements graph with nodes and connections. This
 * graph can be oriented (task graph) or not oriented (system graph). This
 * option is setted via <code>_taskGraph</code> ivar.
 * 
 * @author lamao
 * 
 */
public class PZKSGraph implements Serializable, PZKSModelListener
{
	private static final long serialVersionUID = 1L;

	// ***************************** ivars ***********************************
	private ArrayList<PZKSNode> _nodes = null;
	private ArrayList<PZKSConnection> _connections = null;
	private boolean _taskGraph = false;
	private int _maxNumber = 0;
	private transient PZKSGraphValidator _validator = null;
	private transient EventListenerList _listeners = null;

	// ************************* initializers

	public PZKSGraph(boolean isTaskGraph)
	{
		_taskGraph = isTaskGraph;
	};

	// ********************** accessors
	protected ArrayList<PZKSNode> getMutableNodes()
	{
		if (_nodes == null)
			_nodes = new ArrayList<PZKSNode>();
		return _nodes;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<PZKSNode> getNodes()
	{
		return (ArrayList<PZKSNode>) getMutableNodes().clone();
	}

	public Iterator<PZKSNode> getNodesIterator()
	{
		return getMutableNodes().iterator();
	}

	public boolean isEmpty()
	{
		return getMutableNodes().isEmpty();
	}

	public int getNumberOfNodes()
	{
		return getMutableNodes().size();
	}
	
	public int getTotalWeightOfNodes()
	{
		int result = 0;
		for (PZKSNode node : getMutableNodes())
		{
			result += node.getWeight();
		}
		return result;
	}

	protected ArrayList<PZKSConnection> getMutableConnections()
	{
		if (_connections == null)
			_connections = new ArrayList<PZKSConnection>();
		return _connections;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<PZKSConnection> getConnections()
	{
		return (ArrayList<PZKSConnection>) getMutableConnections().clone();
	}

	public Iterator<PZKSConnection> getConnectionsIterator()
	{
		return getMutableConnections().iterator();
	}

	public boolean isTaskGraph()
	{
		return _taskGraph;
	}

	public void setTaskGraph(boolean value)
	{
		_taskGraph = value;
	}

	private int getMaxNumber()
	{
		return _maxNumber;
	}

	private void setMaxNumber(int number)
	{
		_maxNumber = number;
	};

	public void setGraphValidator(PZKSGraphValidator validator)
	{
		_validator = validator;
	}

	public PZKSGraphValidator getGraphValidator()
	{
		return _validator;
	}

	protected EventListenerList getMutableListeners()
	{
		if (_listeners == null)
		{
			_listeners = new EventListenerList();
		}
		return _listeners;
	}

	// ********************* other methods
	/**
	 * Corrects number of nodes so that first node in arraylist will have number
	 * 0, second - 1, etc.
	 */
	public void correctNumbersOfNodes()
	{
		int i = 0;
		for (PZKSNode node : getMutableNodes())
		{
			node.setNumber(i++);
		}
		setMaxNumber(i);
	}

	public void addNode(PZKSNode node)
	{
		node.setNumber(getMaxNumber());
		setMaxNumber(getMaxNumber() + 1);

		getMutableNodes().add(node);
		// node.addObserver(this);
		node.addModelListner(this);

		fireModelChanged(new ChangeEvent(this));
	}

	public void reconfigureListeners()
	{
		Iterator<PZKSNode> iterator = getNodesIterator();
		while (iterator.hasNext())
		{
			// it.next().addObserver(this);
			iterator.next().addModelListner(this);
		}
	}

	// TODO Think about easer implementation
	public void removeNode(PZKSNode node)
	{
		// node.deleteObserver(this);
		node.removeModelListener(this);

		if (node.getNumber() == getMaxNumber())
		{
			setMaxNumber(getMaxNumber() - 1);
		}
		getMutableNodes().remove(node);

		// remove all node's connections
		int i = 0;
		boolean mustBeDeleted = false;
		while (i < getMutableConnections().size())
		{
			PZKSConnection currConnection = getMutableConnections().get(i);
			if (currConnection.getSrcNode() == node)
			{
				node.removeConnection(currConnection);
				currConnection.getDstNode().removeConnection(currConnection);
				mustBeDeleted = true;
			}
			else if (currConnection.getDstNode() == node)
			{
				if (!isTaskGraph())
				{
					node.removeConnection(currConnection);
				}
				currConnection.getSrcNode().removeConnection(currConnection);
				mustBeDeleted = true;
			}

			if (mustBeDeleted)
			{
				_connections.remove(currConnection);
				mustBeDeleted = false;
				i--;
			}
			i++;
		}

		correctNumbersOfNodes();
		fireModelChanged(new ChangeEvent(this));
	}

	public void removeNodeAtPoint(Point point)
	{
		PZKSNode node = getNodeAtPoint(point);

		if (node != null)
		{
			getMutableNodes().remove(node);
		}
	}

	public PZKSNode getNodeAtPoint(Point point)
	{
		int i = 0;
		PZKSNode result = null;

		while (i < getMutableNodes().size()
				&& !getMutableNodes().get(i).hasPoint(point))
		{
			i++;
		}

		if (i < getMutableNodes().size())
			result = getMutableNodes().get(i);

		return result;
	}

	
	public void addConnection(PZKSNode srcNode, PZKSNode dstNode, int weight)
	{
		assert (srcNode != null);
		assert (dstNode != null);

		if (!areAlreadyConnected(srcNode, dstNode))
		{
			// create new connection
			PZKSConnection newConnection = new PZKSConnection(srcNode, dstNode,
					weight);
			getMutableConnections().add(newConnection);

			// add connection
			srcNode.addConnection(newConnection);

			// notify observers
			fireModelChanged(new ChangeEvent(this));

			dstNode.addConnection(newConnection);
		}
	}

	public void addConnection(PZKSNode srcNode, PZKSNode dstNode)
	{
		addConnection(srcNode, dstNode, 0);
	}

	public void removeConnection(PZKSConnection connection)
	{
		assert (connection != null);

		// remove connection in nodes
		connection.getSrcNode().removeConnection(connection);
		connection.getDstNode().removeConnection(connection);

		// remove connection itself
		getMutableConnections().remove(connection);

		fireModelChanged(new ChangeEvent(this));
	}

	public PZKSConnection getConnection(PZKSNode node1, PZKSNode node2)
	{
		return node1.getConnectionTo(node2);
	}

	public PZKSConnection getConnection(Point point)
	{
		Iterator<PZKSConnection> iterator = getConnectionsIterator();
		PZKSConnection connection = null;

		while (iterator.hasNext())
		{
			connection = iterator.next();
			if (connection.hasPoint(point))
			{
				break;
			}
			else
			{
				connection = null;
			}
		}

		return connection;
	}

	public boolean areAlreadyConnected(PZKSNode srcNode, PZKSNode dstNode)
	{
		boolean result = false;

		// check if such connection already presents
		Iterator<PZKSConnection> iterator = getConnectionsIterator();
		while (iterator.hasNext())
		{
			PZKSConnection currConnection = iterator.next();
			if (currConnection.hasNodes(srcNode, dstNode))
			{
				result = true;
				break;
			}
		}

		return result;
	}

	public void addModelListener(PZKSModelListener listener)
	{
		getMutableListeners().add(PZKSModelListener.class, listener);
	}

	public void removeModelListener(PZKSModelListener listener)
	{
		getMutableListeners().remove(PZKSModelListener.class, listener);
	}

	private void fireModelChanged(ChangeEvent event)
	{
		PZKSModelListener[] listenerArray = getMutableListeners().getListeners(
				PZKSModelListener.class);
		for (int i = 0; i < listenerArray.length; i++)
		{
			listenerArray[i].changeState(event);
		}
	}

	public void changeState(ChangeEvent event)
	{
		fireModelChanged(event);
	}
}
