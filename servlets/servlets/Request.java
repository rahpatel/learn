package servlets;

import java.io.BufferedReader;
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
		System.out.println("length :" +headerline);
		
		String name = headerline.substring(0,fcol);
		String value  = headerline.substring(fcol+1);
	    headers.put(name, value);	
		}
	
	
	if(getMethod().equalsIgnoreCase("post")){
		
	storePostParameters();	
	}
	
		System.out.println("over");
		}
		
		catch(Exception e){
			System.out.println("Exception"+e);
		}
	}
    public void parseuri(String requestline) {
    	int fsp = requestline.indexOf(' ');
		int  nsp = requestline.indexOf(' ',fsp+1);
		String method = requestline.substring(0,fsp);
		setmethod(method);
		String url = requestline.substring(fsp+1,nsp);
		System.out.println("uri: "+url);
		
		seturi(url);
		int lbs = url.lastIndexOf('/');
		String m_url= url.substring(lbs+1);
		this.strservlet = m_url;
       if(method.equalsIgnoreCase("get")){
    	   System.out.println("Its a get request");
		     int fq = m_url.indexOf('?');
		     if(fq != -1){
		     this.strservlet = m_url.substring(0, fsp);
             this.querystring = m_url.substring(fsp+1);
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
		for(int j =0;j< strings.length-1;j+=2){
			parameters.put(strings[j],strings[j+1]);
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
			System.out.println("length"+length);

		 char [] buff = new char[length];

			input.read(buff,0,length);
			String parametersstr = new String(buff);
			System.out.println("Parameters"+parametersstr);
			String [] strings = parametersstr.split("&|=");
			
			for(int j =0;j< strings.length-1;j+=2){
				String name = strings[j];
				String value  = strings[j+1];
				System.out.println("Name  : " +strings[j]+"Value :"+strings[j+1]);
				parameters.put(name, value);	
				
			}
		}catch(Exception e){
			
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
	
******/