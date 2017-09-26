package testes.service;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import pzks.service.PZKSButtonActionsManager;
import junit.framework.TestCase;

public class TestButtonActionManager extends TestCase
{

	public void testAll()
	{
		PZKSButtonActionsManager manager = PZKSButtonActionsManager.defaultManager();
		
		assertEquals(0, manager.getNumberOfButtons("some action"));
		
		manager.addAction("button", new JButton());
		manager.addAction("button", new JMenuItem());
		manager.addAction("otherButton", new JButton());
		
		assertEquals(2, manager.getNumberOfButtons("button"));
		assertEquals(1, manager.getNumberOfButtons("otherButton"));
		
		manager.reoveAction("button");
		
		assertEquals(0, manager.getNumberOfButtons("button"));
	}
}
