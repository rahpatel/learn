package edu.upenn.cis555.webserver;

import java.security.SecureRandom;

public class SessionIdGenerator {

	private   SecureRandom rand ;
    private  int nonce;
    
    
    public SessionIdGenerator(){
        rand = new SecureRandom();
    }
    public  int getNextId(){
        nonce = rand.nextInt(32);
        return nonce;
    }

    public  int getPreviousId(){
        return nonce;
    }

	
}
