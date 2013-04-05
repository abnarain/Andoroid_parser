package dkohl.gatech.typing.error;

/**
 * Error class
 * contains the intended character, 
 * the typed character and the error 
 * type
 * 
 * @author Daniel Kohlsdorf
 */
public class TypingError {
        
    private ErrorTypes type;
    private char symbol, should;
    
    public TypingError(ErrorTypes type, char is, char should) {
	super();
	this.type = type;
	this.symbol = is;
	this.should = should;
    }

    public ErrorTypes getType() {
        return type;
    }

    public void setType(ErrorTypes type) {
        this.type = type;
    }

    public char getIs() {
        return symbol;
    }

    public void setIs(char is) {
        this.symbol = is;
    }
    
    public char getShould() {
	return should;
    }
    
    @Override
    public String toString() {
	return type + "(" + should + " " + symbol  + ")";
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
	TypingError err = (TypingError) obj;
	return err.getType() == type && err.getIs() == symbol && err.getShould() == should;
    }

    
}
