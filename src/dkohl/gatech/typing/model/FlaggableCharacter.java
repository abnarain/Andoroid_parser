package dkohl.gatech.typing.model;

import dkohl.gatech.typing.error.TypingError;

/**
 * A character that can be labled and flagged.
 * 
 * @author Daniel Kohlsdorf
 */
public class FlaggableCharacter {

    
    private boolean isFlagged;
    private TypingError err;
    private int position;
    private char symbol;
    
    public FlaggableCharacter(char symbol) {
	isFlagged = false;
	position = 0;
	this.symbol = symbol;
    }
    
    public void setFlagged(boolean isFlagged) {
	this.isFlagged = isFlagged;
    }
    
    public void setPosition(int position) {
	this.position = position;
    }
    
    public int getPosition() {
	return position;
    }
    
    public boolean isFlagged() {
	return isFlagged;
    }
    
    public char getSymbol() {
	return symbol;
    }

    public void setErr(TypingError err) {
	this.err = err;
    }
    
    public TypingError getErr() {
	return err;
    }
    
    @Override
    public String toString() {
        return "" + symbol;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
	FlaggableCharacter character = new FlaggableCharacter(symbol);
	character.setErr(err);
	character.setPosition(position);
	character.setFlagged(isFlagged);
	return character;
    }
    
    @Override
    public boolean equals(Object obj) {
	FlaggableCharacter chr = (FlaggableCharacter) obj; 
        return symbol == chr.getSymbol();
    }

}
