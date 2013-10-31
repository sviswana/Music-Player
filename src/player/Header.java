package player;

public class Header implements HeaderField{

    private final String title;
    private final int index;
    private int tempo;
    private Fraction temponote;
    private Fraction meter;
    private Fraction noteLength;
    private String composer;
    
    /**
     * Creates an instance of a Header based on the header fields in a piece. 
     * This class allows information from the header fields to be extracted and used
     * by the SequencePlayer
     * 
     * @param index
     * @param title
     * @param composer
     * @param noteLength
     * @param tempo
     * @param temponote
     * @param meter
     */
    public Header(int index, String title, String composer, Fraction noteLength, int tempo,  Fraction temponote, Fraction meter){
    	this.index = index;
    	this.title = title;
    	this.composer = composer;
    	this.noteLength = noteLength;
    	this.tempo = tempo;
    	this.temponote = temponote;
    	this.meter = meter;
    }
    
    /**
     * Returns the tempo of the piece
     * @return int tempo
     */
    public int getTempo(){
    	return this.tempo;
    }
    
    /**
     * Returns the note length that is associated with the tempo value
     * @return Fraction temponote
     */
    public Fraction getTempoNote(){
    	return this.temponote;
    }
    
    /**
     * Returns the default note length
     * @return Fraction noteLength
     */
    public Fraction getDefaultNote(){
    	return this.noteLength;
    }
    
    /**
     * Returns the header in an easy to read format 
     * @return String header
     */
    @Override
    public String toString() {
        String header = "Index Number: " + this.index + "\n"
                + "Title: " + this.title + "\n" 
                + "Meter: " + this.meter + "\n"
                +  "NoteLength: " + this.noteLength + "\n"
                + "Tempo: " + this.temponote + "=" + this.tempo + "\n"
                + "Composer: " + this.composer + "\n";
 
        return header;
    }
}
