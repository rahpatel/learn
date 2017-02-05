package tokenizer;

import static org.junit.Assert.*;
import org.junit.Test;
import junit.framework.TestCase;

public class TokenTest extends TestCase {
    Token name, eoi, eol;

    protected void setUp() throws Exception {
        name = new Token(TokenType.NAME, "name1");
        eoi = new Token(TokenType.EOI, "eoi");
        eol = new Token(TokenType.EOL, "eol");
    }

    @Test
    public final void testToken() {
        assertNotNull(name);

    }

    @Test
    public final void testGetType() {

        assertEquals(TokenType.NAME, name.getType());

    }

    @Test
    public final void testGetValue() {
        assertEquals("name1", name.getValue());
    }

    @Test
    public final void testEqualsObject() {
        Token eoi2 = new Token(TokenType.EOI, "eoi");
        assertTrue(eoi2.equals(eoi));
        assertFalse(eol.equals(eoi));
    }

}
