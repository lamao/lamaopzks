package pzks.model.planners.assignment;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import pzks.model.PZKSConnection;
import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;
import pzks.model.planners.assignment.PZKSProcessorsStorage.PZKSAssignmentNodeElement;
import pzks.model.planners.queue.PZKSQueueElement;

public class PZKSVariant5AssignmentPlanner extends PZKSAssignmentPlanner
{

	public PZKSVariant5AssignmentPlanner(Queue<PZKSQueueElement> queue,
			PZKSGraph systemGraph, PZKSAssignmentStrategy strategy)
	{
		super(queue, systemGraph, strategy);
	}

	@Override
	protected List<PZKSAssignmentElement> getNextElementImpl(PZKSNode node)
	{
		List<PZKSAssignmentElement> result = new LinkedList<PZKSAssignmentElement>();

		int numberOfMinProcessor = getOptimalProcessor(node);

		// if we have found free processor put node on it
		if (numberOfMinProcessor != -1)
		{
			result = getStorage().putFullNode(numberOfMinProcessor, node,
					getClockTime());
		}
		
		return result;
	}
	
	private int getOptimalProcessor(PZKSNode node)
	{
		// find optimal processor
		int minTotalTrans = Integer.MAX_VALUE;
		int result = -1;
		
		int maxTrans = 0;
		for (int i = 0; i < getNumberOfProcessors(); i++)
		{
			if (getStorage().isProcessorFree(i, getClockTime(), 
							node.getWeight() + node.getLongestTransmission()))
			{
				maxTrans = 0;
				for (PZKSConnection connection : node.getIncomingConnections())
				{
					maxTrans = maxTrans + 
							getWeightOfTransmission(i, node, connection);
				}
				
				if (maxTrans < minTotalTrans)
				{
					minTotalTrans = maxTrans;
					result = i;
				}
			}
		}
		
		return result;
	}

	
	private int getWeightOfTransmission(int dstProc, PZKSNode dstNode, 
							PZKSConnection connection)
	{
		// find source proc of this transmission
		int srcProc = getStorage().getNodesMap().get(connection.getSrcNode())
						.numberOfProccessor;

		// calc length of this transmission
		int lenOfTrans = getStorage().getWayLengthBetweenProcessors(srcProc, dstProc);
		PZKSAssignmentNodeElement srcNodeElement = getStorage().getNodesMap()
						.get(connection.getSrcNode());
		
		return srcNodeElement.getEnd() + lenOfTrans * connection.getWeight();
	}
}
