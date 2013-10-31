package player;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import grammar.ABCMusicLexer;
import grammar.ABCMusicParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import sound.LyricListener;
import sound.Pitch;
import sound.SequencePlayer;

public class ListenerTree {

    private Listener l;
    private List<Object> elements;
    private int lcm;
    private int tempo;
    private boolean voices;

    public ListenerTree(String input) throws MidiUnavailableException, InvalidMidiDataException{
        this.elements = parse(input);
        this.lcm = setLCM(input);
        this.tempo = setTempo(input);
        this.voices = setVoices(input);
        
    }
    
    public List<Object> parse(String input) throws MidiUnavailableException, InvalidMidiDataException {
        l = runListener(input);
        List<Object> elements = ((Listener) l).getMusic();

        return elements;
    }
    public List<ArrayList<Object>> parsed(String input) throws MidiUnavailableException, InvalidMidiDataException {
        l = runListener(input);
        int lcm = ((Listener) l).getLeastCommonDenominator();
        int totalTicks = 0;
        List<ArrayList<Object>> parsedNotes = ((Listener) l).getParsedOutput();
        return parsedNotes;
    }
    
    public List<Object> voicesParsed(String input) throws MidiUnavailableException, InvalidMidiDataException {
        l = runListener(input);
        int lcm = ((Listener) l).getLeastCommonDenominator();
        int totalTicks = 0;
        List<Object> parsedVoices = ((Listener) l).getVoices();
        return parsedVoices;
    }
    
    public int setLCM(String input){
        l = runListener(input);
        int lcm = ((Listener) l).getLeastCommonDenominator();
        return lcm;
    }
    
    public int setTempo(String input){
        l = runListener(input);
        Header h = ((Listener) l).getHeader();
        int tempo = h.getTempo();
        
        Fraction defaultLength = h.getDefaultNote();
        Fraction temponote = h.getTempoNote();
    
        int factor = temponote.divide(defaultLength);
        tempo = tempo * factor;
        return tempo;
    }
    
    public boolean setVoices(String input){
        l = runListener(input);
        boolean voices = ((Listener) l).haveVoices();
        return voices;
    }
    
    public boolean getVoices(){
        return voices;
    }
    
    public int getTempo(){
        return tempo;
    }
    
    public int getLCM(){
        return lcm;
    }
    
    public List<Object> getElements(){
        return elements;
    }

    public Header getHeader() {
        return l.getHeader();
    }
    private Listener runListener(String input) {
        // Create a stream of tokens using the lexer.
        CharStream stream = new ANTLRInputStream(input);
        ABCMusicLexer lexer = new ABCMusicLexer(stream);
        lexer.reportErrorsAsExceptions();
        TokenStream tokens = new CommonTokenStream(lexer);

        // Feed the tokens into the parser.

        ABCMusicParser parser = new ABCMusicParser(tokens);
        parser.reportErrorsAsExceptions();

        // Generate the parse tree using the starter rule.
        ParseTree tree;
        tree = parser.abc_tune();

        //((RuleContext) tree).inspect(parser);

        // Walk the tree with the listener.
        ParseTreeWalker walker = new ParseTreeWalker();
        ParseTreeListener listener = new Listener();
        walker.walk(listener, tree);

        return (Listener) listener;
    }
}
