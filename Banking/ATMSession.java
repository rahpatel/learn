import java.io.*;
import java.security.*;
import java.net.*;


public class ATMSession implements Session {
    private Socket s;
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private BufferedReader textIn;

    private String ID;
    private ATMCard card;
    private PublicKey kBank;
    private PrivateKey kUser;
    private Crypto crypto;

    // This field is initialized during authentication
    private Key kSession;

    // Additional fields here
    private ParameterGenerator parameterGenerator;
    long currTimeStamp = 0;
    ATMSession(Socket s, String ID, ATMCard card, PublicKey kBank) {
	this.s = s;
	this.ID = ID;
	this.card = card;
	this.kBank = kBank;
	this.crypto = new Crypto();
	this.parameterGenerator = new ParameterGenerator();

	try {
	    textIn = new BufferedReader(new InputStreamReader(System.in));
	    OutputStream out =  s.getOutputStream();
	    this.os = new ObjectOutputStream(out);
	    InputStream in = s.getInputStream();
	    this.is = new ObjectInputStream(in);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    // This method authenticates the user and establishes a session key.
    public boolean authenticateUser() {
	System.out.println("Please enter your PIN: ");
	
	// First, the smartcard checks the user's pin to get the 
	// user's private key.
	try {
	    String pin = textIn.readLine();
	    kUser = card.getKey(pin);
	} catch (Exception e) {
	    return false;
	}

	try {
    
	//creating the initiation request
	//read the response message from the server  
    firstClientMessage();
    secondClientMessage();
    thirdClientMessage();
    verifySessionKey();
   
	
	return true;
    }catch(Exception ioe){
       ioe.printStackTrace();
       return false;
      }
    }
    

    public void firstClientMessage() throws Exception  {
        
        
      //creating the initiation request
        ATMMessage atmMessage = new ATMMessage();
        
        
        // set the challenge nonce and account id in the request
        
        atmMessage.setNonce(parameterGenerator.getNextNonce());
        atmMessage.setAcctNumber(card.getAcctNum());
        currTimeStamp = atmMessage.getTimeStamp();
        //encrypt the request object using public key of the bank server
        byte[] cipherText = crypto.encryptRSA(atmMessage,kBank);
        
        //send the encrypted object on the stream
        os.writeObject((Serializable)cipherText);
        System.out.println("\nSending AuthInit");
        System.out.println("\nWaiting for Challenge");
            
        
        }
        
    
    
public void secondClientMessage() throws Exception {
        
        
       byte [] cipherText;
        cipherText  = (byte[])is.readObject();
        ATMMessage atmMessage = (ATMMessage) crypto.decryptRSA(cipherText, kUser);

        if(!verifyMessage(atmMessage))error(" Nonce or time stamp does not match") ;
        //System.out.println("First Message received by the client from server with below details : ");
        //System.out.println("Response Nonce value : "+atmMessage.getResponseNonce());
        //System.out.println("Challenge Nonce value : "+atmMessage.getNonce());
        System.out.println("\nGot challenge");
        
        //look at the nonce that was received and compare it with the nonce that was sent [authenticity of B]
        int nonceSend = parameterGenerator.getPreviousNonce();
        int nonceReceived = atmMessage.getResponseNonce();
        int challengeNonce = atmMessage.getNonce();
        //if(nonceSend == nonceReceived) System.out.println("Server Authenticated");
        //System.out.println(" ");
        //prepare the second message in the protocol
        
        atmMessage = new ATMMessage();
        
        //set the Second Client Message 
        atmMessage.setResponseNonce(challengeNonce);
        currTimeStamp = atmMessage.getTimeStamp();
        
        System.out.println("\nSending Response");
        os.writeObject((Serializable)atmMessage);
        //System.out.println("Second Message sent by the client with below details : ");
        //System.out.println("Responce Nonce value : "+atmMessage.getResponseNonce());
        //System.out.println(" ");
            
        }
        
    
    
    
public void thirdClientMessage() throws Exception{
    
byte [] cipherText;
    cipherText  = (byte[])is.readObject();
    
    Key kRandom;
    kRandom = (Key) crypto.decryptRSA(cipherText, kUser);
    //System.out.println("Received Random key from client");
    //System.out.println("\n");
    cipherText  = (byte[])is.readObject();
    ATMMessage atmMessage = (ATMMessage) crypto.decryptRijndael(cipherText, kRandom);
    if(!(currTimeStamp < atmMessage.getTimeStamp())) error ("Time stamp does not match");
    cipherText =  atmMessage.getEncryptedSessionKey();
    kSession = (Key) crypto.decryptRSAPublicKey(cipherText, kBank);
    System.out.println("\nWaiting for session key");
    //System.out.println(" ");
    
    // encrypt message with session key and send to server
    atmMessage = new ATMMessage();
    atmMessage.setNonce(parameterGenerator.getNextNonce());
    //byte[] encryptRijndael(Serializable obj, Key k)
    currTimeStamp  = atmMessage.getTimeStamp();
    cipherText = crypto.encryptRijndael(atmMessage, kSession);
   
    os.writeObject((Serializable)cipherText);
    
    //System.out.println("Third Message sent by the client encrypted with session key with below details : ");
    //System.out.println("Nonce value : "+atmMessage.getNonce());
    //System.out.println(" ");
           
    
    
    
}
 
    public void verifySessionKey() throws Exception{
        
        byte[] cipherText;
        int nonceSend;
        int nonceReceived;
        cipherText  = (byte [])is.readObject();
      
        ATMMessage atmMessage = (ATMMessage) crypto.decryptRijndael(cipherText, kSession);
        
        if(!(currTimeStamp < atmMessage.getTimeStamp())) error ("Time stamp does not match");
        //     if(!verifyMessage(atmMessage))error(" Nonce or time stamp does not match") ;
        //System.out.println("Received third message from the server with below details ");
        //System.out.println("Response Nonce value : "+atmMessage.getResponseNonce());
        //System.out.println(" ");
        
        System.out.println("\nYou have been authenticated. Starting your session");
        
        nonceSend = parameterGenerator.getPreviousNonce();
        nonceReceived = atmMessage.getResponseNonce();
        
        //if((nonceSend + 1) == nonceReceived) System.out.println("Both using the same session key.");
        //System.out.println(" ");
        
    }
    
    void printMenu() {
	   System.out.println("*****************************");
	   System.out.println("(1) Deposit");
	   System.out.println("(2) Withdraw");
	   System.out.println("(3) Get Balance");
	   System.out.println("(4) Quit\n");
	   System.out.print  ("Please enter your selection: ");
    }

    int getSelection() {
	try {
	    String s = textIn.readLine();
	    int i = Integer.parseInt(s, 10);
	    return i;
	} catch (IOException e) {
	    return -1;
	} catch (NumberFormatException e) {
	    return -1;
	}
    }

    double getDouble() {
	try {
	    String s = textIn.readLine();
	    double d = Double.parseDouble(s);
	    return d;
	} catch (IOException e) {
	    return 0.0;
	} catch (NumberFormatException e) {
	    return 0.0;
	}
    }

    void endSession() {
    	sendMessage(TransactionOperation.EndSession);
    }

    void doDeposit() {

        sendMessage(TransactionOperation.Deposit);
        receiveReply();
    }

    void doWithdrawal() {
        sendMessage(TransactionOperation.Withdraw);
        receiveReply();
    }

    void doBalance() {
        sendMessage(TransactionOperation.ViewBalance);
        receiveReply();
   
    }

    
    void sendMessage(TransactionOperation transactionOperation){
    try {
    
    double amount = 0;
    if(transactionOperation != TransactionOperation.EndSession){
    	String value;
    	if(!(transactionOperation == TransactionOperation.ViewBalance)){
    		System.out.println("Enter "+transactionOperation+ " amount : ");
    		value = textIn.readLine();
    		amount = Double.parseDouble(value);
    	}
    }
    
    TransactionMessage transactionMessage = new TransactionMessage(MessageType.Request,transactionOperation,amount);
    SignedMessage signedMessage = new SignedMessage(transactionMessage, kUser, crypto);
    ATMSessionMessage atmSessionMessage = new ATMSessionMessage(signedMessage);
    atmSessionMessage.setNonce(parameterGenerator.getNextNonce());
    currTimeStamp = atmSessionMessage.getTimeStamp();
    byte[] cipherText = crypto.encryptRijndael(atmSessionMessage, kSession);
    os.writeObject((Serializable)cipherText);
    //System.out.println(transactionOperation + " Message sent by the client encrypted with session key with below details : ");
    //System.out.println("Nonce value : "+atmSessionMessage.getNonce());
    //System.out.println("\n\n\n");

    }

    catch(Exception e){
    
    }
    }
    
    void receiveReply() {
        try {
          int nonce;
          byte[] cipherText  = (byte [])is.readObject();
          ATMSessionMessage atmSessionMessage = (ATMSessionMessage) crypto.decryptRijndael(cipherText, kSession);
          if(!(currTimeStamp < atmSessionMessage.getTimeStamp())) error ("Time stamp does not match");
          TransactionMessage transactionMessage = atmSessionMessage.getTransactionMessage();
           TransactionOperation transactionOperation = transactionMessage.getOperation();
           double value = transactionMessage.getValue();
           nonce = atmSessionMessage.getNonce();
           
           if(transactionMessage.getMessageType() == MessageType.ResponseSuccess) {
               System.out.println(transactionOperation+ "  complete. ");
               System.out.println("Balance =  "+value);
             }
           else {
           System.out.println(transactionOperation+ "  failed. ");
           }
          
        }
        catch(Exception e){
        
        }
    }
    public boolean doTransaction() {
	printMenu();
	int x = getSelection();
	switch(x) {
	case 1 : doDeposit(); break;
	case 2 : doWithdrawal(); break;
	case 3 : doBalance(); break;
	case 4 : {endSession(); return false;}
	default: {System.out.println("Invalid choice.  Please try again.");}
	}
	return true;
    } 
    
        
        //verify nonce and timestamp
     public boolean verifyMessage(ATMMessage atmMessage){
         
         //verify nonce and timestamp
         if(!(this.currTimeStamp < atmMessage.getTimeStamp())) error ("Time stamp does not match");
         if(!(parameterGenerator.getPreviousNonce() == atmMessage.getResponseNonce())) error ("Nonce  does not match");
             return true;
        
     }

    public void error(String msg){
      throw new RuntimeException(msg);  
    }

    
}
