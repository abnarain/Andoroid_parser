package dkohl.gatech.fat_thumbs.correction;

import java.util.Hashtable;

/**
 * Used to find adjacent characters
 * 
 * @author Daniel Kohlsdorf
 */
public class KeyMap {

	private static final String QWERTY[] = {
		"qwertyuiop", 
		"asdfghjklj",
		"zxcvbnm" };

	private static Hashtable<Character, KeyBoardPosition> qwertyposSingleton;

	private static void initPositionSingleton() {
		if (qwertyposSingleton == null) {
			qwertyposSingleton = new Hashtable<Character, KeyBoardPosition>();
			for (int i = 0; i < QWERTY.length; i++) {
				for (int j = 0; j < QWERTY[i].length(); j++) {
					Character letter = new Character(QWERTY[i].charAt(j));
					qwertyposSingleton.put(letter, new KeyBoardPosition(i, j));
				}
			}
		}
	}

	// TODO Unit Test
	public static boolean adjacent(char x, char y) {
		initPositionSingleton();
		KeyBoardPosition pos = (KeyBoardPosition) qwertyposSingleton.get(new Character(Character.toLowerCase(x)));
		if(pos == null) {
			return false;
		}
		int col = pos.getCol();
		int row = pos.getRow();
		String keyRow = QWERTY[row];
		
		if(col == 0) {
			if(keyRow.charAt(col + 1) == Character.toLowerCase(y)) {
				return true;
			}
		} else {
			if(keyRow.charAt(col - 1) == Character.toLowerCase(y)) {
				return true;
			}
			
			// Java stops after first expression evaluates to false
			// Prevents from overrun of keyRow
			if(col + 1 < keyRow.length()&& keyRow.charAt(col + 1) == y) {
				return true;
			}
		}
		
		return false;
	}
	
}
