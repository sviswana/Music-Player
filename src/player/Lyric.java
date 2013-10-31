package player;

import sound.SequencePlayer;

public class Lyric implements PlayType{
	private String lyrics;
	
	/**
	 * Creates a Lyric object that contains the text of a lyric element
	 * 
	 * @param lyrics - the text of a lyric element
	 */
	public Lyric(String lyrics){
		this.lyrics = lyrics;
	}
	
    /**
     * Returns the text of a lyric element  
     * @return String lyrics
     */
	public String getText(){
		return this.lyrics;
	}

    /**
     * Adds a lyric event to the SequencePlayer
     * @return int, the new total number of ticks
     */
	@Override
	public int addToSequencePlayer(SequencePlayer s, int totalTicks, int lcm){
		s.addLyricEvent(lyrics, totalTicks);
		return totalTicks;
	}

}
