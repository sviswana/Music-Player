package player;

/**
 * This class handles the title from the field T: in the header
 *
 */
public class Title implements HeaderField{
	private final String title;
	
	/**
	 * Initializes Index with the given index.
	 * @param title non-empty, non-null String
	 */
	public Title(String title){
		this.title = title;
	}
	
	/**
	 * The title of the music.
	 * @return String, representing the title
	 */
    public String getTitle() {
        return this.title;
    }
    
	/**
	 * Returns the Title in easy-to-read format.
     * @return String containing the title 
	 */
	@Override
	public String toString(){
		return "Title: " + title;
	}
}
