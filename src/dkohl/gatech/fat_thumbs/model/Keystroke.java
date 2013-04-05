package dkohl.gatech.fat_thumbs.model;

/**
 * A keystroke represented by the key pressed and the up and down times
 * 
 * @author Daniel Kohlsdorf
 */
public class Keystroke {

	/**
	 * time the character was pressed
	 */
	private long uptime;

	/**
	 * time the character was released
	 */
	private long downtime;

	/**
	 * the key pressed
	 */
	private char character;

	public Keystroke(char character) {
		this.character = character;
		uptime = 0;
		downtime = 0;
	}

	public long getUptime() {
		return uptime;
	}

	public void setUptime(long uptime) {
		this.uptime = uptime;
	}

	public long getDowntime() {
		return downtime;
	}

	public void setDowntime(long downtime) {
		this.downtime = downtime;
	}

	public char getCharacter() {
		return character;
	}

}
