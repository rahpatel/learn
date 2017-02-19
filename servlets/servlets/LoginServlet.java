package servlets;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Enumeration;

public class LoginServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Enumeration e = request.getParameterNames();
		while(e.hasMoreElements()) {
			  String pname = (String)e.nextElement();
			  out.print(pname + " = ");
			  String pvalue = request.getParameter(pname);
			  out.println(pvalue);
			} 

		
		//ServletConfig config = getServletConfig();
		
//		ServletContext context = config.getServletContext();
		out.println("<HTML><HEAD><TITLE>Login Servlet</TITLE></HEAD><BODY>");
//		out.println("<P>The value of 'webmaster' is: " + 
	//			context.getInitParameter("webmaster") + "</P>");
	//	out.println("<P>The value of 'LoginParam' is: " + 
		//		config.getInitParameter("LoginParam") + "</P>");
		out.println("</BODY></HTML>");		
	}
}




/*
public class LoginServlet extends HttpServlet {

 public void doPost (ServletRequest request,ServletResponse response)
   throws ServletException, IOException {
//Get print writer.  
System.out.println("Rahul");
	 PrintWriter pw = response.getWriter();
   
//Get enumeration of parameter names
Enumeration e = request.getParameterNames();

//Dispaly parameter names and values.

while(e.hasMoreElements()) {
  String pname = (String)e.nextElement();
  pw.print(pname + " = ");
  String pvalue = request.getParameter(pname);
  pw.println(pvalue);
} 
   pw.close();
   
  }
 }
 
  */