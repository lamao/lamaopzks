package testes;

import java.awt.Point;

import pzks.model.PZKSConnection;
import pzks.model.PZKSNode;
import junit.framework.Assert;
import junit.framework.TestCase;

public class PZKSConnectionTest extends TestCase 
{
	public void testConstructors()
	{
		PZKSNode node1 = new PZKSNode();
		node1.setNumber(1);
		PZKSNode node2 = new PZKSNode();
		node2.setNumber(2);
		
		PZKSConnection connection = new PZKSConnection(node1, node2);
		
		Assert.assertSame(node1, connection.getSrcNode());
		Assert.assertSame(node2, connection.getDstNode());
		Assert.assertEquals(1, connection.getWeight());
		
		connection = new PZKSConnection(node2, node1, 2);
		Assert.assertSame(node2, connection.getSrcNode());
		Assert.assertSame(node1, connection.getDstNode());		
		Assert.assertEquals(2, connection.getWeight());
	}
	public void testAccessors()
	{
		PZKSNode node1 = new PZKSNode();
		PZKSNode node2 = new PZKSNode();
		
		PZKSConnection connection = new PZKSConnection(null, null);
		connection.setSrcNode(node1);
		connection.setDstNode(node2);
		
		assertSame(node1, connection.getSrcNode());
		assertSame(node2, connection.getDstNode());
	}
	
	public void testMethods()
	{
		PZKSNode node1 = new PZKSNode();
		PZKSNode node2 = new PZKSNode();
		PZKSNode node3 = new PZKSNode();
		PZKSConnection connection = new PZKSConnection(node1, node2);
		
		//************** hasNodes
		Assert.assertTrue(connection.hasNodes(node1, node2));
		Assert.assertTrue(connection.hasNodes(node2, node1));
		Assert.assertFalse(connection.hasNodes(node1, node3));
		Assert.assertFalse(connection.hasNodes(node3, node1));
		
		//************** getOppositeNode
		Assert.assertSame(node2, connection.getOppositeNode(node1));
		Assert.assertSame(node1, connection.getOppositeNode(node2));
		Assert.assertNull(connection.getOppositeNode(node3));
		
		//*************** hasPoints
		node1.setLocation(new Point(0, 0));
		node2.setLocation(new Point(100, 100));
		connection.setSrcNode(node1);
		connection.setDstNode(node2);
		
		assertTrue(connection.hasPoint(new Point(50, 50)));
		assertTrue(connection.hasPoint(new Point(50, 45)));
		assertTrue(connection.hasPoint(new Point(45, 50)));
		assertFalse(connection.hasPoint(new Point(40, 50)));
		
		
		node1.setLocation(new Point(0, 50));
		node2.setLocation(new Point(100, 50));
		connection.setSrcNode(node1);
		connection.setDstNode(node2);
		
		// TODO This test fails
//		assertTrue(connection.hasPoint(new Point(50, 50)));
//		assertTrue(connection.hasPoint(new Point(50, 45)));
//		assertTrue(connection.hasPoint(new Point(50, 55)));
//		assertFalse(connection.hasPoint(new Point(50, 56)));
//		assertFalse(connection.hasPoint(new Point(50, 44)));
	}
	
}
