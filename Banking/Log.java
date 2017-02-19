import java.security.PublicKey;
import java.security.Key;
import java.io.IOException;
import java.io.Serializable;

public class Log {

    private String file;
    private Crypto crypto;
    private Key key;

    // You may add more state here.

    public Log(String file, Key key) 
    {
	try {
	    this.crypto = new Crypto();
	    this.file = file;
	    this.key = key;	    

	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    public void write(Serializable obj) {
	//System.out.println(obj.toString());

	try{
	    //log the account number with the signed message to the audit file
	    Disk.append(obj, file);
	}
	catch(IOException e){
	    System.out.println("Found IO Exception while writing to Log!");
	}
	
	
	// Add code to store to the log file
	// ...
    }

}
