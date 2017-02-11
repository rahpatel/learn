package edu.upenn.cis555.webserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;


public class Response {
	Request request;
	OutputStream output;
	  Hashtable<String,String> mimeType = new Hashtable<String,String>();
	 	
		
	Response(OutputStream os ){
		this.output = os;
	
	}

	public void setRequest(Request request){
		this.request = request;
	}
	
	 public void formMimeType() {
		    mimeType.put("html","text/html");
			mimeType.put("css","text/css");
			mimeType.put("txt","text/plain");
			mimeType.put("xml","text/xml");
			mimeType.put("gif","image/gif");
			mimeType.put("jpg","image/jpeg");
			mimeType.put("png","image/png");
			mimeType.put("pdf","application/pdf");
			mimeType.put("exe","image/png");
			mimeType.put("exe","application/octect-stream");
			mimeType.put("zip","application/octect-stream");
			mimeType.put("mp3","audio/mpeg");
			
	  }
	
	 
	 	 public void process(){
			
	 			String method = request.getMethod();
				String url = request.geturi();
	        
	           String strservlet = request.getServletString();
				HttpServlet servlet = Httpserver.servlets.get(strservlet);
				
				if(servlet == null){
				//	System.out.println("Cannot find mapping for the servlet "+strservlet);
					//System.out.println("Considering as a static resource fetch "+strservlet);
					sendStaticResource();
				  } else {
				       processServlet(servlet);	
				   }
			  }
				
	 	 
	 	 public void processServlet(HttpServlet servlet) {
				
	 		       // System.out.println("Inside processServlet"); 
					String method = request.getMethod();
				
	 		         
	 		         String url = request.geturi();
			         MyHttpServletRequest myrequest = new MyHttpServletRequest();
				     MyHttpServletResponse myresponse = new MyHttpServletResponse(output);
		             myrequest.setResponse(myresponse);
				     assignHeaders(myrequest);
				     assignParameters(myrequest);
				     MyHttpSession session = null;
				     myrequest.setSession();
				    
				     myrequest.setMethod(method);
				     	myrequest.setQueryString(request.getQueryString());
				     //	System.out.println("Before service");
						try {
							servlet.service(myrequest, myresponse);
						} catch (ServletException e) {
							
							System.out.println("Servlet Exception in class Response"+e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							System.out.println("Servlet Exception in class Response"+e);
							
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							myresponse.getWriter().flush();
						} catch (IOException e) {
							System.out.println("IOException in class Response"+e);
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                //  System.out.println("Process servlet over");
				
					
				
	 	 }
	 
	 public void assignHeaders(MyHttpServletRequest myrequest){
	     Enumeration<String> e = request.headers.keys();
			while(e.hasMoreElements()){
				String name = (String)e.nextElement();
			    String value = request.headers.get(name);
			//	System.out.println("Name :"+name+" Value :"+value);
			    myrequest.setHeader(name,value);
			}
 
	 }
	 public void assignParameters(MyHttpServletRequest myrequest){
		 Enumeration<String> e = request.parameters.keys();
			while(e.hasMoreElements()){
				String name = (String)e.nextElement();
			    String value = request.parameters.get(name);
				myrequest.setParameter(name,value);
			}
	 	
	 }

	 
	 private static void usage(){
	//		System.out.println("usage: java TestHarness <path to web.xml>"+"[<GET|POST> <servlet?params>...]");
		}


public void sendStaticResource(){
	try {
    formMimeType();        
    String filepath;
	//System.out.println("Inside send static resource");
    String url = request.geturi();
    String method = request.getMethod();
    
	int ext = url.lastIndexOf('.');
		String extension = url.substring(ext+1);
		String type = mimeType.get(extension);
		
		String rootpath = Httpserver.rootfolder; 
		filepath = rootpath+url;
		
		File file = new File(filepath);
		
		int code;
		String message;
		
		if(file.exists()){
			//System.out.println("File exists");
		    String canonicalpath = file.getCanonicalPath();
		    String absolutepath = file.getAbsolutePath();
		  //  System.out.println("Canonicalpath : " + canonicalpath);  
		   // System.out.println("Absolutepath : " + absolutepath);  
		    if(canonicalpath.startsWith(rootpath)){ 
			     	if (file.isFile()){
		    	       code = 200;
				       message = "OK";
						
				       printStatusLine(code,message);
						
				       printHeader(getDate(),type,(int)file.length());
						
				       writeString("\r\n");
					
				    	  
				       byte [] bytearray = new byte[(int)file.length()];
					   FileInputStream fis = new FileInputStream(file);
					    
					   fis.read(bytearray);
					   output.write(bytearray);
			//	 System.out.println(Thread.currentThread()+"File send");
			         }
				  else {
					  code = 200;
					  message = "OK";
					  printStatusLine(code,message);
					  writeString("Date: "+getDate()+"\r\n");
					  writeString("\r\n");
					  String str1 = "<HTML>" + "<HEAD><TITLE> List of Directory </TITLE></HEAD>" + "<BODY> <p>Requested folder "+file.getName()+" contains the below mentioned files</p>";
					  writeString(str1);
					  File[] files = file.listFiles();
					  for(File fileobj : files){
						if(fileobj.isFile()){
							writeString("<p>  "+fileobj.getName()+" is a file </p>");
							}
						else { 
							writeString("<p>  "+fileobj.getName()+" is a directory </p>");
						}
					 }
					 writeString("</BODY></HTML>" + "\r\n");
				   }
		        }	
					else {
				 code = 403;
			     message="Forbidden";
			     printStatusLine(code,message);
				 writeString("\r\n");
				 String entityBody = "<HTML>" + "<HEAD><TITLE> Forbidden </TITLE></HEAD>" + "<BODY> Requested file or folder outside the root directory</BODY></HTML>" + "\r\n";
				 writeString(entityBody);
			  }
            }
				 	
			else {
			code = 404;
			message = "NOT FOUND";
            printStatusLine(code,message);
			writeString("\r\n");
			String entityBody = "<HTML>" + "<HEAD><TITLE> NOT FOUND </TITLE></HEAD>" + "<BODY> FILE/DIRECTORY Not Found </BODY></HTML>" + "\r\n";
			writeString(entityBody);
		}
	
	
//	System.out.println("Send report");
	}catch(IOException e){
		//	System.out.println(e);
	}	
	}

public boolean validate(String s){
	
	  return true;
}
public void writeString(String s){
		  try {
			byte [] b = s.getBytes();
			output.write(b);
		  }catch(IOException ioe){
		//	  System.out.println(ioe);
		  }
		
		}

public void printStatusLine(int code,String message){
   String statusLine = "HTTP/1.0 "+code+" "+message+"\r\n";
   writeString(statusLine);
		
}

public void printHeader(String date,String type,int length){
	  writeString("Date: "+date+"\r\n");
	  writeString("Content-Type: "+type+"\r\n");
	  writeString("Content-Length: "+length+"\r\n");
	  writeString("Conection: close"+"\r\n");
	   }

public String getDate() {
		Date curDate = new Date();
		String date;
		TimeZone gmtTz = TimeZone.getTimeZone("GMT");
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy hh:mm:ss zzz");
		sdf.setTimeZone(gmtTz);
		date = sdf.format(curDate);
	    return date;
}

}
