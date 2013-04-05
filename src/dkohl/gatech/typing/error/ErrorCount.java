package dkohl.gatech.typing.error;

import java.util.HashMap;

import dkohl.gatech.typing.classification.ClassificationStreamSingleton;

/**
 * Counting error types
 * 
 * @author Daniel Kohlsdorf
 */
public class ErrorCount extends HashMap<ErrorTypes, Number>{

    private static final long serialVersionUID = 1L;
    
    public void inc(ErrorTypes type) {
	int count = 1;
	if(containsKey(type)) {
	    count += get(type);
	}
	put(type, count);
    }
    
    public void inc(ErrorTypes type, int by) {
	int count = by;
	if(containsKey(type)) {
	    count += get(type);
	}
	put(type, count);
    }
    
    public void incDouble(ErrorTypes type, double by) {
	double count = by;
	if(containsKey(type)) {
	    count += getDouble(type);
	}
	put(type, count);
    }

    
    public void merge(ErrorCount counter) {
	for(ErrorTypes type : counter.keySet()) {
	    inc(type, counter.get(type));
	}
    }
    
    @Override
    public Integer get(Object key) {
	if(super.get(key) == null) {
	    return 0;
	}

	Integer count = super.get(key).intValue();
	return count;
    }
    
    public Double getDouble(Object key) {
	if(super.get(key) == null) {
	    return 0.0;
	}

	Double count = super.get(key).doubleValue();
	return count;
    }

    
    @Override
    public String toString() {
	
	int err = get(ErrorTypes.COR_OMISSION) + get(ErrorTypes.COR_INSERTION) + get(ErrorTypes.COR_SUBS) + get(ErrorTypes.UNC_OMISSION) + get(ErrorTypes.UNC_INSERTION) + get(ErrorTypes.UNC_SUBS)
	+  get(ErrorTypes.NUM_UNEQUAL_ALIGNMENTS);

	String stat = "\nWOBBROCK error analysis:\n";
	stat += "CORRECTED_NO-ERROR [COR_NO_ERR]: " + get(ErrorTypes.COR_NO_ERR) + "\n";
	stat += "CORRECTED_OMISSION [COR_INSERTION]: " + get(ErrorTypes.COR_OMISSION) + "\n";
	stat += "CORRECTED_INSERTION [COR_SUBS]: " + get(ErrorTypes.COR_INSERTION) + "\n";
	stat += "CORRECTED_SUBSTITUTION [UNC_OMISSION]: " + get(ErrorTypes.COR_SUBS) + "\n";
	stat += "UNCORRECTED_OMISSION [UNC_OMISSION]: " + get(ErrorTypes.UNC_OMISSION) + "\n";
	stat += "UNCORRECTED_INSERTION [UNC_INSERTION]: " + get(ErrorTypes.UNC_INSERTION) + "\n";
	stat +=	"UNCORRECTED_SUBSTITUTION [UNC_SUBS]: " + get(ErrorTypes.UNC_SUBS) + "\n";
	stat += "UNCORRECTED_NO-ERROR [UNC_NO_ERR]: " + get(ErrorTypes.UNC_NO_ERR) + "\n";
	
	if(ClassificationStreamSingleton.getStream() != null) {

	    stat += "\nFatThumbs ground truth (Off by one error analysis):\n";
	    stat += "OFF_BY_ONE_ROLLON [OBO_ROLLON]: " + get(ErrorTypes.OBO_ROLLON)
		    + "\n";
	    stat += "OFF_BY_ONE_ROLLOFF [OBO_ROLLOFF]: " + get(ErrorTypes.OBO_ROLLOFF)
		    + "\n";
	    stat += "OFF_BY_ONE_MULTIPLE [OBO_MULTIPLE]: "
		    + get(ErrorTypes.OBO_MULTIPLE) + "\n";
	    stat += "OFF_BY_ONE_NO_ERROR [OBO_NOERR]: " + get(ErrorTypes.OBO_NOERR)
	    + "\n";

	    stat += "\nFatThumbs triggers:\n";
	    stat += "FATTHUMBS_ROLLON [FT_ROLLON]: " + get(ErrorTypes.FT_ROLLON)
		    + "\n";
	    stat += "FATTHUMBS_ROLLON_CONTEXT [FT_ROLLON_CONTEXT]: " + get(ErrorTypes.FT_ROLLON_CONTEXT)
	    + "\n";
	    stat += "FATTHUMBS_ROLLOFF [FT_ROLLOFF]: " + get(ErrorTypes.FT_ROLLOFF)
		    + "\n";
	    stat += "FATTHUMBS_EQUAL_DOWNTIME_ERROR [FT_EQUALDT]: " + get(ErrorTypes.FT_EQUALDT)
		    + "\n";

	    stat += "\nCorrect Detections (FatThumbs ground truth and FatThumbs triggers correctly align):\n";
	    stat += "CORRECTLY_DETECTED_FATTHUMBS_ROLLON [DET_ROLLON]: " + get(ErrorTypes.DET_ROLLON)
	    + "\n";
	    stat += "CORRECTLY_DETECTED_FATTHUMBS_ROLLOFF [DET_ROLLOFF]: " + get(ErrorTypes.DET_ROLLOFF)
		    + "\n";
	    stat += "CORRECTLY_DETECTED_FATTHUMBS_EQUAL_DOWNTIME_ERROR [DET_EQUALDT]: " + get(ErrorTypes.DET_EQUALDT)
	    + "\n";
	    stat += "CORRECTLY_DETECTED_FATTHUMBS_NO_ERROR [DET_NOERR]: " + get(ErrorTypes.DET_NOERR)
	    + "\n";
	    
	    stat += "\nIncorrect Detections (FatThumbs ground truth is an OFF_BY_ONE_NO_ERROR but FatThumbs triggers are errors):\n";
	    stat += "INCORRECTLY_DETECTED_FATTHUMBS_ROLLON [WRONG_DET_ROLLON]: "
		    + get(ErrorTypes.WRONG_DET_ROLLON) + "\n";
	    stat += "INCORRECTLY_DETECTED_FATTHUMBS_ROLLOFF [WRONG_DET_ROLLOFF]: "
		    + get(ErrorTypes.WRONG_DET_ROLLOFF) + "\n";
	    stat += "INCORRECTLY_DETECTED_FATTHUMBS_EQUAL_DOWNTIME_ERROR [WRONG_DET_EQUALDT]: "
	    	    + get(ErrorTypes.WRONG_DET_EQUALDT) + "\n";
	    
	    stat += "\nMisclassifications (FatThumbs ground truth is an OFF_BY_ONE_ROLLOFF but FatThumbs triggers a FATTHUMBS_ROLLON or vice verse):\n";	    
	    stat += "ROLLOFF_AS_ROLLON [ROLLOFF_AS_ROLLON]: "  + (get(ErrorTypes.ROLLOFF_AS_ROLLON)) + "\n";
	    stat += "ROLLON_AS_ROLLOFF [ROLLON_AS_ROLLOFF]: "  + (get(ErrorTypes.ROLLON_AS_ROLLOFF)) + "\n\n";

	    
	    stat += "\nMissed Detections (FatThumbs ground truth is an off-by-one error but FatThumbs triggers are FATTHUMBS_NO_ERROR):\n";	    
	    stat += "UNDETECTED_ FATTHUMBS_ROLLON [UNDET_ROLLON]: "
	    + get(ErrorTypes.UNDET_ROLLON) + "\n";
	    stat += "UNDETECTED_ FATTHUMBS_ROLLOFF [UNDET_ROLLOFF]: "
	    + get(ErrorTypes.UNDET_ROLLOFF) + "\n";
	    stat += "UNDETECTED_FATTHUMBS_MULTIPLE [UNDET_MULTIPLE]: "
		    + get(ErrorTypes.UNDET_MULTIPLE) + "\n";
	    stat += "UNDETECTED_FIRST_FOUR [UNDET_FIRST_FOUR]: "
	    + get(ErrorTypes.UNDET_FIRST_FOUR) + "\n\n";
	    
	    if(get(ErrorTypes.NUM_FILES) == 0) {
		inc(ErrorTypes.NUM_FILES);
	    }
	    
	    int checksum = (get(ErrorTypes.OBO_ROLLON) + get(ErrorTypes.OBO_ROLLOFF) + get(ErrorTypes.OBO_MULTIPLE)) - (get(ErrorTypes.DET_ROLLOFF) + get(ErrorTypes.DET_ROLLON) + get(ErrorTypes.DET_EQUALDT)) - (get(ErrorTypes.UNDET_MULTIPLE) + get(ErrorTypes.UNDET_ROLLON)+ get(ErrorTypes.UNDET_ROLLOFF));
	    stat += "\n\nTotal Phrases: "  + get(ErrorTypes.NUM_FILES) + "\n";
	    stat += "Total Characters: " + get(ErrorTypes.TOTAL_STROKES) + "\n";
	    stat += "Total Errors: " + err + "\n";
	    stat += "Total Off-By-One Errors: " + (get(ErrorTypes.OBO_ROLLON) + get(ErrorTypes.OBO_ROLLOFF) + get(ErrorTypes.OBO_MULTIPLE)) + "\n";
	    stat += "Total FatThumbs Triggers: "  + (get(ErrorTypes.FT_ROLLOFF) + get(ErrorTypes.FT_ROLLON) + get(ErrorTypes.FT_EQUALDT)) + "\n";
	    stat += "Total Correct Detections: "  + (get(ErrorTypes.DET_ROLLOFF) + get(ErrorTypes.DET_ROLLON) + get(ErrorTypes.DET_EQUALDT)) + "\n";
	    stat += "Total Incorrect Detections and Misclassifications: "  + (get(ErrorTypes.WRONG_DET_ROLLOFF) + get(ErrorTypes.WRONG_DET_ROLLON) + get(ErrorTypes.WRONG_DET_EQUALDT)) +"\n";
	    stat += "Total Missed Detections: "  + (get(ErrorTypes.UNDET_MULTIPLE) + get(ErrorTypes.UNDET_ROLLON)+ get(ErrorTypes.UNDET_ROLLOFF)) + "\n\n";
	    stat += "Avg Word / min: " + (getDouble(ErrorTypes.AVG_WPM) / ((double) get(ErrorTypes.NUM_FILES))) + "\n";
	    stat += "Avg acc: " + (getDouble(ErrorTypes.AVG_ACC) / ((double) get(ErrorTypes.NUM_FILES))) + "\n";

	    stat += "Checksum (OBO - Correct - Miss): " + checksum;
	}
	return stat;
    }
    
}
