package pzks.ui.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import pzks.model.PZKSConnection;
import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;

/**
 * This is the concrete renderer. It implements abstract 
 * <code>PZKSRenderer</code> class.
 * 
 * @author lamao
 * @see PZKSRenderer
 *
 */
public class PZKSLamaoRenderer extends PZKSRenderer 
{
	public PZKSLamaoRenderer(PZKSGraph graph)
	{
		super(graph);
	}
	
	public void paintNode(Graphics g, PZKSNode node)
	{
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		g.setColor(Color.GRAY);
		
		g.fillOval(node.getLocation().x - PZKSNode.sSize / 2, 
				node.getLocation().y - PZKSNode.sSize / 2, 
				PZKSNode.sSize, PZKSNode.sSize);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Courier New", Font.BOLD, 12));
		g.drawString(String.valueOf(node.getWeight()), 
					node.getLocation().x - PZKSNode.sSize / 4, 
					node.getLocation().y + 5);
		g.setColor(Color.BLACK);
		g.drawString(String.valueOf(node.getNumber()), 
					node.getLocation().x - PZKSNode.sSize / 2,
					node.getLocation().y - PZKSNode.sSize / 2);
		
		g.setColor(oldColor);
		g.setFont(oldFont);
	}
	
	public void paintSelectedNode(Graphics g, PZKSNode node)
	{
		if (node == null)
		{
			return;
		}
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		g.setColor(Color.LIGHT_GRAY);
		
		g.fillOval(node.getLocation().x - PZKSNode.sSize / 2, 
				node.getLocation().y - PZKSNode.sSize / 2, 
				PZKSNode.sSize, PZKSNode.sSize);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Courier New", Font.BOLD, 12));
		g.drawString(String.valueOf(node.getWeight()), 
					node.getLocation().x - PZKSNode.sSize / 4, 
					node.getLocation().y + 5);
		g.setColor(Color.BLACK);
		g.drawString(String.valueOf(node.getNumber()), 
				node.getLocation().x - PZKSNode.sSize / 2,
				node.getLocation().y - PZKSNode.sSize / 2);
		
		g.setColor(oldColor);
		g.setFont(oldFont);
	}
	
	public void paintConnection(Graphics g, PZKSConnection connection)
	{
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		g.setColor(Color.BLUE);
		
		Point src = connection.getSrcNode().getLocation();
		Point dst = connection.getDstNode().getLocation();
		g.drawLine(connection.getSrcNode().getLocation().x, 
				connection.getSrcNode().getLocation().y,
				connection.getDstNode().getLocation().x,
				connection.getDstNode().getLocation().y);
		
		if (getGraph().isTaskGraph())
			paintArrow(g, connection.getSrcNode(), connection.getDstNode());
		
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((src.x + dst.x) / 2 - 5, (src.y + dst.y) / 2 - 12, 30, 15);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Courier New", Font.BOLD, 12));
		g.drawString(String.valueOf(connection.getWeight()), 
				(src.x + dst.x) / 2, (src.y + dst.y) / 2);
		
		
		
		g.setColor(oldColor);
		g.setFont(oldFont);
	}
	
	protected void paintArrow(Graphics g, PZKSNode srcNode, PZKSNode dstNode)
	{
		int x1, y1, x2, y2;
		
		if (Math.abs(srcNode.getLocation().x - dstNode.getLocation().x) > 
			Math.abs(srcNode.getLocation().y - dstNode.getLocation().y))
		{
			x1 = srcNode.getLocation().x;
			y1 = srcNode.getLocation().y - 5;
			x2 = x1;
			y2 = y1 + 10;
		}
		else
		{
			x1 = srcNode.getLocation().x - 5;
			y1 = srcNode.getLocation().y;
			x2 = x1 + 10;
			y2 = y1;
		}
		
		g.drawLine(x1, y1, dstNode.getLocation().x, dstNode.getLocation().y);
		g.drawLine(x2, y2, dstNode.getLocation().x, dstNode.getLocation().y);
	}

}
