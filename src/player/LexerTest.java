package player;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import grammar.ABCMusicLexer;

/**
 * 
 * Testing Strategy: First tests ensured that all individual components of header (such as composer, title, and index) were tested for.
 * Then simple tests were conducted within the body, testing different types of notes.
 * Finally, we tested combinations of both body and header, making sure to test one complex piece as well.
 * (Varying number of white spaces were also tested for within these tests.)  
 * 
 * In general, simple tests were conducted first, before progressing into pieces with both headers and body.
 * 
 * Also illegal inputs (like invalid baseNotes and headers) were tested to ensure that an exception was thrown.
 */
public class LexerTest {

    /*
     * First testing individual components of header.
     */
    @Test
    public void testIndex(){
        verifyLexer("X:2 \n", new String[] {"X:", "2", "\n"});
    }
    @Test
    public void testComposer(){
        verifyLexer("C:W. Mozart\n", new String[] {"C:W. Mozart", "\n"});
    }

    @Test
    public void testKey(){
        verifyLexer("K: Am\n", new String[] {"K: Am", "\n"});
    }
    @Test
    public void testMeter(){
        verifyLexer("M: 1/8\n", new String[] {"M: 1/8", "\n"});
    }
    @Test
    public void testLength(){
        verifyLexer("L: 3/8\n", new String[] {"L:" ,"3/8",  "\n"});
    }

    @Test
    public void testTempo(){
        verifyLexer("Q:250\n", new String[] {"Q:", "250", "\n"});
    }

    /*
     * Testing some combinations of fields inside header.
     */
    @Test
    public void testHeader(){
        verifyLexer("T:Simple scale\nC:Unknown\n", new String[] {"T:Simple scale", "\n", "C:Unknown", "\n"});
    }

    /*
     * Testing various types of notes such as chords and fractions.
     */
    @Test
    public void testIndividualNote(){
        verifyLexer("C3", new String[] {"C", "3"});
    }

    @Test
    public void testChordBarlineNotes(){
        verifyLexer("| [GBg] z G z |", new String[] {"|", "[","G","B","g","]","z","G","z","|"});
    }

    @Test
    public void testFractionalNotes(){
        verifyLexer("z/2 e c/2 d/2 B3/4", new String[] {"z","/2","e","c","/2","d", "/2", "B", "3/4"});
    }
    @Test 
    public void testNotes() {
        //testing all the possible different basenotes
        verifyLexer("C D E F | G A B c | c B A G F E D C |", new String[] {"C","D","E","F","|","G","A","B","c","|",
                "c","B","A","G","F","E","D","C","|"});
    }

    /*
     * More complex piece with both header and body
     */
    @Test
    public void testMusic() {
        verifyLexer("X:1234\nT:Random Song\nC:Debussy\nM:4/4\nL:1/16\nAceBdez", new String[] {
                "X:", "1234", "\n", "T:Random Song", "\n", "C:Debussy", "\n", "M:4/4", "\n", "L:", "1/16", "\n", "A", "c",
                "e","B","d","e","z"});
    }
    
    /*
     * Testing for invalid inputs, such as invalid basenotes
     */
    @Test(expected = RuntimeException.class) 
    public void testIncorrectBaseNote(){
        verifyLexer("| [GBg] z X z |", new String[] {""});
    }

    @Test(expected = RuntimeException.class) 
    public void testInvalidCharacter(){
        verifyLexer("| [GB@] z X z |", new String[] {""});
    }
    @Test(expected = RuntimeException.class)
    //Testing with invalid header input, P.
    public void testInvalidHeader(){
        verifyLexer("T:Simple scale\nP:Unknown\n", new String[] {""});
    }

    public void verifyLexer(String input, String[] expectedTokens) {
        CharStream stream = new ANTLRInputStream(input);
        ABCMusicLexer lexer = new ABCMusicLexer(stream);
        lexer.reportErrorsAsExceptions();
        List<? extends Token> actualTokens = lexer.getAllTokens();


        assertEquals(expectedTokens.length, actualTokens.size());

        for(int i = 0; i < actualTokens.size(); i++) {
            String actualToken = actualTokens.get(i).getText();
            String expectedToken = expectedTokens[i];
            assertEquals(actualToken, expectedToken);
        }
    }

}