package similarity;

import java.util.HashSet;
import java.util.Set;

import similarity.data_structures.CustomHashTable;

/*
 * Each URL given by the user will have an instance of this Object assigned to it.
 * 
 * This "FrequencyTable" is essentially a table of the TF-IDF values for each word,
 * hence the String being the key (representing the word), and a Double being the value
 * (representing the TF-IDF value)
 */
public class FrequencyTable extends CustomHashTable<String, Word>
{
	private int maxRawFrequency;
	private Corpus corpus;
	
	public FrequencyTable(Corpus corpus)
	{
		this.corpus = corpus;
	}
	
	public void addWord(String word)
	{
		Word wordEntry = get(word);
		
		if(wordEntry == null)
			put(word, new Word());
		else
			wordEntry.incrementRawFrequency();
	}
	
	public void calculate()
	{
		for(String key : keySet())
		{
			Word word = get(key);
			if(word != null)
				word.setTfIdf(calculateTfIdf(key));
		}
	}
	
	public static double calculateAngle(FrequencyTable one, FrequencyTable two)
	{
		double dotProduct = dotProduct(one, two);
		double magnitudeOne = one.magnitude();
		double magnitudeTwo = two.magnitude();
		
		return dotProduct / (magnitudeOne * magnitudeTwo);
	}
	
	private static double dotProduct(FrequencyTable one, FrequencyTable two)
	{
		double sum = 0;
		
		//merge key sets
		Set<String> mergedKeySet = new HashSet<>();
		mergedKeySet.addAll(one.keySet());
		mergedKeySet.addAll(two.keySet());
		
		//iterate through words
		for(String word : mergedKeySet)
		{
			Word first = one.get(word);
			Word second = two.get(word);
			
			double valOne = first == null ? 0 : first.getTfIdf();
			double valTwo = second == null ? 0 : second.getTfIdf();
			
			sum += valOne * valTwo;
		}
		
		return sum;
	}
	
	public double magnitude()
	{
		return Math.sqrt(dotProduct(this, this));
	}
	
	private double calculateTfIdf(String word)
	{
		return calculateTermFreq(word) * calculateInverseDocFreq(word);
	}
	
	private double calculateInverseDocFreq(String word)
	{
		return Math.log((double)corpus.size() / (1 + corpus.getTotalDocsContainingTerm(word)));
	}
	
	private double calculateTermFreq(String word)
	{
		Word wordEntry = get(word);
		
		int rawFreq = wordEntry == null ? 0 : wordEntry.getRawFrequency();
		
		if(rawFreq > maxRawFrequency)
			maxRawFrequency = rawFreq;
		
		return 0.5 + (0.5 * (rawFreq / maxRawFrequency));
	}
}