package edu.upenn.cis555.webserver;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class SessionServlet1 extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	//	System.out.println("Rahul Patelu");
		HttpSession session = request.getSession();
	//	System.out.println("rahul session"+session);
		session.setAttribute("TestAttribute", "12345");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Session Servlet 1</TITLE></HEAD><BODY>");
		out.println("<P>TestAttribute set to 12345.</P>");
		out.println("<P>Continue to <A HREF=\"session2\">Session Servlet 2</A>.</P>");
		out.println("</BODY></HTML>");		
	//    System.out.println("Do get of sessionservlet1 ends");
	}
}


