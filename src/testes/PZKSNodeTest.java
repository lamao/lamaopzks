package testes;

import java.awt.Point;
import javax.swing.event.ChangeEvent;

import pzks.model.PZKSNode;
import pzks.model.listeners.PZKSModelListener;
import junit.framework.Assert;
import junit.framework.TestCase;

public class PZKSNodeTest extends TestCase
{

	private class NodeListener implements PZKSModelListener
	{
		public int numberOfChanges = 0;

		public void changeState(ChangeEvent event) 
		{
			numberOfChanges++;
		}
	}
	
	public void testConstructors()
	{
		PZKSNode node = new PZKSNode();
		
		Assert.assertEquals(node.getNumber(), 0);
		Assert.assertEquals(0, node.getWeight());
		Assert.assertTrue(node.getLocation().equals(new Point(0, 0)));
		Assert.assertEquals(node.getNumberOfConnections(), 0);
		Assert.assertEquals(node.getNumberOfOutgoingConnection(), 0);
		
		node = new PZKSNode(new Point(10, 10), 12, 2);
		
		Assert.assertEquals(node.getNumber(), 12);
		Assert.assertEquals(2, node.getWeight());
		Assert.assertTrue(node.getLocation().equals(new Point(10, 10)));
		Assert.assertEquals(node.getNumberOfConnections(), 0);
		Assert.assertEquals(node.getNumberOfOutgoingConnection(), 0);
	}
	
	public void testAccessors()
	{
		PZKSNode node = new PZKSNode();
		node.setLocation(new Point(10, 20));
		node.setNumber(8);
		node.setWeight(3);
		
		Assert.assertEquals(node.getNumber(), 8);
		Assert.assertEquals(3, node.getWeight());
		Assert.assertTrue(node.getLocation().equals(new Point(10, 20)));
	}
	
	public void testEvents()
	{
		PZKSNode node = new PZKSNode();
		NodeListener listener = new NodeListener();
		node.addModelListner(listener);
		
		node.setLocation(new Point(10, 20));
		node.setNumber(2);
		node.setWeight(2);
		
		node.setNumber(2);
		node.setLocation(new Point(10, 20));
		node.setWeight(2);
		
		Assert.assertEquals(3, listener.numberOfChanges);
	}
}
