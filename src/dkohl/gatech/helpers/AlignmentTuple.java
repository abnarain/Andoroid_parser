package dkohl.gatech.helpers;

import dkohl.gatech.typing.model.FlaggableCharacterString;

/**
 * Container for aligned presented and transcribed string.
 * 
 * @author Daniel Kohlsdorf
 */
public class AlignmentTuple {
    private FlaggableCharacterString presented;
    private FlaggableCharacterString transcript;

    public AlignmentTuple(FlaggableCharacterString presented,
	    FlaggableCharacterString transcript) {
	super();
	this.presented = presented;
	this.transcript = transcript;
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
}
