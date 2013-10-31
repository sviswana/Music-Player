package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import sound.Pitch;
import grammar.ABCMusicBaseListener;
import grammar.ABCMusicParser;

/**
 * This Listener class implements methods that are called by the parser as the
 * lexer tokens are parsed.
 * 
 */
public class Listener extends ABCMusicBaseListener {

    //For Header
    private ArrayList<Object> headerList = new ArrayList<Object>(); //list containing all the information from the header

    //For handling individual notes, rests, chords, and tuplets.
    public Stack<Object> stack = new Stack<Object>(); //stack that holds pitches
    private Stack<Object> measureStack = new Stack<Object>(); //stack that holds notes, contains Objects which allow for different types to be added.

    //For handling keys and accidentals
    private HashMap<String, Integer> notesPlayed = new HashMap<String, Integer>(); //hashmap that keeps track of the notesPlayed and the accidentals that each note have in a bar (if any)
    private HashMap<Key, HashMap<BaseNote, Accidental>> KeyMap = new HashMap<Key, HashMap<BaseNote, Accidental>>(); // mapping keys to a second hashmap that contains the basenote mapped to its default accidentals
    private Key key; //song key

    //For handling voices
    private List<String> voiceNames = new ArrayList<String>(); //List of all the names of the voices for a song
    private List<Object> voiceList = new ArrayList<Object>(); //contains all the voices, where each voice contains its measures
    private int currentVoice = -1; //index number of the current voice being parsed in voiceNames
    private boolean voicesSeen = false; //if the song contains voices
    private boolean holdLyric = false; //if there are lyrics to be held to be output together later

    //For handling repeats
    private int startRepeat = 0; //beginning repeat bar number
    private boolean startBarSeen = false; //keeps track if a start repeat bar has been seen
    private int twoRepeatIndex = 0; // records the bar where [2 is seen
    private boolean seenTwoIndex = false; //keeps track if [2 is seen
    private boolean seenOneIndex = false; //keeps track if [1 is seen
    private int oneRepeatIndex; //index number for [1

    //For handling lyrics
    private Stack<String> lyricStack = new Stack<String>(); //contains the lyrics for an abc_line

    //For determining LCM
    private List<Integer> denominators = new ArrayList<Integer>(); //all the denominators of the notes in order to determine the LCM

    //Final Stack with all elements
    private List<Object> cumulativeList = new ArrayList<Object>(); //If there are no voices, cumulativeList contains all notes, rests, tuplets and chords in the song

    @Override
    public void enterAbc_tune(ABCMusicParser.Abc_tuneContext ctx) {
        /*
         * KeyMap is a hashmap whose key is the given key signature, and whose value is SharpsAndFlats, another HashMap
         * SharpsAndFlats is a hashmap whose key is the basenote, and whose value is either 
         * a sharp accidental or a flat accidental.
         */

        // A major and F# minor
        HashMap<BaseNote, Accidental> SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.F, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.C, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.G, Accidental.SHARP);
        KeyMap.put(Key.AMajor, SharpsAndFlats);
        KeyMap.put(Key.FSharpMinor, SharpsAndFlats);

        // C Major and A minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        KeyMap.put(Key.CMajor, SharpsAndFlats);
        KeyMap.put(Key.AMinor, SharpsAndFlats);

        // F Sharp major and D sharp minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.F, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.C, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.G, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.D, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.A, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.E, Accidental.SHARP);
        KeyMap.put(Key.FSharpMajor, SharpsAndFlats);
        KeyMap.put(Key.DSharpMinor, SharpsAndFlats);

        // C Sharp major and A sharp minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.F, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.C, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.G, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.D, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.A, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.E, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.B, Accidental.SHARP);
        KeyMap.put(Key.CSharpMajor, SharpsAndFlats);
        KeyMap.put(Key.ASharpMinor, SharpsAndFlats);

        // C Flat major and A Flat minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.F, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.C, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.G, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.D, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.A, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.E, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.B, Accidental.FLAT);
        KeyMap.put(Key.CFlatMajor, SharpsAndFlats);
        KeyMap.put(Key.AFlatMinor, SharpsAndFlats);

        // G Flat major and E Flat minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.C, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.G, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.D, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.A, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.E, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.B, Accidental.FLAT);
        KeyMap.put(Key.DFlatMajor, SharpsAndFlats);
        KeyMap.put(Key.EFlatMinor, SharpsAndFlats);

        // D Flat major and B Flat minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.G, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.D, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.A, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.E, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.B, Accidental.FLAT);
        KeyMap.put(Key.DFlatMajor, SharpsAndFlats);
        KeyMap.put(Key.BFlatMinor, SharpsAndFlats);

        // A Flat major and F minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.D, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.A, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.E, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.B, Accidental.FLAT);
        KeyMap.put(Key.AFlatMajor, SharpsAndFlats);
        KeyMap.put(Key.FMinor, SharpsAndFlats);

        // E Flat major and C minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.A, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.E, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.B, Accidental.FLAT);
        KeyMap.put(Key.EFlatMajor, SharpsAndFlats);
        KeyMap.put(Key.CMinor, SharpsAndFlats);

        // B Flat major and G minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.B, Accidental.FLAT);
        SharpsAndFlats.put(BaseNote.E, Accidental.FLAT);
        KeyMap.put(Key.BFlatMajor, SharpsAndFlats);
        KeyMap.put(Key.GMinor, SharpsAndFlats);

        // F major and D minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.B, Accidental.FLAT);
        KeyMap.put(Key.FMajor, SharpsAndFlats);
        KeyMap.put(Key.DMinor, SharpsAndFlats);

        // G major and E minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.F, Accidental.SHARP);
        KeyMap.put(Key.GMajor, SharpsAndFlats);
        KeyMap.put(Key.EMinor, SharpsAndFlats);

        // D major and B minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.F, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.C, Accidental.SHARP);
        KeyMap.put(Key.DMajor, SharpsAndFlats);
        KeyMap.put(Key.BMinor, SharpsAndFlats);

        // E major and C# minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.F, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.C, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.G, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.D, Accidental.SHARP);
        KeyMap.put(Key.EMajor, SharpsAndFlats);
        KeyMap.put(Key.CSharpMinor, SharpsAndFlats);

        // B major and G# minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.F, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.C, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.G, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.D, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.A, Accidental.SHARP);
        KeyMap.put(Key.BMajor, SharpsAndFlats);
        KeyMap.put(Key.GSharpMinor, SharpsAndFlats);

        // F# major and D# minor
        SharpsAndFlats = new HashMap<BaseNote, Accidental>();
        SharpsAndFlats.put(BaseNote.F, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.C, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.G, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.D, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.A, Accidental.SHARP);
        SharpsAndFlats.put(BaseNote.E, Accidental.SHARP);
        KeyMap.put(Key.FSharpMajor, SharpsAndFlats);
        KeyMap.put(Key.DSharpMinor, SharpsAndFlats);
    }

    @Override
    public void exitMid_tune_field(ABCMusicParser.Mid_tune_fieldContext ctx) {
        String voiceNam = ctx.field_voice().VOICE().getText();
        //checks if the voice has been initialized in the header
        if (!voiceNames.contains(voiceNam)){
            throw new RuntimeException("You have a voice that is not mentioned in your header!");
        }
        //sets currentVoice to voice whose measures are being parsed
        currentVoice = voiceNames.indexOf(voiceNam);
    }

    @Override
    public void exitField_number(ABCMusicParser.Field_numberContext ctx) {
        //Gets the index of the piece, which is added to the headerlist
        String fieldnum = ctx.fieldnum().DIGIT().getText();
        int index = Integer.parseInt(fieldnum);
        headerList.add(new Index(index));
    }

    @Override
    public void exitField_title(ABCMusicParser.Field_titleContext ctx) {
        // Gets the title of the music piece which is added to the headerList
        String title0 = ctx.TITLE().getText();
        String[] parts = title0.split(":");
        String title = parts[1];
        headerList.add(new Title(title));
    }

    @Override
    public void exitField_composer(ABCMusicParser.Field_composerContext ctx) {
        // Gets the composer of the piece, which is added to the headerList
        String composer0 = ctx.COMPOSER().getText();
        String[] parts = composer0.split(":");
        String composer = parts[1];
        headerList.add(new Composer(composer));
    }

    @Override
    public void exitField_meter(ABCMusicParser.Field_meterContext ctx) {

        // Gets the meter of the piece, which is added to the headerList
        if (ctx.METER() != null) {
            String meter = ctx.METER().getText().replaceAll("\\s","");
            String[] parts = meter.split(":");
            String noteLength = parts[1];

            String[] parts2 = noteLength.split("/");
            int num = Integer.parseInt(parts2[0]);
            int denom = Integer.parseInt(parts2[1]);

            headerList.add(new Meter(new Fraction(num, denom)));
        }

        //commontime  C 
        else if (ctx.COMMONTIME() != null) {
            headerList.add(new Meter(new Fraction(4, 4)));
        }

        //cut time C| 
        else { 
            headerList.add(new Meter(new Fraction(2, 2)));
        }
    }

    @Override
    public void exitField_default_length(ABCMusicParser.Field_default_lengthContext ctx) {

        // Gets the notelength of the piece, which is added to the headerList
        String noteLength = ctx.notelengthstrict().getText().replaceAll("\\s","");
        String[] parts = noteLength.split("/");
        int num = Integer.parseInt(parts[0]);
        int denom = Integer.parseInt(parts[1]);

        headerList.add(new Length(new Fraction(num, denom)));
    }

    @Override
    public void exitField_tempo(ABCMusicParser.Field_tempoContext ctx) {

        // Gets the tempo of the piece, which is added to the headerList
        String tempo0 = ctx.TEMPO().getText().replaceAll("\\s","");
        String[] parts = tempo0.split("=");
        String noteLength = parts[0];
        String[] parts2 = noteLength.split("/");
        int num = Integer.parseInt(parts2[0]);
        int denom = Integer.parseInt(parts2[1]);

        int tempo = Integer.parseInt(parts[1]);

        headerList.add(new Tempo(new Fraction(num, denom), tempo));
    }

    @Override
    public void exitField_key(ABCMusicParser.Field_keyContext ctx) {
        /*
         * Creates HashMap m1, whose keys are the seven possible basenotes, and whose values
         * are the corresponding integer, as given in Pitch.java.
         */
        HashMap<String, Integer> m1 = new HashMap<String, Integer>();
        m1.put("A", 9);
        m1.put("B", 11);
        m1.put("C", 0);
        m1.put("D", 2);
        m1.put("E", 4);
        m1.put("F", 5);
        m1.put("G", 7);

        String key0 = ctx.KEY().getText().replaceAll("\\s","");
        String[] parts = key0.split(":");
        String k = parts[1];

        //This assigns the processed key (which is a string) to the correct Key enum, primarily for readability.
        switch (k) {
        case "C":
            key = Key.CMajor;
            break;
        case "C#":
            key = Key.CSharpMajor;
            break;
        case "Cb":
            key = Key.BMajor;
            break;
        case "Cm":
            key = Key.CMinor;
            break;
        case "C#m":
            key = Key.CSharpMinor;
            break;
        case "Cbm":
            key = Key.BMinor;
            break;
        case "D":
            key = Key.DMajor;
            break;
        case "D#":
            key = Key.EFlatMajor;
            break;
        case "Db":
            key = Key.DMajor;
            break;
        case "Dm":
            key = Key.DMinor;
            break;
        case "D#m":
            key = Key.DSharpMinor;
            break;
        case "Dbm":
            key = Key.CSharpMinor;
            break;
        case "E":
            key = Key.EMajor;
            break;
        case "E#":
            key = Key.FMajor;
            break;
        case "Eb":
            key = Key.EFlatMajor;
            break;
        case "Em":
            key = Key.EMinor;
            break;
        case "E#m":
            key = Key.FMinor;
            break;
        case "Ebm":
            key = Key.EFlatMinor;
            break;
        case "F":
            key = Key.FMajor;
            break;
        case "F#":
            key = Key.FSharpMajor;
            break;
        case "Fb":
            key = Key.EMajor;
            break;
        case "Fm":
            key = Key.FMinor;
            break;
        case "F#m":
            key = Key.FSharpMinor;
            break;
        case "Fbm":
            key = Key.EMinor;
            break;
        case "G":
            key = Key.GMajor;
            break;
        case "G#":
            key = Key.AFlatMajor;
            break;
        case "Gb":
            key = Key.GFlatMajor;
            break;
        case "Gm":
            key = Key.GMinor;
            break;
        case "G#m":
            key = Key.GSharpMinor;
            break;
        case "Gbm":
            key = Key.FSharpMinor;
            break;
        case "A":
            key = Key.AMajor;
            break;
        case "A#":
            key = Key.BFlatMajor;
            break;
        case "Ab":
            key = Key.AFlatMajor;
            break;
        case "Am":
            key = Key.AMinor;
            break;
        case "A#m":
            key = Key.ASharpMinor;
            break;
        case "Abm":
            key = Key.AFlatMinor;
            break;
        case "B":
            key = Key.BMajor;
            break;
        case "B#":
            key = Key.CMajor;
            break;
        case "Bb":
            key = Key.BFlatMajor;
            break;
        case "Bm":
            key = Key.BMinor;
            break;
        case "B#m":
            key = Key.CMinor;
            break;
        case "Bbm":
            key = Key.BFlatMinor;
            break;
        }
    }

    @Override 
    public void exitRepeatbar(ABCMusicParser.RepeatbarContext ctx) {


        //We want to preserve the order of the original stack, so we pop play-types off the measureStack
        //and then into a temporary stack.

        Stack<Object> temporary = new Stack<Object>();
        Stack<Object> measureBar = new Stack<Object>();

        /* Repeatbar is a type of bar, so when we see a repeat
         * if no voices: add everything in the measure to the cumulativeList
         * if there are voices: add everything to the currentVoice in the voiceList 
         */
        if (!measureStack.empty()) {
            while (!measureStack.empty()) {
                temporary.add(measureStack.pop());
            }
            while (!temporary.empty()) {
                measureBar.add(temporary.pop());
            }

            if(voicesSeen){
                if (currentVoice == -1){
                    throw new RuntimeException("You do not have a voice for this music! Please assign this music a voice or remove the voices from your header, thank you!");
                }
                List<Object> tobeAdded = (List<Object>) voiceList.get(currentVoice);
                tobeAdded.add(measureBar);
            }
            else{
                cumulativeList.add(measureBar);
            }
        }

        //if |: is seen, set the startRepeat
        if (ctx.REPEATL() != null) {
            startBarSeen = true;
            if(!voicesSeen){
                startRepeat = cumulativeList.size();
            } 
            else if(voicesSeen){
                if (currentVoice == -1){
                    throw new RuntimeException("You do not have a voice for this music! Please assign this music a voice or remove the voices from your header, thank you!");
                }
                List<Object> voice = (List<Object>) voiceList.get(currentVoice);
                startRepeat = voice.size();
            }
        } 

        //if :| is seen, add the repeated measures to cumulativeList(if no voices) or voiceList(if there are voices)
        else if (ctx.REPEATR() != null) {
            int endRepeat = 0;
            startBarSeen = false;
            if(!voicesSeen){
                endRepeat = cumulativeList.size();
            } else if(voicesSeen){
                if (currentVoice == -1){
                    throw new RuntimeException("You do not have a voice for this music! Please assign this music a voice or remove the voices from your header, thank you!");
                }
                List<Object> voice = (List<Object>) voiceList.get(currentVoice);
                endRepeat = voice.size();
            }

            //if [1 is not seen then repeat the music from startRepeat until the endRepeat bar
            int endingRepeat = 0;
            if(oneRepeatIndex <= startRepeat){
                endingRepeat = endRepeat;
            } else{
                endingRepeat = oneRepeatIndex;
            }

            if(!voicesSeen){
                for (int i = startRepeat; i < endingRepeat; i++) {
                    Stack<Object> repeatedMeasure = (Stack) cumulativeList.get(i);
                    cumulativeList.add(repeatedMeasure);
                }
            } 
            else if(voicesSeen){
                if (currentVoice == -1){
                    throw new RuntimeException("You do not have a voice for this music! Please assign this music a voice or remove the voices from your header, thank you!");
                }
                List<Object> currentVoiceStack = (List<Object>) voiceList.get(currentVoice);
                for (int i = startRepeat; i < endingRepeat; i++){
                    List<Object> repeatedMeasure = (List<Object>) currentVoiceStack.get(i);
                    currentVoiceStack.add(repeatedMeasure);
                }
            }
        }
    }

    @Override 
    public void exitAbc_music(ABCMusicParser.Abc_musicContext ctx) {
        //Check for errors in music piece

        //check if the music has a bar at the end
        if(!measureStack.empty()){
            throw new RuntimeException("You did not have a final bar in your music! Please add a final bar or repeat bar.");
        }
        //has |: but is missing :|
        if (startBarSeen){
            throw new RuntimeException("You're missing an end repeat bar!");
        }
        //have [2, but not [1
        if (!seenOneIndex && seenTwoIndex){
            throw new RuntimeException("You have a [2, but not [1 in your music!");
        }
        //have [1, but not [2
        if (seenOneIndex && !seenTwoIndex){
            throw new RuntimeException("You have a [1, but not [2 in your music!");
        }
        //[2 appears before [1 
        if (seenOneIndex && seenTwoIndex){
            if (oneRepeatIndex >= twoRepeatIndex){
                throw new RuntimeException("You have  [2  before [1 in your music!");
            }
        }
    }

    @Override
    public void exitTuplet_element(ABCMusicParser.Tuplet_elementContext ctx) {
        // Tuplets with 2 notes
        if (ctx.tuplet_element2() != null) {
            Note note1 = (Note) measureStack.pop();
            Note note2 = (Note) measureStack.pop();

            Fraction newlength1 = note1.getLength()
                    .multiply(new Fraction(3, 2));
            Fraction newlength2 = note2.getLength()
                    .multiply(new Fraction(3, 2));

            note1.setLength(newlength1);
            note2.setLength(newlength2);

            //Adding denominator to list 'denominators' to compute LCM later.
            denominators.add(newlength1.getDenominator());
            denominators.add(newlength2.getDenominator());

            measureStack.push(note2);
            measureStack.push(note1);
        }

        // Tuplets with 3 notes
        else if (ctx.tuplet_element3() != null) {
            Note note1 = (Note) measureStack.pop();
            Note note2 = (Note) measureStack.pop();
            Note note3 = (Note) measureStack.pop();

            Fraction newlength1 = note1.getLength().multiply(
                    new Fraction(2, 3).simplify());
            Fraction newlength2 = note2.getLength().multiply(
                    new Fraction(2, 3).simplify());
            Fraction newlength3 = note3.getLength().multiply(
                    new Fraction(2, 3).simplify());

            note1.setLength(newlength1);
            note2.setLength(newlength2);
            note3.setLength(newlength3);

            denominators.add(newlength1.getDenominator());
            denominators.add(newlength2.getDenominator());
            denominators.add(newlength3.getDenominator());

            measureStack.push(note3);
            measureStack.push(note2);
            measureStack.push(note1);
        }

        // Tuplets with 4 notes
        else if (ctx.tuplet_element4() != null) {
            Note note1 = (Note) measureStack.pop();
            Note note2 = (Note) measureStack.pop();
            Note note3 = (Note) measureStack.pop();
            Note note4 = (Note) measureStack.pop();

            Fraction newlength1 = note1.getLength()
                    .multiply(new Fraction(3, 4));
            Fraction newlength2 = note2.getLength()
                    .multiply(new Fraction(3, 4));
            Fraction newlength3 = note3.getLength()
                    .multiply(new Fraction(3, 4));
            Fraction newlength4 = note4.getLength()
                    .multiply(new Fraction(3, 4));

            note1.setLength(newlength1);
            note2.setLength(newlength2);
            note3.setLength(newlength3);
            note4.setLength(newlength4);

            denominators.add(newlength1.getDenominator());
            denominators.add(newlength2.getDenominator());
            denominators.add(newlength3.getDenominator());
            denominators.add(newlength4.getDenominator());

            //Tuplets are added to measureStack
            measureStack.push(note4);
            measureStack.push(note3);
            measureStack.push(note2);
            measureStack.push(note1);
        }
    }

    @Override
    public void exitElement(ABCMusicParser.ElementContext ctx) {
        //if [1 or [2 is seen
        if (ctx.NTHREPEAT() != null) {
            //case when there are no voices
            if (ctx.NTHREPEAT().getText().equals("[1") && !voicesSeen) {
                oneRepeatIndex = cumulativeList.size();
                seenOneIndex = true;
            } 
            //case for where there are voices
            else if(ctx.NTHREPEAT().getText().equals("[1") && voicesSeen) {
                if (currentVoice == -1){
                    throw new RuntimeException("You do not have a voice for this music! Please assign this music a voice or remove the voices from your header, thank you!");
                }
                List<Object> voice = (List<Object>) voiceList.get(currentVoice);
                oneRepeatIndex = voice.size();
                seenOneIndex = true;
            }  
            else if (ctx.NTHREPEAT().getText().equals("[2") && !voicesSeen) {
                twoRepeatIndex = cumulativeList.size();
                seenTwoIndex = true;
            } 
            //case for where there are voices
            else if(ctx.NTHREPEAT().getText().equals("[2") && voicesSeen) {
                if (currentVoice == -1){
                    throw new RuntimeException("You do not have a voice for this music! Please assign this music a voice or remove the voices from your header, thank you!");
                }
                List<Object> voice = (List<Object>) voiceList.get(currentVoice);
                twoRepeatIndex = voice.size();
                seenTwoIndex = true;
            }  
        }
    }

    @Override
    public void exitOther_fields(ABCMusicParser.Other_fieldsContext ctx) {
        //if there is a voice, add it to the list of voice names and instantiate an ArrayList to hold the measures for the voice
        if(ctx.field_voice() != null){
            voiceList.add(new ArrayList<Object>());
            voiceNames.add(ctx.field_voice().VOICE().getText());
            voicesSeen = true;
        }
    }

    @Override
    public void enterAbc_line(ABCMusicParser.Abc_lineContext ctx) {
        if(ctx.lyric() != null){
            String allLyrics = ctx.lyric().LYRIC().getText();

            //splits the lyrics by spaces and '-'s
            String[] lyricsPart = allLyrics.split("[ -]+");

            // holdOn keeps track of the lyrics to be printed together as indicated by /- 
            String holdOn = "";

            Stack<String> tempLyrics = new Stack<String>();
            while(!lyricStack.isEmpty()){
                tempLyrics.add(lyricStack.pop());
            }
            for (int i = 0; i < lyricsPart.length; i ++){
                /* Case 1: 'w: something" will be parsed as 'something'
                 * Case 2: 'w: ' will be ignored
                 */
                if(lyricsPart[i].contains("w:")){
                    String[] changed = lyricsPart[i].split(":");
                    if(changed.length > 1){
                        if(holdOn != ""){
                            changed[1] = holdOn + changed[1];
                            holdOn = "";
                        }
                        tempLyrics.add(changed[1]);
                    }
                } else if(lyricsPart[i].charAt(lyricsPart[i].length()-1) == '\\' ){
                    holdOn = holdOn + lyricsPart[i].replace('\\', '-');	
                }
                // '_' means to hold for extra note, so no lyric will be output during the next note
                else if(lyricsPart[i].contains("_")){
                    String changed = lyricsPart[i].replace("_", "");
                    if(holdOn != ""){
                        changed = holdOn + changed;
                        holdOn = "";
                    }
                    tempLyrics.add(changed);
                    tempLyrics.add("_");
                } 
                //'*' means to skip a note, so no lyric will be output during the next note
                else if(lyricsPart[i].contains("*")){
                    String changed = lyricsPart[i].replace("*", "");
                    if(holdOn != ""){
                        changed = holdOn + changed;
                        holdOn = "";
                    }
                    tempLyrics.add(changed);
                } 
                //'~' means that the notes should be output together, so just remove ~ in output
                else if(lyricsPart[i].contains("~")){
                    String changed = lyricsPart[i].replace("~", " ");
                    if(holdOn != ""){
                        changed = holdOn + changed;
                        holdOn = "";
                    }
                    tempLyrics.add(changed);
                }	else{
                    String changed = lyricsPart[i];
                    if(holdOn != ""){
                        changed = holdOn + changed;
                        holdOn = "";
                    }
                    tempLyrics.add(changed);
                }
            }
            while(!tempLyrics.isEmpty()){
                lyricStack.add(tempLyrics.pop());
            }
        }
    }

    @Override
    public void exitEndbar(ABCMusicParser.EndbarContext ctx) {
        //after end of major section, reset the startRepeat so that if there is a repeatbar, then the music will repeat from after the end of the major section
        if(!voicesSeen){
            startRepeat = cumulativeList.size() +1;
        } else if(voicesSeen){
            //checks to see if the bar of music is assigned to a voice.
            if(currentVoice == -1){
                throw new RuntimeException("You did not indicate the voice for this part!");
            }
            List<Object> voice = (List<Object>) voiceList.get(currentVoice);
            startRepeat = voice.size() + 1;
        }
    }

    @Override
    public void exitBarline(ABCMusicParser.BarlineContext ctx) {

        Stack<Object> temporary = new Stack<Object>();
        Stack<Object> measureBar = new Stack<Object>();

        holdLyric = false;
        notesPlayed.clear();

        if ((ctx.BAR() != null || ctx.endbar() != null) && !measureStack.empty()) {
            /*
             * We want to preserve the order of the original stack, so we pop play-types off the measureStack
             * and then into a temporary stack, before popping them finally into cumulativeList.
             */
            while (!measureStack.empty()) {
                temporary.add(measureStack.pop());
            }

            while (!temporary.empty()) {
                measureBar.add(temporary.pop());
            }
            // barlineStack.add(measureBar);
            if(voicesSeen){
                if (currentVoice == -1){
                    throw new RuntimeException("You do not have a voice for this music! Please assign this music a voice or remove the voices from your header, thank you!");
                }
                List<Object> tobeAdded = (List<Object>) voiceList.get(currentVoice);
                tobeAdded.add(measureBar);
            }else{
                cumulativeList.add(measureBar);
            }
        }
    }

    @Override
    public void exitNote(ABCMusicParser.NoteContext ctx) {

        //Add the relevant lyric to be played with the current note
        if (!holdLyric && !lyricStack.empty()) {
            String lyrics = lyricStack.pop();
            if (lyrics.equals("|")) {
                holdLyric = true;
            } else if (lyrics.equals("_")) {
                measureStack.push(new Lyric(" "));
            } else {
                measureStack.push(new Lyric(lyrics));
            }
        }

        
        /*
         * Creates HashMap, whose keys are the seven possible basenotes, and whose values
         * are the corresponding integer, as given in Pitch.java.
         */
        HashMap<Integer, String> m1 = new HashMap<Integer, String>();
        m1.put(9, "A");
        m1.put(11, "B");
        m1.put(0, "C");
        m1.put(2, "D");
        m1.put(4, "E");
        m1.put(5, "F");
        m1.put(7, "G");

        String noteLength = "";
        int numerator = 1;
        int denominator = 1;
        Accidental accidental;
        
        //Obtaining the note-length of the note
        if (ctx.notelength() != null) {
            if (ctx.notelength().NOTELENGTH() != null) {
                //note: replaceAll is meant to remove white-spaces after the ':' within header
                noteLength = ctx.notelength().NOTELENGTH().getText().replaceAll("\\s",""); 
                //We need to get the numerator and denominator separately so we split based on '/'
                String[] parts = noteLength.split("/");
                if (parts[0].length() > 1) {
                    numerator = Integer.parseInt(parts[0]);
                }
                denominator = Integer.parseInt(parts[1]);
            }

            //Handles the case where note has only numerator and '/'
            else if (ctx.notelength().NOTELENGTH2() != null) {
                noteLength = ctx.notelength().NOTELENGTH2().getText().replaceAll("\\s","");
                String[] parts = noteLength.split("/");
                if (parts.length > 0) {// there is a number in notelength2
                    numerator = Integer.parseInt(parts[0]);
                }
                int count = 0;
                for (int i = 0; i < noteLength.length(); i++) {
                    if (noteLength.charAt(i) == '/') {
                        count += 1;
                    }
                }
                denominator = count * 2;
            }
            //Handles the case where there is no '/', just a digit
            else if (ctx.notelength().DIGIT() != null) {
                noteLength = ctx.notelength().DIGIT().getText().replaceAll("\\s","");
                numerator = Integer.parseInt(noteLength);
                denominator = 1;

                //Handles case where notelength has both numerator and denominator
            } else if (ctx.notelength().NOTELSTRICT() != null) {
                noteLength = ctx.notelength().NOTELSTRICT().getText().replaceAll("\\s","");
                String[] parts = noteLength.split("/");
                numerator = Integer.parseInt(parts[0]);
                denominator = Integer.parseInt(parts[1]);
            }
        }

        /*
         * We have to add the information about pitch calculated in exitPitch to the Note.
         * We have to assign the accidental to the correct Accidental enum.
         */
        Pitch pitch = (Pitch) stack.pop();
        if (pitch.accidental == 0) {
            accidental = Accidental.NONE;
        } else if (pitch.accidental == 1) {
            accidental = Accidental.SHARP;
        } else if (pitch.accidental == 2) {
            accidental = Accidental.DOUBLESHARP;
        } else if (pitch.accidental == -1) {
            accidental = Accidental.FLAT;
        } else if (pitch.accidental == -2) {
            accidental = Accidental.DOUBLEFLAT;
        } else { // natural
            accidental = Accidental.NATURAL;
        }

        //Adding denominator to the list of denominators to calculate the LCM later.
        denominators.add(denominator);
        HashMap m = (HashMap) KeyMap.get(key);
        BaseNote basenote = BaseNote.valueOf((String) m1.get(pitch.value));

        /*
         * Taking into account key signature of the note. If the particular key signature has sharps/flats
         * we must check the HashMap everytime we process a note to see if any of the notes need to 
         * be assigned a sharp or flat.
         */

        if (m.containsKey(basenote) && accidental == Accidental.NONE) {
            if (m.get(basenote) == Accidental.SHARP) {
                accidental = Accidental.SHARP;
            } else if (m.get(basenote) == Accidental.FLAT) {
                accidental = Accidental.FLAT;
            }
        }

        //Pushing the newly created note to measureStack.
        measureStack.push(new Note(basenote, pitch.octave, new Fraction(
                numerator, denominator), accidental));
    }

    @Override
    public void exitPitch(ABCMusicParser.PitchContext ctx) {
        HashMap<String, Integer> m1 = new HashMap<String, Integer>();
        m1.put("A", 9);
        m1.put("B", 11);
        m1.put("C", 0);
        m1.put("D", 2);
        m1.put("E", 4);
        m1.put("F", 5);
        m1.put("G", 7);

        String baseNote = ctx.BASENOTE().getText();
        String upperCase = baseNote.toUpperCase();

        int octaves = 0;

        // if baseNote is lowercase, increase the octave
        if (!baseNote.equals(upperCase)) {
            octaves += 1;
        }

        // Pitch only contains a basenote
        if (ctx.getChildCount() == 1) {
            int accidental = 0; 

            // Checks if the current note should have an accidental applied
            if (notesPlayed.containsKey(upperCase+octaves)) {
                accidental = notesPlayed.get(upperCase+octaves);
            }           
            stack.push(new Pitch((int) m1.get(upperCase), accidental, octaves));
        }

        //Pich contains basenote and accidental or basenote and octaves to be parsed
        else if (ctx.getChildCount() == 2) {

            // if the pitch contains both a basenote and octaves
            int accidental = 0;

            if (ctx.ACCIDENTAL() == null) {
                String octave = ctx.OCTAVE().getText();
                // if octave should be shifted up
                octaves += octave.length();

                // if octave should be shifted down
                if (octave.charAt(0) == ',') {
                    octaves = -octaves;
                }

                // Checks if the current note should have an accidental applied
                if (notesPlayed.containsKey(upperCase+octaves)) {
                    accidental = notesPlayed.get(upperCase+octaves);
                }
                stack.push(new Pitch((int) m1.get(upperCase), accidental, octaves));
            }
            // if the pitch contains a basenote and an accidental
            else {

                String acc_text = ctx.ACCIDENTAL().getText();

                // add the note and accidental to notesPlayed
                if (acc_text.equals("_")) {
                    accidental = -1;
                    notesPlayed.put(upperCase+octaves, accidental);
                } else if (acc_text.equals("__")) {
                    accidental = -2;
                    notesPlayed.put(upperCase+octaves, accidental);
                } else if (acc_text.equals("^")) {
                    accidental = 1;
                    notesPlayed.put(upperCase+octaves, accidental);
                } else if (acc_text.equals("^^")) {
                    accidental = 2;
                    notesPlayed.put(upperCase+octaves, accidental);
                } else if (acc_text.equals("=")) {
                    accidental = 3;
                    notesPlayed.put(upperCase+octaves, accidental);
                } else {
                    accidental = 0;
                }

                stack.push(new Pitch((int) m1.get(upperCase), accidental,
                        octaves));
            }
        }
        // we have accidental, basenote and octaves
        else {
            String acc_text = ctx.ACCIDENTAL().getText();
            int accidental = 0;

            String octave = ctx.OCTAVE().getText();

            // if octave should be shifted up
            octaves += octave.length(); 

            // if octave should be shifted down
            if (octave.charAt(0) == ',') {
                octaves = -octaves;
            }

            // add current note and accidental to notesPlayed
            if (acc_text.equals("_")) {
                accidental = -1;
                notesPlayed.put(upperCase + octaves, accidental);
            } else if (acc_text.equals("__")) {
                accidental = -2;
                notesPlayed.put(upperCase + octaves, accidental);
            } else if (acc_text.equals("^")) {
                accidental = 1;
                notesPlayed.put(upperCase + octaves, accidental);
            } else if (acc_text.equals("^^")) {
                accidental = 2;
                notesPlayed.put(upperCase + octaves, accidental);
            } else if (acc_text.equals("=")) {
                accidental = 3;
                notesPlayed.put(upperCase + octaves, accidental);
            } else {
                accidental = 0;
            }

            //Pushes pitch to the stack, which will store all the pitches to be used later by exitNote.
            stack.push(new Pitch((int) m1.get(upperCase), accidental, octaves));
        }

    }

    @Override
    public void exitRest(ABCMusicParser.RestContext ctx) {

        String noteLength = "";
        int numerator = 1;
        int denominator = 1;

        //Obtaining the notelength of rest
        if (ctx.notelength() != null) {
            if (ctx.notelength().NOTELENGTH() != null) {
                noteLength = ctx.notelength().NOTELENGTH().getText().replaceAll("\\s","");
                String[] parts = noteLength.split("/");
                if (parts[0].length() > 1) {
                    numerator = Integer.parseInt(parts[0]);
                }
                denominator = Integer.parseInt(parts[1]);
            }
            //handles case where there is only numerator and '/'
            else if (ctx.notelength().NOTELENGTH2() != null) {
                noteLength = ctx.notelength().NOTELENGTH2().getText().replaceAll("\\s","");
                String[] parts = noteLength.split("/");
                if (parts.length > 0) {// there is a number in notelength2
                    numerator = Integer.parseInt(parts[0]);
                }
                int count = 0;
                for (int i = 0; i < noteLength.length(); i++) {
                    if (noteLength.charAt(i) == '/') {
                        count += 1;
                    }
                }
                denominator = count * 2;
            }
            //handles case where there is no '/'
            else if (ctx.notelength().DIGIT() != null) {
                noteLength = ctx.notelength().DIGIT().getText().replaceAll("\\s","");
                numerator = Integer.parseInt(noteLength);
                denominator = 1;

                //handles case where there is both numerator and denominator
            } else if (ctx.notelength().NOTELSTRICT() != null) {
                noteLength = ctx.notelength().NOTELSTRICT().getText().replaceAll("\\s","");
                String[] parts = noteLength.split("/");
                numerator = Integer.parseInt(parts[0]);
                denominator = Integer.parseInt(parts[1]);
            }
        }
        //Will store the denominator in "denominators" to determine LCM at the end.
        denominators.add(denominator);

        //Push the rest to measureStack (alongside other elements such as Note)
        measureStack.push(new Rest(new Fraction(numerator, denominator)));

    }


    @Override
    public void exitChord(ABCMusicParser.ChordContext ctx) {
        List<Note> notes = new ArrayList<Note>();
        // since two of the children of Chord will be [ and ], ChildCount-2 will
        // be the number of notes to pop off the stack
        for (int i = 2; i < ctx.getChildCount(); i++) {
            Note temporary = (Note) measureStack.pop();
            notes.add(temporary);
        }

        measureStack.push(new Chord(notes));
    }

    public int getLeastCommonDenominator() {
        // Calculates the least common denominator based on a list of the denominators of the notelengths of all the notes 
        LCM l = new LCM(denominators);
        int lcm = l.getLCM();
        return lcm;
    }

    public List getMusic(){
        //if there are voices: return the voiceList
        if(voicesSeen){
            return voiceList;
        } 
        //if there are no voices: return the cumulativeList
        else {
            return cumulativeList;
        }
    }


    public ArrayList<ArrayList<Object>> getParsedOutput() {
        //return the parsed music (with header and body) as an ArrayList (used for JUnit Test)
        ArrayList<ArrayList<Object>> finalparsed = new ArrayList<ArrayList<Object>>();
        finalparsed.add(headerList);
        finalparsed.add((ArrayList<Object>) cumulativeList);
        return finalparsed;

    }


    public List<Object> getVoices() {
        //return the voiceList (used for JUnit Test)
        return voiceList;
    }

    public boolean haveVoices() {
        //return if there are voices in the music
        return voicesSeen;
    }

    public Header getHeader() {
        //returns the header information of the music

        //default values for each field if header omits the information
        int index = 0;
        String title = "";
        String composer = "Unknown";
        Fraction meter = null;
        Fraction defaultLength = null;
        int tempo = 100;
        Fraction temponote = null;

        //get the values for each field in the header
        for (Object h : headerList) {
            if (h instanceof Title) {
                title = ((Title) h).getTitle();
            } else if (h instanceof Composer) {
                composer = ((Composer) h).getComposer();
            } else if (h instanceof Meter) {
                meter = ((Meter) h).getMeter();
            } else if (h instanceof Length) {
                defaultLength = ((Length) h).getLength();
            } else if (h instanceof Index) {
                index = ((Index) h).getIndex();
            } else if (h instanceof Tempo) {
                tempo = ((Tempo) h).getTempo();
                temponote = ((Tempo) h).getNoteLength();
            }
        }

        //if no notelength for the piece but there is a given meter, then assign the default notelength based on the meter value
        if (defaultLength == null && meter != null) {
            //if meter < 3/4, then notelength is 1/16
            if (meter.convertToDecimal() < 0.75) {
                defaultLength = new Fraction(1, 16);
            } 
            //else notelength is 1/8
            else {
                defaultLength = new Fraction(1, 8);
            }
        } 

        //if no notelength and no meter for the piece, then assign the meter to 4/4 and the notelength to 1/8 
        else if (defaultLength == null && meter == null) {
            meter = new Fraction(4, 4);
            defaultLength = new Fraction(1, 8);
        } 

        //if no meter is given, but a notelength for the piece is given, then assign the meter a default value of 4/4
        else if (meter == null && defaultLength != null) {
            meter = new Fraction(4, 4);
        }

        //if the notelength in the tempo is not provided, assign the default notelength tempo
        if (temponote == null) {
            temponote = defaultLength;
        }

        Header header = new Header(index, title, composer, defaultLength,
                tempo, temponote, meter);
        return header;
    }

}



