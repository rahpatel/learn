package edu.upenn.cis555.webserver;

import java.net.*;
import java.io.*;
import java.util.*;

public class Consumer implements Runnable {
	RequestQueue requestqueue;
	Thread t;
	int id;
	volatile boolean quit;
	public Consumer(RequestQueue requestqueue,int id){
	 
		   this.requestqueue = requestqueue;
		   this.id =id;
		   t = new Thread(this,"Consumer: "+id);
		   quit = false;
		   t.start();
	

	}
	public void quit() {
		quit = true; 
	}
	
	public void run() {
	  try {
		while(!quit){
		  SocketRequest request = requestqueue.dequeue();
	//	  System.out.println(Thread.currentThread()+" "+Thread.currentThread().getState()+"  Release lock and entering processRequest");
		  request.processSocketRequest();
		 }
	   }catch(InterruptedException e){
		//  System.out.println("Got exception from dequeue..thread  terminating...");
	    }
	  
	  }
	
   }


//***** Junk**************
/*
while((requestLine  = br.readLine()).length() != 0) {
	  System.out.println(requestLine);
	}
 */