package pzks.model.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import pzks.model.PZKSGraph;
import pzks.service.PZKSAppStrings;
import pzks.ui.PZKSMainFrame;

public class PZKSMenuListener implements ActionListener{

	private JFileChooser fileChooser;
	private String fileName;
	private PZKSMainFrame _owner = null;
	
	//******************* File Filter ******************** 
	public class TxtFilter extends FileFilter 
	{
		String extension;
		
		public TxtFilter(String extension)
		{
			this.extension=extension;
		}	
		
		public boolean accept(java.io.File file) 
		{
			if (file.isDirectory())
				return true;
	  	
			return (file.getName().endsWith(extension));
		}
	  
		public String getDescription() 
		{
		    ResourceBundle bundle = PZKSAppStrings.getBundle();
			return bundle.getString(PZKSAppStrings.OPENSAVE_FILE_FILTER_NAME_KEY) + 
			        " (*."+extension+")";
		}
	}
	//*****************************************************
	
	
	public PZKSMenuListener(PZKSMainFrame aOwner){
		_owner = aOwner;
	}
		
	public void saveTofile()
	{
		
    	fileChooser=new JFileChooser();
    	fileChooser.addChoosableFileFilter(new TxtFilter("dat"));
		
    	int res1=this.fileChooser.showSaveDialog(null);
    	
    	if (res1==JFileChooser.APPROVE_OPTION)
    	{
    		fileName = fileChooser.getSelectedFile().getPath();
    		try 
    		{
				writeTofile(fileName);
			} 
    		catch (Throwable e) 
    		{
				e.printStackTrace();
			} 
        } 
 	}
			
	
	public void openFromfile()
	{
		
    	fileChooser=new JFileChooser();
    	fileChooser.addChoosableFileFilter(new TxtFilter("dat"));
		
		int res1=this.fileChooser.showOpenDialog(null);
   	  	
   	  	if (res1==JFileChooser.APPROVE_OPTION)
   	  	{
   	  		fileName = fileChooser.getSelectedFile().getPath();
   	  		try 
   	  		{
				readFromfile(fileName);
			} 
   	  		catch (Throwable e) 
   	  		{
				e.printStackTrace();
			} 
   	  	}
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("Save"))
		{
			saveTofile();
		} 
		else if (e.getActionCommand().equals("Load"))
		{
			openFromfile();
		} 
		else if (e.getActionCommand().equals("New"))
		{
			createNewfile();
		}
		
	}
	
	private void writeTofile(String fileName) throws FileNotFoundException, IOException
	{
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
		
		oos.writeObject(_owner.getSystemGraph());
		oos.writeObject(_owner.getTaskGraph());
		
		oos.flush();
		oos.close();
	}
	
	private void readFromfile(String fileName) 
			throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
		
		_owner.setSystemGraph((PZKSGraph)ois.readObject());
		_owner.setTaskGraph((PZKSGraph)ois.readObject());
		
		_owner.initGraphView();
		
		ois.close();
	}
	
	private void createNewfile()
	{
		_owner.setSystemGraph(new PZKSGraph(false));
		_owner.setTaskGraph(new PZKSGraph(true));
		
		_owner.initGraphView();
	}
}
