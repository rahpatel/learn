package edu.upenn.cis555.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Properties;

public class Request {
    String method;
    String uri;
    Hashtable<String,String> headers;
    Hashtable<String,String> parameters;
    String querystring;
    String strservlet;
    InputStreamReader isr;
	BufferedReader input;
	
	Request(InputStreamReader isr){
		this.isr = isr;
		this.input = new BufferedReader(isr);
		headers = new Hashtable<String,String>();
		parameters = new Hashtable<String,String>();
		querystring = null;
		strservlet = null;
	}
	public void parse(){
		try {
		String headerline ;
		String requestline;
		requestline = input.readLine();
		parseuri(requestline);
		
		//headerline = input.readLine()).length() !=0
		//(headerline = input.readLine())!= null
	int c;
	while((headerline = input.readLine()).length() != 0){
		int fcol = headerline.indexOf(':');
		//System.out.println("length :" +headerline);
		
		String name = headerline.substring(0,fcol);
		String value  = headerline.substring(fcol+1);
		//System.out.println("Got header"+" , Name : "+name+" ,value : "+value);
	    if(headers.contains(name)){
	       String value1 = headers.get(name);
	       String newvalue = value1+","+value;
	   	   headers.put(name, newvalue);	
		       
	    }
	    else {
		headers.put(name, value);	
	    }
	}
	
	
	if(getMethod().equalsIgnoreCase("post")){
		
	storePostParameters();	
	}
	
		//System.out.println("over");
		}
		
		catch(Exception e){
		//	System.out.println("Exception"+e);
		}
	}
    public void parseuri(String requestline) {
    	int fsp = requestline.indexOf(' ');
		int  nsp = requestline.indexOf(' ',fsp+1);
		String method = requestline.substring(0,fsp);
		setmethod(method);
		String url = requestline.substring(fsp+1,nsp);
		//System.out.println("uri: "+url);
		
		seturi(url);
		int lbs = url.lastIndexOf('/');
		String m_url= url.substring(lbs+1);
		this.strservlet = m_url;
       if(method.equalsIgnoreCase("get")){
    //	   System.out.println("Its a get request"+m_url);
		     int fq = m_url.indexOf('?');
		     if(fq != -1){
		     this.strservlet = m_url.substring(0, fq);//fsp
             this.querystring = m_url.substring(fq+1);//fsp+1
      //       System.out.println("query string"+this.querystring);
		     processParameters(querystring);
		     }
		     }
    
    }
  
    public  String getQueryString(){
    	return querystring ;
    }
 
    public  String getServletString(){
    	return strservlet;
    }
 	public void  processParameters(String strParameters){
 		String [] strings = strParameters.split("&|=");
		String name,value,value1;
 		for(int j =0;j< strings.length-1;j+=2){
			name = strings[j];
			value  = strings[j+1];
			 value1 = (value.replaceAll("%2F","/")).replaceAll("%5B","[").
             replaceAll("%5B","[").replaceAll("%5D","]").
             replaceAll("%28","(").replaceAll("%29",")").
             replaceAll("%3D","=").replaceAll("%22","\"").
             replaceAll("%3A",":").replaceAll("%2E",".").
             replaceAll("%40","@").replaceAll("%2C",",").
             replaceAll("%20"," ").replaceAll("[+]"," ").
             replaceAll("%7E","~").replaceAll("%3B",";");
			 

		//	System.out.println("Value : "+value1);
			parameters.put(name, value1);	
		
		}
		}

 	
	public String geturi(){
	  	return uri;
	}
	
	public String getMethod(){
	  	return method;
	}
	
	
	public void seturi(String uri){
	this.uri = uri;	
	}

	public void setmethod(String method){
		this.method = method;	
		}
 
	public Hashtable<String,String> getParameters(){
		return parameters;
	}

	public Hashtable<String,String> getHeaders(){
       return headers;		
	}

	
	public void storePostParameters(){
		try{
		 int length = Integer.parseInt(headers.get("Content-Length").trim());
		//	System.out.println("length"+length);

		 char [] buff = new char[length];

			input.read(buff,0,length);
			String parametersstr = new String(buff);
			//System.out.println("Parameters"+parametersstr);
			String [] strings = parametersstr.split("&|=");
			
			String name,value,value1,value2;
	 		for(int j =0;j< strings.length-1;j+=2){
				name = strings[j];
				value  = strings[j+1];
	            value1 = (value.replaceAll("%2F","/")).replaceAll("%5B","[").
	                            replaceAll("%5B","[").replaceAll("%5D","]").
                                replaceAll("%28","(").replaceAll("%29",")").
                                replaceAll("%3D","=").replaceAll("%22","\"").
                                replaceAll("%3A",":").replaceAll("%2E",".").
                                replaceAll("%40","@").replaceAll("%2C",",").
                                replaceAll("%20"," ").replaceAll("[+]"," ").
	                            replaceAll("%7E","~").replaceAll("%3B",";");
				 
	           
	            System.out.println("Name :"+name+"  Value : "+value1);
				parameters.put(name, value1);	
			
			}
		}catch(IOException ioe)	{
			System.out.println("IOEXCeption"+ioe);
			ioe.printStackTrace();
		}
	 }


}


/*********Unused

while (true){
		headerline = input.readLine();
		
		System.out.println("Header Line : "+headerline);
	
	headerline = input.readLine();
		
	while(true){
			
			if(headerline == null) break;
				
				headerline = input.readLine();
			
			System.out.println("Header Line : "+headerline+" :length :"+headerline.length());
		}


	
		int c;
		
		while((c= input.read())!= -1){
			System.out.print((char)c);
		
		}
		
	//	System.out.println("Finish");
		System.out.println("Request Line : "+requestline);
		System.out.println("Method: "+method);
	
		System.out.println("name"+name);
			System.out.println("value"+value);
	
			System.out.println("Header Line : "+headerline);
			
			 try{
	            String[] tstrings = value1.split("[+]");
	            for (String string : tstrings) {
					System.out.println("String:"+string);
				}
	            
	            
	            }catch(Exception e){
	            	System.out.println(e);
	            }
	            
	            
	             // value2 = value1.replaceAll("+"," ");
	           // System.out.println("Value2"+value2);
	            int i = value1.indexOf("[+]");
	                      System.out.println("+ is at :"+i);
	            replaceAll("+"," ")replaceAll("%5B","[")
	            replaceAll("%5B","[").replaceAll("%5D","]").
	                           replaceAll("%28","(").replaceAll("%29",")").
	                           replaceAll("%3D","=").replaceAll("%22","\"");
	              
	                       
	                        *
	                        **************/
	          
	