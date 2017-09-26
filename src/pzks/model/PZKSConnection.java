package pzks.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import pzks.service.Geometry;

/**
 * This class is the model class and represents a single connection between
 * two nodes. It can bee serialized. <br> 
 * It stores weight and two nodes (weak refrences) which it connects.
 *  
 * @author lamao
 * @see PZKNode
 * @see PZKSGraph
 *
 */
public class PZKSConnection implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private PZKSNode _srcNode;	//weak ref
	private PZKSNode _dstNode;	//weak ref
	private int _weight;
	
	//************************ initializers ************************************
	public PZKSConnection(PZKSNode srcNode, PZKSNode dstNode, int weight)
	{
		_srcNode = srcNode;
		_dstNode = dstNode;
		_weight = weight;
	}
	
	public PZKSConnection(PZKSNode srcNode, PZKSNode dstNode)
	{
		this(srcNode, dstNode, 1);
	}
	
	//*********************** accessors ****************************************
	public PZKSNode getSrcNode()
	{
		return _srcNode;
	}
	public void setSrcNode(PZKSNode node)
	{
		_srcNode = node;
	}
	
	public PZKSNode getDstNode()
	{
		return _dstNode;
	}
	public void setDstNode(PZKSNode node)
	{
		_dstNode = node;
	}
	
	public int getWeight()
	{
		return _weight;
	}
	public void setWeight(int weight)
	{
		_weight = weight;
	}
	
	//other methods
	public boolean hasNodes(PZKSNode node1, PZKSNode node2)
	{
		return (getSrcNode() == node1 && getDstNode() == node2) ||
				(getSrcNode() == node2 && getDstNode() == node1);
		
	}
	public PZKSNode getOppositeNode(PZKSNode node)
	{
		PZKSNode result = null;
		if (node == getSrcNode())
		{
			result = getDstNode();
		}
		else if (node == getDstNode())
		{
			result = getSrcNode();
		}
		
		return result;			
	}
	public boolean hasPoint(Point point)
	{
		boolean result = false;
		if (getSrcNode() != null && getDstNode() != null)
		{
			Point srcPoint = getSrcNode().getLocation();
			Point dstPoint = getDstNode().getLocation();
			Rectangle connectionRect = new Rectangle(Math.min(srcPoint.x, dstPoint.x), 
								Math.min(srcPoint.y, dstPoint.y), 
								Math.abs(dstPoint.x - srcPoint.x), 
								Math.abs(dstPoint.y - srcPoint.y));
			
			if (connectionRect.contains(point) && 
				Math.abs(Geometry.getDistanceToLine(srcPoint, dstPoint, point)) <= 5.0f)
			{
				result = true;
			}
		}
		
		return result;
	}
}
