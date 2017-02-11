package edu.upenn.cis555.webserver;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

 public class SocketRequest {
 
	  Socket socket;
	  InputStreamReader isr ;
	  OutputStream os;
	  BufferedReader br;
	  Hashtable<String,String> mimeType = new Hashtable<String,String>();
		
	  public SocketRequest(Socket socket){
		  this.socket = socket;
	  }

	  
	  public void processSocketRequest() {
				try {
				isr = new InputStreamReader(socket.getInputStream());
				//br = new BufferedReader(isr);
				os = socket.getOutputStream();
				Request request = new Request(isr);
				request.parse();
				
				// Create response object
				Response response = new Response(os);
				response.setRequest(request);
				
				response.process();
				
		    	os.close();
		    	socket.close();
		
				}catch(IOException e){
					System.out.println("IOEXception"+e);
					e.printStackTrace();
				}
				}
             
				
 }

 /****************unused
 
 
 
     
				
				if(url.startsWith("/servlet/")){
					ServletProcessor  processor = new ServletProcessor();
					processor.process(WebServer.webdotxml,method,url);
				}
				
				else {
					//staticProcess(method,url);
				}
				
 
 *********/