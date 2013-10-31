package player;

public class Tempo implements HeaderField{
	private final Fraction noteLength;
	private int tempo;
	
	/**
	 * Initializes Tempo with the specified noteLength and tempo
	 * @param noteLength: Fraction, requires length != null
	 * @param tempo: int, representing the tempo
	 */
	public Tempo(Fraction noteLength, int tempo){
		this.noteLength = noteLength;
		this.tempo = tempo;
	}
	
	/**
	 * Returns the noteLength of the Tempo
	 * @return Fraction, representing noteLength
	 */
	public Fraction getNoteLength(){
		return noteLength;
	}
	
	/**
	 * Returns the tempo of the Tempo
	 * @return int, representing tempo
	 */
	public int getTempo(){
		return tempo;
	}
	
	/**
	 * Returns a string representation of tempo (only outputs tempo)
	 * @return String, representing the tempo
	 */
	public String toString() {
	    return "Tempo: " + Integer.toString(this.tempo);
	}
}
