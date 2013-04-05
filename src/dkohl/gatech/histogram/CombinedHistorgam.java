package dkohl.gatech.histogram;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class CombinedHistorgam extends HashMap<String, Histogram>{
    
    private static final long serialVersionUID = 1L;
    
    private String header;
    
    public CombinedHistorgam(String names[]) {
	header = "key, ";
	for(String name : names) {
	    header += name + ", ";
	    put(name, new Histogram());
	}
    }
    
    public void put(String name, int accel) {
	get(name).put((int) accel / 10);
    }
    
    public int global_max() {
	int max = Integer.MIN_VALUE;
	for(String key : keySet()) {
	    if(get(key).max() > max) {
		max = get(key).max();
	    }
	}
	return max;
    }
    
    public int global_min() {
	int min = Integer.MIN_VALUE;
	for(String key : keySet()) {
	    if(get(key).max() > min) {
		min = get(key).min();
	    }
	}
	return min;
    }

    
    public void csv(String filename) throws IOException {
	header = "accel, ";
	for(String key : keySet()) {
	    header += key + ", ";
	}
	BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	writer.write(header + "\n");
	for(int i = global_min(); i < global_max(); i++) {
	    boolean one_not_init = false;
	    String str = i + ", ";
	    for(String key : keySet()) {
		if (get(key).containsKey(i)) {
		    str += get(key).get(i) + ",";
		    one_not_init = true;
		} else {
		    str += 0 + ",";
		}
	    }
	    if (one_not_init) {
		writer.write(str + "\n");
	    }
	}
	writer.close();
    }
    
    public void norm() {
	for (String key : keySet()) {
	    HashMap<Integer, Double> h = get(key).normalized();
	    put(key, new Histogram(h));
	}
    }
    
    
}
