package pzks.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import pzks.model.generator.PZKSGraphInfo;

public class GenerateDialog extends PZKSAbstractGenerateDialog 
			implements ActionListener
{

	private static final long serialVersionUID = 1L;
	DefaultTableModel model;
	PZKSGraphInfo _graphInfo = null;
	
	public GenerateDialog(Frame f)
	{
		super(f);
		setTitle("Генерация графа");
		setFont(new Font("Sans-Serif", Font.PLAIN, 12));

		JTable OptionsTable = new JTable();
		initModel();

		OptionsTable.setTableHeader(null);
		OptionsTable.setModel(model);

		OptionsTable.setRowHeight(25);
		OptionsTable.setGridColor(Color.BLACK);
		OptionsTable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		OptionsTable.setBackground(UIManager.getColor("Panel.background"));

		OptionsTable.setCellSelectionEnabled(false);
		OptionsTable.setDefaultRenderer(Object.class,
				new DefTableCellRenderer());
		OptionsTable.getColumnModel().getColumn(1).setCellEditor(
				new SpinnerCellEditor());
		OptionsTable.getColumnModel().getColumn(1).setMinWidth(10);
		OptionsTable.getColumnModel().getColumn(1).setPreferredWidth(10);

		JButton confirm = new JButton("Ok");
		confirm.setActionCommand("Confirm");
		confirm.setPreferredSize(new Dimension(75, 20));
		JButton cancel = new JButton("Cancel");
		cancel.setActionCommand("Cancel");
		cancel.setPreferredSize(new Dimension(75, 20));

		Box table = Box.createHorizontalBox();
		table.add(Box.createHorizontalStrut(5));
		table.add(OptionsTable);
		table.add(Box.createHorizontalStrut(5));

		Box options = Box.createVerticalBox();
		options.add(Box.createVerticalStrut(5));
		options.add(table);
		options.add(Box.createVerticalStrut(2));

		Box panel = Box.createHorizontalBox();
		panel.add(Box.createHorizontalStrut(35));
		panel.add(confirm);
		panel.add(Box.createHorizontalGlue());
		panel.add(cancel);
		panel.add(Box.createHorizontalStrut(35));

		confirm.addActionListener(this);
		cancel.addActionListener(this);

		setLayout(new BorderLayout());
		add(options, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);

		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent ev)
			{
				((JDialog) ev.getSource()).dispose();
			}
		});

		setSize(248, 250);
		setLocationRelativeTo(f);
    }
	
	
	private void initModel(){
		
		String[] labelNames = { 
				"Связность (%)", "Количество вершин", "Мин. вес вершины", 
				"Макс. вес вершины", "Мин. вес связи", "Макс. вес связи"
		};
		
		Integer[] labelValues = {50, 5, 1, 10, 1, 10};
		
		model = new DefaultTableModel(0,2){
			

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				if (column != 0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			public Class<?> getColumnClass(int column)
			{
				if (column != 0)
				{
					return Integer.class;
				}
				else
				{
					return String.class;
				}
			}
		};

		for (int i = 0; i < labelValues.length; i++)
		{
			model.addRow(new Object[] { labelNames[i], labelValues[i] });
		}
	}

	public PZKSGraphInfo getGraphInfo()
	{
		return _graphInfo;
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
			return l;
		}
	}

	private class SpinnerCellEditor extends AbstractCellEditor implements
			TableCellEditor
	{
		private static final long serialVersionUID = 1L;

		private JSpinner editor;
		
		public SpinnerCellEditor(){
			SpinnerNumberModel model = new SpinnerNumberModel();
			editor = new JSpinner(model);
			editor.setMinimumSize(new Dimension(10,25));
			editor.setPreferredSize(new Dimension(10,25));
		}
		
		public java.awt.Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
			editor.setValue(value);
			return editor;
		}

		public Object getCellEditorValue()
		{
			return editor.getValue();
		}
	}

	public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().equals("Confirm"))
		{
			_graphInfo = new PZKSGraphInfo(
					((Integer)model.getValueAt(0, 1)).intValue(), 
					((Integer)model.getValueAt(1, 1)).intValue(),
					((Integer)model.getValueAt(2, 1)).intValue(), 
					((Integer)model.getValueAt(3, 1)).intValue(),
					((Integer)model.getValueAt(4, 1)).intValue(),
					((Integer)model.getValueAt(5, 1)).intValue()); 
					
			
			if(_graphInfo.Validate()) 
			{
				dispose();
			}
		}
		else
		{
			dispose();
		}
	}

}
