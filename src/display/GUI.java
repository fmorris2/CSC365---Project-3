package display;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import graph.Edge;
import graph.Graph;
import graph.Node;
import utils.Utils;


public class GUI extends JFrame implements MouseMotionListener
{
	private static final long serialVersionUID = 6411499808530678723L;
	private static final String FRAME_NAME = "CSC-365 Assignment 3";
	private static final Dimension PREFERRED_SIZE = new Dimension(400, 400);
	
	private static final Dimension PATH_NODE_DIMENSION = new Dimension(15, 15);
	private static final Dimension OTHER_NODE_DIMENSION = new Dimension(4, 4);
	private static final int MIN_Y = 50;
	
	private JButton displayButton;
	private JTextField pageOne;
	private JTextField pageTwo;
	private JPanel drawPane;
	
	private Graph graph;
	private List<DrawableNode> nodesToDraw;
	private List<Node> shortestPath;
	private DrawableNode lastEntered;
	
	public GUI(Graph graph)
	{
		super(FRAME_NAME);
		this.graph = graph;
		this.drawPane = new DrawPane();
		this.nodesToDraw = new ArrayList<>();
		this.shortestPath = new ArrayList<>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(PREFERRED_SIZE);
		setContentPane(drawPane);
		initializeComponents();
	}
	
	private void initializeComponents()
	{
		displayButton = new JButton("Display");
		displayButton.addActionListener(new DisplayActionListener());
		pageOne = new JTextField(15);
		pageTwo = new JTextField(15);
		
		add(pageOne);
		add(pageTwo);
		add(displayButton);
		
		drawPane.addMouseMotionListener(this);
		
		pack();
		setVisible(true);
	}
	
	private void recalculateShapes()
	{
		nodesToDraw.clear();
		
		if(shortestPath == null)
			return;
		
		for(int i = 0; i < shortestPath.size(); i++)
		{
			Node n = shortestPath.get(i);
			DrawableNode parent = i == 0 ? null : nodesToDraw.get(i - 1);
			Shape parentShape = parent == null ? null : parent.getShape();
				
			DrawableNode parentNode = new DrawableNode(n, new Ellipse2D.Double(Utils.random(0, drawPane.getWidth()), Utils.random(MIN_Y, drawPane.getHeight()), 
					PATH_NODE_DIMENSION.getWidth(), PATH_NODE_DIMENSION.getHeight()), parentShape, true);
			
			nodesToDraw.add(parentNode);
			
			/*
			for(Edge e : n.getEdges())
				nodesToDraw.add(new DrawableNode(e.getDest(), new Ellipse2D.Double(Utils.random(0, drawPane.getWidth()), Utils.random(MIN_Y, drawPane.getHeight()), 
						OTHER_NODE_DIMENSION.getWidth(), OTHER_NODE_DIMENSION.getHeight()), parentNode.getShape(), false));
			*/
		}
	}
	
	private class DisplayActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String sourcePage = pageOne.getText();
			String targetPage = pageTwo.getText();
			
			System.out.println("SHORTEST PATH TO " + targetPage + " FROM " + sourcePage + ": ");
			shortestPath = graph.findShortestPath(graph.get(sourcePage), graph.get(targetPage));
			
			if(shortestPath != null)
			{
				for(Node n : shortestPath)
					System.out.println(n.getUrl());
			}
			else
			
			//display graph on GUI
			recalculateShapes();
			GUI.getFrames()[0].repaint();
		}
	}
	
	private class DrawPane extends JPanel
	{
		private static final long serialVersionUID = -1432135094203983128L;

		public void paintComponent(Graphics g)
        {
			Graphics2D g2 = (Graphics2D)g;
			
			recalculateShapes();
			
			List<DrawableNode> path = new ArrayList<>();
			
			for(DrawableNode n : nodesToDraw)
			{
				if(n.isPartOfPath())
					path.add(n);
				/*
				else
				{
					g2.setColor(Color.RED);
					g2.draw(n.getShape());
				}
				*/
				
				/*
				if(n.getParent() != null)
				{
					Rectangle2D firstBounds = n.getShape().getBounds2D();
					Rectangle2D secondBounds = n.getParent().getBounds2D();
				
					g2.setColor(Color.BLACK);
					g2.drawLine((int)firstBounds.getCenterX(), (int)firstBounds.getCenterY(), 
							(int)secondBounds.getCenterX(), (int)secondBounds.getCenterY());
				}
				*/
			}
			
			for(DrawableNode part : path)
			{
				g2.setColor(Color.GREEN);
				g2.fill(part.getShape());
				
				if(part.getParent() != null)
				{
					Rectangle2D firstBounds = part.getShape().getBounds2D();
					Rectangle2D secondBounds = part.getParent().getBounds2D();
				
					g2.setColor(Color.BLACK);
					g2.drawLine((int)firstBounds.getCenterX(), (int)firstBounds.getCenterY(), 
							(int)secondBounds.getCenterX(), (int)secondBounds.getCenterY());
				}
			}
        }
    }

	@Override
	public void mouseDragged(MouseEvent e)
	{}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		final Point MOUSE_POS = e.getPoint();
		
		for(int i = 0; i < nodesToDraw.size(); i++)
		{
			DrawableNode n = nodesToDraw.get(i);
			
			if(n.getShape().contains(MOUSE_POS))
			{
				Node node = n.getNode();
				Node other = i != 0 ? nodesToDraw.get(i - 1).getNode() : nodesToDraw.size() == 1 ? null : nodesToDraw.get(i + 1).getNode();
				Edge edge = null;
				
				if(other != null)
				{
					for(Edge ed : node.getEdges())
						if(ed.getDest().equals(other))
						{
							edge = ed;
							break;
						}
				}
				
				if(lastEntered == null || !lastEntered.equals(n))
				{
					System.out.println("Node for URL: " + n.getNode().getUrl());
					lastEntered = n;
				}
			}
		}
	}
	
}
