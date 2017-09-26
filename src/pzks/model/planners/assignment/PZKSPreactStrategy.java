package pzks.model.planners.assignment;

import java.util.ArrayList;
import java.util.List;

import pzks.model.PZKSNode;
import pzks.model.planners.assignment.PZKSProcessorsStorage.PZKSAssignmentNodeElement;
import pzks.model.planners.assignment.PZKSProcessorsStorage.PZKSAssignmentTransElement;

public class PZKSPreactStrategy extends PZKSAssignmentStrategy
{
	
	@Override
	public int putFullTrans(PZKSNode srcNode, PZKSNode dstNode, int dstProc,
			List<PZKSAssignmentElement> assignedTransitions)
	{
		PZKSAssignmentNodeElement srcElement = getStorage().getNodesMap().get(srcNode);

		int result = 0;
		if (srcElement.numberOfProccessor == dstProc)
		{
			result = srcElement.start + srcElement.duration;
		}
		else
		{
			ArrayList<Integer> way = getStorage().getWayBetweenProcessors(
					srcElement.numberOfProccessor, dstProc);

			int time = srcElement.start + srcElement.duration;
			int proc = srcElement.numberOfProccessor;
			int weightOfLink = srcNode.getConnectionTo(dstNode).getWeight();
			for (int i = 0; i < way.size(); i++)
			{
				time = Math.max(getStorage().getFirstFreeLink(time, way.get(i), weightOfLink),
								getStorage().getFirstFreeLink(time, proc, weightOfLink));
				time = Math.max(time, result);
				result = time + weightOfLink;
				
				PZKSAssignmentTransElement trans = getStorage()
						.new PZKSAssignmentTransElement(
						proc, 					// src proc
						way.get(i), 				// dest proc
						dstNode.getNumber(),// dest node
						time,				// start
						weightOfLink);		// duration
				getStorage().putTrans(way.get(i), trans);
				getStorage().putTrans(proc, trans);
				
				if (assignedTransitions != null)
				{
					assignedTransitions.add(trans);
				}
				
				proc = way.get(i);
			}
		}
		return result;
	}
}
