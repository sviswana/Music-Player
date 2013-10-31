package player;

/**
 * This class handles the default notelength, L: in the header
 *
 */
public class Length implements HeaderField {

	private final Fraction length;
	
	/**
	 * Initializes Length with the specified length.
	 * @param length Fraction; requires length != null
	 */
	public Length(Fraction length) {
		this.length = length;
	}
	
	/**
	 * The value of Length given in the header.
	 * @return Fraction, representing length
	 */
	public Fraction getLength(){
		return length;
	}
	
	/**
	 * Returns the Length in easy-to-read format.
     * @return String, representing the noteLength 
	 */
	@Override
	public String toString() {
	    return "NoteLength: " + this.length.toString();
	}
}
