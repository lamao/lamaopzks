package pzks.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pzks.service.PZKSAppStrings;

import sun.awt.VerticalBagLayout;

public class PZKSAboutBox extends JDialog
{

    public PZKSAboutBox(Frame owner)
    {
        super(owner);
        setModalityType(ModalityType.APPLICATION_MODAL);
        
        ResourceBundle bundle = PZKSAppStrings.getBundle();
        setTitle(bundle.getString(PZKSAppStrings.ABOUT_BOX_TITLE_KEY));
        
        add(getCentralPane(), BorderLayout.CENTER);
        add(getBottmPanel(), BorderLayout.SOUTH);
        
        validate();
        pack();
        setLocationRelativeTo(owner);       
        GraphicsEnvironment environtment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        setLocation(environtment.getCenterPoint().x - getSize().width / 2, 
                    environtment.getCenterPoint().y - getSize().height / 2);
        setResizable(false);
    }
    
    private JPanel getCentralPane()
    {
        JLabel title = new JLabel(
                "<html>" + 
                "<center><h1><i><u><font color='green' >LAMAOPZKS v0.92</font></u></i></h1></center>" +
        		"<br>" +
        		"<center>" +
        		"<b>Authors</b>" +
        		"<br>" +
        		"<i>Mishcheryakov Vyacheslav</i> aka <i>lamao</i>" +
        		"<br>" +
        		"<i>Kuzmenko Denis</i> aka <i>deniskus</i>" +
        		"<br><br>" +
        		"Ukraine, Kiev, 2009" +
        		"<br><br>" +
        		"Project site is<a href='http://code.google.com/p/lamaopzks'> http://code.google.com/p/lamaopzks</a>" +
        		"</center>" + 
        		"</html>");
        
       
        
        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.add(title);
        
        return panel;
    }
    
    private JPanel getBottmPanel()
    {
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new PZKSAboutBoxListener());
        
        JPanel panel = new JPanel();
        panel.add(okButton);
        
        return panel;
    }
    
    private class PZKSAboutBoxListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            dispose();
        }
        
    }
    
}
