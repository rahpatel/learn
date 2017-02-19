import java.io.Serializable;

 public class TransactionMessage implements Serializable{
    public static final long serialVersionUID = 331004;
    private double _value = 0;
    TransactionOperation _operation;
    MessageType _messageType;
    
    public TransactionMessage(MessageType msgType, TransactionOperation transactionOp, double val)
    {
        this._messageType = msgType;
        this._operation = transactionOp;
        this._value = val;
    }
    
    
    
    public void setValue(int val) { this._value = val; } 
    public double getValue() { return this._value; }
    public TransactionOperation getOperation() { return this._operation; } 
    public MessageType getMessageType() { return this._messageType; }
}
