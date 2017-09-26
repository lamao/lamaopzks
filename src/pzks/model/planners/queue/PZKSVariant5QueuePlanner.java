package pzks.model.planners.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;

public class PZKSVariant5QueuePlanner extends PZKSQueuePlanner
{
	public PZKSVariant5QueuePlanner(PZKSGraph graph)
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
		
		//get criticalNK pathes
		int[] criticalPathes = PZKSParametersCalculator.getCriticalNK(getGraph(),true);
		
		
		//sort nodes
		for (int i = 0; i < nodes.size()-1; i++) {
			int minIndex = i;
			
			for (int k = i + 1; k < nodes.size(); k++) {
				if (criticalPathes[k] > criticalPathes[minIndex]) {
					minIndex = k;
				}
			}
			
			if (i != minIndex) {
				PZKSNode buffer = nodes.get(i);
				nodes.set(i, nodes.get(minIndex));
				nodes.set(minIndex, buffer);
				
				int valueBuffer = criticalPathes[i];
				criticalPathes[i] = criticalPathes[minIndex];
				criticalPathes[minIndex] = valueBuffer;
			}
		}
		
		//build result
		for (int i = 0; i < nodes.size(); i++)
		{
			float[] values = new float[1];
			values[0] = criticalPathes[i];
			result.add(new PZKSQueueElement(nodes.get(i), values));
		}
		
		return result;
	}

}
