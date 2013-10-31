package sound;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

import player.Main;

/**
 * @category no_didit
 */
public class AdditionalABCFilesTest {

    @Test
    public void TestMaryHadALittleLamb() throws MidiUnavailableException, InvalidMidiDataException, IOException{
        Main.play("sample_abc/mary_had_a_little_lamb.abc");
    }
    
    @Test
    public void TestHarryPotter() throws MidiUnavailableException, InvalidMidiDataException, IOException{
        Main.play("sample_abc/harry_potter.abc");
    }

}
