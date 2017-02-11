package edu.upenn.cis555.webserver;

import java.io.*;
public class Control implements Runnable{
	Thread t;
	boolean quit;
	Control(){
		t= new Thread(this,"Control");
		quit = false;
		t.start();
	}
    
	public void quit(){
		quit = true;
		
	}
	public void run(){
	BufferedReader br = new BufferedReader(new InputStreamReader (System.in));	
	String str;
	while(!quit){
	try {
	  str = br.readLine();
	 // System.out.println("You typed : "+str);
	 // Thread.sleep(2*1000);
	  if(str.equalsIgnoreCase("quit")){
		// System.out.println("Closing the web server...");
		 quit();
	    }
	  }//catch(InterruptedException ie){
		  
	 // }
	  catch(IOException ioe){
		// System.out.println("ioe");
	    }
	 }
	}
}
