import java.awt.Component;
import java.awt.image.ComponentSampleModel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dkohl.gatech.conditions.IsLetterCondition;
import dkohl.gatech.conditions.IsNotCondition;
import dkohl.gatech.helpers.AlignmentTriplet;
import dkohl.gatech.helpers.AlignmentTuple;
import dkohl.gatech.helpers.Util;
import dkohl.gatech.typing.analysis.InputStreamAnalysis;
import dkohl.gatech.typing.analysis.OfByOneAnalysis;
import dkohl.gatech.typing.classification.ClassificationStreamSingleton;
import dkohl.gatech.typing.error.ErrorCount;
import dkohl.gatech.typing.error.ErrorStream;
import dkohl.gatech.typing.error.ErrorTypes;
import dkohl.gatech.typing.error.TypingError;
import dkohl.gatech.typing.error.WeightedError;
import dkohl.gatech.typing.model.FlaggableCharacter;
import dkohl.gatech.typing.model.FlaggableCharacterString;


public class Main {

	public static void main(String[] args) throws IOException{
		System.out.println("this is not it!");
		String file ="data/log1.txt";
		BufferedReader stream = null;
		try {
			stream = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
			System.out.println("The log file was not found!");
		}
		String line = stream.readLine();
		while ((line = stream.readLine()) != null) {
			//String components[] = line.split(", ");	
		 		 
			Pattern presented_pattern = Pattern.compile("^PRESENTED_STRING");
			Pattern transcribed_pattern = Pattern.compile("^TRANSCRIBED_STRING");
			Pattern input_pattern = Pattern.compile("^INPUT_STREAM");
			System.out.println("the line is ");
			System.out.println(line);
			Matcher presented_matcher = presented_pattern.matcher(line);
			Matcher transcribed_matcher = transcribed_pattern.matcher(line);
			Matcher input_matcher = input_pattern.matcher(line);
			if(presented_matcher.find()){
				System.out.println("We got a presented matcher");
				String [] presented_components= line.split(",");
				System.out.println(presented_components[0]);
				System.out.println(line);			
				System.out.println(presented_components[0]);
				System.out.println(presented_components[1]);						
			}
		
			if(transcribed_matcher.find()){
				System.out.println("We got a transcribed matcher");
				String [] transcribed_components= line.split(",");
				System.out.println(transcribed_components[0]);
				System.out.println(line);			
				System.out.println(transcribed_components[0]);			
				System.out.println(transcribed_components[1]);						
			}
			if(input_matcher.find()){
				System.out.println("We got a input matcher");
				String [] input_components= line.split(",");
				System.out.println(input_components[0]);
				System.out.println(line);						
				System.out.println(input_components[0]);
				if(input_components.length > 0){
					try {
						System.out.println(input_components[1]);
					} catch (Exception e) {
						System.out.println("input string is empty !");
					}						
				}
			}	
		//System.out.println("components are ");
		 //System.out.println(components);
		 //System.exit(1);
		 
		}
	//strings(presented, transcribed, input, silent);
	//input_stream is the stream of keystrokes with backspaces.
	//transcribed string is the final string that they submitted.
	}


/**
 * Flagging the characters in input stream that compose the transcribed
 * stream. Returning a new stream with all flagged characters (characters
 * from transcribed) as upper case.
 */
public static void flag(FlaggableCharacterString inputStream) {
int count = 0;
for (int i = inputStream.size() - 1; i >= 0; i--) {
    FlaggableCharacter chr = inputStream.charAt(i);
    if (chr.getSymbol() == '<') {
	count++;
	//  since when is ' ' a letter ?????? 0o
    } else if (new IsLetterCondition().applies(chr)) {
	if (count == 0) {
	    chr.setFlagged(true);
	} else {
	    count--;
	}
    }
}

}

/**
 * Computes the dynamic programming matrix for the minimum string distance
 * between the transcribed and presented string.
 */
public static int[][] msd_mat(FlaggableCharacterString presented,
    FlaggableCharacterString transcribed) {
int dp_mat[][] = new int[presented.size() + 1][transcribed.size() + 1];

// initialize dynamic programming
for (int i = 0; i < presented.size() + 1; i++) {
    dp_mat[i][0] = i;
}

for (int j = 0; j < transcribed.size() + 1; j++) {
    dp_mat[0][j] = j;
}

// main "recursion"
for (int i = 1; i < presented.size() + 1; i++) {
    for (int j = 1; j < transcribed.size() + 1; j++) {

	int dist = Util.characterDistance(presented.charAt(i - 1), transcribed.charAt(j - 1));

	dp_mat[i][j] = Util.min(dp_mat[i - 1][j] + 1,
				dp_mat[i][j - 1] + 1, 
				dp_mat[i - 1][j - 1] + dist);

    }
}

return dp_mat;
}

/**
 * Extract the alignments from the dynamic programming matrix.
 */
public static void align(
    FlaggableCharacterString presented,
    FlaggableCharacterString transcribed,
    int dp_mat[][],
    int len_pres,
    int len_trans,
    FlaggableCharacterString new_pres,
    FlaggableCharacterString new_trans,
    Vector<AlignmentTuple> alignments) {
	
if (len_pres == 0 && len_trans == 0) {
    alignments.add(new AlignmentTuple(new_pres, new_trans));
}

if (len_pres > 0 && len_trans > 0) {
    // Everything is okay
    if (dp_mat[len_pres][len_trans] == dp_mat[len_pres - 1][len_trans - 1]
	    && presented.charAt(len_pres - 1).equals(
		    transcribed.charAt(len_trans - 1))) {

	FlaggableCharacterString newPresented = new FlaggableCharacterString(new_pres);
	newPresented.insertElementAt(presented.charAt(len_pres - 1), 0);

	FlaggableCharacterString newTranscribed = new FlaggableCharacterString(new_trans);
	newTranscribed.insertElementAt(transcribed.charAt(len_trans - 1), 0);

	align(presented, transcribed, dp_mat, len_pres - 1,len_trans - 1, newPresented, newTranscribed, alignments);
    }

    // Deletion
    if (dp_mat[len_pres][len_trans] == dp_mat[len_pres - 1][len_trans - 1] + 1) {

	FlaggableCharacterString newPresented = new FlaggableCharacterString(
		new_pres);
	newPresented.insertElementAt(presented.charAt(len_pres - 1), 0);

	FlaggableCharacterString newTranscribed = new FlaggableCharacterString(new_trans);
	newTranscribed.insertElementAt(transcribed.charAt(len_trans - 1), 0);

	align(presented, transcribed, dp_mat, len_pres - 1, len_trans - 1, newPresented, newTranscribed, alignments);
    }

}

// Insertion
if (len_pres > 0
	&& dp_mat[len_pres][len_trans] == dp_mat[len_pres - 1][len_trans] + 1) {

    FlaggableCharacterString newPresented = new FlaggableCharacterString(new_pres);
    newPresented.insertElementAt(presented.charAt(len_pres - 1), 0);

    FlaggableCharacterString newTranscribed = new FlaggableCharacterString(new_trans);
    newTranscribed.insertElementAt(new FlaggableCharacter('-'), 0);

    align(presented, transcribed, dp_mat, len_pres - 1, len_trans, newPresented, newTranscribed, alignments);
}

// Omission
if (len_trans > 0
	&& dp_mat[len_pres][len_trans] == dp_mat[len_pres][len_trans - 1] + 1) {
    
    FlaggableCharacterString newPresented = new FlaggableCharacterString(new_pres);
    newPresented.insertElementAt(new FlaggableCharacter('-'), 0);


    FlaggableCharacterString newTranscribed = new FlaggableCharacterString(new_trans);
    newTranscribed.insertElementAt(transcribed.charAt(len_trans - 1), 0);

    
    align(presented, transcribed, dp_mat, len_pres, len_trans - 1, newPresented, newTranscribed, alignments);
}

}

/**
 * Aligns the input stream
 */
public static void stream_align(FlaggableCharacterString input_stream,
    Vector<AlignmentTuple> alignments,
    Vector<AlignmentTriplet> triplets) {


for (AlignmentTuple alignment : alignments) {
    // copy strings
    FlaggableCharacterString presented = alignment.getPresented();
    FlaggableCharacterString trans = alignment.getTranscript();

    FlaggableCharacterString new_presented = new FlaggableCharacterString(presented);
    FlaggableCharacterString new_transcribed = new FlaggableCharacterString(trans);
    FlaggableCharacterString new_input_stream = new FlaggableCharacterString(input_stream);
    
    // align action of stream for aligned presented and transcribed
    for (int i = 0; i < Math.max(new_transcribed.size(),
	       new_input_stream.size()); i++) {
	
	// i < new_transcribed.size() and i < new_input_stream.size() not in pseudo code
	if (i < new_transcribed.size() && new_transcribed.charAt(i).getSymbol() == '-') {
	    Util.insertInto(new_input_stream, i, new FlaggableCharacter('_'));
	} else if ( i < new_input_stream.size() && !new_input_stream.get(i).isFlagged()) {
	    Util.insertInto(new_presented, i, new FlaggableCharacter('_'));
	    Util.insertInto(new_transcribed, i, new FlaggableCharacter('_'));
	}
	
    }
    triplets.add(new AlignmentTriplet(new_presented, new_transcribed, new_input_stream));
}
}


 /**
 * Assign each character the position it refers tos
 */
public static void assign_position(Vector<AlignmentTriplet> triplets) {
for (AlignmentTriplet triplet : triplets) {
    FlaggableCharacterString stream = triplet.getInputstream();

    int pos = 0;
    for (int i = 0; i < stream.size(); i++) {
	FlaggableCharacter c = stream.charAt(i);
	if (c.isFlagged()) {
	    c.setPosition(0);
	    pos = 0;
	} else {
	    if (c.getSymbol() == '<' && pos > 0) {
		pos--;
	    }
	    c.setPosition(pos);
	    if (new IsLetterCondition().applies(c)) {
		pos++;
	    }
	}
    }
}
}

public static void determine(Vector<AlignmentTriplet> triplets) {
for(AlignmentTriplet triplet : triplets) {
    FlaggableCharacterString presented = triplet.getPresented();
    FlaggableCharacterString trans = triplet.getTranscript();
    FlaggableCharacterString input = triplet.getInputstream();
    int a = 0;
    
    for(int b = 0; b < input.size(); b++) {
	
	// - in trans means ommitting something from presented
	if(trans.get(b).getSymbol() == '-') {
	    
	    presented.get(b).setErr(new TypingError(ErrorTypes.UNC_OMISSION, '-', presented.get(b).getSymbol()));
	
	} else if(input.get(b).isFlagged() || b == input.size() - 1) {
	    HashSet<Integer> M = new HashSet<Integer>(); // corrected oMissions
	    HashSet<Integer> I = new HashSet<Integer>(); // corrected Insertion
	    
	    for(int i = a; i < b; i++) {
		
		int v = input.get(i).getPosition();
		
		// we corrected stuff using <
		if(input.get(i).getSymbol() == '<') {
		   
		    if(M.contains(v)) M.remove(v);
		    if(I.contains(v)) I.remove(v);
		
		} else if(input.get(i).getSymbol() != '_') {
		    int target = Util.lookAhead(presented, b, v + M.size() - I.size(), new IsLetterCondition());
		    
		    int next_p = Util.lookAhead(presented, target, 1, new IsLetterCondition());
		    int prev_p = Util.lookBack(presented, target, 1, new IsLetterCondition());
		    
		    int next_is = Util.lookAhead(input, i, 1, new IsNotCondition('_'));
		    int prev_is = Util.lookBack(input, i, 1, new IsNotCondition('_'));
		    			    			    
		    if(input.get(i).equals(presented.get(target))) {
			
			input.get(i).setErr(new TypingError(ErrorTypes.COR_NO_ERR, input.get(i).getSymbol(), input.get(i).getSymbol()));
			
		    } else if(target >= presented.size() - 1 || 
			    input.get(next_is).equals(presented.get(target)) ||
			    (input.get(prev_is).equals(input.get(i)) && 
				input.get(prev_is).equals(presented.get(prev_p))   
			    )) {
			
			
			
			input.get(i).setErr(new TypingError(ErrorTypes.COR_INSERTION, '-', input.get(i).getSymbol()));
			I.add(v);
			
		    } else if(input.get(i).equals(presented.get(next_p)) && new IsLetterCondition().applies(trans.get(target))) {
			
			presented.get(target).setErr(new TypingError(ErrorTypes.COR_OMISSION, '-', presented.get(target).getSymbol()));
			input.get(i).setErr(new TypingError(ErrorTypes.COR_NO_ERR, input.get(i).getSymbol(), input.get(i).getSymbol()));
			
			M.add(v);
		    } else {
			presented.get(target).setErr(new TypingError(ErrorTypes.COR_SUBS, input.get(i).getSymbol(), presented.get(target).getSymbol()));
		    }
		}
		
	    }

	    if(presented.get(b).getSymbol() == '-') {
		trans.get(b).setErr(new TypingError(ErrorTypes.UNC_INSERTION, '-', trans.get(b).getSymbol()));
	    } else if(!presented.get(b).equals(trans.get(b))) {
		trans.get(b).setErr(new TypingError(ErrorTypes.UNC_SUBS, presented.get(b).getSymbol(), trans.get(b).getSymbol()));
	    }else if(presented.get(b).getSymbol() != '_') {
		trans.get(b).setErr(new TypingError(ErrorTypes.UNC_NO_ERR, presented.get(b).getSymbol(), trans.get(b).getSymbol()));
	    }
	    
	    a = b + 1; 
	}
	
    }
}
}

public static ErrorCount strings(String presented, String transcribed,
    String input, int silent) {
// Transfer to data model
FlaggableCharacterString p = new FlaggableCharacterString(presented);
FlaggableCharacterString t = new FlaggableCharacterString(transcribed);
FlaggableCharacterString i = new FlaggableCharacterString(input);

// Setup data
Vector<AlignmentTuple> alignments = new Vector<AlignmentTuple>();
Vector<AlignmentTriplet> triplets = new Vector<AlignmentTriplet>();
ErrorCount counter = new ErrorCount();

// compute
flag(i);
int[][] dp = InputStreamAnalysis.msd_mat(p, t);
align(p, t, dp, p.size(), t.size(),
	new FlaggableCharacterString(), new FlaggableCharacterString(),
	alignments);


stream_align(i, alignments, triplets);

assign_position(triplets);
determine(triplets);

ErrorStream stream = new ErrorStream(Util.minNumLetters(triplets));
if (silent == 2 && silent >= 0) {
    System.out.println("#Alignments: " + alignments.size());
    System.out.println("--------------");
}
boolean length_diff = false;
for (AlignmentTriplet triplet : triplets) {
    FlaggableCharacterString _p = triplet.getPresented();
    FlaggableCharacterString _t = triplet.getTranscript();
    FlaggableCharacterString _i = triplet.getInputstream();
    IsLetterCondition is = new IsLetterCondition();

    if(Util.numLetters(triplet) > stream.size()) {
	length_diff = true;
	if (silent == 2 && silent >= 0) {
	    System.out.println("ALIGNMENT");
	    System.out.println("P " + _p);
	    System.out.println("I " + _i);
	    System.out.println("T " + _t);
	    System.out.println("--- IGNORED --- ");
	    System.out.println("--------------");
	}
	continue;
    }
    
    int num = 0;
    String align = "";
    for (int x = 0; x < _i.size(); x++) {
	if (is.applies(_p.get(x)) && _p.get(x).getErr() != null) {
	    align += _p.get(x).getErr() + "\n";
	    stream.set(num, _p.get(x).getErr());
	    num++;
	}
	if (is.applies(_t.get(x)) && _t.get(x).getErr() != null) {
	    align += _t.get(x).getErr() + "\n";
	    stream.set(num, _t.get(x).getErr());
	    num++;
	}
	if (is.applies(_i.get(x)) && _i.get(x).getErr() != null) {
	    align += _i.get(x).getErr() + "\n";
	    stream.set(num, _i.get(x).getErr());
	    num++;
	}
    }

    if (silent == 2 && silent >= 0) {
	System.out.println("ALIGNMENT");
	System.out.println("P " + _p);
	System.out.println("I " + _i);
	System.out.println("T " + _t);
	System.out.println("--------------");
	System.out.println("\nERRORS FOR ALLIGNMENT");
	System.out.println(align);
	System.out.println("--------------");
    }
    
}

for(WeightedError err : stream) {
    counter.inc(err.max().getType());
}

if(length_diff) {
    counter.inc(ErrorTypes.NUM_UNEQUAL_ALIGNMENTS);
}

if (silent >= 0) {
    System.out.println("FINAL RESULT BASED ON MAYORITY\n");
    if (silent == 0) {
	stream.setIgnore_noerr(true);
    }
    
    System.out.println(stream);
    System.out.println("--------------");
}

if(ClassificationStreamSingleton.getStream() != null) {
    counter.merge(OfByOneAnalysis.obo_determine(triplets, silent));
}
return counter;
}




}
