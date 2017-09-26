package testes.model.planners.queue;

import java.awt.Point;
import java.util.Queue;

import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;
import pzks.model.planners.queue.PZKSQueueElement;
import pzks.model.planners.queue.PZKSVariant2QueuePlanner;
import junit.framework.TestCase;

public class TestVariant2QueuePlanner extends TestCase
{
	private PZKSGraph buildTestTaskGraph()
	{
		/*
		 * 	1	2
		 * 	 \ /
		 *    3
		 *    1->3 = 1
		 *    2->3 = 2
		 */
		PZKSGraph graph = new PZKSGraph(true);
		PZKSNode node1 = new PZKSNode(new Point(0, 0), 1, 1);
		PZKSNode node2 = new PZKSNode(new Point(0, 0), 2, 2);
		PZKSNode node3 = new PZKSNode(new Point(0, 0), 3, 3);
		
		graph.addNode(node1);
		graph.addNode(node2);
		graph.addNode(node3);
		
		graph.addConnection(node1, node3);
		graph.addConnection(node2, node3);
		
		graph.getConnection(node1, node3).setWeight(1);
		graph.getConnection(node2, node3).setWeight(2);
		
		return graph;
	}
	
	public void testPlanning()
	{
		PZKSVariant2QueuePlanner planner = new PZKSVariant2QueuePlanner(null);		
		assertNull(planner.getGraph());
		assertEquals("Variant 2", planner.getName());
		
		planner.setGraph(buildTestTaskGraph());
		assertNotNull(planner.getGraph());
		
		Queue<PZKSQueueElement> queue = planner.getQueue();
		
		
	}
}
