package player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.LyricListener;
import sound.SequencePlayer;

/**
 * Main entry point of your application.
 **/
public class Main {

	public static void main(String[] args) throws MidiUnavailableException,
			InvalidMidiDataException, IOException {
		play("sample_abc/little_night_music.abc");

	}

	/**
	 * Plays the input file using Java MIDI API and displays header information
	 * to the standard output stream.
	 * 
	 * (Your code should not exit the application abnormally using
	 * System.exit().)
	 * 
	 * @param file the name of input abc file
	 * @throws InvalidMidiDataException
	 * @throws MidiUnavailableException
	 * @throws IOException
	 */
	public static void play(String file) throws MidiUnavailableException,
			InvalidMidiDataException, IOException {
		
		/* Transforms the contents of an input file into a String that will be processed
		 * by ListenerTree
		 */
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
			String music = sb.toString();
			ListenerTree l = new ListenerTree(music);

			List<Object> elements = l.getElements();
			boolean voices = l.getVoices();
			int lcm = l.getLCM();
			int tempo = l.getTempo();

			// When a lyric event is trigged, the lyric text will be printed to the console
			LyricListener listener = new LyricListener() {
				public void processLyricEvent(String text) {
					System.out.println(text);
				}
			};

			SequencePlayer player = new SequencePlayer(tempo, lcm, listener);
			int totalTicks = 0;
			for (Object element : elements) {

				// if a piece of music contains multiple voices, reset the totalTicks for each voice
				if (voices) {
					totalTicks = 0;
					for (Object voice : (List) element) {
						// iterate through each measure and add playable elements to the player
						for (Object playtype : (List) voice) {
							/* addToSequencePlayer will add each element to player and calculate the
							   new total number of ticks */
							totalTicks = ((PlayType) playtype).addToSequencePlayer(player, totalTicks, lcm);
						}
					}
				}

				else {
					// iterate through each measure and add playable elements to the sequence player
					for (Object playtype : (List) element) {
						totalTicks = ((PlayType) playtype).addToSequencePlayer(player, totalTicks, lcm);
					}
				}

			}
			
			// play the notes that were added to player
			player.play();
			
		} finally {
			br.close();
		}
	}

}
