package tokenizer;

import java.io.*;
import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;

import org.junit.Test;

public class TokenizerTest extends TestCase {
    private Tokenizer tokenizer;

    protected void setUp() throws Exception {

    }

    @Test
    public final void testHasNext() {

        String teststr = "";
        StringReader stringreader = new StringReader(teststr);
        Tokenizer tokenizer = new Tokenizer(stringreader);
        assertTrue(tokenizer.hasNext());
        Token tok = tokenizer.next(); // eoi
        assertFalse(tokenizer.hasNext());

        teststr = "a";
        stringreader = new StringReader(teststr);
        tokenizer = new Tokenizer(stringreader);
        assertTrue(tokenizer.hasNext());
        tokenizer.next(); // a
        assertTrue(tokenizer.hasNext());
        tokenizer.next(); // eoi
        assertFalse(tokenizer.hasNext());

        String str = "rahul + 24 + while )+US_A";
        StringReader stringReader = new StringReader(str);
        tokenizer = new Tokenizer(stringReader);
        assertTrue(tokenizer.hasNext());
        assertTrue(tokenizer.hasNext());
        assertTrue(tokenizer.hasNext());
        assertTrue(tokenizer.hasNext());

        tokenizer.next(); // rahul

        assertTrue(tokenizer.hasNext());
        tokenizer.next(); // +

        assertTrue(tokenizer.hasNext());
        tokenizer.next(); // 24

        assertTrue(tokenizer.hasNext());
        tokenizer.next(); // +

        assertTrue(tokenizer.hasNext());
        tokenizer.next(); // while

        assertTrue(tokenizer.hasNext());
        tokenizer.next(); // )

        assertTrue(tokenizer.hasNext());
        tokenizer.next(); // +

        assertTrue(tokenizer.hasNext());
        tokenizer.next(); // US_A

        assertTrue(tokenizer.hasNext());
        Token eoi = tokenizer.next(); // eoi
        assertNotNull(eoi);
        assertEquals(TokenType.EOI, eoi.getType());
        assertEquals("", eoi.getValue());

        assertFalse(tokenizer.hasNext());
        Token error = tokenizer.next();
        assertNotNull(error);
        assertEquals(TokenType.ERROR, error.getType());
        assertEquals("Error", error.getValue());

    }

    @Test
    public final void testNext() {
        String testString = "for(int \" i =0;\" \\i_phone++ 'a' /* C++ style comments ignored */ 8.9 -4 -8.6 / \n rah .6 while )  // C style comments ignored";
        StringReader stringReader = new StringReader(testString);

        tokenizer = new Tokenizer(stringReader);
        Token expected;
        expected = new Token(TokenType.KEYWORD, "for");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "(");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.KEYWORD, "int");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "\"");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NAME, "i");
        assertEquals(expected, tokenizer.next());

        Token equalsToken = new Token(TokenType.SYMBOL, "=");
        assertEquals(equalsToken, tokenizer.next());

        expected = new Token(TokenType.NUMBER, "0.0");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, ";");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "\"");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "\\");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NAME, "i_phone");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "+");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "+");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "'");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NAME, "a");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "'");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NUMBER, "8.9");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "-");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NUMBER, "4.0");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "-");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NUMBER, "8.6");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "/");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.EOL, "\n");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NAME, "rah");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, ".");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NUMBER, "6.0");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.KEYWORD, "while");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, ")");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.EOI, "");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.ERROR, "Error");
        assertEquals(expected, tokenizer.next());

        assertEquals(expected, tokenizer.next());

        assertEquals(expected, tokenizer.next());

    }

    @Test
    public final void testBackup() {
        String strtest = "int x=1;j>0 k--";
        StringReader stringReader = new StringReader(strtest);

        tokenizer = new Tokenizer(stringReader);
        Token expected, expec;
        expected = new Token(TokenType.KEYWORD, "int");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NAME, "x");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "=");
        assertEquals(expected, tokenizer.next());

        tokenizer.backup();

        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NUMBER, "1.0");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, ";");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NAME, "j");
        assertEquals(expected, tokenizer.next());

        tokenizer.backup();
        tokenizer.backup();
        tokenizer.backup();

        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, ">");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NUMBER, "0.0");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.NAME, "k");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "-");
        assertEquals(expected, tokenizer.next());

        expected = new Token(TokenType.SYMBOL, "-");
        assertEquals(expected, tokenizer.next());

    }

}
