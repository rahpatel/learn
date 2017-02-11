package edu.upenn.cis555.webserver;
import java.io.*;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ServletProcessor {
	
	  
	ServletProcessor(){
	
	}
		
		public  Handler parseWebdotxml(String webdotxml) {
			
			Handler h = new Handler();
			File file = new File(webdotxml);
			if(file.exists() == false){
				//System.out.println("error : cannot find "+file.getPath());
				System.exit(-1);
			}
			try {
				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
				parser.parse(file,h);
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
			return h;
		}
		
	public  MyServletContext createContext(Handler h){
		MyServletContext context  = new MyServletContext(h.m_displayname);
		
		for(String param:h.m_contextParams.keySet()){
			context.setInitParam(param,h.m_contextParams.get(param));
		}
	    
		for(String param:h.m_contextAttributes.keySet()){
			context.setAttribute(param,h.m_contextAttributes.get(param));
		}
	    
		return context;
	}
	
	public  HashMap<String,HttpServlet> createServlets(Handler h,MyServletContext context) throws ClassNotFoundException,IllegalAccessException,
	                                                     InstantiationException,ServletException{
		
		HashMap<String,HttpServlet> servlets = new HashMap<String,HttpServlet>();
		for(String servletName : h.m_servlets.keySet()){
			MyServletConfig config = new MyServletConfig(servletName,context);
			String className = h.m_servlets.get(servletName);
			Class servletClass = Class.forName(className);
			HttpServlet servlet = (HttpServlet)servletClass.newInstance();
			HashMap<String,String>servletParams = h.m_servletParams.get(servletName);
			if(servletParams != null){
				for(String param : servletParams.keySet()){
					config.setinitParams(param, servletParams.get(param));
				}
			}
			servlet.init(config);
			servlets.put(servletName, servlet);
		}
		return servlets;
		
	}
	
	public HashMap<String,HttpServlet>  parsexmlandgenerateServlets(){
		
		String webdotxml = Httpserver.webdotxml;
		Handler h = parseWebdotxml(webdotxml);
		h.m_contextAttributes.put("ServletContext",h.m_displayname);
		MyServletContext context = createContext(h);
		HashMap<String, HttpServlet> servlets = null;
		try {
			servlets = createServlets(h, context);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return servlets;
 }


		
}


/********unused
				//try {

				//}
			//catch(Exception e){
			//		System.out.println(" This :"+e);
			//	}
			//	System.out.println("  Rahul Patel");
			 * 
			//String webxmlpath = "C:/Users/admin/workspace/CIS555_HW1/web/WEB-INF/web.xml";
		//String webxmlpath = "C:\\Users\\admin\\workspace\\CIS555_HW1\\web\\WEB-INF\\web1.xml";
		//File file = new File(webxmlpath);
		//if(file.exists()){
		//	System.out.println("Does ");
		//}
		//else{
		//	System.out.println("Not");
		//}
		
		
		//if(args.length < 3 || args.length % 2 == 0 ){
		//	usage();
			//System.exit(-1);
			
		//}
	
		public  void process(String webdotxml)  {
		try {
		System.out.println("Inside process servlet");
		System.out.println(orgrequest.getMethod());
		System.out.println(orgrequest.geturi());
		String method = orgrequest.getMethod();
		String url = orgrequest.geturi();
		int fbs = url.indexOf('/');
		int  nbs = url.indexOf('/',fbs+1);
	//	int sp = url.indexOf(' ',nbs+1);
		String str = url.substring(nbs+1);
	System.out.println(str);
		
	System.out.println("Inside process servlet");
		
		Handler h = parseWebdotxml(webdotxml);
		MyServletContext context = createContext(h);
		HashMap<String,HttpServlet> servlets = createServlets(h, context);
		MyHttpSession session = null;
	
		
		//for(int i =1;i < args.length-1;i+=2){
			MyHttpServletRequest request = new MyHttpServletRequest(session);
			MyHttpServletResponse response = new MyHttpServletResponse(output);
			String [] strings = str.split("\\?|&|=");
			System.out.println(strings[0]);
			HttpServlet servlet = servlets.get(strings[0]);
			
			if(servlet == null){
				System.out.println("Cannot find mapping for the servlet "+strings[0]);
				System.exit(-1);
			}
			
			for(int j =1;j< strings.length-1;j+=2){
				request.setParameter(strings[j],strings[j+1]);
			}
			if(method.compareTo("GET") == 0 || method.compareTo("POST") == 0) {
				request.setMethod(method);
				servlet.service(request, response);

			}else {
				System.out.println("Expecting 'GET' or 'POST' not '"+method+"'");
				usage();
				System.exit(-1);
			}
			session = (MyHttpSession)request.getSession(false);
		//}
		}catch(Exception e){
			
		}

			 	private static void usage(){
		System.out.println("usage: java TestHarness <path to web.xml>"+"[<GET|POST> <servlet?params>...]");
	}

}catch(ClassNotFoundException e){
			System.out.println("Class not found exception "+e);
			e.printStackTrace();
		}catch(IllegalAccessException e){
			System.out.println("IllegalAccessException  "+e);
			e.printStackTrace();
		}catch(ServletException e){
			System.out.println("ServletException  "+e);
			e.printStackTrace();
			
		}catch(InstantiationException e){
			System.out.println("InstantiationException  "+e);
			e.printStackTrace();
			
		}


*********/