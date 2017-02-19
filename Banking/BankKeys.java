import java.security.*;

// This program creates the bank.pub and bank.key files
public class BankKeys {
    private static Crypto crypto = new Crypto();

    public static void main(String[] args) {
	try {
	    //make RSA pair for bank server
		KeyPair kp = crypto.makeRSAKeyPair();
		Disk.save(kp, BankServer.keyPairFile);//save the RSA pair
	    Disk.save(kp.getPublic(), BankServer.pubKeyFile);//save the public key
		
		//make shared key for audit log and encrypt it with the public key
	    Key kLog = crypto.makeRijndaelKey();
	    byte[] logKeyCipher = crypto.encryptRSA(kLog, kp.getPublic());	    
	    Disk.save(logKeyCipher, BankServer.logKeyFile);//save the encrypted shared key for the log
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
