package pzks.model.planners.queue;

import pzks.model.PZKSNode;

/**
 * This is the element which represent
 * on node in queue after planning. Values 
 * array represents all values, which were
 * used for planning.
 * @author lamao
 *
 */
public class PZKSQueueElement 
{
	public PZKSNode node = null;
	public float[] values;
	
	public PZKSQueueElement(PZKSNode aNode, float[] aValues)
	{
		node = aNode;
		values = aValues;
	}
	public PZKSQueueElement() 
	{
		this(null, null);
	}
}
