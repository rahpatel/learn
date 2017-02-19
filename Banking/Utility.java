import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyPair;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utility {

	ArrayList<SignedAction> _signedActions = new ArrayList<SignedAction>();
	private BufferedReader _textIn;
	AccountDB _accountDB;
	Key kLog;//the shared key with which the log has been encrypted

	public static void main(String[] args) {
		Utility utility = new Utility();
		utility.run();
	}

	public void run() {
		init();
		try {
			_accountDB = AccountDB.load();
			readLog(BankServer.logFile);

			while (doView()) {
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void init() {
		_textIn = new BufferedReader(new InputStreamReader(System.in));

		Crypto crypto = new Crypto();

		try {
			// load the public-private key pair (RSA)
			KeyPair pair = (KeyPair) Disk.load(BankServer.keyPairFile);

			// load the encrypted shared key and decrypt it using the server's
			// private key
			byte[] logKeyCipher = (byte[]) Disk.load(BankServer.logKeyFile);
			this.kLog = (Key) crypto
					.decryptRSA(logKeyCipher, pair.getPrivate());
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (KeyException ke) {
			ke.printStackTrace();
		}

	}

	int getSelection() {
		try {
			//read from input
			String s = _textIn.readLine();
			int i = Integer.parseInt(s, 10);
			return i;
		} catch (IOException e) {
			return -1;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public boolean doView() {
		printMenu();
		int x = getSelection();

		switch (x) {
		case 1:
			doViewClientRecords();
			break;
		case 2: {
			return false;
		}
		default: {
			System.out.println("Invalid choice.  Please try again.");
		}
		}
		return true;
	}

	public void printMenu() {
		System.out.println("*****************************");
		System.out.println("(1) View Client transactions");
		System.out.println("(2) Quit");
		System.out.println("Enter your selection: ");
	}

	public void doViewClientRecords() {
		do {

			System.out
					.println("Enter Account # whose transactions you want to see:");

			int i = getSelection();
			if (i == -1) {//ask for input again if invalid
				System.out.println("Invalid Account #. Let's try again.");
				continue;
			}

			Account account = getAccount(i);
			if (null == account) {//ask for account number again if invalid account
				System.out.println("Invalid Account #. Let's try again.");
				continue;
			}

			//filter off the transactions related the client in question
			ArrayList<SignedAction> signedActions = getSignedActionsForClient(i);

			//print the transactions in a formatted way
			printTransactions(signedActions, account);

			break;

		} while (true);

	}

	public void printTransactions(ArrayList<SignedAction> signedActions,
			Account account) {
		System.out.println("\n=== Transactions for " + account.getOwner()
				+ " , Account #:" + account.getNumber() + " ===\n");

		int counter = 1;
		//print each signed action in a formatted way 
		for (SignedAction sa : signedActions) {
			System.out.println("Transaction " + counter++);
			System.out.println("==============");
			printTransaction(sa);
			System.out.println("\n\n");
		}

	}

	public void printTransaction(SignedAction signedAction) {
		TransactionMessage transaction = getTransactionRecord(signedAction);
		System.out.println("Operation:" + transaction.getOperation());

		Date date = new Date(signedAction.getTimeStamp());

		System.out.println("Timestamp:" + date.toString());
		System.out.println("Amount:" + transaction.getValue());
		System.out.println("\nNon-repudiation proof");
		System.out.println("---------------------");
		System.out.println("Original Msg: "
				+ signedAction.getSignedMessage().getMessageBytes());
		System.out.println("Client's Signature: "
				+ signedAction.getSignedMessage().getSignature());
	}

	public TransactionMessage getTransactionRecord(SignedAction signedAction) {
		try {
			//get each signed action, decrypt the transaction message embedded in it and return it
			//for printing
			SignedMessage signedMessage = signedAction.getSignedMessage();
			signedMessage.decryptMessage(this.kLog);
			return ((TransactionMessage) signedMessage.getObject());
			
		} catch (ClassNotFoundException cnfe) {
			return null;
		} catch (IOException ioe) {
			return null;
		}
	}

	public Account getAccount(int acctNumber) {
		try {
			Account account = _accountDB.getAccount("" + acctNumber);
			return account;
		} catch (AccountException ae) {
			return null;
		}
	}

	public ArrayList<SignedAction> getSignedActionsForClient(int acctNumber) {
		ArrayList<SignedAction> actionsArray = new ArrayList<SignedAction>();

		for (SignedAction signedAction : _signedActions) {
			int currActNum = Integer.parseInt(signedAction.getAcctNumber());
			if (currActNum == acctNumber) {
				actionsArray.add(signedAction);
			}
		}

		return actionsArray;
	}

	public void readLog(String fileName) throws Exception {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = null;
			while ((obj = ois.readObject()) != null) {
				_signedActions.add((SignedAction) obj);
			}
			ois.close();
			fis.close();

		} catch (EOFException ex) { // This exception will be caught when EOF is
			// reached
			// System.out.println("End of file reached.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("load failed: " + e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
