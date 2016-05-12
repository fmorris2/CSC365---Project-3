package graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import similarity.Corpus;
import similarity.CustomUrl;
import similarity.FrequencyTable;
import utils.Utils;

public class Node implements Comparable<Node>, Serializable
{
	private static final long serialVersionUID = 679765167453630740L;
	
	String url;
	Corpus corpus;
	List<Edge> edges;
	transient double distance;
	transient Node previous;
	transient boolean mark;
	transient Node parent;
	
	public Node(String url)
	{
		this.url = url;
		corpus = new Corpus();
		edges = new ArrayList<>();
		
		corpus.setPrimaryUrl(new CustomUrl(url, corpus));
		
		//parse words into raw frequency table
		corpus.getPrimaryUrl().addWords();
	}
	
	public Node(Node n)
	{
		url = n.url;
		corpus = n.corpus;
		edges = n.edges;
		distance = n.distance;
		previous = n.previous;
	}
	
	public int expand(Graph g, int currentLinks)
	{
		List<String> subLinks = Utils.getSubLinks(url);
		Collections.shuffle(subLinks);
		
		System.out.println("Sub links for " + url + ": " + subLinks.size());
		System.out.println("Edges size: " + g.getEdges().size());
		
		for(int i = 0; i < subLinks.size() && i < Graph.MAX_SUB_LINKS_PER_PAGE 
				&& (currentLinks + edges.size()) < Graph.MINIMUM_EDGES; i++)
		{
			String link = subLinks.get(i);
			Node newNode = g.get(link);
			if(newNode == null)
				newNode = new Node(link);
			
			edges.add(new Edge(this, newNode));
			newNode.edges.add(new Edge(newNode, this));
			newNode.getCorpus().add(new CustomUrl(url, newNode.getCorpus()));
			
			CustomUrl customUrl = new CustomUrl(link, corpus);
			customUrl.setFreqTable(newNode.corpus.getPrimaryUrl().getFreqTable());
			corpus.add(customUrl);
		}
		
		for(CustomUrl cUrl : corpus)
			cUrl.getFreqTable().calculate();
		
		//set costs to similarity angle
		for(Edge e : edges)
			e.setCost(FrequencyTable.calculateAngle(corpus.getPrimaryUrl().getFreqTable(), e.dest.corpus.getPrimaryUrl().getFreqTable()));
		
		System.out.println("Closest related url: " + corpus.getClosestRelated(corpus.getPrimaryUrl()).getUrl());
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
	
	public List<Edge> getEdges()
	{
		return edges;
	}
	
	public Corpus getCorpus()
	{
		return corpus;
	}

	@Override
	public int compareTo(Node o)
	{
		return Double.compare(distance, o.distance);
	}
}
