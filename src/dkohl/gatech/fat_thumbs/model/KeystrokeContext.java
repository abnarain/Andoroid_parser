package dkohl.gatech.fat_thumbs.model;

import java.util.Vector;

/**
 * Defines a context around a Keystroke by
 * keeping the last n Keystrokes and the 
 * m next Keystrokes
 * 
 * @author Daniel Kohlsdorf
 */
public class KeystrokeContext {
	
	/**
	 * The actual context
	 */
	protected Vector<Keystroke> context;
	
	/**
	 * The total length of the context
	 */
	protected int len;
	
	/**
	 * n strokes back
	 */
	protected int numPrevious;
		
	public KeystrokeContext(int ahead, int back) {
		context = new Vector<Keystroke>();
		this.len = back + 1 + ahead;
		this.numPrevious = back;
	}
	
	/**
	 * Adds a Keystroke to the context
	 * in LIFO manner.
	 * @param stroke
	 */
	public void push(Keystroke stroke) {
		if(context.size() >= len) {
			context.removeElementAt(0);
		}
		context.addElement(stroke);
	}
	
	/**
	 * is the current context fully assigned
	 * 
	 * @return
	 */
	public boolean isUsable() {
		// do pp, p, c, f exist?
		if(context.size() != len) {
			return false;
		} else {
			if(getCurrent().getUptime() == 0) {
				return false;
			}
			if(getCurrent().getCharacter() == ' ') {
				return false;
			}			
			return true;
		}
	}
	
	public Keystroke getCurrent() {
		if(context.size() < numPrevious + 1){
			return null;
		}
		return (Keystroke) context.elementAt(numPrevious);
	}
	
	public String ngram(int []mask) {
		String ngram = new String();
		for(int i = 0; i < mask.length; i++) {
			Keystroke stroke = (Keystroke) context.elementAt(mask[i]);
			ngram += stroke.getCharacter();
		}
		return ngram;
	}
	
	//produces a string of just the letters, which is the right format for the log file.
	public String toLogString() {
		String ngram = new String();
		for(int i = 0; i < context.size(); i++) {
			Keystroke stroke = (Keystroke) context.elementAt(i);
			ngram += stroke.getCharacter();
		}
		return ngram;
		
	}
	
	public void clear() {
	    context = new Vector<Keystroke>();
	}
	
	public Keystroke getStroke(int i) {
	    return (Keystroke) context.elementAt(i);
	}
	
	public int getNumPrevious() {
		return numPrevious;
	}
	
	public int size() {
		return context.size();
	}
	
	protected int getLen() {
		return len;
	}
}

