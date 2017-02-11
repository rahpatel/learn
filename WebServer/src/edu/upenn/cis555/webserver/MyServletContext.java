package edu.upenn.cis555.webserver;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.*;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class MyServletContext implements ServletContext {
    private HashMap<String,Object> attributes;
	private HashMap<String,String> initParams;
	SessionIdGenerator sidg;
	String displayname;
	public MyServletContext(String displayname){
		attributes = new HashMap<String,Object>();
		initParams = new HashMap<String,String>();
	    this. sidg = new SessionIdGenerator();
	    this.displayname = displayname;
	    
	}
	@Override
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		Set<String> keys = attributes.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();}

	@Override
	public ServletContext getContext(String arg0) {
		return null;
	}

	@Override
	public String getInitParameter(String name) {
		return initParams.get(name);
	}

	@Override
	public Enumeration getInitParameterNames() {
		Set<String> keys = initParams.keySet();
		Vector<String> atts = new Vector<String>(keys);
		return atts.elements();
	}

	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public String getMimeType(String arg0) {
		return null;
	}

	@Override
	public int getMinorVersion() {
		return 4;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String arg0) {
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	@Override
	public URL getResource(String arg0) throws MalformedURLException {
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String arg0) {
		return null;
	}

	@Override
	public Set getResourcePaths(String arg0) {
		return null;
	}

	@Override
	public String getServerInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Servlet getServlet(String arg0) throws ServletException {
		return null; //deprecated
	}

	@Override
	public String getServletContextName() {
		return displayname;
	
	}

	@Override
	public Enumeration getServletNames() {
		return null;  //deprecated
 	}

	@Override
	public Enumeration getServlets() {
		return null;  //deprecated
	}

	@Override
	public void log(String arg0) {
		//do nothing 
	}

	@Override
	public void log(Exception arg0, String arg1) {

	}

	@Override
	public void log(String message, Throwable throwable) {
		System.err.println(message);
		throwable.printStackTrace(System.err);

	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
		
	}

	@Override
	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
		
	}

	void setInitParam(String name,String value){
		initParams.put(name, value);
	}
}
