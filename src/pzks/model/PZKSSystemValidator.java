package pzks.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class PZKSSystemValidator extends PZKSGraphValidator
{

	/**
	 * Перевіряє, чи зв'язаний граф системи. 
	 * Виконується шляхом "закрашування вершин" методои
	 * хвильового алгоритму. Спочатку замальовуютсья всі вершини, 
	 * що з'єднані з першою вершиною, далі ті, що зв'язані
	 * з вершинами, замальованими на першому кроці і т.д.
	 * Якщо в результаті весь граф закрашений одним кольором,
	 * то в ньому всі вершини зв'язані 
	 */
	
	public PZKSSystemValidator(PZKSGraph graph) 
	{
		super(graph);
	}
	
	// TODO This about easer implementation
	public boolean isValid() 
	{
		assert(getGraph() != null);
		
		boolean result = false;
		
		if (!getGraph().isTaskGraph())
		{
			//copy nodes to buffer
			ArrayList<PZKSNode> nodes = getGraph().getNodes();
			LinkedList<PZKSNode> currWave  = new LinkedList<PZKSNode>();
			currWave.add(nodes.get(0));
			LinkedList<PZKSNode> nextWave = new LinkedList<PZKSNode>();
			
			
			//save numbers of nodes
			int []oldNumbers = new int[nodes.size()];
			for (int i = 0; i < nodes.size(); i++)
			{
				oldNumbers[i] = nodes.get(i).getNumber();
			}
			
			
			while (!currWave.isEmpty())
			{
				//process all nodes in current wave
				for (int i = 0; i < currWave.size(); i++)
				{
					//get all connection in current node
					PZKSNode currNode = currWave.get(i);
					Iterator<PZKSConnection> connections = currNode.getConnectionIterator();
					
					// 'fill' all connected nodes
					while (connections.hasNext())
					{
						
						PZKSConnection connection = connections.next();
						PZKSNode newNode = connection.getOppositeNode(currNode);
						if (newNode.getNumber() != -1)
						{
							newNode.setNumber(-1);
							nextWave.add(newNode);
						}
					}
				}
				
				currWave = nextWave;
				nextWave = new LinkedList<PZKSNode>();
			}
			
			// Check if all nodes are filled by the same color
			int color = 0;
			for (int i = 0; i < nodes.size(); i++)
			{
				color += nodes.get(i).getNumber();
			}
			
			result = color == -1 * nodes.size();
			
			
			// restore numbers of nodes
			for (int i = 0; i < nodes.size(); i++)
			{
				nodes.get(i).setNumber(oldNumbers[i]);
			}
			
			System.gc();
		}
		
		return result;
	}
	
}
