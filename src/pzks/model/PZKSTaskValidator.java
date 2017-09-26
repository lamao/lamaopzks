package pzks.model;

import java.util.Iterator;
import java.util.Stack;

public class PZKSTaskValidator extends PZKSGraphValidator
{
	public PZKSTaskValidator(PZKSGraph graph) 
	{
		super(graph);
	}
	
	/**
	 * Find all ways in oriented graph. Return false if 
	 * we have any circle.
	 */
	
	public boolean isValid() 
	{
		assert(getGraph() != null);
		
		boolean result = false;
		
		if (getGraph().isTaskGraph())
		{
			if (getGraph().isEmpty())
			{
				result = true;
			}
			else
			{
				Iterator<PZKSNode> iterator = getGraph().getNodesIterator();

				result = true;
				while (iterator.hasNext() && result)
				{
					result &= withoutCircles(iterator.next());
				}
			}
		}
		
		return result;
	}

	private boolean withoutCircles(PZKSNode aNode)
	{
		boolean result = true;
		
		Stack<Iterator<PZKSConnection>>	way = new Stack<Iterator<PZKSConnection>>();
		Stack<PZKSNode> nodesInWay = new Stack<PZKSNode>();
		Iterator<PZKSConnection> connections = aNode.getConnectionIterator();
		
		nodesInWay.push(aNode);
		way.push(null);
		
		do
		{
			aNode = nodesInWay.peek();
			
			//skip all incoming connections
			PZKSConnection connection = null;
			while (connections.hasNext())
			{
				connection = connections.next();
				if (connection.getSrcNode() == aNode)
				{
					break;
				}
				else
				{
					connection = null;
				}
			}
			
			
			if (connection != null)
			{
				//do next step
				way.push(connections);
				aNode = connection.getOppositeNode(aNode);
				nodesInWay.push(aNode);								
				connections = aNode.getConnectionIterator();
				
				result = nodesInWay.indexOf(aNode) == nodesInWay.size() - 1;
			}
			else
			{
				// do one back step
				connections = way.pop();
				nodesInWay.pop(); 
				
			}
		}
		while (!nodesInWay.isEmpty() && result);
		
		if (!way.isEmpty())
		{
			result = false;
		}
		return result;
	}
}
