package pzks.model.planners.queue;

import java.util.Queue;

import pzks.model.PZKSGraph;

/**
 * This class is abstract for all planners.
 * The main methods are getQueue.
 * In your subclasses you only need to realize 
 * getQueue method which should return 
 * planned queue of nodes based on _graph.
 * @author lamao
 * @see PZKSVariant11Planner
 * @see PZKSVariant9Planner
 *
 */
public abstract class PZKSQueuePlanner 
{
	private PZKSGraph _graph = null;
	
	
	//************** initializers
	public PZKSQueuePlanner(PZKSGraph graph)
	{
		_graph = graph;
	}
	public PZKSQueuePlanner() 
	{
		this(null);
	}
	
	//************* accessors
	public PZKSGraph getGraph() {return _graph;}
	public void setGraph(PZKSGraph graph) {_graph = graph;}
	public abstract String getName(); // name for this planner
	
	//************* other methods
	
	public abstract Queue<PZKSQueueElement> getQueue();
	
}
