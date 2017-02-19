import java.net.*;
import java.io.*;
import java.security.*;
import java.util.Calendar;
import java.util.Date;





public class BankSession implements Session, Runnable {
    private Socket s;
    private ObjectOutputStream os;
    private ObjectInputStream is;

    private AccountDB accts;
    private Crypto crypto;
    private PrivateKey kPrivBank;
    private PublicKey  kPubBank;
    
    // These fields are initialized during authentication
    private PublicKey kPubClient;
    private Key kSession;
    private Account currAcct;
    private String atmID;
    long currTimeStamp = 0;
    private Key kLog;
 
    private ParameterGenerator parameterGenerator;
    // Add additional fields you need here

    BankSession(Socket s, AccountDB a, KeyPair p,Key keyLog)
	throws IOException
    {
       
	this.s = s;
	OutputStream out =  s.getOutputStream();
	this.os = new ObjectOutputStream(out);
	InputStream in = s.getInputStream();
	this.is = new ObjectInputStream(in);
	this.accts = a;
	this.kPrivBank = p.getPrivate();
	this.kPubBank = p.getPublic();
	this.crypto = new Crypto();
	
	this.parameterGenerator = new ParameterGenerator();
    this.kLog = keyLog;
    System.out.println("\nWaiting for first message");
    }

    public void run() {
	try {
	    if (authenticateUser()) {
		while (doTransaction()) {
		    // loop
		}
	    }
	    
	    is.close();
	    os.close();
	} 
	catch (Exception e) {
		System.out.println("Terminating session with Acct#: " + currAcct.getNumber());
		e.printStackTrace();
	}
    }
    
    // Interacts with an ATMclient to 
    // (1) Authenticate the user
    // (2) If the user is valid, establish session key and any
    //     additional information needed for the protocol.
    // (3) Maintain a log of whether the login attempt succeeded
    // (4) Returns true if the user authentication succeeds, false otherwise
    public boolean authenticateUser() {
    try {
    
    firstServerMessage();
    sendKeys();
    sessionKeyMsg();
     
    
    
 	return true;
    }
    
    catch(Exception ioe){
      System.out.println("Terminating session with Acct#: " + currAcct.getNumber());
      //ioe.printStackTrace();
      return false;
    }
    
    }

    public void firstServerMessage() throws Exception {
        
       
        String acctNumber;
        int clientChallengeNonce;
        
        // waiting for an incoming initiation request from the client
        //read the stream once request encountered
        
        byte [] cipherText  = (byte [])is.readObject();
        
        System.out.println("\nGot the first message");
        
        //read the stream, decrypt using the private key of the bank and create object
        //ClientMessage clientMessage;
        ATMMessage atmMessage = (ATMMessage) crypto.decryptRSA(cipherText, kPrivBank);
        //read the parameters in the initiation request
        acctNumber = atmMessage.getAcctNumber();
        clientChallengeNonce = atmMessage.getNonce();
        
        //look into the db and get account information of that client
        currAcct = accts.getAccount(acctNumber);    
        kPubClient = currAcct.getKey();
        
        //create a response with the account number and challenge nonce
        //System.out.println("Received first message from the client with below details : ");
        //System.out.println("Account number :  "+acctNumber);        
        //System.out.println("\n\n");
        //ServerMessage serverMessage = new ServerMessage();
        
       
        atmMessage = new ATMMessage();
        atmMessage.setNonce(parameterGenerator.getNextNonce());
        atmMessage.setResponseNonce(clientChallengeNonce);
        currTimeStamp = atmMessage.getTimeStamp();
        //encrypt using public key of the client, serialize and send on stream
        cipherText = crypto.encryptRSA(atmMessage,kPubClient);
        
        System.out.println("\nSending challenge");
        os.writeObject((Serializable)cipherText);
        System.out.println("\nWaiting for response");
        
        //System.out.println("First Message sent by the server with below details : "); 
        //System.out.println("Response Nonce Value : "+atmMessage.getResponseNonce());
        //System.out.println("Challenge Nonce Value : "+atmMessage.getNonce());
        //System.out.println("\n\n");
        
    }
    
    public void sendKeys() throws Exception{
  
    // waiting for the second message    
    ATMMessage atmMessage = (ATMMessage)is.readObject();
    System.out.println("\nGot response");
    
    //System.out.println("Received second message from the client with below details ");
    //System.out.println("Response Nonce value : "+atmMessage.getResponseNonce());
    
    if(!verifyMessage(atmMessage))error(" Nonce or time stamp does not match") ;
    kSession = crypto.makeRijndaelKey();
    
    Key kRandom = crypto.makeRijndaelKey();
    byte [] cipherText = crypto.encryptRSA(kRandom,kPubClient);
    os.writeObject((Serializable)cipherText);
    System.out.println("\nSending Accept and random key");
    
    
    cipherText = crypto.encryptRSAPrivateKey(kSession,kPrivBank);
    atmMessage = new ATMMessage(); 
    atmMessage.setEncryptedSessionKey(cipherText);
    cipherText = crypto.encryptRijndael(atmMessage,kRandom);
    currTimeStamp = System.currentTimeMillis();
    os.writeObject((Serializable)cipherText);
    //System.out.println("Session Key send to the client ");
    
    //SecondServerMessage secondServerMessage = new SecondServerMessage();
    //secondServerMessage.setSessionKey(kSession);
   
    //byte [] cipherText1;
    //cipherText = crypto.encryptRSAPrivateKey(kSession,kPrivBank);
    //cipherText1 = crypto.encryptRSA(cipherText,clientPublicKey);
    //os.writeObject((Serializable)cipherText1);
    
    //System.out.println("Second message send by the server which contains the session key. ");
    //System.out.println("\n\n\n");
   
        
    }
    
    
    public void sessionKeyMsg()throws Exception{
        
        byte[] cipherText  = (byte [])is.readObject();
        
        System.out.println("\nGot response");
        
        ATMMessage atmMessage = (ATMMessage) crypto.decryptRijndael(cipherText, kSession);
        if(!(currTimeStamp < atmMessage.getTimeStamp())) error ("Time stamp does not match");
        //if(!verifyMessage(atmMessage))error(" Nonce or time stamp does not match") ;
        //System.out.println("Received third message from the client with below details ");
        //System.out.println("Nonce value : "+atmMessage.getNonce());
      
        int nuance = atmMessage.getNonce();
        int responseNonce = nuance + 1;
        atmMessage = new ATMMessage();
       
        atmMessage.setResponseNonce(responseNonce);
        currTimeStamp = atmMessage.getTimeStamp();
        cipherText = crypto.encryptRijndael(atmMessage, kSession);
        
        System.out.println("\nStarting session");
        os.writeObject((Serializable)cipherText);
        
        //System.out.println("Third message send by the server with below details ");
       //System.out.println("Response Nonce value : "+atmMessage.getResponseNonce());
         
         
    }
    // Interacts with an ATMclient to 
    // (1) Perform a transaction 
    // (2) or end transactions if end-of-session message is received
    // (3) Maintain a log of the information exchanged with the client
    public boolean doTransaction()  {
        int nonce = -1;
        TransactionOperation transactionOperation = null;
        try {
             double balance;
             
             //read the stream for requests from client
             byte[] cipherText  = (byte [])is.readObject();
             
             // decrypt the ciphertext to obtain the signed message
             ATMSessionMessage atmSessionMessage = (ATMSessionMessage) crypto.decryptRijndael(cipherText, kSession);
             if(!(currTimeStamp < atmSessionMessage.getTimeStamp())) error ("Time stamp does not match");
                           
             //interpret the transaction operation in the request
             SignedMessage signedMessage = atmSessionMessage.getSignedMessage();
             
            
     		//verify signature
     		try {
     			if(!crypto.verify(signedMessage.msg, signedMessage.signature, currAcct.kPub))
     			{
     				System.out.println("Digital signature failed...");
     			}
     			System.out.println("Message signature valid...");
     		} catch (SignatureException e2) {
     			e2.printStackTrace();
     			return false;
     		} catch (KeyException e2) {
     			e2.printStackTrace();
     			return false;
     		}
     		
             TransactionMessage transactionMessage  =  (TransactionMessage)signedMessage.getObject();
             transactionOperation = transactionMessage.getOperation();
             
             //print the signed message
             System.out.println("\n" + (new Date()).toString());
             System.out.println(signedMessage.toString());
             
             //strip out the parameters embedded             
             nonce = atmSessionMessage.getNonce();
             double value = transactionMessage.getValue();
             
             //print the timestamp when the transaction takes place
             Date now = new Date();
             System.out.println("\n" + now.toString());
             
             //re-route the request to the appropriate handling function
             if(transactionOperation == TransactionOperation.Deposit)  {
            	 System.out.println("ACCT #:" + currAcct.getNumber() + " DEPOSIT: " + value);
                 currAcct.deposit(value);
             
                 System.out.println("\n" + (now).toString());
                 System.out.println("Deposit Complete. BALANCE:" + currAcct.getBalance());
             }
             else if(transactionOperation == TransactionOperation.Withdraw)  {
            	 System.out.println("ACCT #:" + currAcct.getNumber() + " WITHDRAW: " + value);
            	 currAcct.withdraw(value);
            	 
                 System.out.println("\n" + (now).toString());
                 System.out.println("Withdrawal Complete. BALANCE:" + currAcct.getBalance());
             }
             else if(transactionOperation == TransactionOperation.EndSession) 
            	 {
            	 	System.out.println("\nTerminating session with ACCT#: " + currAcct.getNumber());
            	 	return false;
            	 	
            	 }
                      
             accts.save();
             //create a successful reply Success and set the balance
             balance = currAcct.getBalance(); 
             transactionMessage = new TransactionMessage(MessageType.ResponseSuccess,transactionOperation,balance);
             atmSessionMessage = new ATMSessionMessage(transactionMessage);
              
             //set the nonce 
             atmSessionMessage.setNonce(nonce);
             currTimeStamp = atmSessionMessage.getTimeStamp();
             //encrypt the response and send it
             cipherText = crypto.encryptRijndael(atmSessionMessage, kSession);
             os.writeObject((Serializable)cipherText);
            
             //Logging.....
             //get the signed message and encrypt it
             
             signedMessage.encryptMessage(this.kLog);
             
             //encrypt the action
             //archive the signed message for logging and non-repudiation purposes             
             SignedAction signedAction = new SignedAction(currAcct.getNumber(), atmSessionMessage.getTimeStamp(), signedMessage);
             
             //flush out transaction record to the audit file
             BankServer.log.write(signedAction);
             
	         return true;
        }
        catch(Exception e){
        if(e instanceof TransException) replyFailure(transactionOperation,nonce);
           System.out.println("Terminating session with ACCT#: " + currAcct.getNumber());
        	//e.printStackTrace();
           
           return true;
        }
     }
    
    public void replyFailure(TransactionOperation transactionOperation,int nonce) {
        try {
        TransactionMessage transactionMessage = new TransactionMessage(MessageType.ResponseFailure,transactionOperation,-1);
        ATMSessionMessage atmSessionMessage = new ATMSessionMessage(transactionMessage);
        atmSessionMessage.setNonce(nonce);
        currTimeStamp = atmSessionMessage.getTimeStamp();     
   
        byte []cipherText = crypto.encryptRijndael(atmSessionMessage, kSession);
        os.writeObject((Serializable)cipherText);
        }
        catch(Exception e){
        	System.out.println("Terminating session with Acct#" + currAcct.getNumber());
        }        
    }
    
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