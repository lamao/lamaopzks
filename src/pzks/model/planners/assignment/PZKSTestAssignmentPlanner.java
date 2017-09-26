package pzks.model.planners.assignment;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;
import pzks.model.planners.queue.PZKSQueueElement;

public class PZKSTestAssignmentPlanner extends PZKSAssignmentPlanner
{
	public PZKSTestAssignmentPlanner(Queue<PZKSQueueElement> queue,
			PZKSGraph systemGraph)
	{
		super(queue, systemGraph, new PZKSPreactStrategy());
	}

	@Override
	protected List<PZKSAssignmentElement> getNextElementImpl(PZKSNode node)
	{
		List<PZKSAssignmentElement> result = new LinkedList<PZKSAssignmentElement>();
		
		if (getStorage().isProcessorFree(0, getClockTime(), node.getWeight()))
		{
			result = getStorage().putFullNode(0, node, 0);
		}
		return result;
	 }
}
