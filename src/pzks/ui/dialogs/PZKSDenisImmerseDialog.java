package pzks.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;



public class PZKSDenisImmerseDialog extends ImmerseDialog
{
	private static final long serialVersionUID = 1L;
	
	private JButton _okButton = null;
	private JButton _cancelButton = null;
	private JComboBox _queuePlanners = null;
	private JComboBox _assignmentPlanners = null;

		
	public PZKSDenisImmerseDialog(Window owner, String title) 
	{
		super(owner, title);
		setFont(new Font("Sans-Serif", Font.PLAIN, 12));

		ImmerseDialogActionListener actionListener = 
				new ImmerseDialogActionListener(this);
	
		//********************** combo-boxes *********************************
		_queuePlanners = new JComboBox();
		_queuePlanners.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
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
		_assignmentPlanners.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
		String plannerName = System.getProperty("planner1");
		plannerName = plannerName.substring(plannerName.lastIndexOf('.') + 1);
		_assignmentPlanners.addItem(plannerName);
	
		plannerName = System.getProperty("planner2");
		plannerName = plannerName.substring(plannerName.lastIndexOf('.') + 1);
		_assignmentPlanners.addItem(plannerName);
		//*******************************************************************
	
		//************************ tables ***********************************
		JTable _queueTable = new JTable();
		_queueTable.setTableHeader(null);
		_queueTable.setModel(new DefaultTableModel(1,2){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				if (column != 0) {
					return true;
				} else {
					return false;
				}
			}});

		_queueTable.setRowHeight(25);
		_queueTable.setGridColor(Color.BLACK);
		_queueTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		_queueTable.setBackground(UIManager.getColor("Panel.background"));
		_queueTable.setDefaultRenderer(Object.class, new DefTableCellRenderer());
		_queueTable.getColumnModel().getColumn(1).setCellEditor(
			new DefaultCellEditor(_queuePlanners));
		_queueTable.getColumnModel().getColumn(0).setMaxWidth(140);
		_queueTable.getColumnModel().getColumn(1).setMaxWidth(250);
		((DefaultTableModel)_queueTable.getModel()).setValueAt("Queue planner", 0, 0);
		((DefaultTableModel)_queueTable.getModel()).setValueAt(_queuePlanners.getItemAt(0), 0, 1);
	
	
		JTable _assignmentTable = new JTable();
		_assignmentTable.setTableHeader(null);
		_assignmentTable.setModel(new DefaultTableModel(1,2){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				if (column != 0) {
					return true;
				} else {
					return false;
				}
			}});

		_assignmentTable.setRowHeight(25);
		_assignmentTable.setGridColor(Color.BLACK);
		_assignmentTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		_assignmentTable.setBackground(UIManager.getColor("Panel.background"));
		_assignmentTable.setDefaultRenderer(Object.class, new DefTableCellRenderer());
		_assignmentTable.getColumnModel().getColumn(1).setCellEditor(
			new DefaultCellEditor(_assignmentPlanners));
		_assignmentTable.getColumnModel().getColumn(0).setMaxWidth(140);
		_assignmentTable.getColumnModel().getColumn(1).setMaxWidth(250);
		((DefaultTableModel)_assignmentTable.getModel()).setValueAt("Assignment planner", 0, 0);
		((DefaultTableModel)_assignmentTable.getModel()).setValueAt(_assignmentPlanners.getItemAt(0), 0, 1);
	
		//********************** buttons **************************
		_okButton = new JButton("OK");
		_okButton.setActionCommand("OK");
		_okButton.setPreferredSize(new Dimension(100, 20));
		_okButton.addActionListener(actionListener);
			
		_cancelButton = new JButton("Cancel");
		_cancelButton.setActionCommand("Cancel");
		_cancelButton.setPreferredSize(new Dimension(100, 20));
		_cancelButton.addActionListener(actionListener);
		//***********************************************************
			
		//*********************** rotation **************************	
		Box table1 = Box.createHorizontalBox();
		table1.add(Box.createHorizontalStrut(5));
		table1.add(_queueTable);
		table1.add(Box.createHorizontalStrut(5));

		Box table2 = Box.createHorizontalBox();
		table2.add(Box.createHorizontalStrut(5));
		table2.add(_assignmentTable);
		table2.add(Box.createHorizontalStrut(5));

		Box options = Box.createVerticalBox();
		options.add(Box.createVerticalStrut(5));
		options.add(table1);
		options.add(Box.createVerticalStrut(0));
		options.add(table2);
		options.add(Box.createVerticalStrut(2));

		Box panel = Box.createHorizontalBox();
		panel.add(Box.createHorizontalStrut(35));
		panel.add(_okButton);
		panel.add(Box.createHorizontalGlue());
		panel.add(_cancelButton);
		panel.add(Box.createHorizontalStrut(35));

		setLayout(new BorderLayout());
		add(options, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
	
		setSize(340, 140);
		setLocationRelativeTo(owner);		
	  	setVisible(true);
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
	
	
	private class DefTableCellRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;

		public DefTableCellRenderer()
		{
			super();
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			JComponent l = (JComponent) super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
			l.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
			l.setBackground(UIManager.getColor("Panel.background"));
			return l;
		}
	}
}
