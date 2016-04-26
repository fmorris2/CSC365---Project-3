package similarity;


public class CustomUrl
{
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
	
	public String getUrl()
	{
		return url;
	}
	
	public FrequencyTable getFreqTable()
	{
		return freqTable;
	}
}