package pzks.model.planners.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;

/**
 * This class plans nodes in descending of number 
 * of their outgoing connections
 * @author lamao
 *
 */

public class PZKSVariant11QueuePlanner extends PZKSQueuePlanner 
{
	public PZKSVariant11QueuePlanner(PZKSGraph graph)
	{
		super(graph);
	}

	public String getName()
	{
		return "Variant 11";
	}
	
	public Queue<PZKSQueueElement> getQueue() 
	{
		Queue<PZKSQueueElement> result = new LinkedList<PZKSQueueElement>();
		
		ArrayList<PZKSNode> nodes = getGraph().getNodes();
		
		int[] numberOfConnections = new int[nodes.size()];
		for (int i = 0; i < nodes.size(); i++)
		{
			numberOfConnections[i] = nodes.get(i).getNumberOfOutgoingConnection();
		}
		
		//sort nodes
		for (int i = 0; i < nodes.size(); i++)
		{
			int minIndex = i;
			
			for (int k = i + 1; k < nodes.size(); k++)
			{
				if (numberOfConnections[k] > numberOfConnections[minIndex]) 
				{
					minIndex = k;
				}
			}
			
			if (i != minIndex)
			{
				PZKSNode buffer = nodes.get(i);
				nodes.set(i, nodes.get(minIndex));
				nodes.set(minIndex, buffer);
				
				int valueBuffer = numberOfConnections[i];
				numberOfConnections[i] = numberOfConnections[minIndex];
				numberOfConnections[minIndex] = valueBuffer;
			}
		}
		
		//build result
		for (int i = 0; i < nodes.size(); i++)
		{
			float[] values = new float[1];
			values[0] = numberOfConnections[i];
			result.add(new PZKSQueueElement(nodes.get(i), values));
		}
		
		return result;
	}

}
