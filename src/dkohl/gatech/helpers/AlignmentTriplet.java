package dkohl.gatech.helpers;

import dkohl.gatech.typing.model.FlaggableCharacterString;

/**
 * Triplet containing presented, transcribed and inputs stream,
 * all alligned.
 * 
 * @author Daniel Kohlsdorf
 */
public class AlignmentTriplet {

    private FlaggableCharacterString presented;
    private FlaggableCharacterString transcript;
    private FlaggableCharacterString inputstream;
    
    public AlignmentTriplet(FlaggableCharacterString presented,
	    FlaggableCharacterString transcript,
	    FlaggableCharacterString inputstream) {
	super();
	this.presented = presented;
	this.transcript = transcript;
	this.inputstream = inputstream;
    }

    public FlaggableCharacterString getPresented() {
        return presented;
    }
    
    public void setPresented(FlaggableCharacterString presented) {
        this.presented = presented;
    }
    
    public FlaggableCharacterString getTranscript() {
        return transcript;
    }
    
    public void setTranscript(FlaggableCharacterString transcript) {
        this.transcript = transcript;
    }
    
    public FlaggableCharacterString getInputstream() {
        return inputstream;
    }
    
    public void setInputstream(FlaggableCharacterString inputstream) {
        this.inputstream = inputstream;
    }
    
}
