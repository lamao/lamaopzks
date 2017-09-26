package pzks.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


import sun.awt.VerticalBagLayout;

public class PZKSLamaoImmerseDialog extends ImmerseDialog
{
	private static final long serialVersionUID = 1L;
	
	private JButton _okButton = null;
	private JButton _cancelButton = null;
	private JComboBox _queuePlanners = null;
	private JComboBox _assignmentPlanners = null;

	public PZKSLamaoImmerseDialog(Window owner, String title) 
	{
		super(owner, title);
		
		setLayout(new VerticalBagLayout());
		
		ImmerseDialogActionListener actionListener = 
					new ImmerseDialogActionListener(this);
		
		//********************** comboboxes *********************************
		JPanel contents = new JPanel();
		contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
		_queuePlanners = new JComboBox();
		
		String queueName = System.getProperty("queue1");
		queueName = queueName.substring(queueName.lastIndexOf('.') + 1);
		_queuePlanners.addItem(queueName);
		
		queueName = System.getProperty("queue2");
		queueName = queueName.substring(queueName.lastIndexOf('.') + 1);
		_queuePlanners.addItem(queueName);
		
		queueName = System.getProperty("queue3");
		queueName = queueName.substring(queueName.lastIndexOf('.') + 1);
		_queuePlanners.addItem(queueName);
		
		_assignmentPlanners = new JComboBox();
		
		String plannerName = System.getProperty("planner1");
		plannerName = plannerName.substring(plannerName.lastIndexOf('.') + 1);
		_assignmentPlanners.addItem(plannerName);
		
		plannerName = System.getProperty("planner2");
		plannerName = plannerName.substring(plannerName.lastIndexOf('.') + 1);
		_assignmentPlanners.addItem(plannerName);

		contents.add(new JLabel("Choose queue planner"));
		contents.add(_queuePlanners);
		contents.add(new JLabel("Choose assignment planner"));
		contents.add(_assignmentPlanners);
		//*******************************************************************
		
		//********************** buttons **************************
		JPanel buttons = new JPanel(new FlowLayout());
		
		_okButton = new JButton("OK");
		_okButton.setActionCommand("OK");
		_okButton.addActionListener(actionListener);
		
		_cancelButton = new JButton("Cancel");
		_cancelButton.setActionCommand("Cancel");
		_cancelButton.addActionListener(actionListener);
		
		buttons.add(_okButton);
		buttons.add(_cancelButton);
		//***********************************************************
		
		add(contents, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);
		
		validate();
		pack();
		
		
		setLocationRelativeTo(owner);		
		GraphicsEnvironment environtment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		setLocation(environtment.getCenterPoint().x - getSize().width / 2, 
					environtment.getCenterPoint().y - getSize().height / 2);
		
	}
	
	public int getNumberOfQueue() 
	{
		assert(_queuePlanners != null);
		return _queuePlanners.getSelectedIndex();
	}
	
	public int getNumberOfAssignment()
	{
		assert(_assignmentPlanners != null);
		return _assignmentPlanners.getSelectedIndex();
	}



	class ImmerseDialogActionListener implements ActionListener
	{
		private ImmerseDialog _dialog;
		
		public ImmerseDialogActionListener(ImmerseDialog dialog) 
		{
			_dialog = dialog;
		}

		public void actionPerformed(ActionEvent e) 
		{
			if (e.getActionCommand().equals("OK"))
			{
				_dialog.setOk(true);
				_dialog.dispose();				
			}
			else if (e.getActionCommand().equals("Cancel"))
			{
				_dialog.dispose();				
			}
			
		}
	}
}
