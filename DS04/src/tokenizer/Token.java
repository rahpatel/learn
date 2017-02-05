package tokenizer;

/**
 * <code>TokenType</code> defines the type of the token.
 * 
 * NAME -- begins with a letter or an underscore, consists of letters,
 * digits,and underscores KEYWORD -- same as a NAME, but in addition is a value
 * that occurs in a keywords array NUMBER -- one or more digits, possibly
 * including a decimal point, but not including a sign. SYMBOL -- any single
 * punctuation mark (not including whitespace) EOL -- an end of line character
 * (value is "\n") EOI -- the end of input (value is empty string, "") ERROR --
 * an error(trying to get another token after getting an EOI)
 * 
 * 
 * @author Rahul
 * @version February 18,2009
 */

enum TokenType {
    NAME, KEYWORD, NUMBER, SYMBOL, EOL, EOI, ERROR
}

/**
 * A Token has a type and a value. The value is a String containing the exact
 * characters that make up the Token. The type is a TokenType that tells what
 * kind of thing the Token represents--a variable, keyword, number, or symbol
 * (punctuation mark). End-of-lines and the (one) end of input are also returned
 * as tokens.
 * 
 * 
 * 
 *@author Rahul
 * @version February 18,2009
 */
public class Token {

    private TokenType type;
    private String    value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * A getter method for the token's value
     * 
     * @return Type of the Token.
     */

    public TokenType getType() {
        return type;
    }

    /**
     * A getter method for the tokens value
     * 
     * @return Value of the Token
     */
    public String getValue() {
        return value;
    }

    /**
     * A test of equality of tokens
     * 
     * @return True if the two Tokens have the same value and are of the same
     *         type.
     */

    @Override
    public boolean equals(Object object) {
        Token that = (Token) object;
        return this.type == that.type && this.value.equals(that.value);
    }

    /**
     * Method giving the value and type of the token.
     * 
     * @return The Token object in string format.
     */

    @Override
    public String toString() {
        return value + ":" + type;
    }

}
