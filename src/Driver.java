import graph.Graph;
import graph.Node;
import display.GUI;


public class Driver
{
	static final String ROOT_PAGE = "McDonalds";
	
	public static void main(String[] args)
	{
		System.out.println("Loading graph");
		Graph graph = new Graph("https://en.wikipedia.org/wiki/" + ROOT_PAGE);
		graph.save();
		for(Node n : graph.getVertices())
			graph.findSpanningTreeBFS(n);
		//graph.findSpanningTreeDFS(graph.getRoot());
		//graph.findSpanningTreeDFS(graph.getRandomNode());
		GUI gui = new GUI(graph);
		System.out.println("Closest related: " + graph.getRoot().getCorpus().getClosestRelated(graph.getRoot().getCorpus().getPrimaryUrl()).getUrl());
		System.out.println("Graph vertices: " + graph.getVertices().size() + ", edges: " + graph.getEdges().size());
	}
}
