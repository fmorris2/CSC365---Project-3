package utils;

import java.util.ArrayList;
import java.util.List;

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
				
				if(!url.startsWith("https://en.wikipedia.org") || !url.contains("/wiki/") || url.substring(6).contains(":") || url.contains("#"))
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
}
