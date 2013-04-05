package dkohl.gatech.fat_thumbs;

import java.util.Vector;

import dkohl.gatech.fat_thumbs.correction.TypingCorrection;
import dkohl.gatech.fat_thumbs.model.Keystroke;
import dkohl.gatech.fat_thumbs.model.KeystrokeContext;
import dkohl.gatech.typing.classification.Classification;
import dkohl.gatech.typing.error.ErrorCount;
import dkohl.gatech.typing.error.ErrorTypes;

public class FatThumbsClassifier {

    private KeystrokeContext context;
    private Vector<Classification> classifications;

    private ErrorCount count;

    public FatThumbsClassifier() {
	context = new KeystrokeContext(1, 2);
	classifications = new Vector<Classification>();
	setCount(new ErrorCount());
    }
    
    public void pushKeyUp(char typed, long uptime) {
	addMissingKeyUp(typed, uptime);

	if (context.isUsable()) {
	    
	    if (TypingCorrection.detectEqualDownTimeException(context) != ErrorTypes.FT_NOERR) {
		count.inc(ErrorTypes.FT_EQUALDT);
		if(TypingCorrection.detectEqualDownTimeException(context) == ErrorTypes.FT_EQUALDT_ROLLON) {
		    markLast(context.getCurrent().getCharacter(), ErrorTypes.FT_EQUALDT_ROLLON);
		} else {
		    markLast(context.getCurrent().getCharacter(), ErrorTypes.FT_EQUALDT_ROLLOFF);
		}
		context.clear();
	    } else {
		if (TypingCorrection.detectRolloff(context)) {
			count.inc(ErrorTypes.FT_ROLLOFF);
			markLast(context.getCurrent().getCharacter(), ErrorTypes.FT_ROLLOFF);
			context.clear();
		} else {
		    if (TypingCorrection.detectRollon(context)) {
			count.inc(ErrorTypes.FT_ROLLON);
			markLast(context.getCurrent().getCharacter(), ErrorTypes.FT_ROLLON);
			context.clear();
		    }
		}
	    }

	}
    }

    public void pushKeyDown(char typed, long downtime) {
	// ignore stuff

	// update context
	Keystroke stroke = new Keystroke(typed);
	stroke.setDowntime(downtime);
	context.push(stroke);
		
	if (context.isUsable()) {
	    if (TypingCorrection.detectEqualDownTimeException(context) != ErrorTypes.FT_NOERR) {
		count.inc(ErrorTypes.FT_EQUALDT);
		if(TypingCorrection.detectEqualDownTimeException(context) == ErrorTypes.FT_EQUALDT_ROLLON) {
		    markLast(context.getCurrent().getCharacter(), ErrorTypes.FT_EQUALDT_ROLLON);
		} else {
		    markLast(context.getCurrent().getCharacter(), ErrorTypes.FT_EQUALDT_ROLLOFF);
		}
		context.clear();
	    } else {		
		if (TypingCorrection.detectRolloff(context)) {
		    count.inc(ErrorTypes.FT_ROLLOFF);	
		    classifications.get(classifications.size() - 1).setClassification(ErrorTypes.FT_ROLLOFF);
		    context.clear();
		} else {
		    if (TypingCorrection.detectRollon(context)) {
			count.inc(ErrorTypes.FT_ROLLON);
			classifications.get(classifications.size() - 1).setClassification(ErrorTypes.FT_ROLLON);
			context.clear();
		    }
		}
	    }
	}
	// no time or velocity or acceleration
	classifications.add(new Classification(typed, ErrorTypes.FT_NOERR));
	count.inc(ErrorTypes.FT_NOERR);
	
    }
    

    public void markLast(char chr, ErrorTypes err) {
	for (int i = classifications.size() - 1; i >= 0; i--) {
	    char c = classifications.get(i).getTyped();   
    	    if (chr != c) {
    		if(err == ErrorTypes.FT_EQUALDT_ROLLOFF) {
    		    classifications.get(i).setClassification(err);
    		} else {
    		    classifications.get(i - 1).setClassification(err);
    		}
    		break;
    	    } 
	}
    }
    
    public void addMissingKeyUp(char target, long uptime) {
	for (int i = 0; i < context.size(); i++) {
	    Keystroke stroke = context.getStroke(i);
	    if (stroke.getCharacter() == target && stroke.getUptime() == 0) {
		context.getStroke(i).setUptime(uptime);
		return;
	    }
	}
    }

    public Vector<Classification> getClassifications() {
	return classifications;
    }

    public void setCount(ErrorCount count) {
	this.count = count;
    }

    public ErrorCount getCount() {
	return count;
    }

    public void clear() {
	context.clear();
    }

}
