package servlets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.SocketException;
public class Producer implements Runnable {
    
	RequestQueue requestqueue;
	Thread t;
	volatile boolean quit ;
    volatile ServerSocket acceptSocket;
	
	Producer(RequestQueue requestqueue) {
		this.requestqueue = requestqueue;
		t = new Thread(this,"Producer");
		quit = false;
		t.start();
	}
	
	public void quit(){
	 quit = true;	
	}
	
	public void serverSocketClose(){
		try {
			acceptSocket.close();
		}
		catch(Exception e) {
			System.out.println("Closing socket exception"+e);
		}
	}
	public void run() {
		try {
			int port = WebServer.portno;
			acceptSocket = new ServerSocket(port);
			
			while(!quit){
				Socket s = acceptSocket.accept();
				SocketRequest request = new SocketRequest(s);
			    requestqueue.enqueue(request);
			    //	s.close();
			}
		  }catch(SocketException se){
	//		  System.out.println("Got socket exception from accept..Producer closing");
		  }
		catch(Exception e) {
		//	  System.out.println(e);
		  }
		
		}
		
}


/**************junk

			System.out.println("Accept   Client with address: " + acceptSocket.getLocalSocketAddress()+":" +acceptSocket.getLocalPort()+";"+acceptSocket.getInetAddress() + ": " + " is connected");
			
				System.out.println("Client with address: " + s.getLocalSocketAddress()+":" +s.getLocalPort()+";"+s.getInetAddress() + ": " + s.getPort() + " is connected");
				   
	System.out.println("Entered while loop");
				
System.out.println(Thread.currentThread()+"New Request Received");
		        
System.out.println(Thread.currentThread()+"Out of enque");
*/