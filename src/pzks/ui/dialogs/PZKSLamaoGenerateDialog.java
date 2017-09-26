package pzks.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pzks.model.generator.PZKSGraphInfo;

public class PZKSLamaoGenerateDialog extends PZKSAbstractGenerateDialog
{
	private static final long serialVersionUID = 1L;
	
	JTextField _connectivity = null;
	JTextField _numberOfNodes = null;
	JTextField _minWeightOfNodes = null;
	JTextField _maxWeightOfNodes = null;
	JTextField _minWeightOfLinks = null;
	JTextField _maxWeightOfLinks = null;
	
	
	public PZKSLamaoGenerateDialog(Frame owner)
	{
		super(owner);
		setTitle("Generate dialog");
		
		add(createContentPanel(), BorderLayout.CENTER);
		add(createButtonsPanel(), BorderLayout.SOUTH);
		
		// set dialog's position
		setLocationRelativeTo(owner);		
		GraphicsEnvironment environtment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		validate();
		pack();
		setLocation(environtment.getCenterPoint().x - getSize().width / 2, 
					environtment.getCenterPoint().y - getSize().height / 2);
		setResizable(false);
	}
	
	private JPanel createContentPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		_connectivity = new JTextField("50");
		_connectivity.setMinimumSize(new Dimension(50, 10));
		_numberOfNodes = new JTextField("10");
		_minWeightOfNodes = new JTextField("1");
		_maxWeightOfNodes = new JTextField("10");
		_minWeightOfLinks = new JTextField("1");
		_maxWeightOfLinks = new JTextField("10");
		
		panel.add(new JLabel("Connectivity"));
		panel.add(_connectivity);
		panel.add(new JLabel("Number of nodes"));
		panel.add(_numberOfNodes);
		panel.add(new JLabel("Min weight of nodes"));
		panel.add(_minWeightOfNodes);
		panel.add(new JLabel("Max weight of nodes"));
		panel.add(_maxWeightOfNodes);
		panel.add(new JLabel("Min weight of links"));
		panel.add(_minWeightOfLinks);
		panel.add(new JLabel("Max weight of links"));
		panel.add(_maxWeightOfLinks);
		
		return panel;
	}
	
	private JPanel createButtonsPanel()
	{
		JPanel panel = new JPanel();
		
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		
		ButtonsActionListener listener = new ButtonsActionListener();
		okButton.addActionListener(listener);
		cancelButton.addActionListener(listener);
		
		
		panel.add(okButton);
		panel.add(cancelButton);
		return panel;
	}
	
	private class ButtonsActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent event)
		{
			if (event.getActionCommand().equals("OK"))
			{
					setGraphInfo(new PZKSGraphInfo(
							Integer.parseInt(_connectivity.getText()),
							Integer.parseInt(_numberOfNodes.getText()),
							Integer.parseInt(_minWeightOfNodes.getText()),
							Integer.parseInt(_maxWeightOfNodes.getText()),
							Integer.parseInt(_minWeightOfLinks.getText()),
							Integer.parseInt(_maxWeightOfLinks.getText())));
					if (getGraphInfo().Validate())
					{
						dispose();
					}
			}
			else if (event.getActionCommand().equals("Cancel"))
			{
				dispose();
			}
		}
		
	}

}
