import java.security.*;
import java.io.*;
import java.net.*;

public class BankServer {
    
    public static final String pubKeyFile = "bank.pub";
    public static final String keyPairFile = "bank.key";
    public static final String logFile = "bank.log";
    public static final String logKeyFile = "log.shared";
    private static KeyPair pair = null;
    public static Log log = null;
    public static Key logSharedKey = null;
   
   public  static Crypto crypto;
    static {
	try {
	   crypto = new Crypto();
	   
	   //load the public-private key pair (RSA)
	   pair = (KeyPair)Disk.load(keyPairFile);
	   
	   //load and decrypt the encrypted shared key for the log
	   byte[] encryptedLogSharedKey = (byte[]) Disk.load(logKeyFile);
	   logSharedKey = (Key) crypto.decryptRSA(encryptedLogSharedKey, pair.getPrivate());
	   
	   //initialize the log file
	   log = new Log(logFile, logSharedKey);
	   
	} catch (IOException e) {
	    e.printStackTrace();
	    System.exit(1);
	}catch(KeyException ke){
		ke.printStackTrace();
		System.exit(1);
	}
    }


    public static void main(String[] args) {
	try {
	    AccountDB accts = AccountDB.load();
	    ServerSocket serverS = new ServerSocket(2198);
	    System.out.println("--------------------------");
	    System.out.println("  Bank Server is Running  ");
	    System.out.println("--------------------------");
	    while (true) {
		try {
		    Socket s = serverS.accept();
		    BankSession session = new BankSession(s, accts, pair,logSharedKey);
		    new Thread(session).start();
		} catch (IOException e) {
			System.exit(1);
		  //  log.write(e); // commented this need to uncomment later
		}
	    }
	} catch (IOException e) {
	    //e.printStackTrace();
	    System.exit(1);
	}
    }
}
