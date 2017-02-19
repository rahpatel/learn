package servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class DemoServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
try {
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>Simple Servlet</TITLE></HEAD><BODY>");
		out.println("<P>Hello Patel Rahul !!!!!</P>");
		out.println("</BODY></HTML>");
		
     }catch(Exception e){
	   System.out.println("Its actually here"+e);
      }
}
}


