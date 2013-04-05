package dkohl.gatech.typing.model;


import java.util.Vector;

/**
 * A string of flaggable characters
 * 
 * @author Daniel Kohlsdorf
 */
public class FlaggableCharacterString extends Vector<FlaggableCharacter> {

    private static final long serialVersionUID = 1L; 
    
    public FlaggableCharacterString(FlaggableCharacterString string) {
	for(FlaggableCharacter chr : string) {
	    try {
		add((FlaggableCharacter) chr.clone());
	    } catch (CloneNotSupportedException e) {
		e.printStackTrace();
	    }
	}
    }
    
    public FlaggableCharacterString(String string) {
	for(int i = 0; i < string.length(); i++) {
	    add(new FlaggableCharacter(string.charAt(i)));
	}
    }
 
    public FlaggableCharacterString() {}

    public FlaggableCharacter charAt(int i) {
	return get(i);
    }
    
    public String posString() {
	String str = new String();
	for(FlaggableCharacter c : this) {
	    str += c.getPosition() + ", ";
	}
	return str;
    }
    
    public String flaggedString() {
	String str = new String();
	for(FlaggableCharacter c : this) {
	    str += c.isFlagged() ? "1, " : "0, ";
	}
	return str;
    }
    
    @Override
    public synchronized String toString() {
	String str = new String();
	for(FlaggableCharacter c : this) {
	    str += c.getSymbol() + ", ";
	}
	return str;
    }

    @Override
    public synchronized int hashCode() {
	return toString().hashCode();
    }
}
