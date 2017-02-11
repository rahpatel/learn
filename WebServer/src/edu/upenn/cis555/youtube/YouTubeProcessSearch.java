package edu.upenn.cis555.youtube;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;


public class YouTubeProcessSearch extends HttpServlet {
	
	InputStream _is;
	InputStreamReader _isr ;
	BufferedReader _br;
	OutputStream _os;
	MessageFactory _messageFactory;
	SOAPFactory _soapFactory;
	Socket _socket;
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
	
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String keyword = request.getParameter("keyword");
		System.out.println("Search is for the videos having keyword: "+keyword);
		
		ServletConfig config = getServletConfig();
		ServletContext context = config.getServletContext();
		String cacheServer = context.getInitParameter("cacheServer");
		
		int cacheServerPort = Integer.parseInt(context.getInitParameter("cacheServerPort"));
		openSocket(cacheServer,cacheServerPort);
		
		SOAPMessage requestSoapMessage = createSoapMessage(keyword); 
	    writeRequestSOAPMessage(requestSoapMessage);	 
		
		String responseContents = getResponseContents();
		
		SOAPMessage responseSOAPMessage = createResponseSOAPMessage(responseContents);
        ListVideos videos = readResult(responseSOAPMessage);
        //System.out.println("Search Result Videos ");
	    //System.out.println(videos);
        ArrayList<Video> alVideos = videos.getListVideos();
        out.println("<html>");
	   out.println("<body>");
       out.println("<P>");
	   // out.println("<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?>");
		//out.println("<?xml-stylesheet type=\"text/xsl\" href=\"videos.xsl\"?>");
		out.println("&lt;videos&gt;");
		out.println("<br/>");
		for (Video video : alVideos) {
			out.println("  &lt;video&gt;");
			out.println("<br/>");
			out.println("     &lt;title&gt;");
			out.println(video.getTile());
			out.println("     &lt;/title&gt;");
			out.println("<br/>");
			out.println("     &lt;url&gt;");
			out.println(video.getUrl());
			out.println("     &lt;/url&gt;");
			out.println("<br/>");
			out.println("  &lt;/video&gt;");
			out.println("<br/>");
			out.println("<br/>");
			}
		out.print("&lt;/videos&gt;");
		out.println("</P>");
		out.println("</body>");
			out.println("</html>");
		    
	
		 
	}
	
	public void openSocket(String __cacheServer,int __cacheServerPort){
		try{
			//System.out.println("Connecting to daemon at port "+__cacheServerPort+".......");
			_socket = new Socket(__cacheServer,__cacheServerPort);	
		 //	System.out.println("Connected to daemon  "+__cacheServerPort);
		    _os = _socket.getOutputStream();
		
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		 
	}
	
	public SOAPMessage createSoapMessage(String __keyword){
	    SOAPMessage soapMessage = null;
		try{
		 _messageFactory = MessageFactory.newInstance();
		soapMessage = _messageFactory.createMessage();
		SOAPHeader header = soapMessage.getSOAPHeader();
		SOAPBody body = soapMessage.getSOAPBody();
		//header.detachNode(); // to delete header;
		
		_soapFactory = SOAPFactory.newInstance();
		Name bodyName = _soapFactory.createName("GetVideosForKeyword","m","http://www.youtube.com");
		SOAPBodyElement bodyElement = body.addBodyElement(bodyName); 
		Name name = _soapFactory.createName("keyword");
		SOAPElement symbol = bodyElement.addChildElement(name);
		symbol.addTextNode(__keyword);
		soapMessage.saveChanges();
	
		}catch(SOAPException soapException){
		soapException.printStackTrace();
	}
	
	return soapMessage;
	}
	
	public void writeRequestSOAPMessage(SOAPMessage _requestSoapMessage){
		try{
		 String statusLine = "GET /videosForKeyword HTTP/1.1"+"\r\n";
		 writeString(statusLine);
		 int length = soapMessageLength(_requestSoapMessage);
		 printHeader(getDate(),"text/xml",length);
		 writeString("\r\n");
	     _requestSoapMessage.writeTo(_os);		
	     //System.out.print("\nRequest SOAP Message send to server:");
	     //_requestSoapMessage.writeTo(System.out);	
	     
	     //_os.flush();
		}catch(SOAPException soapException){
			soapException.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public int soapMessageLength(SOAPMessage soapMessage){
		int length = -1;
		try{
		ByteArrayOutputStream out1 = new ByteArrayOutputStream();
	    soapMessage.writeTo(out1);
	    String strSOAPMessage = out1.toString();
	    length = strSOAPMessage.length();
	   
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(SOAPException soapException){
			soapException.printStackTrace();
		}
		 return length;
			
	}
	
	public SOAPMessage createResponseSOAPMessage(String _responseContents){
	    SOAPMessage responseSOAPMessage = null;	
		try{
		    ByteArrayInputStream bas = new ByteArrayInputStream(_responseContents.getBytes());
	MimeHeaders mimeHeaders = new MimeHeaders();
	//mimeHeaders.addHeader("Content-Type","text/xml; charset=utf-8");
	mimeHeaders.addHeader("Date: ",getDate());
	responseSOAPMessage = _messageFactory.createMessage(mimeHeaders,bas);
	responseSOAPMessage.setProperty("WRITE_XML_DECLARATION",true);
	responseSOAPMessage.saveChanges();
	//System.out.print("\nResponse Soap message received from server:");
	//responseSOAPMessage.writeTo(System.out);
	 	
		}catch(SOAPException soapException){
			soapException.printStackTrace();
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		
    return responseSOAPMessage;
	}
	
	public String getResponseContents(){
		String contentstr = null;
		
		try{
		_is = _socket.getInputStream();
		 _isr = new InputStreamReader(_is);
		 _br = new BufferedReader(_isr);
		   
		String headerline ;
		String statusLine;
		statusLine = _br.readLine();
		//System.out.println(statusLine);
		int contentLength = 0;	
		
	while((headerline = _br.readLine()).length() != 0){
		int fcol = headerline.indexOf(':');
		//System.out.println("headerline :" +headerline);
	    String name1 = headerline.substring(0,fcol);
		String value  = headerline.substring(fcol+1);
		if(name1.equalsIgnoreCase("Content-Length")) contentLength = Integer.parseInt(value.trim());
	}

	char [] buff = new char[contentLength];
	_br.read(buff,0,contentLength);
	contentstr = new String(buff);
	//System.out.println("Content:"+contentstr);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		return  contentstr;
	}
	
	public void sendSearchResults(SOAPMessage __responseSOAPMessage){
		
	}
	
	public ListVideos readResult(SOAPMessage responseSOAPMessage){
		ListVideos videos = new ListVideos();
	    Video video;
	    try{
		SOAPBody soapBody = responseSOAPMessage.getSOAPBody();
		Name bodyName = _soapFactory.createName("GetVideosForKeyword","m","http://www.youtube.com");
		
		//Name name = _soapFactory.createName("keyword");
		Name nameValue = _soapFactory.createName("value");
		Iterator iterator = soapBody.getChildElements(bodyName);
		SOAPBodyElement bodyElementResponse = (SOAPBodyElement)iterator.next();
		Iterator iteratorValue = bodyElementResponse.getChildElements(nameValue);
		SOAPElement elementValue = (SOAPElement)iteratorValue.next();
		//keyword.detachNode();
		
		Name nameVideos = _soapFactory.createName("videos");
		Iterator iteratorVideos = elementValue.getChildElements(nameVideos);
		SOAPElement elementVideos = (SOAPElement)iteratorVideos.next();
	
		Name nameVideo = _soapFactory.createName("video");
		Iterator iteratorVideo = elementVideos.getChildElements(nameVideo);
		
		String strTitle;
		String strUrl;
		Name nameTitle = _soapFactory.createName("title");
		Name nameUrl = _soapFactory.createName("url");
		SOAPElement elementVideo;
		SOAPElement elementTitle;
		SOAPElement elementUrl;
		Iterator iteratorTitle;
		Iterator iteratorUrl;
		while(iteratorVideo.hasNext()) {
			video = new Video();
			elementVideo = (SOAPElement)iteratorVideo.next();
			iteratorTitle = elementVideo.getChildElements(nameTitle);
			elementTitle = (SOAPElement)iteratorTitle.next();
			iteratorUrl = elementVideo.getChildElements(nameUrl);
			elementUrl = (SOAPElement)iteratorUrl.next();
			strTitle = elementTitle.getValue();
			strUrl = elementUrl.getValue();
			video.setTile(strTitle);
			video.setUrl(strUrl);
			videos.addVideo(video);
			
		}
	    }catch(SOAPException soapException){
	    	soapException.printStackTrace();
	    }
	  
	return videos;
	}
	
	public  String getDate() {
		Date curDate = new Date();
		String date;
		TimeZone gmtTz = TimeZone.getTimeZone("GMT");
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy hh:mm:ss zzz");
		sdf.setTimeZone(gmtTz);
		date = sdf.format(curDate);
	    return date;
}

	
	public  void writeString(String s){
		  try {
			byte [] b = s.getBytes();
			_os.write(b);
		  }catch(IOException ioe){
			  System.out.println(ioe);
		  }
		
		}
	public  void printHeader(String date,String type,int length){
		  writeString("Date: "+date+"\r\n");
		  writeString("Content-Type: "+type+"\r\n");
		  writeString("Content-Length: "+length+"\r\n");
		  writeString("Conection: close"+"\r\n");
		   }


}


/*****
String message = keyword+"\r\n";
		 



*****/