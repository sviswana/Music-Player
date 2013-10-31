package player;
/**
 * This class handles the composer from the field C: in the header
 *
 */
public class Composer implements HeaderField {
 
	private final String composer;
	
	/**
     * Initializes composer if given (default is "Unknown").
     * @param composer String; requires composer != null 
     */

	public Composer(String composer){
		this.composer = composer;
	}
	
	/**
     * Returns the composer. 
     * @return String, composer name
     */

	public String getComposer(){
		return this.composer;
	}
	
	/**
     * Prints the composer in easy-to-read format. 
     * @return String, representing composer
     */
	
	@Override
	public String toString() {
	    
	    return "Composer: " + this.composer + "\n";
	}

}
