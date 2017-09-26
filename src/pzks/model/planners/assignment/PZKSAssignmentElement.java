package pzks.model.planners.assignment;

/**
 * This class contains all information about assignment. 
 * Object of this class are returned by method getNextNode() of
 * class PZKSAssignmentPlanner
 * 
 * @author lamao
 * @see PZKSAssignmentPlanner
 */
public class PZKSAssignmentElement 
{
	public enum AssignmentType
	{
		NODE,
		TRANSMISSION
	}
	//***************************** ivars **************************************
	public AssignmentType type;
	public int numberOfProccessor = -1;
	public int start = 0;
	public int duration = 0;
	public String description = "";
	
	//**************************** initializers ********************************
	public PZKSAssignmentElement(AssignmentType type, int numberOfProccessor,
			int start, int duration, String description) 
	{
		this.type = type;
		this.numberOfProccessor = numberOfProccessor;
		this.start = start;
		this.duration = duration;
		this.description = description;
	}
	
	//*************************** accessors ************************************
	public int getEnd()
	{
		return start + duration;
	}
	
}
