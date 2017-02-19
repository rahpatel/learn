                                                                        Project 4
                                                                Secure Distributed Banking
                                                                        
                                                                     Aatir Abdul Rauf
                                                                       Rahul Patel
                                                                      Prashant Kumar                                                                        

    In this project, we implement a secure ditributed banking system. Our complete project is contained in the directory SecureBanking. The contents of this directory are as follows: 

Classes:
1) Account.java                                 
2) AccountDB.java              
3) AccountException.java       
4) ATMCard.java
5) ATMClient.java
6) ATMMessage.java
7) ATMSession.java
8) ATMSessionMessage.java
9) BankKeys.java
10)BankServer.java
11)BankSession.java
12)ClientRequest.java
13)Crypto.java
14)Disk.java
15)Keygen.java
16)Log.java
17)LogInterface.java
18)MakeAccounts.java
19)Message.java
20)MessageType.java
21)ParameterGenerator.java
22)ServerParameterGenerator.java
23)ServerResponse.java
24)Session.java
25)SignedAction.java
26)SignedMessage.java
27)TransactionMessage.java
28)TransactionOperation.java
29)TransException.java
30)Utillity.java
Report: Project4.doc
README: README.txt
jar file:bcprov-jdk15-134.java

The main classes used by us were the BankSession, ATM Session, BankServer and ATMClient. Besides, we developed a number of helper classes as follows:  
ATMMessage.java -           This class creates a template for the ATMMessage. It assigns a nonce, timestamp to the messages.
ATMSessionMessage.java -    This is the template for the transaction message and has both the signed and transaction message in plaintext. 
MessageType.java -          It says whether the messag is a Request, ResponseSuccess or ResponseFailure message.
ParameterGenerator.java -   It generates the nonce for authentication and transaction.
SignedAction.java -         It has the signed message, timestamp and account number which is logged in the bank's database.
TransactionMessage.java -   It gives the message type and transaction operation type. 
TransactionOperation.java - It says whether the transaction aoperation is ViewBalance, Withdraw, Deposit or EndSession.
Utillity.java -             This class provides the functionality to store and retrieve the logged transactions for future reference.



TUTORIAL SCRIPT:

A tutorial script (in a file called README) that the grader can follow to start your software and to convince himself that your system implements the required functionality. Expect the grader to spend at most 20 minutes on this task.

Step 1: export CLASSPATH="the path of bcprov-jdk15-134.jar":$CLASSPATH

Step 2: Compile files using javac *.java 

Step 2: Now run BanKeys.java using "java BankKeys". It generates bank.key, bank.pub and log.shared which store the baks public-private keypair, public key an shared key for encrypting the log respectively.

Step 3: Then run MakeAccounts.java using "java MakeAccounts". This generates acct.db to generate the various accounts with starting balance for each. Give the account owner, pin number and card name. It will generate a card which stores the users private key. The account numbers are given in sequential order. So the first person will have acctno 0, then 1 and so on. The acctNo is needed when you want to check the log. 

Step 4: Then run Bankserver.java using "java BankServer". Now the bank server is waiting for clients.

Step 5: Then run ATMClient.java on another mod using "java ATMClient ATM# BankServer". For example if the server is hosted at mod1.seas.upenn.edu and the ATM ID is 12, then run using "java ATMClient 12 mod1.seas.upenn.edu". You can create multiple clients and run the program for all the corner cases, to check its correctness. An invalid user input is handled by a warning at every step. A sample implementation is as shown be low.

Step 6: If the client has grievances or try to repudiate a transaction made on his behalf, an administrator could then pull up the records from the log file. To do so, we have created a utility which can be run using java Utility. It takes in the account number, if you want to check your transaction. Note that your account number is the number that was provided in the order in which you created an account in the bank. So the first person gets avvt no 0 and so on. For testing purposes, it will be better if you start from acct no "0" and proceed thereon. Note that if an account has not been created and you enter an account number, then you will get a warning.  

  
SAMPLE RUN:
A) Making accounts:

File: acct.db not found. Create it? (y/n): y
Creating new database
----- Creating Account -----
Account owner: prashant
Please enter a pin number: 1234
Please enter ATM card filename: prask.card
Starting Balance: 100
Creating account. Please wait.
File: acct.db not found. Create it? (y/n): n
Account owner: prashant
Please enter a pin number: 1234
Please enter ATM card filename: prask.card
Starting Balance: 100
Creating account. Please wait.

B) Example banksession:

--------------------------
  Bank Server is Running  
--------------------------

Waiting for first message

Got the first message

Sending challenge

Waiting for response

Got response

Sending Accept and random key

Got response

Starting session
Message signature valid...

Tue Apr 28 16:59:59 EDT 2009
banking.SignedMessage@1b64e6a

Tue Apr 28 16:59:59 EDT 2009
ACCT #:0 DEPOSIT: 100.0

Tue Apr 28 16:59:59 EDT 2009
Deposit Complete. BALANCE:509.0
Message signature valid...

Tue Apr 28 17:00:17 EDT 2009
banking.SignedMessage@9cb0f4

Tue Apr 28 17:00:17 EDT 2009
ACCT #:0 WITHDRAW: 1.0

Tue Apr 28 17:00:17 EDT 2009
Withdrawal Complete. BALANCE:508.0
Message signature valid...

Tue Apr 28 17:00:20 EDT 2009
banking.SignedMessage@e0cc23

Tue Apr 28 17:00:20 EDT 2009

Terminating session with ACCT#: 0



C) Example ATMsession:

*****************************
           ATM #50

Please insert card: prask.card
Please enter your PIN: 
1234

Sending AuthInit

Waiting for Challenge

Got challenge

Sending Response

Waiting for session key

You have been authenticated. Starting your session
*****************************
(1) Deposit
(2) Withdraw
(3) Get Balance
(4) Quit

Please enter your selection: 1
Enter Deposit amount : 
100
Deposit  complete. 
Balance =  509.0
*****************************
(1) Deposit
(2) Withdraw
(3) Get Balance
(4) Quit

Please enter your selection: 2
Enter Withdraw amount : 
1
Withdraw  complete. 
Balance =  508.0
*****************************
(1) Deposit
(2) Withdraw
(3) Get Balance
(4) Quit

Please enter your selection: 4
Goodbye!
*****************************
*****************************
           ATM #50

Please insert card: 



D) Example Uility:


java Utility
*****************************
(1) View Client transactions
(2) Quit
Enter your selection:
1
Enter Account # whose transactions you want to see:
1

=== Transactions for rahul , Account #:1 ===

Transaction 1
==============
Operation:Deposit
Timestamp:Tue Apr 28 15:27:51 EDT 2009
Amount:100.0

Non-repudiation proof
---------------------
Original Msg: [B@105d88a
Client's Signature: [B@cb6009
 

CONCLUSION: The distributed banking system is implemented and resists all active and passive wiretapper attacks.





First run BanKeys.java to generate bank.key, bank.pub and log.shared
Then run MakeAccounts.java to generate acct.db. Then generate the various accounts with starting balance for each. 

File: acct.db not found. Create it? (y/n): y
Creating new database
----- Creating Account -----
Account owner: prashant
Please enter a pin number: 1234
Please enter ATM card filename: prask.card
Starting Balance: 100
Creating account. Please wait.

For example:
account owner:prashant          ATM card filename: prask.card
account owner:aatir             ATM card filename: aatir.card
account owner:rahul             ATM card filename: rahul.card

Then run Bankserver.java

Then run ATMClient.java
*****************************
           ATM #50

Please insert card: prask.card                            
Please enter your PIN: 
1234

Sending AuthInit

Waiting for Challenge

Got challenge

Sending Response

Waiting for session key

You have been authenticated. Starting your session
*****************************
(1) Deposit
(2) Withdraw
(3) Get Balance
(4) Quit

Please enter your selection:  
      



