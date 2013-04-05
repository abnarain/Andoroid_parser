package dkohl.gatech.helpers;

import java.util.Vector;

import dkohl.gatech.conditions.Condition;
import dkohl.gatech.conditions.IsLetterCondition;
import dkohl.gatech.typing.model.FlaggableCharacter;
import dkohl.gatech.typing.model.FlaggableCharacterString;

/**
 * Helper methods needed by InputStreamAnalysis
 * 
 * @author Daniel Kohlsdorf
 */
public class Util {
    
    /**
     * Delete noise from input stream.
     * Only allows characters, '<' and ' '
     */
    public static String clean_input(String input) {
	String clean = "";
	for(int i = 0; i < input.length(); i++) {
	    if(Character.isLetter(input.charAt(i)) || input.charAt(i) == ' ' || input.charAt(i) == '<') {
		clean += input.charAt(i);
	    }
	}
	return clean;

    }
    
    /**
     * Apply '<' to a cleaned input stream such that
     * the result is the transcribed string
     */
    public static String transcript(String inputStream) {
	String transcript = "";
	for(int i = 0; i < inputStream.length(); i++) {
	    if(inputStream.charAt(i) == '<') {
		if(transcript.length() > 0) {
		    transcript = transcript.substring(0, transcript.length() - 1);
		}
	    } else if(Character.isLetter(inputStream.charAt(i)) || inputStream.charAt(i) == ' ') {
		transcript += inputStream.charAt(i);
	    }
	}
	return transcript;
    }
    
    public static int numLetters(AlignmentTriplet triplet) {
	FlaggableCharacterString _p = triplet.getPresented();
	FlaggableCharacterString _t = triplet.getTranscript();
	FlaggableCharacterString _i = triplet.getInputstream();
	IsLetterCondition is = new IsLetterCondition();
	
	int num = 0;
	for(int i = 0; i < _i.size(); i++) {

	    if(is.applies(_p.get(i)) && _p.get(i).getErr() != null) {
		num++;
	    }
	    if(is.applies(_t.get(i)) && _t.get(i).getErr() != null) {
		num++;
	    }
	    if(is.applies(_i.get(i)) && _i.get(i).getErr() != null) {
		num++;
	    }
	}
	return num;
    }

    /**
     * Count number of errors on letters
     */
    public static int minNumLetters(Vector<AlignmentTriplet> triplets) {
	int min = Integer.MAX_VALUE;
	for (AlignmentTriplet triplet : triplets) {
	    int num = numLetters(triplet);

	    if(num < min) {
		min = num;
	    } 
	}
	return min;
    }
    
    /**
     * Check backwards in string for condition
     */
    public static int lookBack(FlaggableCharacterString target, int start, int count, Condition condition) {
	int index = start;
	
	while(0 <= index && index < target.size() && !condition.applies(target.get(index))) {
	    index--;
	}
	
	while(count > 0 && index > 0) {
	    index--;
	    if(index < 0){ 
		break;
	    } else if(condition.applies(target.get(index))) {
		count--;
	    }
	}
	
	return index;
    }
    
    /**
     * Check forwards in string for condition
     */
    public static int lookAhead(FlaggableCharacterString target, int start, int count, Condition condition) {
	int index = start;
	
	while(0 <= index && index < target.size() - 1 && !condition.applies(target.get(index))) {
	    index++;
	}
	
	// was index < target.size() 
	while(count > 0 && index < target.size() - 1) {
	    index++;
	    if(index == target.size()){ 
		break;
	    } else if(condition.applies(target.get(index))) {
		count--;
	    }
	}

	return index;
    }
    
    /**
     * Inserts a character (toInsert) into a (target) String at position (pos).
     */
    public static void insertInto(FlaggableCharacterString target, int pos, FlaggableCharacter toInsert) {
	target.insertElementAt(toInsert, pos);
    }
    
    /**
     * Will return 0 if a character x and another one y are not equal 
     * and zero otherwise.
     */
    public static int characterDistance(FlaggableCharacter x, FlaggableCharacter y) {
	if(x.equals(y)) {
	    return 0;
	}
	return 1;
    }
    
    /**
     * Computes the minimum between three integers
     */
    public static int min(int x, int y, int z) {
	if(x < y && x < z) {
	    return x;
	}
	if(y < x && y < z) {
	    return y;
	}
	return z;
    }
    
}
