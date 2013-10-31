package player;

import sound.SequencePlayer;

/*
 * The PlayType interface is used to handle the four main playable elements:
 * Note, Chord, Rest, and Lyric, which all implement it
 */

public interface PlayType {
	
	/**
	 * Adds an event to be scheduled in a SequencePlayer
	 * 
	 * @param s - a SequencePlayer object that will play notes and lyrics that are passed to it
	 * @param totalTicks - the current number of ticks in the SequencePlayer
	 * @param lcm - the least common multiple of the denominators of each playable element. 
	 * 				lcm is used to calculate tick counts
	 * @return the new total number of ticks after processing the playable element
	 **/

	public int addToSequencePlayer(SequencePlayer s, int totalTicks, int lcm);
	
	/**
	 * Creates a String representation of the PlayType in an readable format
	 * 
	 * @return a String representation of the PlayType
	 **/
	public String toString();
}
