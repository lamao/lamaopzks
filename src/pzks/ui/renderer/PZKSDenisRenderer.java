package pzks.ui.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

import pzks.model.*;

public class PZKSDenisRenderer extends PZKSRenderer 
{
	public PZKSDenisRenderer(PZKSGraph graph)
	{
		super(graph);
	}
	
	public void paintNode(Graphics g, PZKSNode node)
	{
		Graphics2D g2 = (Graphics2D)g;
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		
		BasicStroke pen = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,30);
		g2.setStroke(pen);
		
		g2.setColor(Color.getHSBColor((float)160/255, (float)140/255, (float)255/255));
		g2.fillOval(node.getLocation().x - PZKSNode.sSize / 2, 
				node.getLocation().y - PZKSNode.sSize / 2, 
				PZKSNode.sSize, PZKSNode.sSize);
				
		g2.setColor(Color.black);

		g2.drawOval(node.getLocation().x - PZKSNode.sSize / 2, 
				node.getLocation().y - PZKSNode.sSize / 2, 
				PZKSNode.sSize, PZKSNode.sSize);
		
		
		
		g2.setColor(Color.BLACK);
		
		Font font = new Font(Font.SANS_SERIF,Font.BOLD+Font.ITALIC,16);
		g2.setFont(font);
		
		FontMetrics frc = g2.getFontMetrics(font);
				
		String st = String.format("%.2f", node.getWeight());
		int TextLeft = node.getLocation().x + 1 - frc.stringWidth(st) / 2;
		int TextTop = node.getLocation().y - 1 + frc.getHeight() / 2;
		g2.drawString(st, TextLeft, TextTop);
		
		g2.drawString(String.valueOf(node.getNumber()), 
					node.getLocation().x - 2 + PZKSNode.sSize / 2,
					node.getLocation().y + 2- PZKSNode.sSize / 2);
		
		g.setColor(oldColor);
		g.setFont(oldFont);

	}
	
	
	public void paintConnection(Graphics g, PZKSConnection connection)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		Color oldColor = g2.getColor();
		Font oldFont = g2.getFont();
		g2.setColor(Color.darkGray);
		
		Point src = connection.getSrcNode().getLocation();
		Point dst = connection.getDstNode().getLocation();
		
		BasicStroke pen = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,30);
		g2.setStroke(pen);
	    
		g2.drawLine(
				connection.getSrcNode().getLocation().x, 
				connection.getSrcNode().getLocation().y,
				connection.getDstNode().getLocation().x,
				connection.getDstNode().getLocation().y );
		
		if (getGraph().isTaskGraph()){ 
			paintArrow(g,connection.getSrcNode(),
				connection.getDstNode());
		}
		
		g2.setColor(Color.blue);
		
		Font font = new Font(Font.SANS_SERIF,Font.BOLD+Font.ITALIC,16);
		g2.setFont(font);
		
		g2.drawString(String.format("%.2f",connection.getWeight()), 
				(src.x + dst.x) / 2, (src.y + dst.y) / 2);
		
		g2.setColor(oldColor);
		g2.setFont(oldFont);

	}


	@Override
	protected void paintArrow(Graphics g, PZKSNode srcNode, PZKSNode dstNode) {
		double angle;
		int xa, ya, xb, yb, xc, yc, xd, yd, xe, ye;

		int x1 = srcNode.getLocation().x;
		int y1 = srcNode.getLocation().y;
		int x2 = dstNode.getLocation().x;
		int y2 = dstNode.getLocation().y;

		Graphics2D g2 = (Graphics2D) g;

		if (x2 - x1 == 0) {
			if (y2 - y1 > 0)
				angle = Math.PI / 2;
			else
				angle = -Math.PI / 2;
		} else {
			angle = Math.atan((double) (y2 - y1) / (x2 - x1));
			if (x2 - x1 < 0)
				angle = angle + Math.PI;
		}

		angle = angle + Math.PI;

		xa = x2 + (int) Math.round(PZKSNode.sSize / 2 * Math.cos(angle));
		ya = y2 + (int) Math.round(PZKSNode.sSize / 2 * Math.sin(angle));

		xd = x1	+ (int) Math.round(PZKSNode.sSize / 2
						* Math.cos(angle - Math.PI));
		
		yd = y1	+ (int) Math.round(PZKSNode.sSize / 2
						* Math.sin(angle - Math.PI));

		xb = xa + (int) Math.round(15 * Math.cos(angle - Math.PI / 12));
		yb = ya + (int) Math.round(15 * Math.sin(angle - Math.PI / 12));

		xc = xa + (int) Math.round(15 * Math.cos(angle + Math.PI / 12));
		yc = ya + (int) Math.round(15 * Math.sin(angle + Math.PI / 12));

		xe = xa + (int) Math.round(10 * Math.cos(angle));
		ye = ya + (int) Math.round(10 * Math.sin(angle));

		Polygon pol = new Polygon();

		pol.addPoint(xa, ya);
		pol.addPoint(xc, yc);
		pol.addPoint(xe, ye);
		pol.addPoint(xb, yb);

		g2.fillPolygon(pol);
		g2.drawLine(xa, ya, xd, yd);

	}

	@Override
	public void paintSelectedNode(Graphics g, PZKSNode node) {
		// TODO Auto-generated method stub
	}

}
