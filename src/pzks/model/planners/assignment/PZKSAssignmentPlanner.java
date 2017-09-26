package pzks.model.planners.assignment;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;
import pzks.model.planners.queue.PZKSQueueElement;

/**
 * This class is the base class for all assignment planners. It takes 
 * queue of nodes in the constructor (which you get from PZKSQueuePlanner). 
 * When create concrete planners you must implement getNextElementImpl() method.
 * 
 * @author lamao
 *
 */
public abstract class PZKSAssignmentPlanner 
{
	//*********************** ivars *****************************************
	private Queue<PZKSNode>	_nodes = null;
	private int _maxClockTime = 0;
	private PZKSProcessorsStorage _storage = null;
	private int _clockTime = 0;
	
	/**
	 * Estimated time of performing all currently assigned on 
	 * the single processor
	 */
	private int _estimatedTimeOnOneProc = 0;
	
	
	
	//*********************** initializers **********************************
	public PZKSAssignmentPlanner(Queue<PZKSQueueElement> queue, 
			PZKSGraph systemGraph, PZKSAssignmentStrategy strategy)
	{
		assert(queue != null);
		
		_nodes = new LinkedList<PZKSNode>();
		
		for (PZKSQueueElement element: queue)
		{
			_nodes.add(element.node);
		}
		
		_storage = new PZKSProcessorsStorage(systemGraph, strategy);
		strategy.setStorage(_storage);
	}
	
	protected Queue<PZKSNode> getNodes()
	{
		return _nodes;
	}

	public PZKSProcessorsStorage getStorage()
	{
		return _storage;
	}
	
	public int getNumberOfProcessors()
	{
		return getStorage().getNumberOfProcessors();
	}
	
	public int getMaxClockTime()
	{
		return _maxClockTime;
	}
	
	protected int getClockTime()
	{
		return _clockTime;
	}
	
	public int getEstimatedTimeOnOneProcessor()
	{
		return _estimatedTimeOnOneProc;
	}
	
	/**
	 * Resets planners for beginning planning. You must invoke this
	 * method when you need perform planning again
	 * 
	 * @param numberOfProcessors - number of processors for new planning
	 */
	public void reset()
	{
		getStorage().reset();
		
		for (PZKSNode node : _nodes)
		{
			node.setFree(true);
		}
		
		_maxClockTime = 0;
		_clockTime = 0;
		_estimatedTimeOnOneProc = 0;
	}
	
	/**
	 * This method just assigns all nodes and returns total queue of assigned
	 * nodes (each element in this queue is one step of assigning).
	 * 
	 * @return queue of elements for assign
	 */
	public Queue<List<PZKSAssignmentElement>> getAssignmesQueue()
	{
		Queue<List<PZKSAssignmentElement> > result = new LinkedList<List<PZKSAssignmentElement> >();
		
		List<PZKSAssignmentElement> element = getNextElement();
		while (element != null)
		{
			result.add(element);
			element = getNextElement();
		}
		
		return result;
	}
	
	public PZKSNode getFirstFreeNode()
	{
		PZKSNode result = null;
		if (getNodes().size() != 0)
		{
			_clockTime--;
			do
			{
				_clockTime++;
				for (PZKSNode node : getNodes())
				{
					if (getStorage().isReady(node, _clockTime))
					{
						result = node;
						break;
					}
				}
			}
			while (result == null);
		}
		
		return result;
	}
	
	/**
	 * This method is part of template method. It retrieves first free node
	 * from queue and tries assign it. If empty <b>result</b> was returned 
	 * from <code>getNextElemntImpl</code> assigning will be repeated at 
	 * the next clokTime and so on. This method always assigns one node 
	 * (if any presents in queue). Assigned node will be removed from queue.
	 * @return List of elements (one node and transitions for it) that have 
	 * 			been assigned during this step. <b>null</b> only if there are
	 * 			no nodes for assignment.
	 */
	public List<PZKSAssignmentElement> getNextElement()
	{
		List<PZKSAssignmentElement> result = null;
		if (getNodes().size() != 0)
		{
			// do until we can put this node on any processor at _clockTime
			PZKSNode node = null;
			do
			{
				node = getFirstFreeNode();
				result = getNextElementImpl(node);
				assert(result != null);
				if (result.size() == 0)
				{
					_clockTime++;
				}
			}
			while (result.size() == 0);

			_estimatedTimeOnOneProc += node.getWeight();
			getNodes().remove(node);
			for (PZKSAssignmentElement element : result)
			{
				if (element.start + element.duration > _maxClockTime)
				{
					_maxClockTime = element.start + element.duration;
				}
			}
		}
		return result;
	}
	
	/**
	 * Implementation for planning next node with all transitions for it.
	 * 
	 * @return List of elements (node and transitions) that has been assigned
	 * 			during current step. You haven't return null. If there are no 
	 * 			free processor at <b>_clockTime</b> time you should return 
	 * 			empty list but not null.
	 */
	protected abstract List<PZKSAssignmentElement> getNextElementImpl(PZKSNode node);

}
