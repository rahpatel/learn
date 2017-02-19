import java.security.SecureRandom;

public class ServerParameterGenerator {
    private  SecureRandom rand;
    private  int nonce;
    
    ServerParameterGenerator(){
        rand = new SecureRandom();
    }
    public  int getNextNonce(){
        nonce = rand.nextInt(32);
        return nonce;
    }

    public  int getNonce(){
        return nonce;
    }

}
