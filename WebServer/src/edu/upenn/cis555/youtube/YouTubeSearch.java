package edu.upenn.cis555.youtube;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class YouTubeSearch extends HttpServlet{

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<html>");
		out.println("<body>");
		out.println("<center>");
		out.println("<form name='Search'");
		out.println("method='Post'");
		out.println("action= 'http://localhost:8092/youtubeprocesssearch'>");
		out.println("<input type=textbox name='keyword' size='25' value=''/>");
		out.println("<input type=submit value= 'Search' />");
		out.println("</form>");
		out.println("</center>");
		out.println("</body>");
		out.println("</html>");
	
		
	}
	
}
