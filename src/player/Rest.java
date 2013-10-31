package player;

import sound.SequencePlayer;
/**
 * This class handles Rests in the music. 
 */

public class Rest implements PlayType{
    
    private Fraction length;
    
    /**
     * Initializes the rest with the specified length.
     * @param length Fraction; requires length != null 
     */

    public Rest(Fraction length){
    	this.length = length;
    }
    
    /**
     * The length of the rest.
     * @return Fraction, rest length
     */
    
    public Fraction getLength() {
        return length;
    }
    
    
    /**
     * Returns the rest in easy-to-read format.
     * @return output as a string 
     */
    @Override 
    public String toString() {
        String rest = "Rest" + length;
        return rest;
    }
    
    /**
     * Calculates the number of ticks for the rest based on the lcm and the fraction of the rest
     * @param lcm int
     * @return int, the number of ticks for a rest
     */
	public int getTicks(int lcm) {
		int numerator = length.getNumerator() * lcm;
		Fraction f = new Fraction(numerator, length.getDenominator()).simplify();
		return f.getNumerator();
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
		int newTicks = ticks + totalTicks;
		return newTicks;
	}

}
