package pzks.model.planners.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;

public class PZKSVariant1QueuePlanner extends PZKSQueuePlanner
{
	public PZKSVariant1QueuePlanner(PZKSGraph graph)
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
		int[] criticalNK_Pathes = PZKSParametersCalculator.getCriticalNK(getGraph(),true);
		
		//get criticalTK pathes
		float[] criticalTK_Pathes = PZKSParametersCalculator.getCriticalTK(getGraph(),true);
			
		
		//get boundary TK & NK pathes
		int boundaryNK_Pathes = criticalNK_Pathes[0];
		float boundaryTK_Pathes = criticalTK_Pathes[0];

		for(int i=1; i<nodes.size(); i++){
			if(criticalNK_Pathes[i] > boundaryNK_Pathes){
				boundaryNK_Pathes = criticalNK_Pathes[i];
			}
			if(criticalTK_Pathes[i] > boundaryTK_Pathes){
				boundaryTK_Pathes = criticalTK_Pathes[i];
			}
		}
		
		//get norming sum
		float [] priority = new float[nodes.size()];
		
		for(int i=0; i< nodes.size(); i++){
			priority[i] = criticalTK_Pathes[i]/boundaryTK_Pathes + 
				(float)criticalNK_Pathes[i]/boundaryNK_Pathes;
		}
				
		//sort nodes
		for (int i = 0; i < nodes.size()-1; i++) {
			int minIndex = i;
			
			for (int k = i + 1; k < nodes.size(); k++) {
				if (priority[k] > priority[minIndex]) {
					minIndex = k;
				}
			}
			
			if (i != minIndex) {
				PZKSNode buffer = nodes.get(i);
				nodes.set(i, nodes.get(minIndex));
				nodes.set(minIndex, buffer);
				
				float valueBuffer = priority[i];
				priority[i] = priority[minIndex];
				priority[minIndex] = valueBuffer;
			}
		}
		
		//build result
		for (int i = 0; i < nodes.size(); i++) {
			float[] values = new float[1];
			values[0] = priority[i];
			result.add(new PZKSQueueElement(nodes.get(i), values));
		}
		
		return result;
	}

}
