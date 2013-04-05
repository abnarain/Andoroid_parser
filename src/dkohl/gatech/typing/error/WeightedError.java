package dkohl.gatech.typing.error;

import java.util.HashMap;

/**
 * Contains a set of possible error
 * explanation for a specific position
 * 
 * @author Daniel Kohlsdorf
 */
public class WeightedError {

    private HashMap<TypingError, Integer> count;
    
    public WeightedError() {
	count = new HashMap<TypingError, Integer>();
    }
    
    public void add(TypingError err) {
	int c = 1;
	if(count.containsKey(err)) {
	    c += count.get(err);
	}
	count.put(err, c);
    }
    
    public TypingError max() {
	int max_val = Integer.MIN_VALUE;
	TypingError max_err = null;
	for(TypingError err : count.keySet()) {
	    if(count.get(err) >=  max_val) {
		max_val = count.get(err);
		max_err = err;
	    }
	}
	return max_err;
    }
    
}
