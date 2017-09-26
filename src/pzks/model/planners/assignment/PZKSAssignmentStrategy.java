package pzks.model.planners.assignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pzks.model.PZKSConnection;
import pzks.model.PZKSNode;
import pzks.model.planners.assignment.PZKSProcessorsStorage.PZKSAssignmentNodeElement;
import pzks.model.planners.assignment.PZKSProcessorsStorage.PZKSAssignmentTransElement;

public abstract class PZKSAssignmentStrategy
{
//****************************** fields ****************************************
	private PZKSProcessorsStorage _storage = null;	// weak ref
	
//****************************** fields for undo *******************************
	private PZKSNode _lastAssignedNode = null;
	private List<PZKSAssignmentTransElement> _lastAssignedTrans = null;
	
	
//******************************** initializers ********************************
	public PZKSAssignmentStrategy(PZKSProcessorsStorage storage)
	{
		super();
		_storage = storage;
	}
	
	public PZKSAssignmentStrategy()
	{
		this(null);
	}

//******************************** accessors ***********************************
	public void setStorage(PZKSProcessorsStorage storage)
	{
		_storage = storage;
	}

	public PZKSProcessorsStorage getStorage()
	{
		return _storage;
	}
	
	private List<PZKSAssignmentTransElement> getLastAssignedTrans()
	{
		if (_lastAssignedTrans == null)
		{
			_lastAssignedTrans = new LinkedList<PZKSAssignmentTransElement>();
		}
		return _lastAssignedTrans;
	}
	
	
//******************************** other methods *******************************
	/**
	 * Get all incoming connection to <b>node</b>, sorts them in descending 
	 * order of <code>connection.getSrcNode().getEnd()</code>. It means 
	 * nodes which were assigned early send data first.
	 *   
	 * @param node
	 * @return
	 */
	private ArrayList<PZKSConnection> getSortedConnection(PZKSNode node)
	{
		ArrayList<PZKSAssignmentNodeElement> nodes = 
			new ArrayList<PZKSAssignmentNodeElement>();
		ArrayList<PZKSConnection> result = new ArrayList<PZKSConnection>();
		
		for (PZKSConnection connection : node.getIncomingConnections())
		{
			nodes.add(getStorage().getNodesMap().get(connection.getSrcNode()));
			result.add(connection);
		}
		
		// sort
		int minIndex = 0;
		for (int i = 0; i < result.size(); i++)
		{
			minIndex = i;
			for (int k = i + 1; k < result.size(); k++)
			{
				PZKSAssignmentNodeElement element = nodes.get(k);
				if (element.getEnd() < nodes.get(minIndex).getEnd())
				{
					nodes.set(k, nodes.get(minIndex));
					nodes.set(minIndex, element);
					
					PZKSConnection tmp = result.get(k);
					result.set(k, result.get(minIndex));
					result.set(minIndex, tmp);
				}
			}
		}
		
		return result;
	}
	
	public abstract int putFullTrans(PZKSNode srcNode, PZKSNode dstNode, int dstProc,
			List<PZKSAssignmentElement> assignedTransitions);
	
	/**
	 * Assigns <b>node</b> and all transmissions for sending data
	 * on <b>processor</b> taking into account all delays. Node will
	 * be assigned not earlier than <b>clockTime</b> 
	 * NOTE: This method suppose that node is ready for assigning 
	 * (i.e. all its ancestors are assigned)
	 * 
	 * @param processor
	 * @param PZKSNode
	 * @return
	 */
	public List<PZKSAssignmentElement> putFullNode(int processor, PZKSNode node,
			int clockTime)
	{
		ArrayList<PZKSConnection> connections = getSortedConnection(node);
//		connections = node.getIncomingConnections();
		
		List<PZKSAssignmentElement> result = new LinkedList<PZKSAssignmentElement>();
		
		int maxTime = clockTime;
		int time = clockTime;
		for (PZKSConnection connection : connections)
		{
			time = putFullTrans(connection.getSrcNode(), node, 
					processor, result);
			maxTime = Math.max(time, maxTime);
		}

		// save data for undo
		getLastAssignedTrans().clear();
		for (PZKSAssignmentElement trans : result)
		{
			getLastAssignedTrans().add((PZKSAssignmentTransElement) trans);
		}
		_lastAssignedNode = node;

		maxTime = getStorage().getFreeIntervalForNode(maxTime, processor,
					node.getWeight());
		result.add(getStorage().putNode(processor, maxTime, node));
		
		
		return result;
	}
	
	
	/**
	 * Undoes one last assignment. After undo you can't perform redo or 
	 * one more undo.
	 */
	public void undoLastAssignment()
	{
		if (_lastAssignedNode != null)
		{
			getStorage().removeNode(_lastAssignedNode);
			_lastAssignedNode = null;
		}
		
		if (getLastAssignedTrans().size() != 0)
		{
			for (PZKSAssignmentTransElement element : getLastAssignedTrans())
			{
				getStorage().removeTrans(element);
			}
		}
	}

}
