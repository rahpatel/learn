import java.io.*;
import java.security.*;

public class SignedMessage implements Message {
    public static final long serialVersionUID = 331004;
    byte[] msg;
    byte[] signature;

    public SignedMessage(Serializable msg, PrivateKey kPriv, Crypto crypto) 
	throws SignatureException, KeyException, IOException
    {
	this.msg = crypto.objToBytes(msg);
	this.signature = crypto.sign(this.msg, kPriv);
    }
    
    public void encryptMessage(Key sharedKey){
    	Crypto crypto = new Crypto();
    	
    	//decrypt the message with the shared key and update the message to the decrypted byte array
    	try{
    		byte[] encryptedMsg = (byte[]) crypto.encryptRijndael(msg, sharedKey);
    		msg = encryptedMsg;
    	}catch(KeyException ke){
    		ke.printStackTrace();
    	}catch(IOException ioe){
    		ioe.printStackTrace();
    	}
    }
    
    public void decryptMessage(Key sharedKey){ 
    	Crypto crypto = new Crypto();
    	
    	//decrypt the message with the shared key and update the message to the decrypted byte array
    	try{
    		byte[] decryptedMsg = (byte[]) crypto.decryptRijndael(msg, sharedKey);
    		msg = decryptedMsg;
    	}catch(KeyException ke){
    		ke.printStackTrace();
    	}catch(IOException ioe){
    		ioe.printStackTrace();
    	}
    }
    
    public byte[] getMessageBytes(){ return msg; }
    public byte[] getSignature(){ return signature; }
    
    public Object getObject() throws IOException, ClassNotFoundException {
    	ByteArrayInputStream bis = new ByteArrayInputStream(msg);
    	ObjectInputStream ois = new ObjectInputStream(bis);
    	return ois.readObject();
    }
    
}
