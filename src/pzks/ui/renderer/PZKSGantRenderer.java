package pzks.ui.renderer;

import java.awt.Graphics;
import java.util.List;
import java.util.Queue;

import pzks.model.planners.assignment.PZKSAssignmentElement;
import pzks.model.planners.assignment.PZKSProcessorsStorage.PZKSAssignmentNodeElement;
import pzks.model.planners.assignment.PZKSProcessorsStorage.PZKSAssignmentTransElement;

public abstract class PZKSGantRenderer
{
	private Queue<List<PZKSAssignmentElement>> _elements = null;
	private int _numberOfProcessorrs = 0;
	private int _maxClockTime = 0;
	
	public PZKSGantRenderer(Queue<List<PZKSAssignmentElement>> elements)
	{
		super();
		setElements(elements);
	}
	
	
	public void setElements(Queue<List<PZKSAssignmentElement>> elements)
	{
		_elements = elements;
	}

	public Queue<List<PZKSAssignmentElement>> getElements()
	{
		return _elements;
	}

	public int getNumberOfProcessorrs()
	{
		return _numberOfProcessorrs;
	}

	public void setNumberOfProcessorrs(int value)
	{
		_numberOfProcessorrs = value;
	}

	public int getMaxClockTime()
	{
		return _maxClockTime;
	}

	public void setMaxClockTime(int clockTime)
	{
		_maxClockTime = clockTime;
	}


	//************************ drawing methods ***************************
	public void paintGantDialgram(Graphics g)
	{
		paintBackground(g);

		if (getElements() != null)
		{
			for (List<PZKSAssignmentElement> list: getElements())
			{
				for (PZKSAssignmentElement element : list)
				{
					if (element instanceof PZKSAssignmentNodeElement)
					{
						paintNode(g, (PZKSAssignmentNodeElement)element);
					}
					else if (element instanceof PZKSAssignmentTransElement)
					{
						paintTransmission(g, (PZKSAssignmentTransElement)element);
					}
				}
			}
		}
	}
	
	protected abstract void paintBackground(Graphics g);
	protected abstract void paintNode(Graphics g, PZKSAssignmentNodeElement element);
	protected abstract void paintTransmission(Graphics g, PZKSAssignmentTransElement element);


}
