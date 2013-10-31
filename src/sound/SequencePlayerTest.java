package sound;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

import player.Main;

public class SequencePlayerTest {

    @Test
    
    public void Testpiece1() {
        SequencePlayer player;
        try {

            
            LyricListener listener = new LyricListener() {
                public void processLyricEvent(String text) {
                    System.out.println(text);
                }
            };
            
            player = new SequencePlayer(140, 12, listener);

            player.addNote(new Pitch('C').toMidiNote(), 0, 12);
            player.addNote(new Pitch('C').toMidiNote(), 12, 12);
            player.addNote(new Pitch('C').toMidiNote(), 24, 9);
            player.addNote(new Pitch('D').toMidiNote(), 33, 3);
            player.addNote(new Pitch('E').toMidiNote(), 36, 12);

            player.addNote(new Pitch('E').toMidiNote(), 48, 9);
            player.addNote(new Pitch('D').toMidiNote(), 57, 3);
            player.addNote(new Pitch('E').toMidiNote(), 60, 9);
            player.addNote(new Pitch('F').toMidiNote(), 69, 3);
            player.addNote(new Pitch('G').toMidiNote(), 72, 24);

            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 96, 4);
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 100, 4);
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 104, 4);
            player.addNote(new Pitch('G').toMidiNote(), 108, 4);
            player.addNote(new Pitch('G').toMidiNote(), 112, 4);
            player.addNote(new Pitch('G').toMidiNote(), 116, 4);
            player.addNote(new Pitch('E').toMidiNote(), 120, 4);
            player.addNote(new Pitch('E').toMidiNote(), 124, 4);
            player.addNote(new Pitch('E').toMidiNote(), 128, 4);
            player.addNote(new Pitch('C').toMidiNote(), 132, 4);
            player.addNote(new Pitch('C').toMidiNote(), 136, 4);           
            player.addNote(new Pitch('C').toMidiNote(), 140, 4);

            player.addNote(new Pitch('G').toMidiNote(), 144, 9);
            player.addNote(new Pitch('F').toMidiNote(), 153, 3);
            player.addNote(new Pitch('E').toMidiNote(), 156, 9);
            player.addNote(new Pitch('D').toMidiNote(), 165, 3);
            player.addNote(new Pitch('C').toMidiNote(), 168, 24);



            System.out.println(player);

            // play!
            player.play();

            /*
             * Note: A possible weird behavior of the Java sequencer: Even if the
             * sequencer has finished playing all of the scheduled notes and is
             * manually closed, the program may not terminate. This is likely
             * due to daemon threads that are spawned when the sequencer is
             * opened but keep on running even after the sequencer is killed. In
             * this case, you need to explicitly exit the program with
             * System.exit(0).
             */
            // System.exit(0);

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Testpiece2() {
        SequencePlayer player;
        try {

            LyricListener listener = new LyricListener() {
                public void processLyricEvent(String text) {
                    System.out.println(text);
                }
            };
            player = new SequencePlayer(200, 12, listener);

            player.addNote(new Pitch('F').transpose(1).toMidiNote(), 0, 6);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), 0, 6);
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), 6, 6);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), 6, 6);
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), 18, 6);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), 18, 6);
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), 30, 6);
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 30, 6);
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), 36, 12);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), 36, 12);

            player.addNote(new Pitch('G').toMidiNote(), 48, 12);
            player.addNote(new Pitch('B').toMidiNote(), 48, 12);
            player.addNote(new Pitch('G').transpose(Pitch.OCTAVE).toMidiNote(), 48, 12);
            player.addNote(new Pitch('G').toMidiNote(), 72, 12);

           
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 96, 18);
            player.addNote(new Pitch('G').toMidiNote(), 114, 6);
            player.addNote(new Pitch('E').toMidiNote(), 132, 12);

            player.addNote(new Pitch('E').toMidiNote(), 148, 6);
            player.addNote(new Pitch('A').toMidiNote(), 154, 12);
            player.addNote(new Pitch('B').toMidiNote(), 166, 12);
            player.addNote(new Pitch('B').transpose(-1).toMidiNote(), 178, 6);
            player.addNote(new Pitch('A').toMidiNote(), 184, 12);

            player.addNote(new Pitch('G').toMidiNote(), 196, 8);
            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), 204, 8);
            player.addNote(new Pitch('G').transpose(Pitch.OCTAVE).toMidiNote(), 212, 8);
            player.addNote(new Pitch('A').transpose(Pitch.OCTAVE).toMidiNote(), 220, 12);
            player.addNote(new Pitch('F').transpose(Pitch.OCTAVE).toMidiNote(), 232, 6);
            player.addNote(new Pitch('G').transpose(Pitch.OCTAVE).toMidiNote(), 238, 6);

            player.addNote(new Pitch('E').transpose(Pitch.OCTAVE).toMidiNote(), 250, 12);
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 262, 6);
            player.addNote(new Pitch('D').transpose(Pitch.OCTAVE).toMidiNote(), 268, 6);
            player.addNote(new Pitch('B').toMidiNote(), 274, 9);

            System.out.println(player);

            // play!
            player.play();

            /*
             * Note: A possible weird behavior of the Java sequencer: Even if the
             * sequencer has finished playing all of the scheduled notes and is
             * manually closed, the program may not terminate. This is likely
             * due to daemon threads that are spawned when the sequencer is
             * opened but keep on running even after the sequencer is killed. In
             * this case, you need to explicitly exit the program with
             * System.exit(0).
             */
            // System.exit(0);

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void Testpiece3() {
        SequencePlayer player;
        try {

           
            LyricListener listener = new LyricListener() {
                public void processLyricEvent(String text) {
                    System.out.println(text);
                }
            };
            
            player = new SequencePlayer(100, 6, listener);

            player.addNote(new Pitch('D').toMidiNote(), 4, 2);
            player.addLyricEvent("A",4);
            player.addNote(new Pitch('G').toMidiNote(), 6, 4);
            player.addLyricEvent("ma", 6);
            player.addNote(new Pitch('B').toMidiNote(), 10, 1);
            player.addLyricEvent("zing", 10);
            player.addNote(new Pitch('G').toMidiNote(), 11, 1);

            player.addNote(new Pitch('B').toMidiNote(), 12, 4);
            player.addLyricEvent("grace!", 12);
            player.addNote(new Pitch('A').toMidiNote(), 16, 2);
            player.addLyricEvent("How", 16);

            player.addNote(new Pitch('G').toMidiNote(), 18, 4);
            player.addLyricEvent("sweet", 18);
            player.addNote(new Pitch('E').toMidiNote(), 22, 2);
            player.addLyricEvent("the", 22);

            player.addNote(new Pitch('D').toMidiNote(), 24, 4);
            player.addLyricEvent("sound", 24);
            player.addNote(new Pitch('D').toMidiNote(), 28, 2);
            player.addLyricEvent("That", 28);

            player.addNote(new Pitch('G').toMidiNote(), 30, 4);
            player.addLyricEvent("saved", 30);
            player.addNote(new Pitch('B').toMidiNote(), 34, 1);
            player.addLyricEvent("a", 34);
            player.addNote(new Pitch('G').toMidiNote(), 35, 1);

            player.addNote(new Pitch('B').toMidiNote(), 36, 4);
            player.addLyricEvent("wretch", 36);
            player.addNote(new Pitch('A').toMidiNote(), 40, 2);
            player.addLyricEvent("like", 40);
            player.addNote(new Pitch('D').transpose(Pitch.OCTAVE).toMidiNote(), 42, 8);
            player.addLyricEvent("me", 42);



            System.out.println(player);

            // play!
            player.play();

            /*
             * Note: A possible weird behavior of the Java sequencer: Even if the
             * sequencer has finished playing all of the scheduled notes and is
             * manually closed, the program may not terminate. This is likely
             * due to daemon threads that are spawned when the sequencer is
             * opened but keep on running even after the sequencer is killed. In
             * this case, you need to explicitly exit the program with
             * System.exit(0).
             */
            // System.exit(0);

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    /*
     * The tests below are for exceptions. These files are not played.
     * To see other abc files that play, please see AdditionalABCFilesTest.java
     */
    
    @Test(expected = RuntimeException.class)
    public void TestUnderTheSea() throws MidiUnavailableException, InvalidMidiDataException, IOException{
    	Main.play("sample_abc/under_the_sea.abc");
    }
    
    @Test(expected = RuntimeException.class)
    public void TestBonnieBanks() throws MidiUnavailableException, InvalidMidiDataException, IOException{
    	Main.play("sample_abc/bonnie_banks.abc");
    }

}


