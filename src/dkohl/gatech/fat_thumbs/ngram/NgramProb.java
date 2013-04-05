package dkohl.gatech.fat_thumbs.ngram;

public class NgramProb {

	public static int index(char x) {
		if (x == ' ') {
			return 0;
		}
		int index = (int) x - 96;
		return index;
	}

	public static int map_table(int index) {
		return (index) / 7;
	}

	public static int map_index(int table, int index) {
		return index - ((table) * 7);
	}

	public static int prob(String trigram) {
		int index_one = index(trigram.charAt(0));
		int index_two = index(trigram.charAt(1));
		int index_three = index(trigram.charAt(2));

		int table = map_table(index_one);
		int table_index = map_index(map_table(index_one), index_one);

		switch (table) {
		case 0:
			return NgramOne.table[table_index][index_two][index_three];
		case 1:
			return NgramTwo.table[table_index][index_two][index_three];
		case 2:
			return NgramThree.table[table_index][index_two][index_three];
		case 3:
			return NgramFour.table[table_index][index_two][index_three];
		}

		return 0;
	}

}
