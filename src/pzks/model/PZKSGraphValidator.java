package pzks.model;

public abstract class PZKSGraphValidator 
{
	//******************** ivars 
	private PZKSGraph _graph;
	
	//******************** initializers
	public PZKSGraphValidator(PZKSGraph graph)
	{
		_graph = graph;
	}
	
	//******************** accessors
	public PZKSGraph getGraph() {return _graph;}
	public void setGraph(PZKSGraph graph) {_graph = graph;}
	
	//******************** other methods	
	public abstract boolean isValid();

}
