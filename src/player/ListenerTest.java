package player;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

public class ListenerTest {
 
    /*************     Testing Strategy   ********** 
     * 
     * The overall testing strategy is to make sure that as many combinations of header and body 
     * are tested as possible. 
     * 
     * For the header:
     *    If both M and L are present
     *    If M is present and L is not 
     *    If only L is present and M is not 
     *    both M and L are not present
     *    
     * For body (notes):
     *    Basic basenotes 
     *    A scale (other than standard C)  
     *    Fractional notes (with numerator and/or denominator)
     *    Dotted notes
     *    Chords
     *    Tuplets - duplets, triplets, and quadruplets 
     *    Accidentals
     *    Different pitches 
     *    Repeats - normal repeats, and ones with [1 and [2
     *    Multiple voices
     *
     * Finally:
     *    A complex piece combining body and header.
     *        
     * In general the values tested for the basenotes included all the notes, and different octaves/pitches   
     *   
     * We will also test for invalid input that would lead to parsing errors and
     * throw exceptions
     *    Invalid Ordering - Index must be first, Title must be second, and Key must be last.
     *    Errors with Repeat Bars:
     *          Missing end repeat bar
     *          Swapped [1 and [2
     *          Missing either [1 or [2
     *    Extra Voices than those initialized
     *    Miscellaneous: extra accidentals
     *    
     */ 

    final static String DEFAULT_HEADER = "X:1\nT:Sample\nC:Unknown\nM:1/8\nL:1/4\nK:A\n";
    final static String DEFAULT_HEADERCMajor = "X:1\nT:Sample\nC:Unknown\nM:1/8\nL:1/4\nK:C\n";
    final static String DEFAULT_HEADERM = "X:1\nT:Sample\nC:Unknown\nM:7/8\nK:C\n";


    /*
     * Basic Header tests - test to make sure that noteLength and meter are assigned correctly depending on given input
     */

    @Test
    //Testing the header if M is given, but L is not. If M > 0.75, noteLength is 1/8
    public void BasicHeaderTestWithoutL() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:1\nT:Sample\nC:Unknown\nM:7/8\nK:C\nA|\n";
        ListenerTree l = new ListenerTree(inputFile);

        Header output = l.getHeader();
        String expectedOutput = "Index Number: 1\nTitle: Sample\nMeter: 7/8\nNoteLength: 1/8\nTempo: 1/8=100\nComposer: Unknown\n";
        assertTrue(expectedOutput.equals(output.toString()));

    }

    @Test
    //Testing the header if M is given, but L is not. If M < 0.75, noteLength is 1/16
    public void BasicHeaderTestWithoutLAndMLessThan75() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:1\nT:Sample\nC:Unknown\nM:2/8\nK:C\nA|\n";
        ListenerTree l = new ListenerTree(inputFile);

        Header output = l.getHeader();
        String expectedOutput = "Index Number: 1\nTitle: Sample\nMeter: 2/8\nNoteLength: 1/16\nTempo: 1/16=100\nComposer: Unknown\n";  
        assertTrue(expectedOutput.equals(output.toString()));

    }

    @Test
    //Testing the header if L is given, but M is not - M should be set to 4/4 in this case.
    public void BasicHeaderTestWithoutM() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:1\nT:Sample\nC:Unknown\nL:7/8\nK:C\nA|\n";
        ListenerTree l = new ListenerTree(inputFile);

        Header output = l.getHeader();
        String expectedOutput = "Index Number: 1\nTitle: Sample\nMeter: 4/4\nNoteLength: 7/8\nTempo: 7/8=100\nComposer: Unknown\n";
        assertTrue(expectedOutput.equals(output.toString()));  
    }

    @Test
    //Testing the header if both L and M are not given - note Length should be set to 1/8
    public void BasicHeaderTestWithoutMandL() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:1\nT:Sample\nC:Unknown\nK:C\nA|\n";
        ListenerTree l = new ListenerTree(inputFile);

        Header output = l.getHeader();
        String expectedOutput = "Index Number: 1\nTitle: Sample\nMeter: 4/4\nNoteLength: 1/8\nTempo: 1/8=100\nComposer: Unknown\n";
        assertTrue(expectedOutput.equals(output.toString()));  
    }

    /*
     * Note tests - testing a variety of note-types (including, but not limited to, notes, rests, chord, tuplets, accidentals, and octaves).
     */

    @Test
    // tests that a few basic notes are parsed correctly
    public void NoteTest() throws MidiUnavailableException, InvalidMidiDataException {

        String inputFile = "X:1\nT:Sample\nC:Unknown\nM:1/8\nL:1/4\nK:A\nC C2 C,3 =C4 |\n";
        ListenerTree l = new ListenerTree(inputFile);
        String expectedOutput ="[[Index: 1, Title: Sample, Composer: Unknown\n, Meter: 1/8, NoteLength: 1/4], [[(C, 1/1, 0, SHARP) , (C, 2/1, 0, SHARP) , (C, 3/1, -1, SHARP) , (C, 4/1, 0, NATURAL) ]]]"; 

        assertEquals(expectedOutput, l.parsed(inputFile).toString());
    }

    @Test
    //Testing F minor.
    public void FSharpScaleTest() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:1\nT:Sample\nC:Unknown\nK:Fm\nF G A B C D E F|\n";
        ListenerTree l = new ListenerTree(inputFile);
        String expectedOutput = "[[Index: 1, Title: Sample, Composer: Unknown\n], [[(F, 1/1, 0, NONE) , (G, 1/1, 0, NONE) , (A, 1/1, 0, FLAT) , (B, 1/1, 0, FLAT) , (C, 1/1, 0, NONE) , (D, 1/1, 0, FLAT) , (E, 1/1, 0, FLAT) , (F, 1/1, 0, NONE) ]]]";

        assertTrue(expectedOutput.equals(l.parsed(inputFile).toString()));  
    }

    @Test
    //testing various note lengths, including fractions.
    public void NoteLengthsTest() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:1\nT:Sample\nC:Unknown\nM:1/8\nL:1/4\nK:A\nB3/8 B/8 B/ B B1 B8 B1 B2 B4 |\n";

        String expectedOutput = "[[Index: 1, Title: Sample, Composer: Unknown\n, Meter: 1/8, NoteLength: 1/4], [[(B, 3/8, 0, NONE) , (B, 1/8, 0, NONE) , (B, 1/2, 0, NONE) , (B, 1/1, 0, NONE) , (B, 1/1, 0, NONE) , (B, 8/1, 0, NONE) , (B, 1/1, 0, NONE) , (B, 2/1, 0, NONE) , (B, 4/1, 0, NONE) ]]]";
        ListenerTree m = new ListenerTree(inputFile);
        assertEquals(expectedOutput, m.parsed(inputFile).toString());

    }

    @Test
    //testing various note lengths, including dotted notes.
    public void DottedNoteLengthsTest() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:1\nT:Sample\nC:Unknown\nM:1/8\nL:1/4\nK:C\nC C C3/4 D/4 E | E3/4 D/4 E3/4 F/4 G2 |\n";

        String expectedOutput = "[[Index: 1, Title: Sample, Composer: Unknown\n, Meter: 1/8, NoteLength: 1/4], [[(C, 1/1, 0, NONE) , (C, 1/1, 0, NONE) , (C, 3/4, 0, NONE) , (D, 1/4, 0, NONE) , (E, 1/1, 0, NONE) ], [(E, 3/4, 0, NONE) , (D, 1/4, 0, NONE) , (E, 3/4, 0, NONE) , (F, 1/4, 0, NONE) , (G, 2/1, 0, NONE) ]]]";
        ListenerTree m = new ListenerTree(inputFile);
        assertEquals(expectedOutput, m.parsed(inputFile).toString());
    }

    @Test
    //testing tuplets.
    public void TupletNotesTest() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:1\nT:Sample\nC:Unknown\nM:1/8\nL:1/4\nK:C\n(3G e g a f/2 g/2 |(2G a|(4E f g a|\n";

        String expectedOutput = "[[Index: 1, Title: Sample, Composer: Unknown\n, Meter: 1/8, NoteLength: 1/4], [[(G, 2/3, 0, NONE) , (E, 2/3, 1, NONE) , (G, 2/3, 1, NONE) , (A, 1/1, 1, NONE) , (F, 1/2, 1, NONE) , (G, 1/2, 1, NONE) ], [(G, 3/2, 0, NONE) , (A, 3/2, 1, NONE) ], [(E, 3/4, 0, NONE) , (F, 3/4, 1, NONE) , (G, 3/4, 1, NONE) , (A, 3/4, 1, NONE) ]]]";
        ListenerTree m = new ListenerTree(inputFile);
        assertEquals(expectedOutput, m.parsed(inputFile).toString());
    }


    @Test
    //testing various accidentals
    public void AccidentalsTest() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = DEFAULT_HEADER + "=F ^A ^^B _C __D |\n";

        String expectedOutput ="[[Index: 1, Title: Sample, Composer: Unknown\n, Meter: 1/8, NoteLength: 1/4], [[(F, 1/1, 0, NATURAL) , (A, 1/1, 0, SHARP) , (B, 1/1, 0, DOUBLESHARP) , (C, 1/1, 0, FLAT) , (D, 1/1, 0, DOUBLEFLAT) ]]]";

        ListenerTree l = new ListenerTree(inputFile);
        assertEquals(expectedOutput, l.parsed(inputFile).toString());
    }

    @Test
    //testing various pitches
    public void DifferentPitchesTest() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = DEFAULT_HEADER + "F, f G,, g A' A'' |\n";

        String expectedOutput = "[[Index: 1, Title: Sample, Composer: Unknown\n, Meter: 1/8, NoteLength: 1/4], [[(F, 1/1, -1, SHARP) , (F, 1/1, 1, SHARP) , (G, 1/1, -2, SHARP) , (G, 1/1, 1, SHARP) , (A, 1/1, 1, NONE) , (A, 1/1, 2, NONE) ]]]"; 
        ListenerTree l = new ListenerTree(inputFile);
        assertEquals(expectedOutput, l.parsed(inputFile).toString());
    }

    @Test
    //testing regular repeat
    public void BasicRepeatTest() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = DEFAULT_HEADERM + "| D D B B:|\n";

        String expectedOutput = "[[Index: 1, Title: Sample, Composer: Unknown\n, Meter: 7/8], [[(D, 1/1, 0, NONE) , (D, 1/1, 0, NONE) , (B, 1/1, 0, NONE) , (B, 1/1, 0, NONE) ], [(D, 1/1, 0, NONE) , (D, 1/1, 0, NONE) , (B, 1/1, 0, NONE) , (B, 1/1, 0, NONE) ]]]"; 
        ListenerTree l = new ListenerTree(inputFile);
        assertEquals(expectedOutput, l.parsed(inputFile).toString());
    }

    @Test
    //testing numbered repeat
    public void NumberedRepeatTest() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = DEFAULT_HEADERM + "B |: C[1AB :|[2E||\n";

        String expectedOutput = "[[Index: 1, Title: Sample, Composer: Unknown\n, Meter: 7/8], [[(B, 1/1, 0, NONE) ], [(C, 1/1, 0, NONE) , (A, 1/1, 0, NONE) , (B, 1/1, 0, NONE) ], [(C, 1/1, 0, NONE) , (A, 1/1, 0, NONE) , (B, 1/1, 0, NONE) ], [(E, 1/1, 0, NONE) ]]]"; 
        ListenerTree l = new ListenerTree(inputFile);
        assertEquals(expectedOutput, l.parsed(inputFile).toString());
    }

    @Test
    //testing multiple voices! - will create a list for each voice
    public void BasicVoicesTest() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:1\nT:Sample\nC:Unknown\nM:7/8\nV:1\nV:2\nK:C\n" + "V:1\ne z z ^ded|\nV:2\nz ^de z z2|\n";

        String expectedOutput = "[[[(E, 1/1, 1, NONE) , Rest1/1, Rest1/1, (D, 1/1, 1, SHARP) , (E, 1/1, 1, NONE) , (D, 1/1, 1, SHARP) ]], [[Rest1/1, (D, 1/1, 1, SHARP) , (E, 1/1, 1, NONE) , Rest1/1, Rest2/1]]]"; 
        ListenerTree l = new ListenerTree(inputFile);
        assertEquals(expectedOutput, l.voicesParsed(inputFile).toString());
    }

    @Test
    //testing a complex piece of music with chords, accidentals, and different pitches.
    public void testParseComplexPiece() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:2\nT:No.2\nM:4/4\nL:1/4\nQ:1/4=200\nK:C\n[^F/2e/2] [^F/2e/2] z/2 [^F/2e/2] z/2 [^F/2c/2] [^Fe] | (3G e g a f/2 g/2 |]\n";
        String expectedOutput = "[[Index: 2, Title: No.2, Meter: 4/4, NoteLength: 1/4, Tempo: 200], [[(E, 1/2, 1, NONE) (F, 1/2, 0, SHARP) 1/2, (E, 1/2, 1, NONE) (F, 1/2, 0, SHARP) 1/2, Rest1/2, (E, 1/2, 1, NONE) (F, 1/2, 0, SHARP) 1/2, Rest1/2, (C, 1/2, 1, NONE) (F, 1/2, 0, SHARP) 1/2, (E, 1/1, 1, NONE) (F, 1/1, 0, SHARP) 1/1], [(G, 2/3, 0, NONE) , (E, 2/3, 1, NONE) , (G, 2/3, 1, NONE) , (A, 1/1, 1, NONE) , (F, 1/2, 1, NONE) , (G, 1/2, 1, NONE) ]]]";
        ListenerTree l = new ListenerTree(inputFile);
        assertEquals(expectedOutput, l.parsed(inputFile).toString());
    }
    /*
     * Testing that improper ordering of the header fields results in an exception being thrown.
     */

    @Test(expected = RuntimeException.class)
    //Index is not first 
    public void testMissingIndex() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "T:Sample\nC:Unknown\nM:7/8\nK:C\nA|\n";
        ListenerTree l = new ListenerTree(inputFile);
        l.parsed(inputFile).toString();
    }
    @Test(expected = RuntimeException.class)
    //Title is not second 
    public void testTitleError() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:2\nC:Unknown\nT:Sample\nM:7/8\nK:C\nA|\n";
        ListenerTree l = new ListenerTree(inputFile);
        l.parsed(inputFile).toString();
    }
    @Test(expected = RuntimeException.class)
    //Key is not last  
    public void testKeyNotLast() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:2\nC:Unknown\nT:Sample\nK:C\nM:7/8\nA|\n";
        ListenerTree l = new ListenerTree(inputFile);
        l.parsed(inputFile).toString();
    }

    @Test(expected = RuntimeException.class)
    //Beginning Repeat |: provided, but missing :|  
    public void testEndRepeatMissing() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = DEFAULT_HEADERM + "|: D D B B|\n";
        ListenerTree l = new ListenerTree(inputFile);
        l.parsed(inputFile).toString();
    }

    @Test(expected = RuntimeException.class)
    //Ordering of [1 and [2 is reversed  
    public void testSwappedRepeatOrder() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = DEFAULT_HEADERM + "B |: C[2AB :|[1E||\n";
        ListenerTree l = new ListenerTree(inputFile);
        l.parsed(inputFile).toString();
    }

    @Test(expected = RuntimeException.class)
    //Only provided [1 and not [2  
    public void testMissingSecondRepeat() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = DEFAULT_HEADERM + "B |: C[1AB :|\n";
        ListenerTree l = new ListenerTree(inputFile);
        l.parsed(inputFile).toString();
    }

    @Test(expected = RuntimeException.class)
    //End bar is missing  
    public void testEndBarMissing() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = DEFAULT_HEADERM + "B |: C[1AB\n";
        ListenerTree l = new ListenerTree(inputFile);
        l.parsed(inputFile).toString();
    }
    
    @Test(expected = RuntimeException.class)
    //Incorrect accidental is missing  
    public void testIncorrectAccidentals() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = DEFAULT_HEADERM + "^F, ^^^f G,, g A' A''\n";
        ListenerTree l = new ListenerTree(inputFile);
        l.parsed(inputFile).toString();
    }
    
    @Test(expected = RuntimeException.class)
    //More voices than initialized  
    public void testExtraVoices() throws MidiUnavailableException, InvalidMidiDataException {
        String inputFile = "X:1\nT:Sample\nC:Unknown\nM:7/8\nV:1\nK:C\n" + "V:1\ne z z ^ded|\nV:2\nz ^de z z2|\n";
        ListenerTree l = new ListenerTree(inputFile);
        l.parsed(inputFile).toString();
    }


}