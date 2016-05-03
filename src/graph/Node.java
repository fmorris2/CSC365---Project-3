package graph;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import similarity.Corpus;
import similarity.CustomUrl;
import similarity.FrequencyTable;
import utils.Utils;

public class Node implements Serializable
{
	private static final long serialVersionUID = 679765167453630740L;
	
	String url;
	Corpus corpus;
	Set<Edge> edges;
	
	public Node(String url)
	{
		this.url = url;
		corpus = new Corpus();
		edges = new HashSet<>();
		
		corpus.setPrimaryUrl(new CustomUrl(url, corpus));
		//parse words into raw frequency table
		corpus.getPrimaryUrl().addWords();
	}
	
	public int expand()
	{
		List<String> subLinks = Utils.getSubLinks(url);
		System.out.println("Sub links: " + subLinks.size());
		
		for(int i = 0; i < subLinks.size(); i++)//String link : subLinks)
		{
			//System.out.println("Adding node for sub link: " + i);
			String link = subLinks.get(i);
			Node newNode = new Node(link);
			edges.add(new Edge(this, newNode));
			
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
	
	public Set<Edge> getEdges()
	{
		return edges;
	}
}
