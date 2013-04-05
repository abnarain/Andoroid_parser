package dkohl.gatech.fat_thumbs.correction;

import dkohl.gatech.fat_thumbs.model.KeystrokeContext;
import dkohl.gatech.typing.error.ErrorTypes;

public class TypingCorrection {

    public static int ROLLOFF_TH = 170;
    public static int ROLLON_TH = 125;
    public static long INTENTIONAL_TYPING_THRESHOLD = 300;
    public static int DROPPROB_TH = -66666666;

    public static ErrorTypes detectEqualDownTimeException(KeystrokeContext context) {
	// That is like a weak rollon: fut_dt == 0

	boolean equal = context.getCurrent().getDowntime() == context
		.getStroke(context.size() - 1).getDowntime();
	equal = equal && FeatureExtraction.futcuradj(context);
	  

	if (equal) {
	    if (FeatureExtraction.dropprobdiff(context) < DROPPROB_TH) {
		return ErrorTypes.FT_EQUALDT_ROLLON;
	    }

	    return ErrorTypes.FT_EQUALDT_ROLLOFF;
	}
	return ErrorTypes.FT_NOERR;
    }

    public static boolean detectRolloff(KeystrokeContext context) {
	int c = context.getNumPrevious();
	int p = context.getNumPrevious() - 1;

	// check for negative values and allow current up missing
	// KeyUp: Cur, after assignment cur.up is assigned

	if (FeatureExtraction.dt_ud(context, c, p) < ROLLOFF_TH) {
	    if (FeatureExtraction.prevcuradj(context)) {
		return true;
	    }
	}

	return false;
    }

    public static boolean detectRollon(KeystrokeContext context) {

	// KeyDown: Future
	if (FeatureExtraction.futcuradj(context)) {
	    if (FeatureExtraction.fut_dt(context) < ROLLON_TH) {
		if (FeatureExtraction.dropprobdiff(context) < DROPPROB_TH) {
		    return true;
		}
	    }
	    
	    // Lowest observed probability in integer
	    // representation is one.
	    if (FeatureExtraction.prob(context) <= 1
		    && FeatureExtraction.fut_dt(context) < INTENTIONAL_TYPING_THRESHOLD) {
		return true;
	    }
	}

	return false;
    }
}
