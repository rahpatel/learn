package servlets;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


  class Handler extends DefaultHandler {
    private int m_state = 0;
    private String m_servletName;
    private String m_paramName;
	 String m_displayname;
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
		}else if(qname.compareTo("display-name") == 0){
			m_state = 7;
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
		}else if(m_state == 7){
			m_displayname = value;
		}
	}
	
}

