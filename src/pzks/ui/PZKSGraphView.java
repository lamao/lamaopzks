package pzks.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;

import pzks.model.listeners.PZKSModelListener;
import pzks.model.*;
import pzks.service.PZKSAppStrings;
import pzks.service.PZKSButtonActionsManager;
import pzks.ui.renderer.PZKSRenderer;

public class PZKSGraphView extends JComponent implements ActionListener
{
	private static final long serialVersionUID = 1L;

	// ******************** ivars
	private PZKSGraph _graph = null;
	private PZKSRenderer _renderer = null;
	private PZKSNode _selectedNode = null;

	private int _state = 0;

	private static int DEFAULT = 0;
	private static int ADDING_NODE = 1;
	private static int REMOVING_NODE = 2;
	private static int ADDING_CONNECTION = 3;
	private static int REMOVING_CONNECTION = 4;

	private PZKSGraphListener _modelListener = null;
	
	private boolean _receptActions = true;

	// ******************** initializers
	public PZKSGraphView(PZKSGraph graph, PZKSRenderer renderer)
	{
		super();
		setBorder(BorderFactory.createEtchedBorder());

		_graph = graph;
		if (_graph != null)
		{
			_graph.addModelListener(getModelListener());
		}
		_renderer = renderer;

		GraphMouseListener mouseListener = new GraphMouseListener();
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		subsribeOnActions();
	}
	
	private void subsribeOnActions()
	{
		PZKSButtonActionsManager manager = PZKSButtonActionsManager.defaultManager();
		
		manager.addActionListener(PZKSButtonActionsManager.ADD_NODE_ACTION, this);
		manager.addActionListener(PZKSButtonActionsManager.REMOVE_NODE_ACTION, this);
		manager.addActionListener(PZKSButtonActionsManager.ADD_CONNECTION_ACTION, this);
		manager.addActionListener(PZKSButtonActionsManager.REMOVE_CONNECTION_ACTION, this);
		manager.addActionListener(PZKSButtonActionsManager.VALIDATE_ACTION, this);
	}

	public PZKSGraphView()
	{
		this(null, null);
	}

	// ******************* accessors
	public PZKSGraph getGraph()
	{
		return _graph;
	}

	public void setGraph(PZKSGraph graph)
	{
		if (_graph != null)
		{
			setSelectedNode(null);
			_graph.removeModelListener(getModelListener());
		}
		_graph = graph;
		if (_graph != null)
		{
			_graph.addModelListener(getModelListener());
			repaint();
		}
	}

	public PZKSRenderer getRenderer()
	{
		return _renderer;
	};

	public void setRenderer(PZKSRenderer renderer)
	{
		_renderer = renderer;
	};

	public PZKSNode getSelectedNode()
	{
		return _selectedNode;
	}

	public void setSelectedNode(PZKSNode node)
	{
		if (_selectedNode != node)
		{
			if (_selectedNode != null)
			{
				repaint(_selectedNode.getRect());
			}
			_selectedNode = node;

			if (_selectedNode != null)
			{
				repaint(_selectedNode.getRect());
			}
		}
	}
	

	public void setReceptActions(boolean receptActions)
	{
		_receptActions = receptActions;
	}

	public boolean isReceptActions()
	{
		return _receptActions;
	}


	public PZKSGraphListener getModelListener()
	{
		if (_modelListener == null)
		{
			_modelListener = new PZKSGraphListener();
		}
		return _modelListener;
	}

	// ******************* other methods
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (getGraph() != null && getRenderer() != null)
		{
			// process all connections
			Iterator<PZKSConnection> connectionIterator = getGraph()
					.getConnectionsIterator();
			PZKSConnection currConnection = null;
			while (connectionIterator.hasNext())
			{
				currConnection = connectionIterator.next();
				getRenderer().paintConnection(g2, currConnection);
			}

			// process all nodes
			Iterator<PZKSNode> nodesIterator = getGraph().getNodesIterator();
			PZKSNode currNode = null;

			while (nodesIterator.hasNext())
			{
				currNode = nodesIterator.next();
				getRenderer().paintNode(g2, currNode);
			}

			// draw selected node
			if (getSelectedNode() != null)
			{
				getRenderer().paintSelectedNode(g2, getSelectedNode());
			}
		}
	}

	/**
	 * This method responds for processing all action from all controls in
	 * toolbar and in the menu. It would be better split actions that don't
	 * refer to graph into other class, but present building of GUI (using
	 * configuration.xml) doesn't allow use such approach.
	 * 
	 * @param event
	 *            - action from control
	 */

	/*
	 * TODO Some of actions proceeded in this method should be moved outside it
	 * Among them are ShowImmerseDialog and Validate
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (!isReceptActions())
		{
			return;
		}
		
		
		if (event.getActionCommand().equals(PZKSButtonActionsManager.ADD_NODE_ACTION))
		{
			_state = PZKSGraphView.ADDING_NODE;
		}
		else if (event.getActionCommand().equals(PZKSButtonActionsManager.REMOVE_NODE_ACTION))
		{
			PZKSNode node = getSelectedNode();
			if (node != null)
			{
				getGraph().removeNode(node);
				_state = PZKSGraphView.DEFAULT;
			}
			else
			{
				_state = PZKSGraphView.REMOVING_NODE;
			}
		}
		else if (event.getActionCommand().equals(PZKSButtonActionsManager.ADD_CONNECTION_ACTION))
		{
			_state = PZKSGraphView.ADDING_CONNECTION;
		}
		else if (event.getActionCommand().equals(PZKSButtonActionsManager.REMOVE_CONNECTION_ACTION))
		{
			_state = PZKSGraphView.REMOVING_CONNECTION;
		}
		else if (event.getActionCommand().equals(PZKSButtonActionsManager.VALIDATE_ACTION))
		{
			boolean result = false;
			if (getGraph().getGraphValidator() != null && !getGraph().isEmpty())
			{
				result = getGraph().getGraphValidator().isValid();
			}

			ResourceBundle bundle = PZKSAppStrings.getBundle();
			if (result)
			{
				JOptionPane.showMessageDialog(this,
				        bundle.getString(PZKSAppStrings.GRAPH_VALIDATION_TRUE_KEY),
						bundle.getString(PZKSAppStrings.GRAPH_VALIDATION_TITLE_KEY),
						JOptionPane.INFORMATION_MESSAGE);
    		}
    		else
    		{
    			JOptionPane.showMessageDialog(this, 
    			        bundle.getString(PZKSAppStrings.GRAPH_VALIDATION_FALSE_KEY),
                        bundle.getString(PZKSAppStrings.GRAPH_VALIDATION_TITLE_KEY), 
                        JOptionPane.ERROR_MESSAGE);	
    		}
    	}
    }

	
	class GraphMouseListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent event)
		{
			// if user click left button
			if (event.getButton() == MouseEvent.BUTTON1)
			{
				PZKSNode newSelectedNode = getGraph().getNodeAtPoint(
						event.getPoint());
				// if user clicks on node
				if (newSelectedNode != null)
				{
					if (_state == PZKSGraphView.ADDING_CONNECTION)
					{
						if (getSelectedNode() != null)
						{
							getGraph().addConnection(getSelectedNode(),
									newSelectedNode);
						}
					}
					else if (_state == PZKSGraphView.REMOVING_NODE)
					{
						getGraph().removeNode(newSelectedNode);
						newSelectedNode = null;
						_state = PZKSGraphView.DEFAULT;
					}
					else
					{
						_state = PZKSGraphView.DEFAULT;
					}

					setSelectedNode(newSelectedNode);
				}
				else
				// if user clicks on free area
				{
					if (_state == PZKSGraphView.ADDING_NODE)
					{
						PZKSNode newNode = new PZKSNode(event.getPoint(), 0, 1);
						getGraph().addNode(newNode);
						setSelectedNode(newNode);
					}
					else if (_state == PZKSGraphView.REMOVING_CONNECTION)
					{
						setSelectedNode(null);
						PZKSConnection connection = getGraph().getConnection(
								event.getPoint());
						if (connection != null)
						{
							getGraph().removeConnection(connection);
						}
					}
					else
					{
						_state = PZKSGraphView.DEFAULT;
						setSelectedNode(newSelectedNode);
					}
				}
			}
			// if user clicks right mouse button
			else if (event.getButton() == MouseEvent.BUTTON3)
			{
				// if user clicks on node
				PZKSNode newSelectedNode = _graph.getNodeAtPoint(event
						.getPoint());

				if (newSelectedNode != null)
				{
				    ResourceBundle bundle = PZKSAppStrings.getBundle();
					setSelectedNode(newSelectedNode);
					// get new weight of node
					String newWeight = JOptionPane.showInputDialog(
						bundle.getString(PZKSAppStrings
						        .GRAPH_INPUT_WEIGHT_NODE_STRING_KEY), 
						newSelectedNode.getWeight());
					if (newWeight != null)
					{
						try
						{
							newSelectedNode.setWeight(Integer
									.parseInt(newWeight));
						}
						catch (Exception ex)
						{
							JOptionPane.showMessageDialog(null,
								bundle.getString(PZKSAppStrings.INVALID_VALUE_KEY),
								bundle.getString(PZKSAppStrings.ERROR_KEY),
								JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				else
				{
					PZKSConnection selectedConnection = _graph
							.getConnection(event.getPoint());
					if (selectedConnection != null)
					{
					    ResourceBundle bundle = PZKSAppStrings.getBundle();
						String newWeight = JOptionPane.showInputDialog(
							bundle.getString(PZKSAppStrings
							        .GRAPH_INPUT_WEIGHT_CONNECTION_STRING_KEY),
							selectedConnection.getWeight());
						if (newWeight != null)
						{
							try
							{
								selectedConnection.setWeight(Integer
										.parseInt(newWeight));
							}
							catch (Exception ex)
							{
								JOptionPane.showMessageDialog(null,
								    bundle.getString(PZKSAppStrings.INVALID_VALUE_KEY),
								    bundle.getString(PZKSAppStrings.ERROR_KEY),
									JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					else
					{
						setSelectedNode(null);
						_state = PZKSGraphView.DEFAULT;
					}
				}
			}

			repaint();
		}

		public void mouseDragged(MouseEvent event)
		{
			PZKSNode node = getSelectedNode();
			if (node != null)
			{
				node.setLocation(event.getPoint());
			}
		}
	}

	private class PZKSGraphListener implements PZKSModelListener
	{

		public void changeState(ChangeEvent event)
		{
			repaint();
		}
	}

}
