package graph;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.Utils;

public class Node implements Serializable
{
	private static final long serialVersionUID = 679765167453630740L;
	
	String url;
	Set<Edge> edges;
	
	public Node(String url)
	{
		this.url = url;
		edges = new HashSet<>();
	}
	
	public int expand()
	{
		List<String> subLinks = Utils.getSubLinks(url);
		
		for(String link : subLinks)			
			edges.add(new Edge(this, new Node(link)));
		
		return edges.size();
	}
	
	public void printAll()
	{
		System.out.println(url);
		for(Edge e : edges)
			e.dest.printAll();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (url == null)
		{
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public Set<Edge> getEdges()
	{
		return edges;
	}
}
