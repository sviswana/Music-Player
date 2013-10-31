package player;

import java.util.List;

import sound.SequencePlayer;

/**
 * This class handles Chords in the music.
 *
 */
public class Chord implements PlayType{


    private List<Note> notes;

    /**
     * Initializes chord, as a list of notes.
     * @param notes List<Note>; requires notes != null 
     */

    public Chord(List<Note> notes){
        this.notes = notes;
    }

    /**
     * Returns the list of notes in chord.  
     * @return notes
     */

    public List<Note> getNotes(){
        return this.notes;
    }

    /**
     * Returns the length of chord as a Fraction. 
     * @return Fraction length  
     */

    public Fraction getChordLength(){
        return this.notes.get(0).getLength();
    }

    /**
     * Returns the number of ticks that the chord should be played for.
     * @param lcm int; the lcm we have computed
     * @return number of ticks should be played
     */
    
    public int getTicks(int lcm){
        Note note1 = notes.get(0);
        return note1.getTicks(lcm);
    }

    /**
     * Returns chord in easy-to-read format
     * @return output as a string 
     */

    @Override
    public String toString(){
        String output = "";
        for (int i = 0; i < notes.size(); i++ ){
            output += notes.get(i).toString();
        }
        output += getChordLength().toString();
        return output;
    }
    
    /**
     * Adds an event to be schedules in a Sequence Player
     * @param s SequencePlayer - the object passed into the method
     * @param totalTicks int - the current number of ticks in Sequence Player
     * @param lcm int - least common multiple of the denominators in each playable element. 
     * @return the new total number of ticks after processing the playable element. 
     */
	@Override
	public int addToSequencePlayer(SequencePlayer s, int totalTicks, int lcm){
		int ticks = getTicks(lcm);
		
		for (Note n : notes){
			s.addNote(n.getPitch().toMidiNote(), totalTicks, ticks);
		}
		
		int newTicks = ticks + totalTicks;
		return newTicks;
	}

}