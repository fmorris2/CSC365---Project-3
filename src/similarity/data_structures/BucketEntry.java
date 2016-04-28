package similarity.data_structures;

import java.io.Serializable;

public class BucketEntry<K, V> implements Serializable
{
	private static final long serialVersionUID = 9122454182878454672L;
	
	private K key;
	private V value;
	
	public BucketEntry(K key, V value)
	{
		this.key = key;
		this.value = value;
	}
	
	public K getKey()
	{
		return key;
	}
	
	public V getValue()
	{
		return value;
	}
	
	public void setValue(V val)
	{
		value = val;
	}
}