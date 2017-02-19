package servlets;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class MyHttpServletResponse implements HttpServletResponse {
//Cookie cookie;
OutputStream os;
BufferedOutputStream output;
int m_statuscode;
String m_statusmessage;
MyPrintWriter m_printwriter;
Properties m_headers;
boolean m_committed;
int m_buffersize = 512;
String m_contenttype = "text/html";
MyHttpSession m_session;

MyHttpServletResponse(OutputStream os){
	this.os = os;
	output = new BufferedOutputStream(os);
	this.m_printwriter = new MyPrintWriter(output,true,this);
    this.m_headers = new Properties();
       m_committed = false;
       
}

	@Override
	public void addCookie(Cookie cookie) {
		String name = cookie.getName();
		String value = cookie.getValue();
		int maxage   = cookie.getMaxAge();
		Long time = System.currentTimeMillis() + maxage*1000;
		String strdate = getFormatedDate(time);
		System.out.println("Max age : "+ maxage);
		String domain = cookie.getDomain();
		String path = cookie.getPath();
		
		String str = name+"="+value+"; "+"expires="+strdate;
		addHeader("Set-Cookie",str);
		
	}
	
	public void removeSessionIdCookie(){
		String value = (String)m_headers.remove("Set-Cookie");
		int index = value.indexOf(',');
		if(index != -1){
			String [] strings = value.split(",");
		String newvalue="";
		for (String string : strings) {
		    int eq = string.indexOf('=');
		    String name = string.substring(0,eq).trim();
		    if (name.equals("SessionId")) continue;
		    newvalue = newvalue+","+string;
		}
		String newvalue1 = newvalue.substring(1);
		setHeader("Set-Cookie",newvalue1);
		}
		else {
			 int eq = value.indexOf('=');
			    String name = value.substring(0,eq).trim();
			    if (!name.equals("SessionId")) {
			    	
			    	setHeader("Set-Cookie",value);
					
			    }
			 
			
		     }
		}

	@Override
	public void addDateHeader(String name, long arg1) {
          String value = getFormatedDate(arg1);
		if(this.containsHeader(name)){
			String value1 = m_headers.getProperty(name);
			String value2 = value1+","+value;
			m_headers.setProperty(name, value2);
		}
			m_headers.setProperty(name, value);
		
		// TODO Auto-generated method stub

	}

	public String getFormatedDate(long value){
		Date date = new Date(value);
		String strdate;
		TimeZone gmtTz = TimeZone.getTimeZone("GMT");
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy hh:mm:ss zzz");
		sdf.setTimeZone(gmtTz);
		strdate = sdf.format(date);
	    return strdate;
	}
	
	@Override
	public void addHeader(String name, String value) {
	if(this.containsHeader(name)){
		String value1 = m_headers.getProperty(name);
		String value2 = value1+","+value;
		m_headers.setProperty(name, value2);
	}
		m_headers.setProperty(name, value);
	
	}

	@Override
	public void addIntHeader(String name, int value) {
		if(this.containsHeader(name)){
			String value1 = m_headers.getProperty(name);
			String value2 = value1+","+value;
			m_headers.setProperty(name, value2);
		}
			m_headers.setProperty(name,Integer.toString(value));
	
	}

	@Override
	public boolean containsHeader(String arg0) {
	  return m_headers.containsKey(arg0);
		
		
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public String encodeRedirectUrl(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public String encodeURL(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public String encodeUrl(String arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public void sendError(int arg0) throws IOException {
	    if(m_committed) {
	    	throw new IllegalStateException();
	    }
		this.m_statuscode = arg0;
	  	m_printwriter.writeErrorMessage();
		  
	    
	}

	@Override
	public void sendError(int statuscode, String statusmessage) throws IOException {
	    if(m_committed) {
	    	throw new IllegalStateException();
	    }
		this.m_statuscode = statuscode;
		this.m_statusmessage = statusmessage;
		
		m_printwriter.writeErrorMessage();
	
	
	}

	@Override
	public void sendRedirect(String arg0) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("[DEBUG] redirect to " + arg0 + " requested");
		System.out.println("[DEBUG] stack trace: ");
		Exception e = new Exception();
		StackTraceElement[] frames = e.getStackTrace();
		for (int i = 0; i < frames.length; i++) {
			System.out.print("[DEBUG]   ");
			System.out.println(frames[i].toString());
		}
	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		String value = getFormatedDate(arg1); //  to check this u might need to add system.currenttime milllis.
		m_headers.setProperty(arg0, value);
	
	}

	@Override
	public void setHeader(String arg0, String arg1) {
		m_headers.setProperty(arg0, arg1);
	
	}

	@Override
	public void setIntHeader(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStatus(int statuscode) {
		this.m_statuscode = statuscode;

	}

	@Override
	public void setStatus(int statuscode, String statusmessage) {
		this.m_statuscode = statuscode;
		this.m_statusmessage = statusmessage;

		// TODO Auto-generated method stub

	}

	@Override
	public void flushBuffer() throws IOException {
		m_printwriter.flush();
		
	}

	@Override
	public int getBufferSize() {
		return 512;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return "ISO-8859-1";
	}

	@Override
	public String getContentType() {
	
	//to do more	
		return m_contenttype;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return null;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
	
		return this.m_printwriter; 
		
	//new PrintWriter(System.out,true);
	}

	@Override
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return m_committed;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetBuffer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBufferSize(int size) {
		m_buffersize = size;
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacterEncoding(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContentLength(int value) {
		m_headers.setProperty("Content-Length", Integer.toString(value));
			
		
	}

	@Override
	public void setContentType(String value) {
		m_contenttype = value;
	m_headers.setProperty("Content-Type", m_contenttype);
	}

	@Override
	public void setLocale(Locale arg0) {
		// TODO Auto-generated method stub

	}

	public int  getStatus(){
		return m_statuscode;
	}
	
	public String  getStatusMessage(){
		return m_statusmessage;
	}

	public void setCommited(boolean value){
		this.m_committed = value;
	}

	public MyHttpSession getSession(){
		return m_session;
	}
	
	public void setUpSession(MyHttpSession session){
		m_session = session;
	}
	
	
}
