import java.security.SecureRandom;

public class ParameterGenerator {
    private   SecureRandom rand ;
    private  int nonce;
    
    
    public ParameterGenerator(){
        rand = new SecureRandom();
    }
    public  int getNextNonce(){
        nonce = rand.nextInt(32);
        return nonce;
    }

    public  int getPreviousNonce(){
        return nonce;
    }

}
