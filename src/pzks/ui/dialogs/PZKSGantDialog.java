package pzks.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pzks.model.planners.assignment.PZKSAssignmentElement;
import pzks.model.planners.assignment.PZKSAssignmentPlanner;
import pzks.model.planners.queue.PZKSQueueElement;
import pzks.service.PZKSConfigurationFactory;
import pzks.ui.PZKSGantView;
import pzks.ui.renderer.PZKSGantRenderer;

public class PZKSGantDialog extends JDialog
{

	private static final long serialVersionUID = 1L;
	
	private PZKSAssignmentPlanner _assignmentPlanner = null;
	private PZKSGantRenderer _renderer = null;
	private Queue<List<PZKSAssignmentElement>> _elements = 
							new LinkedList<List<PZKSAssignmentElement>>();
	
	public PZKSGantDialog(Window owner, Queue<PZKSQueueElement> queue,
			PZKSAssignmentPlanner planner)
	{
		super();
		_assignmentPlanner = planner;
		
		// reset assignment planner
		_assignmentPlanner.reset();
		
		// build queue of assignment
		_elements = new LinkedList<List<PZKSAssignmentElement> >();
		
		
		// create gant renderer
		_renderer = PZKSConfigurationFactory.createGantRenderer(_elements);
		_renderer.setMaxClockTime(_assignmentPlanner.getMaxClockTime());
		_renderer.setNumberOfProcessorrs(_assignmentPlanner.getNumberOfProcessors());
		
		//create gant view
		PZKSGantView view = new PZKSGantView(_elements, _renderer);
		add(view, BorderLayout.CENTER);
		
		
		// create top label with queue
		add(createQueueLabel(queue), BorderLayout.NORTH);
		add(createBottomPanel(), BorderLayout.SOUTH);
		
		
		// set dialog's position
		setLocationRelativeTo(owner);		
		GraphicsEnvironment environtment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		setSize(400, 300);
		setLocation(environtment.getCenterPoint().x - getSize().width / 2, 
					environtment.getCenterPoint().y - getSize().height / 2);
	}
	
	/**
	 * Creates the label for show incoming queue of nodes
	 * @param queue
	 * @return
	 */
	private JLabel createQueueLabel(Queue<PZKSQueueElement> queue)
	{
		String text = "<html>";
		for (PZKSQueueElement element : queue)
		{
			
			text += "<b color = red>" + element.node.getNumber() + 
					"</b> <i color = gray> (";
			for (int i = 0; i < element.values.length; i++)
			{
				text += element.values[i] + ", ";
			}
			text += ") </i> ";
		}
		text += "</html>";
		
		return new JLabel(text);
	}
	
	/**
	 * Creates bottom panel where user can set number of processors and
	 * reassign nodes
	 * @return
	 */
	private JPanel createBottomPanel()
	{
		JPanel panel = new JPanel(new FlowLayout());
		
		PanelActionListener listener = new PanelActionListener();
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(listener);
		
		JButton stepButton = new JButton("Step");
		stepButton.setActionCommand("Step");
		stepButton.addActionListener(listener);
		
		JButton allButton = new JButton("All");
		allButton.setActionCommand("All");
		allButton.addActionListener(listener);
		
		panel.add(okButton);
		panel.add(stepButton);
		panel.add(allButton);
		
		return panel;
	}
	
	
	private class PanelActionListener implements ActionListener
	{

		public void actionPerformed(ActionEvent event)
		{
			if (event.getActionCommand().equals("OK"))
			{
				dispose();
			}
			else if (event.getActionCommand().equals("Step"))
			{
				stepAction();
			}
			else if (event.getActionCommand().equals("All"))
			{
				allAction();
			}
		}
		
		private void stepAction()
		{
			List<PZKSAssignmentElement> elements = 
				_assignmentPlanner.getNextElement();
			if (elements == null)
			{
				JOptionPane.showMessageDialog(null, 
						"All nodes have been assigned", "The End", 
						JOptionPane.INFORMATION_MESSAGE);	
			}
			else
			{
				_elements.add(elements);
				_renderer.setMaxClockTime(_assignmentPlanner.getMaxClockTime());
				repaint();
			}
		}
		
		private void allAction()
		{
			List<PZKSAssignmentElement> elements = null;
			do
			{
				elements = _assignmentPlanner.getNextElement();
				if (elements != null)
				{
					_elements.add(elements);
				}
			}
			while (elements != null);
			
			_renderer.setMaxClockTime(_assignmentPlanner.getMaxClockTime());
			repaint();
			
			JOptionPane.showMessageDialog(null, 
					"All nodes have been assigned\n" +
					"Total time is " + _assignmentPlanner.getMaxClockTime() +
					"\n T is " + _assignmentPlanner.getEstimatedTimeOnOneProcessor() +
					"\n Ku is " + getKU() + 
					"\n Kef is " + getKef(), 
					"The End", 
					JOptionPane.INFORMATION_MESSAGE);
		}
		
		private float getKU()
		{
			return (float)_assignmentPlanner.getEstimatedTimeOnOneProcessor() / 
						_assignmentPlanner.getMaxClockTime();
		}
		
		private float getKef()
		{
			return getKU() / _assignmentPlanner.getNumberOfProcessors();
		}
	}

}
