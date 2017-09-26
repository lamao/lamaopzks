package pzks.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pzks.model.PZKSGraph;
import pzks.model.PZKSSystemValidator;
import pzks.model.PZKSTaskValidator;
import pzks.model.listeners.PZKSMenuListener;
import pzks.model.listeners.PZKSTopLevelListener;
import pzks.service.PZKSAppStrings;
import pzks.service.PZKSButtonActionsManager;
import pzks.service.PZKSConfigurationFactory;
import pzks.service.PZKSConfigurationReader;
import pzks.service.XMLStorage;
import pzks.ui.dialogs.ConfirmationDialog;

public class PZKSMainFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	private PZKSGraphView _systemGraphView = null;
	private PZKSGraphView _taskGraphView = null;

	private PZKSGraph _systemGraph = null;
	private PZKSGraph _taskGraph = null;

	private JToolBar _toolbar = null;
	private JMenuBar _menu = null;
	private JTabbedPane tp = null;
	private boolean _authentificate = false;

	private ConfirmationDialog dialog;

	public PZKSGraph getSystemGraph()
	{
		return _systemGraph;
	}

	public PZKSGraph getTaskGraph()
	{
		return _taskGraph;
	}

	public void setSystemGraph(PZKSGraph aGraph)
	{
		_systemGraph = aGraph;
	}

	public void setTaskGraph(PZKSGraph aGraph)
	{
		_taskGraph = aGraph;
	}

	// *************** Main Window Adapter ***************
	public class MainWindowAdapter extends WindowAdapter
	{
		JFrame Frame;

		public MainWindowAdapter(JFrame Frame)
		{
			this.Frame = Frame;
		}

		public void windowClosing(WindowEvent ev)
		{
			System.exit(0);
		}
	}

	// ****************************************************

	private boolean isAuthentificate()
	{
		return _authentificate;
	}

	public void setAuthentificate(boolean auth)
	{
		_authentificate = auth;
	}

	public void initGraphView()
	{
		_systemGraph.reconfigureListeners();
		_taskGraph.reconfigureListeners();

		_systemGraph.setGraphValidator(new PZKSSystemValidator(_systemGraph));
		_taskGraph.setGraphValidator(new PZKSTaskValidator(_taskGraph));

		_systemGraphView.setGraph(_systemGraph);
		_systemGraphView.getRenderer().setGraph(_systemGraph);

		_taskGraphView.setGraph(_taskGraph);
		_taskGraphView.getRenderer().setGraph(_taskGraph);

		tp.setComponentAt(0, new JScrollPane(_systemGraphView));
		tp.setComponentAt(1, new JScrollPane(_taskGraphView));

	}

	public void authenticate()
	{
		dialog = new ConfirmationDialog(this, true);
		dialog.setVisible(true);
	}

	public void initialize()
	{
		buildComponentsFromXML();
		createGraphViews();
		buildTabs();		
		
		_taskGraphView.setReceptActions(false);
		addWindowListener(new MainWindowAdapter(this));
		subscribeOnActions();
	}
	
	private void subscribeOnActions()
	{
		PZKSTopLevelListener listener = new PZKSTopLevelListener(this);
		PZKSButtonActionsManager manager = PZKSButtonActionsManager.defaultManager();
		
		manager.addActionListener(PZKSButtonActionsManager.SHOW_IMMERSE_DIALOG_ACTION, listener);
		manager.addActionListener(PZKSButtonActionsManager.GENERATE_GRAPH_ACTION, listener);
		manager.addActionListener(PZKSButtonActionsManager.EXIT_ACTION, listener);
		manager.addActionListener(PZKSButtonActionsManager.SHOW_ABOUT_BOX, listener);
		
	}
	
	private void createGraphViews()
	{
		_systemGraphView = new PZKSGraphView(_systemGraph,
				PZKSConfigurationFactory.createRenderer(_systemGraph));
		_taskGraphView = new PZKSGraphView(_taskGraph, PZKSConfigurationFactory
				.createRenderer(_taskGraph));
	}
	
	private void buildComponentsFromXML() 
	{
		XMLStorage storage = new XMLStorage(System.getProperty("storage"));

		// -------------- Create toolbar
		_toolbar = (JToolBar) storage.get("ToolBar");
		add(_toolbar, BorderLayout.EAST);

		// -------------- Create menu
		_menu = (JMenuBar) storage.get("MenuBar");
		add(_menu, BorderLayout.NORTH);

		PZKSMenuListener listener = new PZKSMenuListener(this);

		// add listeners for menu
		JMenu subMenu = (JMenu) _menu.getMenu(0);
		for (int j = 0; j < subMenu.getMenuComponentCount(); j++)
		{
			if (subMenu.getMenuComponent(j) instanceof JMenuItem)
			{
				((JMenuItem) subMenu.getMenuComponent(j))
						.addActionListener(listener);
			}
		}
	}
	
	public void buildTabs()
	{
	    ResourceBundle bundle = PZKSAppStrings.getBundle();
	    
		tp = new JTabbedPane();
		tp.addTab(bundle.getString(PZKSAppStrings.SYSTEM_GRAPH_TAB_TITLE_KEY), 
		        new JScrollPane(_systemGraphView));
		tp.addTab(bundle.getString(PZKSAppStrings.TASK_GRAPH_TAB_TITLE_KEY), 
		        new JScrollPane(_taskGraphView));
		tp.addChangeListener(new FrameChangeListener());

		add(tp, BorderLayout.CENTER);
	}

	PZKSMainFrame(String aName)
	{
		super(aName);
		setBounds(300, 200, 500, 400);

		_systemGraph = new PZKSGraph(false);
		_systemGraph.setGraphValidator(new PZKSSystemValidator(_systemGraph));

		_taskGraph = new PZKSGraph(true);
		_taskGraph.setGraphValidator(new PZKSTaskValidator(_taskGraph));
	}

	// other methods

	public void addListeners(PZKSGraphView graphView)
	{
		assert (graphView != null);
		assert (_toolbar != null);
		assert (_menu != null);

		// add listeners for toolbar
		for (int i = 0; i < _toolbar.getComponentCount(); i++)
		{
			if (_toolbar.getComponent(i) instanceof JButton)
			{
				((JButton) _toolbar.getComponent(i))
						.addActionListener(graphView);
			}
		}

		// add listeners for menu
		for (int i = 1; i < _menu.getMenuCount(); i++)
		{
			JMenu subMenu = (JMenu) _menu.getMenu(i);

			for (int j = 0; j < subMenu.getMenuComponentCount(); j++)
			{
				if (subMenu.getMenuComponent(j) instanceof JMenuItem)
				{
					((JMenuItem) subMenu.getMenuComponent(j))
							.addActionListener(graphView);
				}
			}
		}
	}

	public void removeListeners(PZKSGraphView graphView)
	{
		assert (graphView != null);
		assert (_toolbar != null);
		assert (_menu != null);

		// add listeners for toolbar
		for (int i = 0; i < _toolbar.getComponentCount(); i++)
		{
			if (_toolbar.getComponent(i) instanceof JButton)
			{
				((JButton) _toolbar.getComponent(i))
						.removeActionListener(graphView);
			}
		}

		// add listeners for menu
		for (int i = 1; i < _menu.getMenuCount(); i++)
		{
			JMenu subMenu = (JMenu) _menu.getMenu(i);

			for (int j = 0; j < subMenu.getMenuComponentCount(); j++)
			{
				if (subMenu.getMenuComponent(j) instanceof JMenuItem)
				{
					((JMenuItem) subMenu.getMenuComponent(j))
							.removeActionListener(graphView);
				}
			}
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException
	{
		PZKSConfigurationReader.getDefaultReader().readConfiguration(
				"resourses/configuration/Configuration.xml");

		// setup look & feel

		if (System.getProperty("default-look").equals("true")
				|| System.getProperty("default-look").equals("yes"))
		{
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);

		}
		
		String lookAndFeel = System.getProperty("look-and-feel");
		if (lookAndFeel != null)
		{
			try
			{
				UIManager.setLookAndFeel(lookAndFeel);
			}
			catch (ClassNotFoundException ex)
			{
				System.out.println("This is not classname");
				for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels())
				{
					if (lookAndFeel.equals(laf.getName()))
					{
						UIManager.setLookAndFeel(laf.getClassName());
					}
				}
			}
		}
		
		SwingUtilities.invokeLater(new Runnable() 
			{
				public void run() 
				{
				    ResourceBundle bundle = PZKSAppStrings.getBundle();
					PZKSMainFrame frame = new PZKSMainFrame(bundle
					        .getString(PZKSAppStrings.MAIN_FRAME_TITLE_KEY));
					frame.initialize();
					frame.setVisible(true);
				}
			});
	}
	
	public class FrameChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent event)
		{
			if (event.getSource() instanceof JTabbedPane)
			{
				JTabbedPane pane = (JTabbedPane) event.getSource();

				ResourceBundle bundle = PZKSAppStrings.getBundle();
				if (pane.getTitleAt(pane.getSelectedIndex()).equals(
						bundle.getString(PZKSAppStrings.SYSTEM_GRAPH_TAB_TITLE_KEY)))
				{
					_taskGraphView.setReceptActions(false);
					_systemGraphView.setReceptActions(true);
				}
				else if (pane.getTitleAt(pane.getSelectedIndex()).equals(
				        bundle.getString(PZKSAppStrings.TASK_GRAPH_TAB_TITLE_KEY)))
				{
					_taskGraphView.setReceptActions(true);
					_systemGraphView.setReceptActions(false);
				}
			}
		}
	}
	
	
}
