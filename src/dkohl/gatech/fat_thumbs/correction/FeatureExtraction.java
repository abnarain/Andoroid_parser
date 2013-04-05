package dkohl.gatech.fat_thumbs.correction;

import dkohl.gatech.fat_thumbs.model.KeystrokeContext;
import dkohl.gatech.fat_thumbs.ngram.NgramProb;

/**
 * Feature extraction for Keystroke Context
 * 
 * @author Daniel Kohlsdorf
 */
public class FeatureExtraction {
	
	public static final int[] TRIGRAM_MASK_CUR = {0, 1, 2};
	public static final int[] TRIGRAM_MASK_FUT = {0, 1, 3};
	
	public static long dt_ud(KeystrokeContext context, int off1, int off2) {
		return context.getStroke(off1).getUptime() - context.getStroke(off2).getDowntime();
	}
	
	public static long fut_dt(KeystrokeContext context) {
		return context.getStroke(3).getDowntime() - context.getStroke(2).getDowntime();
	}
	
	public static boolean samesasprev(KeystrokeContext context) {
		int prevPos = context.getNumPrevious() - 1;
		return context.getCurrent().getCharacter() == context.getStroke(prevPos).getCharacter();
	}
	
	public static boolean prevcuradj(KeystrokeContext context) {
		int prevPos = context.getNumPrevious() - 1;
		char prev = context.getStroke(prevPos).getCharacter();
		char cur = context.getCurrent().getCharacter();
		return KeyMap.adjacent(prev, cur);
	}
	
	public static boolean futcuradj(KeystrokeContext context) {
		int futPos = context.size() - 1;
		char fut = context.getStroke(futPos).getCharacter();
		char cur = context.getCurrent().getCharacter();
		return KeyMap.adjacent(fut, cur);
	}
	
	public static int prob(KeystrokeContext context) {
		String ngram = context.ngram(TRIGRAM_MASK_CUR);
		return NgramProb.prob(ngram);
	}
	
	public static int dropprobdiff(KeystrokeContext context) {
		String ngram_cur = context.ngram(TRIGRAM_MASK_CUR);
		String ngram_fut = context.ngram(TRIGRAM_MASK_FUT);
		int p_cur = NgramProb.prob(ngram_cur);
		int p_fut = NgramProb.prob(ngram_fut);

		return p_cur - p_fut;
	}
	
	public static String toString(KeystrokeContext context) {
		String print = "Attrs: ";
		
		print += "FCA:" + (futcuradj(context)?"T":"F") + " ";
		print += "PCA:" + (prevcuradj(context)?"T":"F") + " ";
		print += "SAP:" + (samesasprev(context)?"T":"F") + " ";
		print += "DPD:" + dropprobdiff(context) + " \t";
		print += "PRB:" + prob(context) + " \t";
		print += "DTFut:" + fut_dt(context) + " \t";
		print += "DTUDcp:" + dt_ud(context, context.getNumPrevious(), context.getNumPrevious() - 1) + " \t";
		return print;
	}
}
