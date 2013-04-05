package dkohl.gatech.typing.error;

import java.util.Vector;

/**
 * The final result of error classification.
 * A set of weighted characters. 
 * 
 * @author Daniel Kohlsdorf
 */
public class ErrorStream extends Vector<WeightedError> {

    private static final long serialVersionUID = 1L;
    
    private boolean ignore_noerr;
    
    public ErrorStream(int len) {
	for(int i = 0; i < len; i++) {
	    add(new WeightedError());
	}
    }

    public void set(int index, TypingError err) {
	get(index).add(err);
    }
    
    public TypingError max(int index) {
	return get(index).max();
    }
    
    @Override
    public synchronized String toString() {
	String str = "";
	for(WeightedError err : this) {
	    if (err.max() != null) {
		if(!(ignore_noerr && (err.max().getType() == ErrorTypes.UNC_NO_ERR || 
			err.max().getType() == ErrorTypes.OBO_NOERR))) { 
		    
		    str += err.max();
		    str += "\n";
		}
	    }
	}
	return str;
    }

    public void setIgnore_noerr(boolean ignore_noerr) {
	this.ignore_noerr = ignore_noerr;
    }

    public boolean isIgnore_noerr() {
	return ignore_noerr;
    }

}
