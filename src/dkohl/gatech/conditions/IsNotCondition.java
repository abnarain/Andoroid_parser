package dkohl.gatech.conditions;

import java.util.Vector;

import dkohl.gatech.typing.model.FlaggableCharacter;

/**
 * Checks if a character is not in a set of other
 * characters.
 * 
 * @author Daniel Kohlsdorf
 */
public class IsNotCondition implements Condition {

    private Vector<Character> isnot;

    public IsNotCondition(char isNotThis) {
	isnot = new Vector<Character>();
	isnot.add(isNotThis);
    }
    
    public void add(Character c) {
	isnot.add(c);
    }
    
    @Override
    public boolean applies(FlaggableCharacter chr) {
	for(Character c : isnot) {
	    if(chr.getSymbol() == c) {
		return false;
	    }
	}
	return true;
    }

}
