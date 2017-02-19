import java.io.Serializable;
import java.security.SecureRandom;
//import java.util.Random;

public class ATMMessage implements Serializable, Message{
    public static final long serialVersionUID = 33100l;
    private int nonce;
    private long timeStamp;
    private String acctNumber;
    private int responseNonce;
    private byte[] _encrytedSessionKey;
  
    
    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setEncryptedSessionKey(byte[] encrytedSessionKey) {
        this._encrytedSessionKey = encrytedSessionKey;
    }

    public byte[] getEncryptedSessionKey() {
        return this._encrytedSessionKey;
    }

    
    public ATMMessage() {
        timeStamp = System.currentTimeMillis();
    }

    public int getNonce() {
       return nonce;
    }

    public void setNonce(int value) {
       this.nonce = value;
    }

    public void  setAcctNumber(String accountNumber) {
        this.acctNumber = accountNumber;
     }

   public String getAcctNumber() {
     return acctNumber;
   }
   
   
   public void setResponseNonce(int value) {
       this.responseNonce = value;
   }

   public int getResponseNonce() {
       return responseNonce;
   }


}
