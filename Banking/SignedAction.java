import java.io.Serializable;

public class SignedAction implements Serializable{
    public static final long serialVersionUID = 33100l;
    String _acctNumber;
        
    public String getAcctNumber() {
        return _acctNumber;
    }
    public void setAcctNumber(String number) {
        _acctNumber = number;
    }
    SignedMessage _signedMessage;
    
    public SignedMessage getSignedMessage() {
        return _signedMessage;
    }
    public void setSignedMessage(SignedMessage message) {
        _signedMessage = message;
    }
    
    private long _timeStamp;
    public long getTimeStamp() {
		return _timeStamp;
	}
	public void setTimeStamp(long stamp) {
		_timeStamp = stamp;
	}
    
    public SignedAction(String acctId, long timeStamp, SignedMessage signedMessage)
    {
        this._acctNumber = acctId;
        this._timeStamp = timeStamp;
        this._signedMessage = signedMessage;        
    }
	
}
