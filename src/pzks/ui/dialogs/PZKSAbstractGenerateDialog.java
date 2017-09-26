package pzks.ui.dialogs;

import java.awt.Frame;

import javax.swing.JDialog;

import pzks.model.generator.PZKSGraphInfo;

public abstract class PZKSAbstractGenerateDialog extends JDialog
{
	private static final long serialVersionUID = 1L;

	private PZKSGraphInfo _graphInfo = null;
	
	public PZKSAbstractGenerateDialog(Frame owner)
	{
		super(owner);
		setModal(true);
	}
	
	public void setGraphInfo(PZKSGraphInfo graphInfo)
	{
		_graphInfo = graphInfo;
	}
	
	public PZKSGraphInfo getGraphInfo()
	{
		return _graphInfo;
	}
}
