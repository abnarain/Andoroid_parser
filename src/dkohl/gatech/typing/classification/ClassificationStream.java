package dkohl.gatech.typing.classification;

import java.util.Vector;

import dkohl.gatech.histogram.CombinedHistorgam;
import dkohl.gatech.histogram.Hist;
import dkohl.gatech.typing.error.ErrorCount;
import dkohl.gatech.typing.error.ErrorStream;
import dkohl.gatech.typing.error.ErrorTypes;
import dkohl.gatech.typing.error.WeightedError;
import dkohl.gatech.typing.model.FlaggableCharacterString;

public class ClassificationStream extends Vector<Classification>{

    private static final long serialVersionUID = 1L;
    private boolean ignore_noerr;
    private FlaggableCharacterString input;
    
    public ClassificationStream(Vector<Classification> classification) {	
	// delete special characters
	for(Classification c : classification) {
	    if(c.getTyped() != '_' && c.getTyped() != '-' && c.getTyped() != '<') {
		add(c);
	    }
	}
    }
    
    public String alignment(ErrorStream obostream) {
	String str = "";
	int i = 0;
	for(WeightedError err : obostream) {
	    if (err.max() != null && i < size()) {
		if(!(ignore_noerr && (err.max().getType() == ErrorTypes.UNC_NO_ERR || 
			err.max().getType() == ErrorTypes.OBO_NOERR))) { 
		    if (err.max().getIs() == get(i).getTyped() || err.max().getShould() == get(i).getTyped()) {
			
			str += err.max() + " " + get(i) + " " + get(i).getAcceleration() + "\n";
			i++;
		    } else {
			str += err.max() + "\n";
		    }
		}
	    }
	}
	return str;
    }
    
    @Override
    public synchronized String toString() {
	String str = "";
	for(Classification c : this) {
	    System.out.println(c);
	}
	return str;
    }
    
    /**
     * OBO Works
     * FT_ROLLON fails 5
     * FT_ROLLOFFS fails 1
     * FT_EQUALDT fails 1
     */
    public ErrorCount classification(ErrorStream obostream) {
	ErrorCount count = new ErrorCount();
	int i = 0;
	int j = 0;
	int misses_smaller_4 = 0;
	boolean skip = false;
	for (WeightedError err : obostream) {
	    if (err.max() != null && i < size()) {
		if (err.max().getIs() != get(i).getTyped() && err.max().getShould() != get(i).getTyped()) {
		    count.inc(err.max().getType());
		    j++;
		    continue;
		}
		
		if(skip) {
		    skip = false;
		    i++;
		    j++;
		    continue;
		}

		// decision
		ErrorTypes err_class = get(i).getClassification();
		ErrorTypes err_ground = err.max().getType();
		if (err_ground == ErrorTypes.OBO_NOERR) {
		    count.inc(ErrorTypes.OBO_NOERR);
		    
		    if (err_class == ErrorTypes.FT_ROLLON_CONTEXT || err_class == ErrorTypes.FT_ROLLON) {
			// if next line is multiple ignore next
			// and count current as detected rollon
			if(j + 1 < obostream.size() && obostream.get(j + 1).max() != null) {
			    if(obostream.get(j + 1).max().getType() == ErrorTypes.OBO_MULTIPLE) {
				count.inc(ErrorTypes.DET_ROLLON);
				if (i > 2) Hist.hist().put("INCERR", get(i).getAcceleration());
				skip = true;
			    } else {
				count.inc(ErrorTypes.FALSE_TRIGGER);
				count.inc(ErrorTypes.WRONG_DET_ROLLON);
				if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
			    }
			}
		    }
		    
		    if(err_class == ErrorTypes.FT_EQUALDT_ROLLON || err_class == ErrorTypes.FT_EQUALDT_ROLLOFF || err_class == ErrorTypes.FT_EQUALDT) {
			// wrong deteced equal dt
			// when next is rolloff or multiple 
			// otherwise is a false trigger
			if(j + 1 < obostream.size()) {
			    if(obostream.get(j + 1).max() != null && obostream.get(j + 1).max().getType() == ErrorTypes.OBO_ROLLOFF)  {
				if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
				count.inc(ErrorTypes.DET_EQUALDT);
				count.inc(ErrorTypes.FALSE_TRIGGER);
				skip = true;
			    } else if(obostream.get(j + 1).max() != null && obostream.get(j + 1).max().getType() == ErrorTypes.OBO_MULTIPLE) {
				if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
				count.inc(ErrorTypes.DET_EQUALDT);
				skip = true;
			    } else {
				if (i > 2) Hist.hist().put("INCERR", get(i).getAcceleration());
				count.inc(ErrorTypes.FALSE_TRIGGER);
				count.inc(ErrorTypes.WRONG_DET_EQUALDT);
			    }
			}
		    }
		    if (err_class == ErrorTypes.FT_ROLLOFF) {
			if (i > 2) Hist.hist().put("INCERR", get(i).getAcceleration());
			count.inc(ErrorTypes.FALSE_TRIGGER);
			count.inc(ErrorTypes.WRONG_DET_ROLLOFF);
		    }
		    if (err_class == ErrorTypes.FT_NOERR) {
			count.inc(ErrorTypes.DET_NOERR);
			if (i > 2) Hist.hist().put("NOERR", get(i).getAcceleration());
		    }
		}

		if (err_ground == ErrorTypes.OBO_MULTIPLE) {
		    count.inc(ErrorTypes.OBO_MULTIPLE);
		    if (err_class == ErrorTypes.FT_ROLLON) {
			if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
			count.inc(ErrorTypes.DET_ROLLON);
		    }
		    if (err_class == ErrorTypes.FT_ROLLON_CONTEXT) {
			if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
			count.inc(ErrorTypes.DET_ROLLON);
		    }
		    if (err_class == ErrorTypes.FT_ROLLOFF) {
			if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
			count.inc(ErrorTypes.DET_ROLLOFF);
		    }
		    if (err_class == ErrorTypes.FT_EQUALDT_ROLLON || err_class == ErrorTypes.FT_EQUALDT_ROLLOFF || err_class == ErrorTypes.FT_EQUALDT) {
			if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
			count.inc(ErrorTypes.DET_EQUALDT);
		    } 

		    if (err_class == ErrorTypes.FT_NOERR) {
			// if next is rolloff
			// count det rolloff 
			// otherwise undet multiple
			if(i + 1 < size()) {
			    if(get(i + 1).getClassification() != null && get(j + 1).getClassification() == ErrorTypes.FT_ROLLOFF) {
				count.inc(ErrorTypes.DET_ROLLOFF);
				if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
				skip = true;
			    }
			} else {
			    if (i < 4) {
				misses_smaller_4++;
			    }
			    count.inc(ErrorTypes.UNDET_MULTIPLE);
			    if (i > 2) Hist.hist().put("INCERR", get(i).getAcceleration());
			}
		    }
		}

		if (err_ground == ErrorTypes.OBO_ROLLON) {
		    count.inc(ErrorTypes.OBO_ROLLON);
		    if (err_class == ErrorTypes.FT_ROLLON) {
			count.inc(ErrorTypes.DET_ROLLON);
			if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
		    }
		    if (err_class == ErrorTypes.FT_ROLLON_CONTEXT) {
			count.inc(ErrorTypes.DET_ROLLON);
			if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
		    }
		    if (err_class == ErrorTypes.FT_ROLLOFF) {
			count.inc(ErrorTypes.ROLLON_AS_ROLLOFF);
			count.inc(ErrorTypes.WRONG_DET_ROLLOFF);
			if (i > 2) Hist.hist().put("INCERR", get(i).getAcceleration());
		    }
		    if (err_class == ErrorTypes.FT_EQUALDT_ROLLON || err_class == ErrorTypes.FT_EQUALDT_ROLLOFF || err_class == ErrorTypes.FT_EQUALDT) {
			count.inc(ErrorTypes.DET_EQUALDT);
			if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
		    }
		    if (err_class == ErrorTypes.FT_NOERR) {
			// classify equal dt
			if(i + 1 < size()) {
			    if(get(i + 1).getClassification() == ErrorTypes.FT_EQUALDT_ROLLOFF || get(i + 1).getClassification() == ErrorTypes.FT_EQUALDT_ROLLON ||  get(i + 1).getClassification() == ErrorTypes.FT_EQUALDT) {
				count.inc(ErrorTypes.DET_EQUALDT);
				if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());
				skip = true;
			    } else {
				if (i < 4) {
				    misses_smaller_4++;
				}
				if (i > 2) Hist.hist().put("INCERR", get(i).getAcceleration());
				count.inc(ErrorTypes.UNDET_ROLLON);
			    }
			} else {
			    if (i < 4) {
				misses_smaller_4++;
			    }
			    if (i > 2) Hist.hist().put("INCERR", get(i).getAcceleration());	
			    count.inc(ErrorTypes.UNDET_ROLLON);
			}
		    }
		}

		if (err_ground == ErrorTypes.OBO_ROLLOFF) {
		    count.inc(ErrorTypes.OBO_ROLLOFF);

		    if (err_class == ErrorTypes.FT_ROLLON) {
			count.inc(ErrorTypes.WRONG_DET_ROLLON);
			if (i > 2) Hist.hist().put("INCERR", get(i).getAcceleration());	
		    }
		    if (err_class == ErrorTypes.FT_ROLLON_CONTEXT) {
			count.inc(ErrorTypes.WRONG_DET_ROLLON);
			count.inc(ErrorTypes.ROLLOFF_AS_ROLLON);
			if (i > 2) Hist.hist().put("INCERR", get(i).getAcceleration());	
		    }
		    if (err_class == ErrorTypes.FT_ROLLOFF) {
			count.inc(ErrorTypes.DET_ROLLOFF);
			if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());	
		    }
		    if (err_class == ErrorTypes.FT_EQUALDT_ROLLON || err_class == ErrorTypes.FT_EQUALDT_ROLLOFF || err_class == ErrorTypes.FT_EQUALDT) {
			count.inc(ErrorTypes.DET_EQUALDT);
			if (i > 2) Hist.hist().put("CORERR", get(i).getAcceleration());	
		    } 
		    if (err_class == ErrorTypes.FT_NOERR) {
			    if (i < 4) {
				misses_smaller_4++;
			    }
			    count.inc(ErrorTypes.UNDET_ROLLOFF);
			    if (i > 2) Hist.hist().put("INCERR", get(i).getAcceleration());
		    }
		}
		i++;
	    } 
	    j++;
	}
	count.inc(ErrorTypes.UNDET_FIRST_FOUR, misses_smaller_4);
	return count;
    }
    
    public void setIgnore_noerr(boolean ignore_noerr) {
	this.ignore_noerr = ignore_noerr;
    }

    public boolean isIgnore_noerr() {
	return ignore_noerr;
    }

    public void setInput(FlaggableCharacterString input) {
	this.input = input;
    }

    public FlaggableCharacterString getInput() {
	return input;
    }
}
