package pzks.ui.dialogs;

import java.awt.Window;
import javax.swing.JDialog;

/**
 * This dialog was designed for choosing queue planner and assignment planner.
 *  
 * @author lamao
 *
 */
public abstract class ImmerseDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * This field shows if user clicks on OK button (e.i. 
	 * he made choice).
	 */
	private boolean _ok = false;
	
	public ImmerseDialog(Window owner, String title) 
	{
		super(owner, title);
	}
	
	
	public boolean isOk() 
	{
		return _ok;
	}

	public void setOk(boolean ok) 
	{
		_ok = ok;
	}
	
	/**
	 * This method should return choosed number of queue planner. 
	 * @return
	 */
	public abstract int getNumberOfQueue();
	
	/**
	 * This method should return choosed number of assignment planner. 
	 * @return
	 */
	public abstract int getNumberOfAssignment();

}
