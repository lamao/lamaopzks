package pzks.service;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;

public class PZKSButtonActionsManager
{
	public static final String SHOW_IMMERSE_DIALOG_ACTION = "ShowImmerseDialog";
	public static final String ADD_NODE_ACTION = "Add Node";
	public static final String REMOVE_NODE_ACTION = "Remove Node";
	public static final String ADD_CONNECTION_ACTION = "Add Connection";
	public static final String REMOVE_CONNECTION_ACTION = "Remove Connection";
	public static final String VALIDATE_ACTION = "Validate";
	public static final String GENERATE_GRAPH_ACTION = "Generate graph";
	public static final String EXIT_ACTION = "Exit";
	public static final String SHOW_ABOUT_BOX = "About box";
	
	private Map<String, List<AbstractButton>> _actionMap = null;
	private static PZKSButtonActionsManager _manager = null;
	
	public static PZKSButtonActionsManager defaultManager()
	{
		if (_manager == null)
		{
			_manager = new PZKSButtonActionsManager();
		}
		return _manager;
	}
	
	private Map<String, List<AbstractButton>>getActionMap()
	{
		if (_actionMap == null)
		{
			_actionMap = new HashMap<String, List<AbstractButton>>();
		}
		return _actionMap;
	}
	
	public int getNumberOfButtons(String actionName)
	{
		List<AbstractButton> buttons = getActionMap().get(actionName);
		return buttons == null ? 0 : buttons.size();
	}
	
	public void addAction(String actionName, AbstractButton produser)
	{
		List<AbstractButton> produsers = getActionMap().get(actionName);
		if (produsers == null)
		{
			produsers = new LinkedList<AbstractButton>();
			getActionMap().put(actionName, produsers);
		}
		if (!produsers.contains(produser))
		{
			produsers.add(produser);
		}
	}
	
	public void reoveAction(String actionName)
	{
		getActionMap().remove(actionName);
	}
	
	public void addActionListener(String actionName, ActionListener listener)
	{
		List<AbstractButton> buttons = getActionMap().get(actionName);
		if (buttons == null)
		{
			System.out.println("No action " + actionName);
		}
		else
		{
			for (AbstractButton button : buttons)
			{
				button.addActionListener(listener);
			}
		}
	}
	
	public void removeActionListener(String actionName, ActionListener listener)
	{
		List<AbstractButton> buttons = getActionMap().get(actionName);
		if (buttons == null)
		{
			System.out.println("No action " + actionName);
		}
		else
		{
			for (AbstractButton button : buttons)
			{
				button.removeActionListener(listener);
			}
		}
	}
}
