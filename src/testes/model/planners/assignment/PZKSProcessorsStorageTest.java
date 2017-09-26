//package testes.model.planners.assignment;
//
//import java.awt.Point;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//import pzks.model.PZKSGraph;
//import pzks.model.PZKSNode;
//import pzks.model.planners.assignment.PZKSAssignmentElement;
//import pzks.model.planners.assignment.PZKSProcessorsStorage;
//import pzks.model.planners.assignment.PZKSAssignmentElement.AssignmentType;
//import junit.framework.TestCase;
//
//public class PZKSProcessorsStorageTest extends TestCase
//{
//	private PZKSGraph buildTestGraph()
//	{
//		/*
//		 * 		4
//		 * 	   /  \
//		 * 	  1----2
//		 * 	   \  /
//		 * 		3
//		 */
//		PZKSGraph graph = new PZKSGraph(false);
//		PZKSNode node1 = new PZKSNode(new Point(0, 0), 0, 1);
//		PZKSNode node2 = new PZKSNode(new Point(0, 0), 1, 2);
//		PZKSNode node3 = new PZKSNode(new Point(0, 0), 2, 3);
//		PZKSNode node4 = new PZKSNode(new Point(0, 0), 3, 4);
//		
//		graph.addNode(node1);
//		graph.addNode(node2);
//		graph.addNode(node3);
//		graph.addNode(node4);
//		
//		graph.addConnection(node1, node2);
//		graph.addConnection(node2, node3);
//		graph.addConnection(node3, node1);
//		graph.addConnection(node1, node4);
//		graph.addConnection(node2, node4);
//		
//		return graph;
//	}
//	
//	private PZKSGraph buildTestTaskGraph()
//	{
//		/*
//		 * 	1	2
//		 * 	 \ /
//		 *    3
//		 *    1->3 = 1
//		 *    2->3 = 2
//		 */
//		PZKSGraph graph = new PZKSGraph(true);
//		PZKSNode node1 = new PZKSNode(new Point(0, 0), 1, 1);
//		PZKSNode node2 = new PZKSNode(new Point(0, 0), 2, 2);
//		PZKSNode node3 = new PZKSNode(new Point(0, 0), 3, 3);
//		
//		graph.addNode(node1);
//		graph.addNode(node2);
//		graph.addNode(node3);
//		
//		graph.addConnection(node1, node3);
//		graph.addConnection(node2, node3);
//		
//		graph.getConnection(node1, node3).setWeight(1);
//		graph.getConnection(node2, node3).setWeight(2);
//		
//		return graph;
//	}
//	
//	
//	public void testConstructors()
//	{
//		PZKSProcessorsStorage storage = 
//			new PZKSProcessorsStorage(buildTestGraph());
//		assertEquals(4, storage.getNumberOfProcessors());
//	}
//	
//	public void testPutNode()
//	{
//		PZKSGraph testGraph = buildTestTaskGraph();
//		ArrayList<PZKSNode> nodes = testGraph.getNodes();
//		PZKSProcessorsStorage storage = 
//				new PZKSProcessorsStorage(buildTestGraph());
//		
//		assertEquals(0, storage.getFirstFreeLink(0, 2, 10));
//		assertEquals(2, storage.getFirstFreeLink(2, 2, 10));
//		
//		assertEquals(4, storage.getFreeProcessors(0, 10).length);
//		assertEquals(4, storage.getFreeProcessors(2, 10).length);
//		
//		/*
//		 * ***------
//		 * ---------
//		 * ---------
//		 */
//		PZKSNode node = nodes.get(0);
//		node.setWeight(3);
//		storage.putNode(0,	// processor 
//						0, 	// start
//						node);	//node
//		assertTrue(storage.isAssigned(node));
//		assertTrue(storage.isCompleted(node, 3));
//		assertFalse(storage.isCompleted(node, 2));
//		assertFalse(storage.isAssigned(nodes.get(1)));
//		assertFalse(storage.isAssigned(nodes.get(2)));
//		assertEquals(3, storage.getFreeProcessors(0, 3).length);
//		assertEquals(4, storage.getFreeProcessors(3, 3).length);
//		
//		/*
//		 * ***------
//		 * --**-----
//		 * ---------
//		 */
//		node = nodes.get(1);
//		node.setWeight(2);
//		storage.putNode(1,		//processor 
//						2, 		//start
//						node);	//node
//		assertTrue(storage.isAssigned(node));
//		assertTrue(storage.isCompleted(node, 4));
//		assertEquals(2, storage.getFreeProcessors(0, 3).length);
//		assertEquals(3, storage.getFreeProcessors(3, 3).length);
//		assertEquals(2, storage.getFreeProcessors(2, 3).length);
//		assertEquals(4, storage.getFreeProcessors(4, 3).length);
//		assertEquals(3, storage.getFreeProcessors(1, 1).length);
//		
//		assertFalse(storage.isReady(nodes.get(2), 2));
//		assertFalse(storage.isReady(nodes.get(2), 3));
//		assertTrue(storage.isReady(nodes.get(2), 4));
//		
//	}
//	
//	public void testPutTrans()
//	{
//		PZKSProcessorsStorage storage = 
//				new PZKSProcessorsStorage(buildTestGraph());
//		
//		/*
//		 * processor 1) ***------
//		 */
//		storage.putTrans(1, storage.new PZKSAssignmentTransElement(
//				0, //processor 
//				1, //dest processor
//				1, //dest node
//				0, //start
//				3));//duration
//		assertEquals(4, storage.getFreeProcessors(0, 3).length);
//		assertEquals(4, storage.getFreeProcessors(3, 3).length);
//		assertEquals(3, storage.getFirstFreeLink(0, 1, 2));
//		assertEquals(3, storage.getFirstFreeLink(1, 1, 2));
//		
//		
//		/*
//		 * processor 1) 111------
//		 * 				--22-----
//		 */
//		storage.putTrans(2, storage.new PZKSAssignmentTransElement(
//				0, //processor 
//				2, //dest processor
//				2, //dest node
//				2, //start
//				2));//duration
//		assertEquals(0, storage.getFirstFreeLink(0, 2, 2));
//		assertEquals(4, storage.getFirstFreeLink(0, 2, 3));
//		assertEquals(3, storage.getFirstFreeLink(0, 1, 2));
//		
//	}
//	
//	public void testPutFullTrans()
//	{
//		PZKSProcessorsStorage storage = 
//			new PZKSProcessorsStorage(buildTestGraph());
//		
//		ArrayList<PZKSNode> nodes = buildTestTaskGraph().getNodes();
//		
//		/*
//		 * 0-----
//		 * 11----
//		 * ------
//		 * ------
//		 */
//		storage.putNode(0, 0, nodes.get(0));
//		storage.putNode(1, 0, nodes.get(1));
//		
//		int time = 0;
//		List<PZKSAssignmentElement> assignedElements = new LinkedList<PZKSAssignmentElement>();
//		
//		time = Math.max(time, storage.putFullTrans(
//				nodes.get(0), nodes.get(2), 0, assignedElements));
//		assertEquals(1, time);
//		assertEquals(0, assignedElements.size());
//		
//		time = Math.max(time, storage.putFullTrans(
//				nodes.get(1), nodes.get(2), 0, assignedElements));
//		assertEquals(4, time);
//		assertEquals(1, assignedElements.size());
//		assertEquals(AssignmentType.TRANSMISSION, assignedElements.get(0).type);
//		
//		/*
//		 * 0-222-
//		 * 11----
//		 * ------
//		 */
//		assignedElements.add(storage.putNode(0, time, nodes.get(2)));
//		
//		assertEquals(2, assignedElements.size());
//		assertEquals(AssignmentType.NODE, assignedElements.get(1).type);
//	}
//	
//	public void testWayCalculation()
//	{
//		PZKSProcessorsStorage storage = 
//			new PZKSProcessorsStorage(buildTestGraph());
//		
//		assertEquals(1, storage.getWayLengthBetweenProcessors(0, 1));
//		assertEquals(1, storage.getWayLengthBetweenProcessors(0, 2));
//		assertEquals(1, storage.getWayLengthBetweenProcessors(0, 3));
//		assertEquals(2, storage.getWayLengthBetweenProcessors(2, 3));
//		assertEquals(2, storage.getWayLengthBetweenProcessors(3, 2));
//		assertEquals(1, storage.getWayLengthBetweenProcessors(2, 1));
//		assertEquals(1, storage.getWayLengthBetweenProcessors(3, 1));
//		assertEquals(1, storage.getWayLengthBetweenProcessors(3, 0));		
//	}
//
//}
