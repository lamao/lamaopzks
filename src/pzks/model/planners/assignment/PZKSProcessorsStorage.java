package pzks.model.planners.assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import pzks.model.PZKSConnection;
import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;

/**
 * This class is designed as storage of information during assignment.
 * It stores all assigned nodes, all transitions and you can retreive
 * some service information from it. 
 * 
 * @author lamao
 *
 */
public class PZKSProcessorsStorage
{
	private PZKSGraph _systemGraph = null;
	private PZKSAssignmentStrategy _strategy = null;
	private List<PZKSAssignmentNodeElement> _nodes[] = null;
	private List<PZKSAssignmentTransElement> _transitions[] = null;
	private int[][] _ways = null;
	
	/**
	 * This is the map which links model nodes with nodes already
	 * assigned. If <code>_nodes.get(someNode) != null</code> someNode
	 * is already assigned. 
	 */
	private Map<PZKSNode, PZKSAssignmentNodeElement> _nodesMap = null; 
	
	/**
	 * Build internal structure based on systemGraph.
	 *  
	 * @param systemGraph - system graph. 
	 */
	public PZKSProcessorsStorage(PZKSGraph systemGraph, 
								PZKSAssignmentStrategy strategy)
	{
		_systemGraph = systemGraph;
		_strategy = strategy;
		_strategy.setStorage(this);
		
		reset();		
		calculateWays();
	}
	
	Map<PZKSNode, PZKSAssignmentNodeElement> getNodesMap()
	{
		if (_nodesMap == null)
		{
			_nodesMap = new HashMap<PZKSNode, PZKSAssignmentNodeElement>();
		}
		return _nodesMap;
	}
	
	public int getNumberOfProcessors()
	{
		return _systemGraph.getNumberOfNodes(); 
	}
	
	public boolean isAssigned(PZKSNode node)
	{
		return getNodesMap().get(node) != null;
	}
	
	public boolean isCompleted(PZKSNode node, int time)
	{
		PZKSAssignmentNodeElement element = getNodesMap().get(node);
		return element != null && element.start + element.duration <= time;
	}
	
	public boolean isReady(PZKSNode node, int time)
	{
		for (PZKSConnection connection : node.getIncomingConnections())
		{
			if (!isCompleted(connection.getSrcNode(), time))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns whether processor <code>processor</code> is free at time 
	 * <b>clockTime</b>. It must be no less then <b>duration</b> time 
	 * since <b>clockTime</b>
	 * @param processor - number of processor 
	 * @param clockTime - current clock time
	 * @param duration - duration of desirable free interval
	 * @return true if processor is free
	 */
	public boolean isProcessorFree(int processor, int clockTime, int duration)
	{
		// TODO Replace this almost copy-paste from getFirstFreeLink
		//	by some common method
		//---------------------- COPY-PASTE --------------------------
		Iterator<PZKSAssignmentNodeElement> iterator = _nodes[processor].iterator();

		int result = -1;
		int prevRightSide = 0;
		while (iterator.hasNext() && result == -1)
		{
			PZKSAssignmentNodeElement element = iterator.next();
			if (element.start > clockTime && 
					isInside(prevRightSide, element.start, clockTime, 
							clockTime + duration))
			{
				result = clockTime;
			}
			prevRightSide = element.start + element.duration;
		}

		if (result == -1)
		{
			result = prevRightSide > clockTime ? prevRightSide : clockTime;
		}
		//---------------------- COPY-PASTE --------------------------
		
		return result == clockTime;			
	}
	
	/**
	 * Returns array of free processors at time <b>clockTime</b>. They
	 * must be no less then <b>duration</b> time since <b>clockTime</b> 
	 * @param clockTime - current clock time
	 * @param duration - duration of desirable free interval
	 * @return An array of free processors
	 */
	public int[] getFreeProcessors(int clockTime, int duration)
	{
		List<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < _nodes.length; i++)
		{
			if (isProcessorFree(i, clockTime, duration))
			{
				list.add(i);
			}
		}

		// fill array
		int result[] = new int[list.size()];
		int i = 0;
		for (int value : list)
		{
			result[i++] = value; 
		}
		
		return result;
	}
	
	/**
	 * Returns first interval of time where link to dstProc is 
	 * not busy (It means that there are not any transmissions to 
	 * dsrProc direction during clockTime...clockTime + duration and
	 * we can perform new transmission in this time). We suppose that 
	 * every processor has only one receiving channel. 
	 *  
	 * @param clockTime - start time of interval
	 * @param dstProc - destination processor of transmission
	 * @param duration - duration of interval
	 * @return First moment greater then clockTime when this link will be not busy.
	 */
	public int getFirstFreeLink(int clockTime, int dstProc, int duration)
	{
		return getFirstFreeElement(_transitions, clockTime, dstProc, duration);
	}
	
	/**
	 * Returns first interval of time where processor <b>processor</b> is 
	 * not busy (It means that there it is not performing calculations  
	 * during clockTime...clockTime + duration and we can put new
	 * node in this time). We suppose that every processor has only 
	 * one core. 
	 *  
	 * @param clockTime - start time of interval
	 * @param processor - number of processor to find free interval
	 * @param duration - duration of interval
	 * @return First moment not less then clockTime when this processor is 
	 * 		not busy
	 */	
	public int getFreeIntervalForNode(int clockTime, int processor, int duration)
	{
		return getFirstFreeElement(_nodes, clockTime, processor, duration);
	}
	
	/**
	 * Returns first interval of time where array <b>array</b> not contains  
	 * any elements (It means that there it is no elements during  
	 * clockTime...clockTime + duration and we can put new one in this time).  
	 *  
	 * @param array - array to find free interval
	 * @param clockTime - start time of interval
	 * @param processor - processor to find free interval
	 * @param duration - duration of interval
	 * @return First moment not less then clockTime when this array has 
	 * 		not any elements
	 */	
	private int getFirstFreeElement(
				List<? extends PZKSAssignmentElement> array[],
				int clockTime, int processor, int duration)
	{
		Iterator<? extends PZKSAssignmentElement> iterator = 
			array[processor].iterator();

		int result = -1;
		int prevRightSide = 0;
		while (iterator.hasNext() && result == -1)
		{
			PZKSAssignmentElement element = iterator.next();
			if (element.start > clockTime && 
					element.start - Math.max(prevRightSide, clockTime) >= duration)
			{
				result = Math.max(prevRightSide, clockTime);
			}
			prevRightSide = element.start + element.duration;
		}
		
		if (result == -1)
		{
			result = prevRightSide > clockTime ? prevRightSide : clockTime;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public void reset()
	{
		int n = _systemGraph.getNumberOfNodes();
		_nodes = new LinkedList[n];
		for (int i = 0; i < n; i++)
		{
			_nodes[i] = new LinkedList<PZKSAssignmentNodeElement>();
		}
		_transitions = new LinkedList[n];
		for (int i = 0; i < n; i++)
		{
			_transitions[i] = new LinkedList<PZKSAssignmentTransElement>();
		}
	}
	
	/**
	 * Puts <b>element</b> on processor <b>processor</b>. This method just
	 * keeps ascending order of elements, but does not checks whether this 
	 * processor is busy or no. You should do this yourself.
	 * 
	 * @param processor - number of processor
	 * @param element - element
	 * @param array - array in which you want add element
	 */
	private void putElement(int processor, PZKSAssignmentElement element,
			List<PZKSAssignmentElement> array[])
	{
		if (processor < array.length)
		{
			Iterator<PZKSAssignmentElement> iterator = 
				array[processor].iterator();
			
			int index = 0;
			while (iterator.hasNext() && iterator.next().start < element.start)
			{
				index++;
			}
			
			array[processor].add(index, element);
		}
	}
	
	/**
	 * Puts <b>node</b> on processor <b>processor</b>. This method just
	 * keeps ascending order of nodes, but does not checks whether this 
	 * processor is busy or no. You should do this yourself.
	 * 
	 * @param processor - number of processor
	 * @param node - task node
	 * @return PZKSAssignmentNodeElement which has been assigned
	 */
	@SuppressWarnings("unchecked")
	public PZKSAssignmentNodeElement putNode(int processor, int start, PZKSNode node)
	{
		PZKSAssignmentNodeElement nodeElement = new PZKSAssignmentNodeElement(
				processor, start, node.getWeight(), node.getNumber());
		List<? extends PZKSAssignmentElement> array[] = _nodes;
		putElement(processor, nodeElement, (List<PZKSAssignmentElement>[]) array);
		
		getNodesMap().put(node, nodeElement);
		
		return nodeElement;
	}
	
	/**
	 * Puts transition <b>trans</b> on processor <b>processor</b>. This method 
	 * just keeps ascending order of transitions, but does not checks whether 
	 * this link is busy or no. You should do it yourself.
	 * 
	 * @param processor - number of processor
	 * @param trans - transition node
	 */
	@SuppressWarnings("unchecked")
	public void putTrans(int processor, PZKSAssignmentTransElement trans)
	{
		List<? extends PZKSAssignmentElement> array[] = _transitions;
		putElement(processor, trans, (List<PZKSAssignmentElement>[]) array);
	}
	
	public void removeNode(PZKSNode node)
	{
		PZKSAssignmentNodeElement element = getNodesMap().remove(node);
		_nodes[element.numberOfProccessor].remove(element);
	}
	
	public void removeTrans(PZKSAssignmentTransElement transition)
	{
		_transitions[transition.numberOfProccessor].remove(transition);
		_transitions[transition.destProcessor].remove(transition);
	}
	
	/**
	 * Calculate all ways for all nodes in graph using 'wave algorithm'
	 */
	private void calculateWays()
	{
		int n = _systemGraph.getNumberOfNodes();
		_ways = new int[n][n];
		for (int i = 0; i < n; i++)
		{
			Arrays.fill(_ways[i], -1);
		}

		// for each node find ways to all nodes
		for (PZKSNode srcNode : _systemGraph.getNodes())
		{
			HashSet<PZKSNode> markedNodes = new HashSet<PZKSNode>();
			
			// add src node in the first wave
			Queue<PZKSNode> nextWave = new LinkedList<PZKSNode>();
			nextWave.add(srcNode);
			
			// do wave algorithm
			while (nextWave.size() != 0)
			{
				PZKSNode intermediateNode = nextWave.poll();
				markedNodes.add(intermediateNode);
				for (PZKSConnection connection : intermediateNode.getConnections())
				{
					PZKSNode dstNode = connection.getOppositeNode(intermediateNode);
					if (!markedNodes.contains(dstNode)) 
					{
						nextWave.add(dstNode);
						markedNodes.add(dstNode);
						_ways[srcNode.getNumber()][dstNode.getNumber()] = 
									intermediateNode.getNumber();
					}
				}
			}
		}
	}

	/**
	 * Calculates the way from <b>src</b> node to 
	 * <b>dst</b> node using <b>_ways</b> matrix, calculated
	 * in <b>calculateWays</b>
	 * @param src
	 * @param dst
	 * @return Numbers of processors in the way
	 */
	public ArrayList<Integer> getWayBetweenProcessors(int src, int dst)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		result.add(dst);
		while (_ways[src][dst] != src)
		{
			result.add(0, _ways[src][dst]);
			dst = _ways[src][dst];			
		}
		
		return result;	
	}
	private ArrayList<Integer> getWayBetweenProcessors(PZKSNode srcProc, PZKSNode dstProc)
	{
		return getWayBetweenProcessors(srcProc.getNumber(), dstProc.getNumber());
	}
	
	
	/**
	 * Calculates the length of way from <b>src</b> node to 
	 * <b>dst</b> node using <b>_ways</b> matrix, calculated
	 * in <b>calculateWays</b>
	 * @param srcNode
	 * @param dstNode
	 * @return
	 */
	public int getWayLengthBetweenProcessors(PZKSNode srcProc, PZKSNode dstProc)
	{
		int result = -1;
		if (srcProc == dstProc)
		{
			result = 0;
		}
		else
		{
			result =  getWayBetweenProcessors(srcProc, dstProc).size();
		}
		return result;
	}
	
	public int getWayLengthBetweenProcessors(int srcProc, int dstProc)
	{
		ArrayList<PZKSNode> nodes = _systemGraph.getNodes();
		PZKSNode src = nodes.get(srcProc);
		PZKSNode dst = nodes.get(dstProc);
		return getWayLengthBetweenProcessors(src, dst);
	}

	
	
	/**
	 * Checks whether two ranges intersects
	 * @param l1
	 * @param r1
	 * @param l2
	 * @param r2
	 * @return
	 */
//	private boolean areIntersected(int l1, int r1, int l2, int r2)
//	{
//		return (l1 <= l2 && l2 <= r1) || (l2 <= l1 && l1 <= r2);
//	}
	
	/**
	 * Checks whether bigger <b>smaller</b> is inside <b>bigger</b>
	 * @param bigger1
	 * @param bigger2
	 * @param smaller1
	 * @param smaller2
	 * @return
	 */
	private boolean isInside(int bigger1, int bigger2, int smaller1, int smaller2)
	{
		return (bigger1 <=  smaller1 && smaller2 <= bigger2);
	}
	
//************************* PZKSAssignmentStrategy methods *********************
	public List<PZKSAssignmentElement> putFullNode(int processor, PZKSNode node,
			int clockTime)
	{
		return _strategy.putFullNode(processor, node, clockTime);
	}
	
	public void undoLastAssignment()
	{
		_strategy.undoLastAssignment();
	}
	
	
	//***************************** internal classes **************************
	public class PZKSAssignmentTransElement extends PZKSAssignmentElement
	{
		public int destNode = -1;
		public int destProcessor = -1;
		
		public PZKSAssignmentTransElement(int numberOfProccessor, int destProcessor,
				int destNode, int start, int duration)
		{
			super(AssignmentType.TRANSMISSION, numberOfProccessor, start, 
					duration, "");
			this.destNode = destNode;
			this.destProcessor = destProcessor;
		}
	}

	public class PZKSAssignmentNodeElement extends PZKSAssignmentElement
	{
		/**
		 * Number of assigned node.
		 * P.S.Usually it is index of PZKSNode in PZKSGraph.getNodes() array
		 */
		public int number = -1;	
		
		public PZKSAssignmentNodeElement(int numberOfProccessor, 
				int start, int duration, int number)
		{
			super(AssignmentType.NODE, numberOfProccessor, start, 
					duration, "");
			this.number = number;
		}
	}
	
	
	
}


