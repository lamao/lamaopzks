package pzks.model.listeners;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;
import java.util.ResourceBundle;

import pzks.model.generator.PZKSGraphGenerator;
import pzks.model.planners.assignment.PZKSAssignmentPlanner;
import pzks.model.planners.queue.PZKSQueueElement;
import pzks.model.planners.queue.PZKSQueuePlanner;
import pzks.service.PZKSAppStrings;
import pzks.service.PZKSButtonActionsManager;
import pzks.service.PZKSConfigurationFactory;
import pzks.ui.PZKSMainFrame;
import pzks.ui.dialogs.ImmerseDialog;
import pzks.ui.dialogs.PZKSAboutBox;
import pzks.ui.dialogs.PZKSAbstractGenerateDialog;
import pzks.ui.dialogs.PZKSGantDialog;

public class PZKSTopLevelListener implements ActionListener
{
	private PZKSMainFrame _owner = null;
	
	
	
	public PZKSTopLevelListener(PZKSMainFrame owner)
	{
		super();
		_owner = owner;
	}



	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().equals(PZKSButtonActionsManager.SHOW_IMMERSE_DIALOG_ACTION))
		{
			showImmerseDialog();
		}
		else if (event.getActionCommand().equals(PZKSButtonActionsManager.GENERATE_GRAPH_ACTION))
		{
			generateGraph();
		}
		else if (event.getActionCommand().equals(PZKSButtonActionsManager.EXIT_ACTION))
		{
		    _owner.dispose();
		}
		else if (event.getActionCommand().equals(PZKSButtonActionsManager.SHOW_ABOUT_BOX))
		{
		    showAboutBox();
		}
	}
	
	private void showImmerseDialog()
	{
	    ResourceBundle bundle = PZKSAppStrings.getBundle();
		ImmerseDialog dialog = PZKSConfigurationFactory.createImmerseDialog(null, 
		                bundle.getString(PZKSAppStrings.IMMERSE_DIALOG_TITLE_KEY));
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setVisible(true);

		// TODO Next peace of code is bletcherous
		if (dialog.isOk())
		{
			PZKSQueuePlanner queuePlanner = null;
			switch (dialog.getNumberOfQueue())
			{
			case 0:
				queuePlanner = PZKSConfigurationFactory
							.createFirstQueuePlanner(_owner.getTaskGraph());
				break;
			case 1:
				queuePlanner = PZKSConfigurationFactory
							.createSecondQueuePlanner(_owner.getTaskGraph());
				break;
			case 2:
				queuePlanner = PZKSConfigurationFactory
							.createThirdQueuePlanner(_owner.getTaskGraph());
				break;
			}

			Queue<PZKSQueueElement> queue = queuePlanner.getQueue();
			PZKSAssignmentPlanner assignmentPlanner = null;
			switch (dialog.getNumberOfAssignment())
			{
			case 0: assignmentPlanner = PZKSConfigurationFactory
						.createFirstAssignmentPlanner(queue, _owner.getSystemGraph());
					break;
			case 1: assignmentPlanner = PZKSConfigurationFactory
						.createSecondAssignmentPlanner(queue, _owner.getSystemGraph());
					break;
			}

			PZKSGantDialog gantDialog = new PZKSGantDialog(
					null, queue, assignmentPlanner);
			gantDialog.setModalityType(ModalityType.APPLICATION_MODAL);
			gantDialog.setVisible(true);
		}
	}
	
	public void generateGraph()
	{
		PZKSAbstractGenerateDialog dialog = PZKSConfigurationFactory
					.createGeneraetDialog(null);
		dialog.setVisible(true);
		if (dialog.getGraphInfo() != null)
		{
			PZKSGraphGenerator generator = new PZKSGraphGenerator(
					_owner.getTaskGraph(), dialog.getGraphInfo());

			generator.AutoGenerate();
		}
	}
	
	private void showAboutBox()
	{
	    PZKSAboutBox aboutBox = new PZKSAboutBox(null);
	    aboutBox.setVisible(true);
	}
}
