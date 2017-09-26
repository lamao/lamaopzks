package pzks.model;

import java.awt.Point; 
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

import pzks.model.listeners.PZKSModelListener;

/**
 * This class is the model class. It represents a single node of the graph
 * It can be serialized an it notifies about any internal changes. It has
 * location, weight, number and list of connections.
 * 
 * @author lamao
 * @see PZKSConnection
 * @see PZKSGraph
 *
 */
public class PZKSNode implements Serializable
{
	//------------------------------- constants ------------------------------- 
	public final static int sSize = 50;
	private static final long serialVersionUID = 1L;
	
	//-------------------------------- ivars ----------------------------------
	private Point _location;
	private int _weight;
	private ArrayList<PZKSConnection> _connections = null;
	private int	_number;
	private transient EventListenerList _listeners = null;
	private boolean _free = true;
	
	
	//--------------------------------- constructors --------------------------
	public PZKSNode(Point aLocation, int aNumber, int aWeight)
	{
		_location = aLocation;
		_number = aNumber;
		_weight = aWeight;
	}
	
	public PZKSNode()
	{
		this(new Point(0, 0), 0, 1);
	}
	
	//---------------------------------- accessors ----------------------------
	public Point getLocation() {return _location;}
	public void setLocation(Point aLocation) 
	{
		if (!_location.equals(aLocation))
		{
			_location = aLocation;
			
			fireModelChanged(new ChangeEvent(this));
		}
	}
	
	public int getNumber() {return _number;}
	public void setNumber(int aNumber)
	{
		if (_number != aNumber)
		{
			_number = aNumber;
			
			fireModelChanged(new ChangeEvent(this));			
		}
	}
	
	public int getWeight() {return _weight;}
	public void setWeight(int aWeight) 
	{
		if (_weight != aWeight)
		{
			_weight = aWeight;
			
			fireModelChanged(new ChangeEvent(this));
		}
	}
	
	public boolean isFree() {return _free;}
	public void setFree(boolean value) {_free = value;}
	
	protected ArrayList<PZKSConnection> getMutableConnections()
	{
		if (_connections == null)
		{
			_connections = new ArrayList<PZKSConnection>();
		}
		return _connections;
	}
	
	public ArrayList<PZKSConnection> getConnections()
	{
		return getMutableConnections();
	}
	
	public PZKSConnection getConnectionTo(PZKSNode oppositeNode)
	{
		PZKSConnection result = null;

		for (PZKSConnection connection : getMutableConnections())
		{
			if (connection.getOppositeNode(this) == oppositeNode)
			{
				result = connection;
				break;
			}
		}

		return result;	
	}
	
	public int getLongestTransmission()
	{
		int result = 0;
		for (PZKSConnection connection : getMutableConnections())
		{
			if (connection.getDstNode() == this)
			{
				result = Math.max(result, connection.getWeight());
			}
		}
		
		return result;
	}
	
	public ArrayList<PZKSConnection> getOutgoingConnections()
	{
		ArrayList<PZKSConnection> result = new ArrayList<PZKSConnection>();
		for (PZKSConnection connection : getMutableConnections())
		{
			if (connection.getSrcNode() == this)
			{
				result.add(connection);
			}
		}
		return result;
	}
	
	public ArrayList<PZKSConnection> getIncomingConnections()
	{
		ArrayList<PZKSConnection> result = new ArrayList<PZKSConnection>();
		for (PZKSConnection connection : getMutableConnections())
		{
			if (connection.getSrcNode() != this)
			{
				result.add(connection);
			}
		}
		return result;
	}
	
	public Iterator<PZKSConnection> getConnectionIterator()
	{
		return getMutableConnections().iterator();
	}
	public int getNumberOfOutgoingConnection()
	{
		int result = 0;
		for (PZKSConnection connection : getMutableConnections())
		{
			if (connection.getSrcNode() == this)
			{
				result++;
			}
		}
		
		return result;
	}
	public int getNumberOfConnections() {return getMutableConnections().size();}
	
	protected EventListenerList getMutableListeners() 
	{
		if (_listeners == null)
		{
			_listeners = new EventListenerList();
		}
		return _listeners;
	};
	
	//-------------------------------- other methods --------------------------
	public void addConnection(PZKSConnection connection)
	{
		getMutableConnections().add(connection);
	}
	
	public void removeConnection(PZKSConnection connection)
	{
		getMutableConnections().remove(connection);
	}
	public boolean hasPoint(Point point)
	{
		return Math.sqrt(Math.pow(point.x - getLocation().x, 2) + 
						Math.pow(point.y - getLocation().y, 2)) < 
						PZKSNode.sSize / 2;
	}
	public Rectangle getRect()
	{
		Rectangle result = new Rectangle(getLocation().x - PZKSNode.sSize / 2, 
										getLocation().y - PZKSNode.sSize / 2,
										getLocation().x + PZKSNode.sSize / 2,
										getLocation().y + PZKSNode.sSize / 2);
		return result;
	}
	
	//**************************** listeners **********************************
	public void addModelListner(PZKSModelListener listener)
	{
		assert(listener != null);
		getMutableListeners().add(PZKSModelListener.class, listener);
	}
	
	public void removeModelListener(PZKSModelListener listener)
	{
		assert(listener != null);
		getMutableListeners().remove(PZKSModelListener.class, listener);
	}
	
	private void fireModelChanged(ChangeEvent event)
	{
		PZKSModelListener[] listeners = getMutableListeners().
				getListeners(PZKSModelListener.class);
		for (int i = 0; i < listeners.length; i++)
		{
			listeners[i].changeState(event);
		}
	}
	
}
