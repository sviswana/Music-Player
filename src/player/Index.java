package player;
/**
 * This class handles the index from the field X: in the header
 *
 */
public class Index implements HeaderField{
	
	private final int index;
	
	/**
	 * Initializes Index with the given index.
	 * @param index integer
	 */
	public Index(int index){
		this.index = index;
	}
	
	/**
	 * The index in the header.
	 * @return int, representing the Index
	 */
	public int getIndex(){
		return index;
	}
	
	 /**
     * Returns the Index in easy-to-read format.
     * @return output as a string 
     */
	public String toString() {
	    return "Index: " + Integer.toString(this.index);
	}

}
