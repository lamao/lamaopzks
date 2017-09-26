package pzks.model.planners.queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import pzks.model.PZKSConnection;
import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;

/**
 * This class was designed for getting
 * different parameters of graph.
 * @author lamao
 *
 */
public class PZKSParametersCalculator 
{
	/**
	 * Returns criticalNK ways for all nodes
	 * @param graph
	 * @param finalDir
	 * @return
	 */
	static int[] getCriticalNK(PZKSGraph graph, boolean finalDir)
	{
		assert(graph != null);
		
		Iterator<PZKSNode> iterator = graph.getNodesIterator();
		int[] result = new int[graph.getNumberOfNodes()];

		//proccess all nodes
		int index = 0; 
		while (iterator.hasNext())
		{
			result[index++] = getCriticalNK(graph, iterator.next(), finalDir);
		}
		
		return result;	
	}
	
	
	/**
	 * Returns criticalNK way from srcNode  
	 * @param graph
	 * @param node
	 * @param finalDir
	 * @return
	 */
	public static int getCriticalNK(PZKSGraph graph, PZKSNode srcNode, boolean finalDir)
	{
		ArrayList<PZKSNode> nodes = graph.getNodes();
		if (!nodes.contains(srcNode))
		{
			return -1;
		}
		
		LinkedList<PZKSNode> currWave = new LinkedList<PZKSNode>();
		LinkedList<PZKSNode> nextWave = new LinkedList<PZKSNode>();	
		int[] criticalWays = new int[nodes.size()];
		
		Arrays.fill(criticalWays, 0);
		
		if(finalDir)
		{
			criticalWays[nodes.indexOf(srcNode)] = 1;
		} 
		else
		{
			criticalWays[nodes.indexOf(srcNode)] = 0;
		}
		
		// Initializaton
		currWave.add(srcNode);
		
		// process current node 
		while (!currWave.isEmpty())
		{
			//process nodes in currWave
			Iterator<PZKSNode> currWaveIterator = currWave.iterator();
			PZKSNode node = null;
			while (currWaveIterator.hasNext())
			{
				// process connection for currWaveIterator.next
				node = currWaveIterator.next();
				Iterator<PZKSConnection> connections = node.getConnectionIterator();
				PZKSConnection connection = null;
				while (connections.hasNext())
				{
					connection = connections.next();
					
					if(finalDir) 
					{
						if (connection.getSrcNode() == node) 
						{
							// if current critical way larger then new, remember new
							// way and process this connection in next loop
							if (criticalWays[nodes.indexOf(connection.getDstNode())] < 
								criticalWays[nodes.indexOf(node)] + 1) 
							{
								criticalWays[nodes.indexOf(connection.getDstNode())] = 
									criticalWays[nodes.indexOf(node)] + 1;
								nextWave.add(connection.getDstNode());
							}
						}
					
					} 
					else 
					{
						if (connection.getDstNode() == node) 
						{
							// if current critical way larger then new, remember new
							// way and process this connection in next loop
							if (criticalWays[nodes.indexOf(connection.getSrcNode())] < 
								criticalWays[nodes.indexOf(node)] + 1) 
							{
								criticalWays[nodes.indexOf(connection.getSrcNode())] = 
									criticalWays[nodes.indexOf(node)] + 1;
								nextWave.add(connection.getSrcNode());
							}
						}
					}
					
				}
			}
			
			currWave = nextWave;
			nextWave = new LinkedList<PZKSNode>();
		}
		
		//find critical way - max value in array
		int result = -1;
		for (int i = 0; i < criticalWays.length; i++)
		{
			if (criticalWays[i] > result)
			{
				result = criticalWays[i];
			}
		}
		
		System.gc();
		return result;		
	}
	
	
	/**
	 * Returns criticalTK ways for all nodes
	 * @param graph
	 * @param finalDir
	 * @return
	 */
	static float[] getCriticalTK(PZKSGraph graph, boolean finalDir)
	{
		assert(graph != null);
		
		Iterator<PZKSNode> iterator = graph.getNodesIterator();
		float[] result = new float[graph.getNumberOfNodes()];

		//proccess all nodes
		int index = 0; 
		while (iterator.hasNext())
		{
			result[index++] = getCriticalTK(graph, iterator.next(), finalDir);
		}
		
		
		return result;	
	}
	
	/**
	 * Returns criticalNK way from srcNode 
	 * @param graph
	 * @param node
	 * @param finalDir
	 * @return
	 */
	public static float getCriticalTK(PZKSGraph graph, PZKSNode srcNode, boolean finalDir)
	{
		ArrayList<PZKSNode> nodes = graph.getNodes();
		if (!nodes.contains(srcNode))
		{
			return -1;
		}
		
		LinkedList<PZKSNode> currWave = new LinkedList<PZKSNode>();
		LinkedList<PZKSNode> nextWave = new LinkedList<PZKSNode>();	
		float[] criticalWays = new float[nodes.size()];
		
		Arrays.fill(criticalWays, 0);
		
		if(finalDir)
		{
			criticalWays[nodes.indexOf(srcNode)] = srcNode.getWeight();;
		} 
		else 
		{
			criticalWays[nodes.indexOf(srcNode)] = 0;
		}
		
		// Initializaton
		currWave.add(srcNode);
		
		// process current node 
		while (!currWave.isEmpty())
		{
			//process nodes in currWave
			Iterator<PZKSNode> currWaveIterator = currWave.iterator();
			PZKSNode node = null;
			while (currWaveIterator.hasNext())
			{
				// process connection for currWaveIterator.next
				node = currWaveIterator.next();
				Iterator<PZKSConnection> connections = node.getConnectionIterator();
				PZKSConnection connection = null;
				while (connections.hasNext())
				{
					connection = connections.next();
					if(finalDir) 
					{
						if (connection.getSrcNode() == node)
						{
							// if current critical way larger then new, remember new
							// way and process this connection in next loop
							if (criticalWays[nodes.indexOf(connection.getDstNode())] < 
								criticalWays[nodes.indexOf(node)] + connection.getDstNode().getWeight())
							{
								criticalWays[nodes.indexOf(connection.getDstNode())] = 
									criticalWays[nodes.indexOf(node)] + connection.getDstNode().getWeight();
								nextWave.add(connection.getDstNode());
							}
						}
					} 
					else 
					{
						if (connection.getDstNode() == node)
						{
							
							// if current critical way larger then new, remember new
							// way and process this connection in next loop
							if (criticalWays[nodes.indexOf(connection.getSrcNode())] < 
								criticalWays[nodes.indexOf(node)] + connection.getSrcNode().getWeight())
							{
								criticalWays[nodes.indexOf(connection.getSrcNode())] = 
									criticalWays[nodes.indexOf(node)] + connection.getSrcNode().getWeight();
								nextWave.add(connection.getSrcNode());
							}
						}
					}
				}
			}
			
			currWave = nextWave;
			nextWave = new LinkedList<PZKSNode>();
		}
		
		//find critical way - max value in array
		float result = -1;
		for (int i = 0; i < criticalWays.length; i++)
		{
			if (criticalWays[i] > result)
			{
				result = criticalWays[i];
			}
		}
		
		System.gc();
		return result;		
	}
	
			
}
