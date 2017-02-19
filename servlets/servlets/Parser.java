/*
package servlets;
import java.io.*;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Parser {
	
	Parser(){
		
	}
	static  class Handler extends DefaultHandler {
	     private int m_state = 0;
	     private String m_servletName;
	     private String m_paramName;
		 HashMap<String,String > m_servlets = new HashMap<String,String>();
		 HashMap<String,String > m_contextParams = new HashMap<String,String>();
		 HashMap<String,HashMap<String,String>> m_servletParams = new HashMap<String,HashMap<String,String>>();
		 
		 
		 
		public void startElement(String uri,String localname,String qname,Attributes attributes){
			if(qname.compareTo("servlet-name") == 0){
				m_state = 1;
			}else if (qname.compareTo("servlet-class") == 0){
				m_state = 2;
			}else if(qname.compareTo("context-param") == 0){
				m_state = 3;
			}else if(qname.compareTo("init-param") == 0 ){
				m_state = 4;
			}else if(qname.compareTo("param-name") == 0){
				m_state = (m_state == 3) ? 10:20;
			}else if(qname.compareTo("param-value") == 0){
				m_state = (m_state == 10) ? 11 : 21;
			}
		}
		
		public void characters(char[] ch,int start,int length){
			String value = new String(ch,start,length);
			if(m_state ==1){
				m_servletName = value;
				m_state = 0;
			}else if(m_state == 2){
				m_servlets.put(m_servletName, value);
				m_state = 0;
			}else if (m_state == 10 || m_state == 20){
				m_paramName = value;
			}else if(m_state == 11){
				if(m_paramName == null){
					System.out.println("Context param value '"+value+"'without name ");
					System.exit(-1);
				}
				m_contextParams.put(m_paramName, value);
				m_paramName = null;
				m_state =0;
			}else if (m_state == 21){
				if(m_paramName == null){
					System.out.println("Servlet param value '"+value+"'without name ");
					System.exit(-1);
				}
		        HashMap<String,String> p = m_servletParams.get(m_servletName);
		        if(p == null){
		        	p = new HashMap<String,String>();
                    m_servletParams.put(m_servletName, p);		        	
		        }
		        p.put(m_paramName, value);
		        m_paramName = null;
		        m_state = 0;
			}
		}
		
	}
		
		private static  Handler parseWebdotxml(String webdotxml) {
			
			Handler h = new Handler();
			File file = new File(webdotxml);
			if(file.exists() == false){
				System.out.println("error : cannot find "+file.getPath());
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
		
	private static MyServletContext createContext(Handler h){
		MyServletContext context  = new MyServletContext();
		
		for(String param:h.m_contextParams.keySet()){
			context.setInitParam(param,h.m_contextParams.get(param));
		}
	
		return context;
	}
	
	private static HashMap<String,HttpServlet> createServlets(Handler h,MyServletContext context) throws Exception{
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
	
	private static void usage(){
		System.out.println("usage: java TestHarness <path to web.xml>"+"[<GET|POST> <servlet?params>...]");
	}

	public  void parser(String args[]) throws Exception {
		//String webxmlpath = "C:/Users/admin/workspace/CIS555_HW1/web/WEB-INF/web.xml";
		//String webxmlpath = "C:\\Users\\admin\\workspace\\CIS555_HW1\\web\\WEB-INF\\web1.xml";
		//File file = new File(webxmlpath);
		//if(file.exists()){
		//	System.out.println("Does ");
		//}
		//else{
		//	System.out.println("Not");
		//}
		
		
		if(args.length < 3 || args.length % 2 == 0 ){
			usage();
			System.exit(-1);
			
		}
		
		
		
		Handler h = parseWebdotxml(args[0]);
		MyServletContext context = createContext(h);
		HashMap<String,HttpServlet> servlets = createServlets(h, context);
		MyHttpSession session = null;
		
		for(int i =1;i < args.length-1;i+=2){
			MyHttpServletRequest request = new MyHttpServletRequest(session);
			MyHttpServletResponse response = null ;//new MyHttpServletResponse();
			String [] strings = args[i+1].split("\\?|&|=");
			HttpServlet servlet = servlets.get(strings[0]);
			
			if(servlet == null){
				System.out.println("Cannot find mapping for the servlet "+strings[0]);
				System.exit(-1);
			}
			
			for(int j =1;j< strings.length-1;j+=2){
				request.setParameter(strings[j],strings[j+1]);
			}
			if(args[i].compareTo("GET") == 0 || args[i].compareTo("POST") == 0) {
				request.setMethod(args[i]);
				servlet.service(request, response);

			}else {
				System.out.println("Expecting 'GET' or 'POST' not '"+args[i]+"'");
				usage();
				System.exit(-1);
			}
			session = (MyHttpSession)request.getSession(false);
		}
	}
}
*/

/********unused
				//try {

				//}
			//catch(Exception e){
			//		System.out.println(" This :"+e);
			//	}
			//	System.out.println("  Rahul Patel");


*********/