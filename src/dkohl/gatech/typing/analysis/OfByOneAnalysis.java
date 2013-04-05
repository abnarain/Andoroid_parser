package dkohl.gatech.typing.analysis;

import java.util.Vector;

import dkohl.gatech.conditions.IsLetterCondition;
import dkohl.gatech.helpers.AlignmentTriplet;
import dkohl.gatech.helpers.KeyMap;
import dkohl.gatech.typing.classification.ClassificationStreamSingleton;
import dkohl.gatech.typing.error.ErrorCount;
import dkohl.gatech.typing.error.ErrorStream;
import dkohl.gatech.typing.error.ErrorTypes;
import dkohl.gatech.typing.error.TypingError;
import dkohl.gatech.typing.model.FlaggableCharacter;
import dkohl.gatech.typing.model.FlaggableCharacterString;

/**
 * Searching for of by one errors. These errors can be found using transcribed
 * and presented only.
 * 
 * If presented and transcribed are aligned, an insertion is indicated by '-' in
 * the presented string.
 * 
 * ROLLON ROLLOFF REPEAT SUBSTITUTION
 * 
 * all off by one.
 * 
 * @author Daniel Kohlsdorf
 */
public class OfByOneAnalysis {
 
    public static boolean weCare(FlaggableCharacter fromPres, FlaggableCharacter fromTrans, FlaggableCharacter fromIn) {
	boolean weCare = false;
	if(fromTrans.getErr() != null) {
	    weCare =  fromTrans.getErr().getType() == ErrorTypes.UNC_INSERTION || fromTrans.getErr().getType() == ErrorTypes.UNC_SUBS;
	}
	if(fromPres.getErr() != null) {
	    weCare |= fromPres.getErr().getType() == ErrorTypes.COR_SUBS;
	}
	if(fromIn.getErr() != null) {
	    weCare |=  fromIn.getErr().getType() == ErrorTypes.COR_INSERTION;
	}
	return weCare;
    }
    
    public static ErrorCount obo_determine(Vector<AlignmentTriplet> alignment, int silent) {
	ErrorCount counter = new ErrorCount();
	
	// min phrase
	int min = Integer.MAX_VALUE;
	for(AlignmentTriplet triplet : alignment) {	
	    if(triplet.getInputstream().size() < min) {
		// size - num '_'
		min = triplet.getInputstream().size();
	    }
	}
	
	ErrorStream stream = new ErrorStream(min);	
	for (AlignmentTriplet triplet : alignment) {
	    FlaggableCharacterString presented = triplet.getPresented();
	    FlaggableCharacterString trans = triplet.getTranscript();
	    FlaggableCharacterString input = triplet.getInputstream();
	    
	    if(input.size() != min) {
		if (silent == 2 && silent >= 0) {
		    System.out.println("ALIGNMENT");
		    System.out.println("P " + presented);
		    System.out.println("T " + trans);
		    System.out.println("I " + input);
		    System.out.println("--- IGNORED --- ");
		    System.out.println("--------------");
		}
		continue;
	    }
	    if (silent == 2 && silent >= 0) {
		System.out.println("ALIGNMENT");
		System.out.println("P " + presented);
		System.out.println("T " + trans);
		System.out.println("I " + input);
		System.out.println("--------------");
	    }

	    IsLetterCondition isLetter = new IsLetterCondition();
	    String align = "";	
	    Vector<TypingError> err = new Vector<TypingError>();
	    for (int i = 0; i < presented.size(); i++) {
		int j = i;
		boolean rollon = false;
		boolean rolloff = false;
		
		if(weCare(presented.get(i), trans.get(i), input.get(i))) {
		    if (i + 1 < input.size()) {
			// rollon
			char next_intended = input.get(i + 1).getSymbol();
			char typed = input.get(i).getSymbol();
			boolean all_letters = (isLetter.applies(input.get(i + 1))
					&& isLetter.applies(input.get(i)));
			rollon = KeyMap.adjacent(next_intended, typed)
				&& all_letters;
		    }
		    
		    // Rolloff
		    if (i > 1) {
			char prev_intended = input.get(i - 1).getSymbol();
			if(prev_intended == '<') {
			    j = i - 2;
			    while(j > 0 && prev_intended == '<') {
				prev_intended = input.get(j).getSymbol();
				j--;
			    }
			}
			char typed = input.get(j).getSymbol();
			boolean all_letters = Character.isLetter(prev_intended)
				&& Character.isLetter(typed);

			rolloff = KeyMap.adjacent(prev_intended, typed)
				&& all_letters;
		    }
		} 
		

		if (rolloff && !rollon) {
		    // set rolloff
		    if(j  + 1 < err.size()) {
			TypingError newRolloff = new TypingError(ErrorTypes.OBO_ROLLOFF, input.get(j).getSymbol(), input.get(j + 1).getSymbol());
			err.set(j + 1, newRolloff);
			err.add(new TypingError(ErrorTypes.OBO_NOERR, input
				.get(i).getSymbol(), input.get(i).getSymbol()));
		    }  else {
			err.add(new TypingError(ErrorTypes.OBO_ROLLOFF, input
				.get(j - 1).getSymbol(), input.get(j).getSymbol()));
		    }
		    // add 
		} else if (!rolloff && rollon) {
		    TypingError newRollon = new TypingError(ErrorTypes.OBO_ROLLON, input.get(i + 1).getSymbol(), input.get(i).getSymbol());
		    err.add(newRollon);	    
		} else if (rolloff && rollon) {
		    TypingError newMult = new TypingError(ErrorTypes.OBO_MULTIPLE, input.get(i).getSymbol(), '-');
		    err.add(newMult);
		}  else {
		    if (isLetter.applies(input.get(i))) {
			TypingError newNoObo = new TypingError(
				ErrorTypes.OBO_NOERR, input.get(i).getSymbol(),
				input.get(i).getSymbol());
			err.add(newNoObo);
		    }
		}
	    } // end i -> presented.size
	    
	    for(int i = 0; i < err.size(); i++) {
		align += err.get(i) + "\n";
		stream.set(i, err.get(i));
	    }
	    
	    if (silent == 2 && silent >= 0) {
		System.out.println("OBO Errors:");
		System.out.println(align);
		System.out.println("--------------");
	    }

	}
	
	counter.merge(ClassificationStreamSingleton.getStream().classification(stream));
	if (silent >= 0) {
	    System.out.println("OBO RESULT BASED ON MAYORITY\n");
	    if (silent == 0) {
		stream.setIgnore_noerr(true);
	    }
	    if(ClassificationStreamSingleton.getStream() != null) {
		if (silent == 0) {
		    ClassificationStreamSingleton.getStream().setIgnore_noerr(true);
		}
		System.out.println(ClassificationStreamSingleton.getStream().alignment(stream));
	    }
	}
	return counter;
    }
}
