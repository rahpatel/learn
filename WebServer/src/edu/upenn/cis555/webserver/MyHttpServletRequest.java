package edu.upenn.cis555.webserver;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class MyHttpServletRequest implements HttpServletRequest {
	MyHttpServletRequest(){
		
	}
	
    public void setResponse(MyHttpServletResponse response){
    	this.m_response = response;
    }
    //check in the hw.
	@Override
	public String getAuthType() {
		return "BASIC_AUTH";
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		//System.out.println("Inside getCookies()");
	    if(!m_headers.containsKey("Cookie")) return null;
		String strcookies = m_headers.getProperty("Cookie");
	    
	   // System.out.println("strcookies: "+strcookies);
		
	    String []strings = strcookies.split(",");
		int length = strings.length;
	//	System.out.println("length: "+length);
		Cookie [] cookies = new Cookie[length];
		
		for (int i =0;i<strings.length;i++){
			int eq = strings[i].indexOf('=');
			String name = strings[i].substring(0,eq).trim();
			String value = strings[i].substring(eq+1).trim();
		//	System.out.println("name: "+name+"value: "+value);
			
			cookies[i] = new Cookie(name,value);
		}
		return cookies;
	}

	@Override
	public long getDateHeader(String arg0) {
		
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHeader(String arg0) {
		return m_headers.getProperty(arg0);
	}

	@Override
	public Enumeration getHeaderNames() {
		return m_headers.propertyNames();
	}

	@Override
	public Enumeration getHeaders(String arg0) {
		return null;
	}

	@Override
	public int getIntHeader(String arg0) {
		return Integer.parseInt(arg0);
	}

	@Override
	public String getMethod() {
		return m_method;
	}

	@Override
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathTranslated() {
		return null;
	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return m_querystring;
	}

	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return getSession(true);
	}

	@Override
	public HttpSession getSession(boolean arg0) {
		// TODO Auto-generated method stub
		
		if(arg0){
			   if(!hasSession()){
				        String sessionId = getNewId();	
				        m_session = new MyHttpSession(sessionId);
					    m_response.setUpSession(m_session);
					       
					    Httpserver.sessions.put(sessionId, m_session);
					       
					    Cookie cookie = new Cookie("SessionId",sessionId);
					    cookie.setMaxAge(3600);
					       
					    this.m_response.addCookie(cookie);
					    return m_session;	             
			   }
		       }
			
	     else {
				if(!hasSession()){
					m_session = null;  
					 m_response.setUpSession(m_session);
					   
					return m_session;   
				}
              }
		
		
		m_session = Httpserver.sessions.get(getSessionIdFromCookie());
		 m_response.setUpSession(m_session);
		   
		return m_session;
	}

	public String getNewId(){
		return  Integer.toString(Httpserver.sidgen.getNextId());
		
		}
		
	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		return false;
	}

	@Override
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return m_props.get(arg0);
	}

	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return m_props.keys();
	}

	@Override
	public String getCharacterEncoding() {
		return m_characterencoding;
	}

	@Override
	public int getContentLength() {
		return Integer.parseInt(m_headers.getProperty("Content-Length").trim());
	}

	@Override
	public String getContentType() {
		return m_headers.getProperty("Content-Type");
		
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
return null;
		//return m_locale;
	}

	@Override
	public Enumeration getLocales() {
		return null;
	}

	@Override
	public String getParameter(String arg0) {
		// TODO Auto-generated method stub
		return m_params.getProperty(arg0);
	}

	@Override
	public Map getParameterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getParameterNames() {
		// TODO Auto-generated method stub
		return m_params.keys();
	}

	@Override
	public String[] getParameterValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProtocol() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	@Override
	public String getScheme() {
		return "http";
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub

	}
// how to ensure that it is not a valid encoding and throw exception
	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
	   this.m_characterencoding = arg0;
		
	}
	
	
	void setParameter(String key,String value){
		m_params.setProperty(key, value);
	}

	void setHeader(String key,String value){
		m_headers.setProperty(key, value);
	}

	
	void setMethod(String method){
		m_method = method;
	}
	
	void setQueryString(String querystr){
		m_querystring = querystr;
	}
	
	
	boolean hasSession(){
		return ((m_session != null) && m_session.isvalid());
		
			
	}
	
	public void setSession(){
		if(sessionIdCookieExists()){
           String sessionid  = getSessionIdFromCookie();
           
            if(Httpserver.sessions.containsKey(sessionid)){
            	m_session = Httpserver.sessions.get(sessionid);
                m_response.setUpSession(m_session);
                Cookie cookie = getSessionIdCookie();
        //        System.out.println("Max age of session Id cookie : "+cookie.getMaxAge()); // the server returns the max age as -1..so setting the max age to 3600
                cookie.setMaxAge(3600);
                m_response.addCookie(cookie);
            }
            else {  // the browser has sent us some cokkie..but the corresponding session does not exist
            	m_session = null;
         	    m_response.setUpSession(m_session);  // so we wont sent back the cokkie to browser.
         	    Cookie c =   getSessionIdCookie();
    			c.setMaxAge(0);  // we also need to remove the cookie from the browser so in response we set the same cookie that we 
    			m_response.addCookie(c);   //got in request with max age 0. so the browser will delete the cookie.
                  
         	    //             m_response.addCookie(getSessionIdCookie());	
            }
            
          }
       else{
    	   m_session = null;
    	    m_response.setUpSession(m_session);
             
       }
     }
	
	public String getSessionIdFromCookie(){
		Cookie [] cookies = getCookies();
		 String sessionId = null;
		 for (Cookie cookie : cookies) {
			           if(cookie.getName().equals("SessionId")){
				           sessionId = cookie.getValue();
		                   break;
			            }
		     }
		return sessionId;
	}
	 public boolean sessionIdCookieExists(){
	 		Cookie [] cookies = getCookies();
	 		if(cookies == null) return false;
	 		for (Cookie cookie : cookies) {
				if(cookie.getName().equals("SessionId")){
					return true;
				}
			}
	 		return false;
		 		
	 	 }
	 	 
	 	 public Cookie getSessionIdCookie(){
		 		Cookie [] cookies = getCookies();
		 		for (Cookie cookie : cookies) {
					if(cookie.getName().equals("SessionId")){
						return cookie;
					}
				}
		 		return null;
			 		
		 	 }
	private MyHttpServletResponse m_response;
	private MyHttpSession m_session = null;
	private Properties m_params= new Properties();
	private Properties m_props = new Properties();
	private Properties m_headers = new Properties();
	
	private String m_method;
	private String m_querystring;
	private String m_characterencoding = "ISO-8859-1";
	private String m_locale = null;
  
}


/****


m_session != null)
		Cookie [] cookies = getCookies();
		 boolean sessionexist = false;;
		 for (Cookie cookie : cookies) {
			if(cookie.getName().equals("SessionId")){
				sessionexist = true;
		        break;
			}
		 }
		return(sessionexist);
		
*******/