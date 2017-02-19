import java.io.Serializable;

public class ATMSessionMessage extends ATMMessage implements Serializable{
    private TransactionMessage _message;
    private SignedMessage _signedMessage;
    
    public ATMSessionMessage(SignedMessage signedMessage){
        super();
        this._signedMessage = signedMessage;
    }
    
    public ATMSessionMessage(TransactionMessage transactionMessage){
        super();
        this._message = transactionMessage;
    }
    
    
    public TransactionMessage getTransactionMessage(){
        return _message;
    }
   
    
    public SignedMessage getSignedMessage(){
        return _signedMessage;
    }
    
    public void setSignedMessage(SignedMessage signedMessage){
        this._signedMessage = signedMessage;
    }
}
