package pzks.ui.renderer;

import java.awt.Graphics;

import pzks.model.*;

/**
 * This is an abstract renderer for graph rendering. 
 * You need to implement methods <code>paintNode, paintSelectedNode, 
 * paintConnection, paintArrow</code> in subclasses. 
 * 
 * @author lamao
 * @see PZKSLamaoRenderer
 * 
 */
public abstract class PZKSRenderer
{
	//**************** ivars
	private PZKSGraph _graph = null;
	
	//**************** initializers
	public PZKSRenderer(PZKSGraph graph)
	{
		_graph = graph;
	}
	
	//**************** accessors
	public PZKSGraph getGraph() {return _graph;}
	public void setGraph(PZKSGraph graph) {_graph = graph;}
	
	//**************** other methods
	public abstract void paintNode(Graphics g, PZKSNode node);
	public abstract void paintSelectedNode(Graphics g, PZKSNode node);	
	public abstract void paintConnection(Graphics g, PZKSConnection connection);		
	protected abstract void paintArrow(Graphics g, PZKSNode srcNode, PZKSNode dstNode);
	
}
