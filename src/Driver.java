import graph.Graph;
import graph.Node;


public class Driver
{
	static final String ROOT_PAGE = "Multiprotein_complex";
	static final String TARGET_PAGE = "Meth";
	
	public static void main(String[] args)
	{
		Graph graph = new Graph("https://en.wikipedia.org/wiki/" + ROOT_PAGE);
		graph.save();
		System.out.println("Can reach " + TARGET_PAGE + ": " + graph.find(graph.getRoot(), new Node("https://en.wikipedia.org/wiki/" + TARGET_PAGE)));
	}
}
