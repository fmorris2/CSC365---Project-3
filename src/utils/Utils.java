package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Utils
{
	public static List<String> getSubLinks(String base)
	{
		List<String> linkList = new ArrayList<>();
		try
		{
			Document doc = Jsoup.connect(base).get();
			Elements links = doc.body().select("a[href]");
			
			for(Element link : links)
			{
				String url = link.attr("abs:href");
				
				if(!url.startsWith("https://en.wikipedia.org") || !url.contains("/wiki/") || url.substring(6).contains(":") 
						|| url.contains("#") || url.contains("%"))
					continue;
				
				linkList.add(url);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return linkList;
	}
	
	public static String getWebPageBody(String url)
	{
		try
		{
			Document doc = Jsoup.connect(url).get();
			Element body = doc.body();
			return body == null ? null : body.text().replaceAll("[^a-zA-Z ]", "").toLowerCase();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static int random(int min, int max)
	{
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
}
