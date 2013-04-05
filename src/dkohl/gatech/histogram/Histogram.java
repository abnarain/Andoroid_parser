package dkohl.gatech.histogram;

import java.util.HashMap;

public class Histogram extends HashMap<Integer, Double> {
    
    private static final long serialVersionUID = 1L;
    private int inserted;
    
    public Histogram() {
	inserted = 0;
    }

    public Histogram(HashMap<Integer, Double> hist) {
	putAll(hist);
    }
    
    public Double put(Integer key) {
	inserted++;
	double val = 0;
	if(containsKey(key)) {
	    val = get(key);
	}
	val += 1;
	return put(key, val);
    }
    
    public HashMap<Integer, Double> normalized() {
	HashMap<Integer, Double> normalized = new HashMap<Integer, Double>();
	for(Integer i : keySet()) {
	    Double norm = get(i) / inserted;
	    normalized.put(i, norm);
	}
	return normalized;
    }
    
    public int max() {
	int max = Integer.MIN_VALUE;
	for(Integer i : keySet()) {
	    if(i > max) {
		max = i;
	    }
	}
	return max;
    }
    
    public int min() {
	int  min = Integer.MAX_VALUE;
	for(Integer i : keySet()) {
	    if(i < min) {
		min = i;
	    }
	}
	return min;
    }

    
}
