package pzks.model.planners.assignment;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;
import pzks.model.planners.queue.PZKSQueueElement;

public class PZKSVariant3AssignmentPlanner extends PZKSAssignmentPlanner
{
	/**
	 * These are number of processors which are sorted by their number of links
	 */
	private int[] _sortedProcessors = null;
	
	public PZKSVariant3AssignmentPlanner(Queue<PZKSQueueElement> queue,
			PZKSGraph systemGraph, PZKSAssignmentStrategy strategy)
	{
		super(queue, systemGraph, strategy);
		_sortedProcessors = new int[systemGraph.getNumberOfNodes()];
		int i = 0;
		for (PZKSNode node : systemGraph.getNodes())
		{
			_sortedProcessors[i++] = node.getNumber();
		}
		init(systemGraph);
	}
	
	private void init(PZKSGraph systemGraph)
	{
		int n = systemGraph.getNumberOfNodes();
		int links[] = new int[n];
		
		// init
		int i = 0;
		for (PZKSNode node : systemGraph.getNodes())
		{
			links[i++] = node.getNumberOfConnections();
		}
		
		// sort
		int maxIndex = 0;
		for (i = 0; i < n; i++)
		{
			maxIndex = i;
			for (int k = i + 1; k < n; k++)
			{
				if (links[maxIndex] < links[k])
				{
					maxIndex = k;
				}
			}
			//swap
			if (maxIndex != i)
			{
				int tmp = links[maxIndex];
				links[maxIndex] = links[i];
				links[i] = tmp;
				tmp = _sortedProcessors[maxIndex];
				_sortedProcessors[maxIndex] = _sortedProcessors[i];
				_sortedProcessors[i] = tmp;
			}
		}
	}
	

	@Override
	protected List<PZKSAssignmentElement> getNextElementImpl(PZKSNode node)
	{
		List<PZKSAssignmentElement> result = new LinkedList<PZKSAssignmentElement>();
		
		// find first free processor
		int k = 0;
		while (k < _sortedProcessors.length && 
				!getStorage().isProcessorFree(_sortedProcessors[k], 
							getClockTime(), 
							node.getWeight() + node.getLongestTransmission()))
		{
			k++;
		}

		// if we have found free processor put node on it
		if (k < _sortedProcessors.length)
		{
			result = getStorage().putFullNode(_sortedProcessors[k], node,
					getClockTime());
		}
		
		return result;
	}
	
}
