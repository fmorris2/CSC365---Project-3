package graph;

import java.io.Serializable;

public class Edge implements Serializable
{
	private static final long serialVersionUID = -2534396158095934966L;
	
	double cost; //similarity value
	Node dest;
	Node source;
	
	public Edge(Node source, Node dest)
	{
		this.source = source;
		this.dest = dest;
	}
	
	public void setCost(double cost)
	{
		this.cost = cost;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dest == null) ? 0 : dest.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
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
		Edge other = (Edge) obj;
		if (dest == null)
		{
			if (other.dest != null)
				return false;
		} else if (!dest.equals(other.dest))
			return false;
		if (source == null)
		{
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}
}
