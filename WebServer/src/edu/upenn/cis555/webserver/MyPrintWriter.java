package edu.upenn.cis555.webserver;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

public class MyPrintWriter extends PrintWriter{
	StringBuffer  stringbuffer; 
	MyHttpServletResponse myresponse;
	
  MyPrintWriter(BufferedOutputStream output,boolean bool,MyHttpServletResponse response){
	  super(output,bool);
	  stringbuffer = new StringBuffer();  
      this.myresponse = response;
  }
  
  
  public void println(String string){
	 // System.out.println("Inside println of my printwriter");
	    
	  stringbuffer.append(string);
  }

  public void print(String string){
	 // System.out.println("Inside println of my printwriter");
	    
	  stringbuffer.append(string);
  }

  
  //overrides java.io.PrintWriter.flush

  public void flush(){
	  if(myresponse.isCommitted() == false){
	  writeStatus();
	//  System.out.println("after status");
	  writeHeaders();
	 // System.out.println("after status1");
	  myresponse.setCommited(true);
	//  System.out.println("after status2");
	  }
	  writeBuffer();
	 // System.out.println("after status3");
  }
  
  public void writeStatus(){
	  int statuscode = myresponse.getStatus();
	  String message = myresponse.getStatusMessage();
	  String statusline = "HTTP/1.0 "+statuscode+" "+message+"\r\n";
	  super.print(statusline);
  }
  public void writeBuffer(){
	 // System.out.println("Inside writebuffer");
	  String string = stringbuffer.toString();
	  super.print(string);
	  super.flush();
  }
  
  public void writeHeaders(){
	//  System.out.println(myresponse.getSession());
	  
	  if(myresponse.getSession() == null){
		  myresponse.removeSessionIdCookie();
	  }
	  
	  if(myresponse.getSession() != null){
	   if(!myresponse.getSession().isvalid()) {
		  
		  myresponse.removeSessionIdCookie();
	    }
	  }
	 // System.out.println("Session is not valid 2");
	  Enumeration e = myresponse.m_headers.keys();
	  String headers ="";
		while(e.hasMoreElements()){
			String name = (String)e.nextElement();
		    String value = (String)myresponse.m_headers.get(name);
		   // System.out.println("Header : "+"name :"+name+"value :"+value);
	        headers = headers+name+": "+value+"\r\n";
	      }
		    
	        super.print(headers);
	        super.print("\r\n");
	        super.flush();
	   }
  
 	  
  public void writeErrorMessage(){
	  int statuscode = myresponse.getStatus();
	  String message = myresponse.getStatusMessage();
	  String statusline = "HTTP/1.0 "+statuscode+" "+message+"\r\n";
	  String entityBody = "<HTML>" + "<HEAD><TITLE> Error </TITLE></HEAD>" + "<BODY> Error code "+statuscode+"</BODY></HTML>" + "\r\n";
	  super.print(statusline);
	  myresponse.setCommited(true);
	  super.print(entityBody);
      super.flush();
  }
 }

