package similarity;

import java.io.Serializable;


public class Word implements Serializable
{
	private static final long serialVersionUID = -8534384444013287621L;
	
	private int rawFrequency;
	private double tfIdf;
	
	public Word()
	{
		rawFrequency = 1;
	}
	
	public int getRawFrequency()
	{
		return rawFrequency;
	}
	
	public void incrementRawFrequency()
	{
		rawFrequency++;
	}
	
	public double getTfIdf()
	{
		return tfIdf;
	}
	
	public void setTfIdf(double d)
	{
		tfIdf = d;
	}
}