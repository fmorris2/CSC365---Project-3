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
import java.util.LinkedList;
import java.util.Queue;


public class Graph implements Serializable
{
	private static final long serialVersionUID = 7949733030162889875L;
	
	private static final int SUB_LINKS = 1000000;
	private static final String FILE_DIR = "graphs/";
	
	private Node root;
	private int subLinks;
	
	public Graph(String rootUrl)
	{
		root = new Node(rootUrl);
		subLinks = 0;
		if(!load())
			expand();
	}
	
	public void expand()
	{
		int loadedPages = 0;
		Node current = root;
		Queue<Edge> queue = new LinkedList<>();
		
		while(subLinks < SUB_LINKS)
		{
			System.out.println("Expanding off of " + current.url);
			
			//Expand the current node
			subLinks += current.expand();
			
			//Add all of its edges to the queue
			queue.addAll(current.edges);
			
			if(queue.isEmpty()) //No edges are in the queue, we can't continue
				break;
			
			//Set the current node to the next edge
			current = queue.poll().dest;
			
			loadedPages++;
		}
		
		System.out.println("Had to load " + loadedPages + " pages to parse " + SUB_LINKS + " sub links");
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
}
