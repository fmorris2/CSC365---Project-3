package display;
import graph.Node;

import java.awt.Shape;


public class DrawableNode
{
	private Node node;
	private Shape shape;
	private Shape parent;
	private boolean isPartOfPath;
	
	public DrawableNode(Node n, Shape s, Shape p, boolean b)
	{
		node = n;
		shape = s;
		parent = p;
		isPartOfPath = b;
	}
	
	public Node getNode()
	{
		return node;
	}
	
	public Shape getShape()
	{
		return shape;
	}
	
	public Shape getParent()
	{
		return parent;
	}
	
	public boolean isPartOfPath()
	{
		return isPartOfPath;
	}
}
