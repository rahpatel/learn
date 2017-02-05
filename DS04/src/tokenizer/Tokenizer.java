package tokenizer;

import java.io.*;

/**
 * Given a java.io.Reader,the Tokenizer reads a stream from it and returns a
 * series of tokens.
 * 
 * 
 * @author Rahul
 * @version February 18,2009
 */
public class Tokenizer {

    private StreamTokenizer streamTokenizer;
    private boolean         pastEndOfInput = false;
    private String[]        keywords       = { "abstract", "continue", "for",
            "new", "switch", "assert", "default", "goto", "package",
            "synchronized", "boolean", "do", "if", "private", "this", "break",
            "double", "implements", "protected", "throw", "byte", "else",
            "import", "public", "throws", "case", "enum", "instanceof",
            "return", "transient", "catch", "extends", "int", "short", "try",
            "char", "final", "interface", "static", "void", "class", "finally",
            "long", "strictfp", "volatile", "const", "float", "native",
            "super", "while"              };

    /**
     * Constructor for Tokenizer. Sets the string or file to be tokenized.
     * 
     * @param reader
     *            The input to be tokenized.
     */

    public Tokenizer(Reader reader) {
        streamTokenizer = new StreamTokenizer(reader);
        streamTokenizer.wordChars('_', '_');
        streamTokenizer.slashStarComments(true);
        streamTokenizer.slashSlashComments(true);
        streamTokenizer.ordinaryChar('/');
        streamTokenizer.ordinaryChar('.');
        streamTokenizer.ordinaryChar('-');
        streamTokenizer.eolIsSignificant(true);
        streamTokenizer.ordinaryChar('"');
        streamTokenizer.ordinaryChar('\'');

    }

    /**
     * Method to determine whether a word is a keyword.
     * 
     * @param word
     *            The word to be checked whether it is a keyword.
     * @return true if the word is a keyword.
     */

    private boolean isKeyword(String word) {
        for (String keyword : keywords)
            if (keyword.equals(word))
                return true;
        return false;
    }

    /**
     * Method to determine whether there are more tokens left in the input.
     * Returns <code>true</code> up till that the EOI token is returned. After
     * that, returns <code>false</code>
     * 
     * 
     * @return True if there are one or more tokens to be returned.
     */
    public boolean hasNext() {
        return !pastEndOfInput;
    }

    /**
     * Method to return the next token from the input(string or file) Returns
     * the next token.The last "valid" token returned will be an EOI token.
     * Further one or more calls will result in an ERROR token.
     * 
     * @return The next token.
     */

    public Token next() {

        if (pastEndOfInput) {
        return new Token(TokenType.ERROR, "Error");
        }

        try {
        streamTokenizer.nextToken();
        } catch (IOException ioe) {
        System.out.println("Error :" + ioe);

        }

        switch (streamTokenizer.ttype) {

            case StreamTokenizer.TT_WORD:
                if (isKeyword(streamTokenizer.sval))
                    return new Token(TokenType.KEYWORD, streamTokenizer.sval);
                else
                    return new Token(TokenType.NAME, streamTokenizer.sval);

            case StreamTokenizer.TT_NUMBER:
                return new Token(TokenType.NUMBER, streamTokenizer.nval + "");

            case StreamTokenizer.TT_EOL:
                return new Token(TokenType.EOL, "\n");

            case StreamTokenizer.TT_EOF:
                pastEndOfInput = true;
                return new Token(TokenType.EOI, "");

            default:
                return new Token(TokenType.SYMBOL, (char) streamTokenizer.ttype
                        + "");

        }

    }

    /**
     * Method to put back the extracted token. "backs up" one token, so that
     * whatever was returned from the most recent call to next() will be
     * returned again the next time next() is called; only one token is
     * remembered, so if you call backUp() multiple times, the second and
     * subsequent calls don't make any difference.
     * 
     */
    public void backup() {
        streamTokenizer.pushBack();
    }

}
