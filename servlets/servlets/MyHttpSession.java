package servlets;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class MyHttpSession implements HttpSession {
	
	MyHttpSession(String sessionId){
		this.sessionId = sessionId;
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
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getId() {
		
		return this.sessionId;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
        m_valid = false;
		String sessionId = getId();
		WebServer.sessions.remove(sessionId);
	    	
	
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
	   m_props.put(arg0, arg1);
	}

	@Override
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
       m_props.remove(arg0);
	}

	@Override
	public void removeValue(String arg0) {
		// TODO Auto-generated method stub
     m_props.remove(arg0);
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
     m_props.put(arg0, arg1);
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub

	}

	boolean isvalid(){
		return m_valid;
			}

	 public boolean sessionIdCookieExists(MyHttpServletRequest request){
	 		Cookie [] cookies = request.getCookies();
	 		for (Cookie cookie : cookies) {
				if(cookie.getName().equals("SessionId")){
					return true;
				}
			}
	 		return false;
		 		
	 	 }
	 	 
	 	 public Cookie getSessionIdCookie(MyHttpServletRequest request){
		 		Cookie [] cookies = request.getCookies();
		 		for (Cookie cookie : cookies) {
					if(cookie.getName().equals("SessionId")){
						return cookie;
					}
				}
		 		return null;
			 		
		 	 }
	 	 
	private Properties m_props = new Properties();
	private boolean m_valid = true;
    private String sessionId;	
    

}
