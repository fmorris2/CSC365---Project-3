package similarity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import utils.Utils;


public class CustomUrl implements Serializable
{
	private static final long serialVersionUID = 3524769793224936714L;

	private static List<String> exclusions = Arrays.asList("and", "or", "to", "the");
	
	private String url;
	private FrequencyTable freqTable;
	
	public CustomUrl(String url, Corpus corpus)
	{
		this.url = fixInput(url);
		this.freqTable = new FrequencyTable(corpus);
		corpus.add(this);
	}
	
	private String fixInput(String url)
	{
		final boolean CONTAINS_HTTP = url.contains("http");
		
		if(!url.contains("www.") && !CONTAINS_HTTP)
			url = "www." + url;
		if(!CONTAINS_HTTP)
			url = "http://" + url;
		
		return url;
	}
	
	public void addWords()
    {
		try
		{
    		//System.out.println("Adding words from url: " + url);
    		
    		//parse body of web page with JSoup
    		String body = Utils.getWebPageBody(url);
    		
    		//split by spaces
    		String[] bodyParts = body != null ? body.split(" ") : null;
    		
    		if(bodyParts == null)
    		{
    			System.out.println("ERROR: COULD NOT PARSE FROM PRIMARY URL!");
    			return;
    		}
    			
    		for(String s : bodyParts)
    		{
    			if(s.length() == 0)
    				continue;
    			if(exclusions.contains(s))
    				continue;
    			
    			freqTable.addWord(s);
    		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
	
	public String getUrl()
	{
		return url;
	}
	
	public FrequencyTable getFreqTable()
	{
		return freqTable;
	}
	
	public void setFreqTable(FrequencyTable t)
	{
		freqTable = t;
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
		CustomUrl other = (CustomUrl) obj;
		if (url == null)
		{
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	
}