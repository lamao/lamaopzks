package pzks.model.generator;

import java.awt.Point;
import java.util.Random;

import pzks.model.PZKSConnection;
import pzks.model.PZKSGraph;
import pzks.model.PZKSNode;

public class PZKSGraphGenerator
{

	private PZKSGraph graph;
	private PZKSGraphInfo graphInfo;

	public PZKSGraphGenerator(PZKSGraph aGraph, PZKSGraphInfo graphInfo)
	{
		this.graph = aGraph;
		this.graphInfo = graphInfo;
	}
	
	  public int GetTotalNodesWeight() 
	  {
		int totalWeight = 0;

		for (PZKSNode node : graph.getNodes())
		{
			totalWeight += node.getWeight();
		}

		return totalWeight;
	}
	  
	  
	  public int GetTotalLinksWeight()
	  {
		  int totalWeight = 0 ;
		  
		  for(PZKSConnection connection : graph.getConnections())
		  {
			  totalWeight +=  connection.getWeight(); 
		  }
		  
		  return totalWeight;
	  }	  
	
	  
	  public int GetNumLinks()
	  {
	      return graph.getConnections().size();
	  }
	  
	  
	  public boolean isCorrected() 
	  {
		  int TotalNodesValue = GetTotalNodesWeight();

		  return Math.abs(graphInfo.getConnectivity() * (GetTotalLinksWeight() + 
				  TotalNodesValue) - TotalNodesValue * 100) <= 
				  (float) graphInfo.getConnectivity() / 2;
	  }
	  
	  /**
	   * Corrects weight of links if generated graph doesn't correspond
	   * to given conditions
	   */
	  public void CorrectLinksWeights() 
	  {
		  int TotalNodesValue = GetTotalNodesWeight();

		  while (!isCorrected()) 
		  {
			  for (PZKSConnection connection : graph.getConnections()) 
			  {
				  if (graphInfo.getConnectivity() * (GetTotalLinksWeight() + 
						  TotalNodesValue) < TotalNodesValue * 100) 
				  {
					  connection.setWeight(connection.getWeight() + 1);
				  } 
				  else if (connection.getWeight() > 0) 
				  {
					  connection.setWeight(connection.getWeight() - 1);
				  }

				  if (isCorrected()) 
				  {
					  return;
				  }
			  }
		  }
	  }
	  
	  
	  /**
	   * Formula for correct generating
	   * sum(Wi)/(sum(Wi) + sum(Ei)) == connectivity
	   * 
	   * Wi - weight of node
	   * Ei - weight of connection 
	   */
	  public void AutoGenerate() 
	  {
		Random random = new Random();
		
		removeAllConnections();
		placeGraphNodes();
		createFullConnectedGraph();

		// Remove links while the clause is true
		PZKSConnection connection = graph.getConnections().get(random.nextInt(GetNumLinks()));
				
		while(!(GetTotalNodesWeight() * (100-graphInfo.getConnectivity()) > 
			GetTotalLinksWeight() * graphInfo.getConnectivity() - connection.getWeight()))
		{			
			if(graph.getConnections().size() == 1) break;
			

			graph.removeConnection(connection);
			connection = graph.getConnections().get(random.nextInt(GetNumLinks()));
		}
		
		// Makes connections weight correction if needed
		CorrectLinksWeights();
		
		graph.correctNumbersOfNodes();
	}
	  
	/**
	 * Removes all connections from graphs
	 */
	private void removeAllConnections()
	{
		//clear graph elements
		while(graph.getNumberOfNodes() > 0)
		{
			graph.removeNode(graph.getNodes().get(0));
		}
	}

	/**
	 * Calculate positions for all nodes so that they will be
	 * placed in grid order.
	 */
	private void placeGraphNodes()
	{
		//locate nodes on the graph form 
		int NumCols = (int) Math.round(Math.sqrt(graphInfo.getNumVerses()));
		int Col = 0, Row = 0, Radius = 200;

		for (int i = 0; i < graphInfo.getNumVerses(); i++)
		{
			Point location = new Point(
					Math.round(Radius + 50 + Radius * (float)Math.cos(2 * Math.PI * (float)i / graphInfo.getNumVerses())), 
					Math.round(Radius + 50 + Radius * (float)Math.sin(2 * Math.PI * (float)i / graphInfo.getNumVerses())));

			PZKSNode newNode = new PZKSNode(location, i, graphInfo
					.EvalNodeValue());
			graph.addNode(newNode);

			Col++;
			if (Col >= NumCols)
			{
				Col = 0;
				Row++;
			}
		}
	}
	
	/**
	 * Creates full-connected graph. This method supposes that
	 * graph is without connections 
	 */
	private void createFullConnectedGraph()
	{
		// Create all possible links
		for (int i = 0; i < graph.getNumberOfNodes() - 1; i++)
		{
			for (int j = i + 1; j < graph.getNumberOfNodes(); j++)
			{
				graph.addConnection(graph.getNodes().get(i), graph.getNodes().get(j), graphInfo.EvalLinkValue());
			}
		}
	}
}
