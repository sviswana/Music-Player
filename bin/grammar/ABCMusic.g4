/**

 * This file is the grammar file used by ANTLR.

 *

 * In order to compile this file, navigate to this directory

 * (<src/grammar>) and run the following command:

 *

 * java -jar ../../antlr.jar ABCMusic.g4

 */


grammar ABCMusic;


/*

 * This puts 'package grammar;' at the top of the output Java files.

 * Do not change these lines unless you know what you're doing.

 */

@header {

package grammar;

}


/*

 * This adds code to the generated lexer and parser. This makes the lexer and

 * parser throw errors if they encounter invalid input. Do not change these

 * lines unless you know what you're doing.

 */

@members {

    // This method makes the lexer or parser stop running if it encounters

    // invalid input and throw a RuntimeException.

    public void reportErrorsAsExceptions() {

        removeErrorListeners();

        addErrorListener(new ExceptionThrowingErrorListener());

    }


    private static class ExceptionThrowingErrorListener extends BaseErrorListener {

        @Override

        public void syntaxError(Recognizer<?, ?> recognizer,

                Object offendingSymbol, int line, int charPositionInLine,

                String msg, RecognitionException e) {

            throw new RuntimeException(msg);

        }

    }

}


/*

 * These are the lexical rules. They define the tokens used by the lexer.

 */


REST : 'z';
DIGIT: [0-9]+;

BASENOTE : 'C' | 'D' | 'E' | 'F' | 'G' | 'A' | 'B'

        | 'c' | 'd' | 'e' | 'f' | 'g' | 'a' | 'b';

TITLE : 'T:' [' ']* [a-zA-Z\.\'0-9()?,' '-/~`!@#$%\^&\*\t\"<>,:;|\\+={}\[\]]*;
COMPOSER : 'C:' [' ']* [a-zA-Z\.\'0-9()?,' '-/~`!@#$%\^&\*\t\"<>,:;|\\+={}\[\]]*;
VOICE : 'V:' [' ']* [a-zA-Z\.\'0-9()?,' '-/~`!@#$%\^&\*\t\"<>,:;|\\+={}\[\]]*;
LYRIC : 'w:' [' ']* ([a-zA-Z\.\'0-9()?,' '-/~`!@#$%\^&\*\t\"<>,:;|\\+={}\[\]] | LYRICALELEMENT)* ;
COMMENT: '%' [' ']* [a-zA-Z\.\'0-9()?,' '-/~`!@#$%\^&\*\t\"<>,:;|\\+={}\[\]]*;
METER: 'M:' [' ']* NOTELSTRICT;
COMMONTIME : 'M:' [' ']* 'C';
CUTTIME : 'M:' [' ']* 'C|';
KEY : 'K:' [' ']* [A-G] KEYACCIDENTAL? MINOR?;

NOTELSTRICT : ([0-9]+'/'[0-9]+);

KEYACCIDENTAL  : '#' | 'b';
MINOR : 'm';
LINEFEED: '\r' | '\r'?'\n';
WHITESPACE : [ \t\r]+ -> skip ;
ACCIDENTAL : '=' |'^' | '^^' | '_' | '__';  

TEMPO : NOTELSTRICT '=' DIGIT;
OCTAVE : [''']+ | [',']+;

NOTELENGTH : [0-9]*'/'[0-9]+;
NOTELENGTH2 : [0-9]*'/'+;

TUPLET2 : LEFTP [' ']* '2';
TUPLET3 : LEFTP [' ']* '3';
TUPLET4 : LEFTP [' ']* '4';

LEFTP   : '(';
BAR      : '|';
THINTHIN : '||';
THINTHICK : '|]';
THICKTHIN : '[|';
REPEATL  : '|:';
REPEATR  : ':|';
NTHREPEAT : '[1' | '[2';
LYRICALELEMENT : ' '+ | '-' | '_' | '*' | '~' | '\-' | '|';

/*

 * These are the parser rules. They define the structures used by the parser.

 *

 * You should make sure you have one rule that describes the entire input.

 * This is the 'start rule'. The start rule should end with the special

 * predefined token EOF so that it describes the entire input. Below, we've made

 * 'line' the start rule.

 *

 * For more information, see

 * http://www.antlr.org/wiki/display/ANTLR4/Parser+Rules#ParserRules-StartRulesandEOF
 */


abc_tune : abc_header abc_music EOF;
abc_header : field_number comment* field_title other_fields* field_key;
field_number : 'X:' fieldnum end_of_line;
field_title : TITLE end_of_line;
other_fields : field_composer | field_default_length | field_meter | field_tempo | field_voice | comment;
field_composer: COMPOSER end_of_line;
field_default_length : 'L:' notelengthstrict end_of_line;
field_meter : (METER | COMMONTIME | CUTTIME) end_of_line;
field_tempo : 'Q:' TEMPO end_of_line;
field_voice : VOICE end_of_line;
field_key : KEY end_of_line;
fieldnum: DIGIT;
temp: DIGIT;
temponote: notelengthstrict;
notelengthstrict: NOTELSTRICT;

abc_music : abc_line+;
abc_line : element* LINEFEED (lyric LINEFEED)? | mid_tune_field | comment;
play_types : note | rest | chord | tuplet_element; 
element : play_types | barline | NTHREPEAT | WHITESPACE; 

note : pitch (notelength)?;
pitch : (ACCIDENTAL)? BASENOTE (OCTAVE)?;
rest : REST (notelength)?;

notelength: NOTELENGTH2 | NOTELENGTH | DIGIT | NOTELSTRICT;

tuplet_element2 : TUPLET2 note note;
tuplet_element3 : TUPLET3 note note note;
tuplet_element4 : TUPLET4 note note note note;
tuplet_element : tuplet_element2 | tuplet_element3 | tuplet_element4;

chord : '[' note+ ']' (notelength)?;

endbar  : THINTHIN | THINTHICK | THICKTHIN;

repeat  : REPEATL note+ REPEATR;
barline : BAR | endbar | repeatbar;
repeatbar  : REPEATL | REPEATR;

mid_tune_field : field_voice;
comment : COMMENT LINEFEED;
end_of_line : comment | LINEFEED;

lyric : LYRIC;

