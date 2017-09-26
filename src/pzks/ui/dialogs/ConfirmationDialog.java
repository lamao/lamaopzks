package pzks.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import pzks.service.PZKSConfigurationReader;
import pzks.ui.PZKSMainFrame;


public class ConfirmationDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	JTextField logint;
	JPasswordField passwordt;
	PZKSMainFrame owner; 
	
	public ConfirmationDialog(Frame f, boolean modal)
	{
		super(f,"Autherization",modal);
		owner = (PZKSMainFrame)f;
		setFont(new Font("Sans-Serif",Font.PLAIN,12));
		
		JLabel loginl = new JLabel("Login:");
		JLabel passwordl = new JLabel("Password:");
		
		logint = new JTextField();
		passwordt = new JPasswordField();
		passwordt.setEchoChar('*');
		
		JButton confirm = new JButton("Ok");
		confirm.setActionCommand("Confirm");
		confirm.setPreferredSize(new Dimension(75, 20));
		JButton cancel = new JButton("Cancel");
		cancel.setActionCommand("Cancel");
		cancel.setPreferredSize(new Dimension(75, 20));
		
		Box vert1 = Box.createVerticalBox();
		vert1.add(Box.createVerticalStrut(15));
		vert1.add(loginl);
		vert1.add(Box.createVerticalStrut(10));
		vert1.add(passwordl);
		vert1.add(Box.createVerticalStrut(5));
		
		Box vert2 = Box.createVerticalBox();
		vert2.add(Box.createVerticalStrut(10));
		vert2.add(logint);
		vert2.add(Box.createVerticalStrut(5));
		vert2.add(passwordt);
		
		Box horiz1 = Box.createHorizontalBox();
		horiz1.add(Box.createHorizontalStrut(10));
		horiz1.add(vert1);
		horiz1.add(Box.createHorizontalStrut(15));
		horiz1.add(vert2);
		horiz1.add(Box.createHorizontalStrut(5));
				
		Box horiz2 = Box.createHorizontalBox();
		horiz2.add(Box.createHorizontalStrut(35));
		horiz2.add(confirm);
		horiz2.add(Box.createHorizontalGlue());
		horiz2.add(cancel);
		horiz2.add(Box.createHorizontalStrut(35));
		
		setLayout(new BorderLayout());
		add(horiz1, BorderLayout.NORTH);
		add(horiz2, BorderLayout.CENTER);
		
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		
		setSize(250,140);
		setLocationRelativeTo(f);
		setVisible(true);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev) 
			{
				owner.setAuthentificate(false);
				((JDialog)ev.getSource()).dispose();
			}
		});
	}

	
	public void actionPerformed(ActionEvent event) 
	{
		// TODO Auto-generated method stub
		if(event.getActionCommand().equals("Confirm"))
		{
			System.setProperty("login", logint.getText().trim());
			System.setProperty("password", String.valueOf(passwordt.getPassword()));
			
			//read application configuration
			if (!PZKSConfigurationReader.getDefaultReader().
					readConfiguration("resourses/configuration/Configuration.xml"))
			{
				JOptionPane.showMessageDialog(null, "Authorization failed. Bad login or password.");
			
			} 
			else 
			{
				owner.setAuthentificate(true);
				this.dispose();
			}
		} 
		else 
		{
			owner.setAuthentificate(false);
			this.dispose();
		}
	}
}
