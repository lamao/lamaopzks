package pzks.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import pzks.model.planners.assignment.PZKSAssignmentElement;
import pzks.ui.renderer.PZKSGantRenderer;

public class PZKSGantView extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	//******************** ivars *************************
	private PZKSGantRenderer _renderer = null;
	private Queue<List<PZKSAssignmentElement>> _elements = null; 
	
	
	//******************** initializers **************************
	public PZKSGantView(Queue<List<PZKSAssignmentElement>> elements,
						PZKSGantRenderer renderer)
	{
		super();
		setBorder(BorderFactory.createEtchedBorder());
		setAssignmentElements(elements);
		setRenderer(renderer);
	}

	//******************** assessors ******************************
	public void setAssignmentElements(Queue<List<PZKSAssignmentElement>> elements)
	{
		_elements = elements;
	}

	public Queue<List<PZKSAssignmentElement>> getAssignmentElements()
	{
		return _elements;
	}

	public void setRenderer(PZKSGantRenderer renderer)
	{
		_renderer = renderer;
	}

	public PZKSGantRenderer getRenderer()
	{
		return _renderer;
	}


	//********************** other methods ******************************
	@Override
	protected void paintComponent(Graphics g)
	{
		if (getRenderer() != null && getAssignmentElements() != null)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			
			getRenderer().paintGantDialgram(g2);
		}
		super.paintComponent(g);
		
	}



	

	
}
