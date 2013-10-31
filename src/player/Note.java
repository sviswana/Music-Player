package player;

import sound.Pitch;
import sound.SequencePlayer;

/**
 * Represents the note, the basic element of music. 
 */

public class Note implements PlayType{
    
    private int octavesUp;

    private Accidental accidental;
    private BaseNote noteName;
    private Fraction noteLength;


    /**
     * Initializes a single note in the music file. 
     * @param noteName BaseNote, represents the note letter; requires noteName != null
     * @param octavesUp int, represents the octaves above/below middle C,
     * positive integers are octaves above middle C, 0 is middle C, negative integers are below middle C 
     * requires octavesUp != null
     * @param noteLength Fractions; requires noteLength != null.
     * @param accidental Accidental, integers representing none, sharps/flats and double sharps/flats;
     * requires accidental =! null.
     *  
     */

    public Note(BaseNote noteName, int octavesUp, Fraction noteLength, Accidental accidental) {
        this.noteName = noteName; 
        this.accidental = accidental;
        this.octavesUp = octavesUp; 
        this.noteLength = noteLength; 

    }

    /**
     * Returns note letter.
     *  
     */
    public BaseNote getBaseNote() {
        return this.noteName;
    }

    /**
     * Based on Pitch.java, each basenote is associated with a particular integer.
     * This method converts a given basenote into its corresponding integer.
     * @param noteName BaseNote; requires noteName != null.
     */
    public int convertBaseNoteInt(BaseNote noteName) {
        if (noteName == BaseNote.C){
            return 0;
        }
        else if (noteName == BaseNote.D){
            return 2;
        }
        else if (noteName == BaseNote.E){
            return 4;
        }
        else if (noteName == BaseNote.F){
            return 5;
        }
        else if (noteName == BaseNote.G){
            return 7;
        }
        else if (noteName == BaseNote.A){
            return 9;
        }
        else { // (noteName == BaseNote.B)
            return 11;
        }

    }
    /**
     * Based on Pitch.java, each accidental is associated with a particular integer.
     * This method converts a given accidental into its corresponding integer.
     * @param accidental Accidental; requires accidental != null.
     */
    public int convertAccidentalInt(Accidental accidental){
        if (accidental == Accidental.SHARP){
            return 1;
        }
        else if (accidental == Accidental.FLAT){
            return -1;
        }
        else if (accidental == Accidental.DOUBLESHARP){
            return 2;
        }
        else if (accidental == Accidental.DOUBLEFLAT){
            return -2;
        }
        else{
            return 0;
        }
    }

    /**
     * Returns the length of a particular note as a Fraction.
     * @return the notelength
     */
    public Fraction getLength() {
        return this.noteLength;
    }
    
    /**
     * Changes the notelength, by setting it to a different notelength.
     * @param length Fraction; length != null.
     */
    public void setLength(Fraction length) {
        this.noteLength = length;
    }
    
    /**
     * Constructs a new pitch for a note, given information about notename, accidental, and octaves.
     * @return newly constructed Pitch which can be passed to Sequence Player
     */
    public Pitch getPitch() {
        return new Pitch(convertBaseNoteInt(noteName), convertAccidentalInt(accidental), octavesUp);
    }


    /**
     * Returns the notename, the length, octaves, and accidental in a easy-to-read format.
     * @return output string.
     */
    @Override
    public String toString() {
        return "(" + noteName.toString() +", " +  noteLength.toString() + ", " + Integer.toString(octavesUp) + ", " + accidental.toString() + ") ";
    }

    /**
     * Checks whether two notes are equal to one another
     * @param otherObj The other Object we are comparing to
     * @return true, if the two expressions are structurally equal; false otherwise
     */
    @Override
    public boolean equals(Object other){
        Note n = (Note) other;
        if (n.toString().equals(other.toString())){
            return true;
        }
        return false;
    }
    
    /**
     * Returns the octaves above or below from middle C
     * @return the integer, octavesUp
     */
    public int getOctavesUp() {
        return octavesUp;
    }

    /**
     * Returns the number of ticks 
     * @return the integer, octavesUp
     */
    public int getTicks(int lcm) {
        int numerator = noteLength.getNumerator() * lcm;
        Fraction f = new Fraction(numerator, noteLength.getDenominator()).simplify();
        return f.getNumerator();

    }
    
    /**
     * Add the number of ticks of the note to the totalTicks and return the new total number of ticks
     * @return int, the new total number of ticks
     */
    @Override
    public int addToSequencePlayer(SequencePlayer s, int totalTicks, int lcm){
        int ticks = getTicks(lcm);
        s.addNote(getPitch().toMidiNote(), totalTicks, ticks);
        int newTicks = ticks + totalTicks;
        return newTicks;
    }


}
