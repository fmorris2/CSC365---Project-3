package graph;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import similarity.CustomUrl;
import utils.Utils;


public class Graph implements Serializable
{
	private static final long serialVersionUID = 7949733030162889875L;
	
	public static final int MINIMUM_EDGES = 1250;
	public static final int MAX_SUB_LINKS_PER_PAGE = 30;
	
	private static final String FILE_DIR = "graphs/";
	
	private Set<Node> vertices;
	private Set<Edge> edges;
	private Node root;
	private int subLinks;
	
	public Graph(String rootUrl)
	{
		root = new Node(rootUrl);
		vertices = new HashSet<>();
		edges = new HashSet<>();
		
		vertices.add(root);
		subLinks = 0;
		if(!load())
			expand();
	}
	
	public void expand()
	{
		int loadedPages = 0;
		Node current = root;
		Queue<Edge> queue = new LinkedList<>();
		
		while(subLinks < MINIMUM_EDGES)
		{	
			System.out.println("Expanding off of " + current.url);
			
			//Expand the current node
			subLinks += current.expand(this, subLinks);
			
			//Add all new vertices & edges
			for(Edge e : current.edges)
			{
				edges.add(e);
				vertices.add(e.dest);
			}
			
			//Add all of its edges to the queue
			queue.addAll(current.edges);
			
			if(queue.isEmpty()) //No edges are in the queue, we can't continue
				break;
			
			//Set the current node to the next edge
			current = queue.poll().dest;
			
			loadedPages++;
		}
		
		for(Node n : vertices)
		{
			if(n.getEdges().get(0).cost == 0)
			{
				System.out.println("Calculating for a node with " + n.getEdges().size());
				for(CustomUrl cUrl : n.corpus)
					cUrl.getFreqTable().calculate();
			}
		}
			
			
		System.out.println("Had to load " + loadedPages + " pages to parse " + MINIMUM_EDGES + " sub links");
	}
	
	public Node get(String page)
	{
		for(Node n : vertices)
			if(n.getUrl().equals(page) || n.getUrl().endsWith("/"+page))
			{
				System.out.println(page + " exists in graph");	
				return n;
			}
		
		System.out.println(page + " does not exist in graph");
		return null;
	}
	
	public boolean find(Node src, Node dest)
	{
		//System.out.println("Src: " + src.url + ", dest: " + dest.url);
		
		if(src.equals(dest))
			return true;
		
		for(Edge p : src.edges)
		{
			if(find(p.dest, dest))
			{
				System.out.println(p.dest.url);
				return true;
			}
		}
		
		return false;
	}
	
	public static int findSpanningTree(Node starting)
	{
		return -1;
	}
	
	public List<Edge> findSpanningTreeBFS(Node starting)
	{
		resetMarksAndParents();
	    Queue<Node> nodeSet = new LinkedList<>();
	    List<Edge> edgeList = new ArrayList<>();
	    nodeSet.add(starting);
	    starting.mark = true;
	    starting.parent = null;
	    
	    while(!nodeSet.isEmpty())
	    {
	    	Node vertex = nodeSet.poll();
	    	for(Edge e : vertex.edges)
	    	{
	    		if(!e.dest.mark)
	    		{
	    			edgeList.add(e);
	    			nodeSet.add(e.dest);
	    			e.dest.mark = true;
	    			e.dest.parent = vertex;
	    		}
	    	}
	    }
	    
	    System.out.println("Found spanning tree for node " + starting.url + " with " + edgeList.size() + " edges");
	    return edgeList;
	}
	
	private void resetMarksAndParents()
	{
		for(Node n : vertices)
		{
			n.mark = false;
			n.parent = null;
		}
	}
	
	public List<Node> findShortestPath(Node src, Node dest)
	{	
		if(src == null || dest == null)
			return null;
		
		resetDistances();
		src.distance = 0;
		Queue<Node> pq = new PriorityQueue<>();
		pq.add(src);
		
		while(!pq.isEmpty())
		{
			Node u = pq.poll();
			
			for(Edge e : u.edges)
			{
				Node v = e.dest;
				double cost = e.cost;
				double distanceThroughU = u.distance + cost;
				
				if(distanceThroughU < v.distance)
				{
					pq.remove(v);
					v.distance = distanceThroughU;
					v.previous = u;
					pq.add(v);
				}
			}
		}
		
		List<Node> path = new ArrayList<>();
		for(Node n = dest; n != null; n = n.previous)
			path.add(n);
		Collections.reverse(path);
		
		if(path.isEmpty() || !path.get(0).equals(src))
			return null;
		
		return path;
		
	}
	
	private void resetDistances()
	{
		for(Node n : vertices)
		{
			n.distance = Double.MAX_VALUE;
			n.previous = null;
		}
	}
	
	public Node getRandomNode()
	{
		Node[] nodes = vertices.toArray(new Node[vertices.size()]);
		
		return nodes[Utils.random(0, nodes.length - 1)];
	}
	
	private boolean load()
	{
		File f = new File(FILE_DIR + root.url.substring(root.url.lastIndexOf("/")) + ".ser");
		
		if(f.exists())
		{
			//deserialize the file
			try(
					InputStream file = new FileInputStream(f);
					InputStream buffer = new BufferedInputStream(file);
					ObjectInput input = new ObjectInputStream (buffer);
		    )
		    {
				//deserialize the graph
		    	Graph g = (Graph)input.readObject();
		    	this.root = g.root;
		    	this.subLinks = g.subLinks;
		    	this.vertices = g.vertices;
		    	this.edges = g.edges;
		    	return true;
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    }
		}
		
		return false;
	}
	
	public void save()
	{
		File f = new File(FILE_DIR + root.url.substring(root.url.lastIndexOf("/")) + ".ser");
		
	    try (
	      OutputStream file = new FileOutputStream(f);
	      OutputStream buffer = new BufferedOutputStream(file);
	      ObjectOutput output = new ObjectOutputStream(buffer);
	    )
	    {
	      output.writeObject(this);
	    }  
	    catch(IOException ex)
	    {
	      ex.printStackTrace();
	    }
	}
	
	public void printAll()
	{
		root.printAll();
	}
	
	public Node getRoot()
	{
		return root;
	}
	
	public Set<Node> getVertices()
	{
		return vertices;
	}
	
	public Set<Edge> getEdges()
	{
		return edges;
	}
}
