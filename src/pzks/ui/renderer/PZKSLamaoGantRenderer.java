package pzks.ui.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import java.util.Queue;

import pzks.model.planners.assignment.PZKSAssignmentElement;
import pzks.model.planners.assignment.PZKSProcessorsStorage.PZKSAssignmentNodeElement;
import pzks.model.planners.assignment.PZKSProcessorsStorage.PZKSAssignmentTransElement;

public class PZKSLamaoGantRenderer extends PZKSGantRenderer
{

	public Color colorOfNodes = Color.GREEN;
	public Color colorOfNodesText = Color.BLACK;
	public Color colorOfTransmission = Color.BLUE;
	public Color colorOfTransmissionText = Color.BLACK;
	public Color colorOfTransmissionTerminators = Color.RED;
	public int drawingStep = 40;
	public Color backgroundColor = Color.GRAY;
	
	public PZKSLamaoGantRenderer(Queue<List<PZKSAssignmentElement>> elements)
	{
		super(elements);
	}

	public void paintBackground(Graphics g)
	{
		Color oldColor = g.getColor();
		
		g.setColor(Color.LIGHT_GRAY);
		for (int i = 0; i < getMaxClockTime() + 1; i++)
		{
			g.drawLine(i * drawingStep, 0, i  * drawingStep, 
					getNumberOfProcessorrs() * drawingStep);
		}
		g.setColor(Color.GRAY);
		for (int i = 0; i < getNumberOfProcessorrs() + 1; i++)
		{
			g.drawLine(0, i * drawingStep, getMaxClockTime() * drawingStep, 
					i  * drawingStep);
			g.drawLine(0, i * drawingStep - 1, getMaxClockTime() * drawingStep, 
					i  * drawingStep - 1);
		}
		
		g.setColor(oldColor);
	}
	
	@Override
	public void paintNode(Graphics g, PZKSAssignmentNodeElement element)
	{
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		
		g.setColor(colorOfNodes);
		g.fillRect(element.start * drawingStep, 
				element.numberOfProccessor * drawingStep + drawingStep / 2, 
				element.duration * drawingStep , 
				drawingStep / 2);
		
		g.setColor(colorOfNodesText);
		g.drawRect(element.start * drawingStep, 
				element.numberOfProccessor * drawingStep + drawingStep / 2, 
				element.duration * drawingStep ,
				drawingStep / 2);
		
		String nodeText = String.valueOf(element.number);
		g.setFont(new Font(Font.SANS_SERIF, Font.ITALIC | Font.BOLD, 12));
		g.drawString(nodeText, 
				element.start * drawingStep + 10, 
				element.numberOfProccessor * drawingStep + 15  + drawingStep / 2 );
		
		g.setFont(oldFont);
		g.setColor(oldColor);
	}
	
	
	/**
	 * This method draws transmission like this
	 * 
	 * 	 1->2
	 * |--------|
	 * 
	 */
	@Override
	public void paintTransmission(Graphics g, PZKSAssignmentTransElement element)
	{
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		
		g.setColor(colorOfTransmission);
		
		Point start = new Point(element.start * drawingStep,
				element.numberOfProccessor * drawingStep + 3 * drawingStep / 4);
		Point end = new Point((element.start + element.duration) * drawingStep,
				element.destProcessor * drawingStep + 3 * drawingStep / 4);
		g.drawLine(start.x, start.y, end.x, end.y); 
		
		g.setColor(colorOfTransmissionTerminators);
		g.fillOval(start.x - 3, start.y - 3, 6, 6);
		g.fillOval(end.x - 3, end.y - 3, 6, 6);
		
//		// left vertical line
//		g.drawLine(element.start * drawingStep + 2, 
//				element.numberOfProccessor * drawingStep, 
//				element.start * drawingStep + 2, 
//				(element.numberOfProccessor + 1) * drawingStep - drawingStep / 2);
//		// right vertical line 
//		g.drawLine((element.start + element.duration) * drawingStep - 2, 
//				element.numberOfProccessor * drawingStep, 
//				(element.start + element.duration) * drawingStep - 2, 
//				(element.numberOfProccessor + 1) * drawingStep - drawingStep / 2);
//		// horizontal line
//		g.drawLine(element.start * drawingStep + 2, 
//				element.numberOfProccessor * drawingStep + drawingStep / 4, 
//				(element.start + element.duration) * drawingStep - 2, 
//				(element.numberOfProccessor) * drawingStep + drawingStep / 4);

		// draw description
		g.setColor(colorOfTransmissionText);
		g.setFont(new Font(Font.SANS_SERIF, Font.ITALIC , 11));
		
		String text = "" + element.numberOfProccessor + "->" + 
				element.destProcessor + "(" + element.destNode + ")";
//		g.drawString(text, 
//				element.start * drawingStep + 10, 
//				element.numberOfProccessor * drawingStep + drawingStep / 4);
		g.drawString(text, (start.x + end.x) / 2 - 10, (start.y + end.y) / 2 + 5);
		
		g.setFont(oldFont);
		g.setColor(oldColor);
	}

}
