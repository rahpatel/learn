package edu.upenn.cis555.youtube;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TimeZone;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;

public class Worker implements Runnable{
  
	Thread t;
	volatile boolean quit ;
    Socket socket;
    InputStream is;
    InputStreamReader isr ;
    BufferedReader br;
	OutputStream os;
	  MyApp app;
	  MessageFactory _messageFactory;
	  SOAPFactory soapFactory;
    
	Worker(Socket s,MyApp app) {
		this.socket = s;
		this.app = app;
		t = new Thread(this,"Worker");
	//	quit = false;
		t.start();
		
	}


	@Override
	public void run() {
				openStreams();
				String requestContents = getRequestContents();
		       SOAPMessage requestSOAPMessage = createRequestSOAPMessage(requestContents);
		       String keyword = getKeyword(requestSOAPMessage);
		       System.out.println("KeywordExtracted:"+keyword);
		       ListVideos videos = searchVideos(keyword);
		       
		       SOAPMessage responseSOAPMessage = createResponseSOAPMessage(requestSOAPMessage,videos);
		sendResponseSOAPMessage(responseSOAPMessage);
		//closeStreams();	
			}
	
	public void openStreams(){
	try{
		is = socket.getInputStream();
		isr = new InputStreamReader(socket.getInputStream());
		br = new BufferedReader(isr);
		os = socket.getOutputStream();
	}catch(IOException ioe){
		ioe.printStackTrace();
	}
	}
	
	public ListVideos searchVideos(String keyword){
		YRequest yRequest = new YRequest();
		yRequest.setKeyword(keyword);
		
		yRequest.setValueSet(false);
		int msgId = app.msgIdGen.getNextId();
		app.yReqResTable.put(msgId,yRequest);
		app.sendMessage(MsgType.GET,keyword,msgId);
	 	
		YResponse yResponse = yRequest.getYResponse();
		String value = yResponse.getValue();
		ListVideos videos = yResponse.getListVideos();
		
		return videos;
		
	}
	
	public String getRequestContents(){
	
		String contentstr = null;
		try{
		//System.out.println("Worker started");
		Hashtable<String,String> headers;
		headers = new Hashtable<String,String>();
		
		String headerline ;
		String requestline;
		requestline = br.readLine();
		//System.out.println(requestline);
			int contentLength =0 ;
			
	   while((headerline = br.readLine()).length() != 0){
		int fcol = headerline.indexOf(':');
		//System.out.println("headerline :" +headerline);
	    String name = headerline.substring(0,fcol);
		String value  = headerline.substring(fcol+1);
		headers.put(name,value);
		if(name.equalsIgnoreCase("Content-Length")) contentLength = Integer.parseInt(value.trim());
	}

	char [] buff = new char[contentLength];

	
		br.read(buff,0,contentLength);
	contentstr = new String(buff);
	//System.out.println("Content:"+contentstr);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	
	return contentstr;
	}
	
	public SOAPMessage createRequestSOAPMessage(String _requestContent){
		SOAPMessage requestSOAPMessage =  null;
			try{
			    ByteArrayInputStream bas = new ByteArrayInputStream(_requestContent.getBytes("UTF-8"));
				MimeHeaders mimeHeaders = new MimeHeaders();
			mimeHeaders.addHeader("Date: ",getDate());
		    //System.out.println("Date: "+getDate());
			_messageFactory = MessageFactory.newInstance();
			requestSOAPMessage = _messageFactory.createMessage(mimeHeaders,bas);
			//System.out.print("\nRequest Soap mssage received from client:");
			//requestSOAPMessage.writeTo(System.out);
			}catch(SOAPException soapException){
				soapException.printStackTrace();
			}
			catch(IOException ioe)	{
				ioe.printStackTrace();
			}
			return requestSOAPMessage;
				
	}
	
	
	public SOAPMessage createResponseSOAPMessage(SOAPMessage requestSOAPMessage,ListVideos videos){
	SOAPMessage responseSOAPMessage = null;
	/*
	videos = new ListVideos();
	Video video1= new Video();
	video1.setTile("mickey");
	video1.setUrl("www.mickey.edu");
	videos.addVideo(video1);
	//System.out.println(videos.toString()); 
	video1= new Video();
		video1.setTile("donald");
		video1.setUrl("www.donald.edu");
		videos.addVideo(video1);
		video1= new Video();
		video1.setTile("baloo");
		video1.setUrl("www.baloo.edu");
		videos.addVideo(video1);
		
	*/	
		//System.out.println(videos.toString()); 
					
    

		
		try{
	    
	SOAPBody soapBody = requestSOAPMessage.getSOAPBody();
	Name bodyName = soapFactory.createName("GetVideosForKeyword","m","http://www.youtube.com");
	Name name = soapFactory.createName("keyword");
	Iterator iterator = soapBody.getChildElements(bodyName);
	SOAPBodyElement bodyElementResponse = (SOAPBodyElement)iterator.next();
	Iterator iterator1 = bodyElementResponse.getChildElements(name);
	SOAPElement keyword = (SOAPElement)iterator1.next();
	keyword.detachNode();
	Name name1 = soapFactory.createName("value");
	SOAPElement value = bodyElementResponse.addChildElement(name1);
	
	Name nameVideos = soapFactory.createName("videos");
	
	SOAPElement elementVideos = value.addChildElement(nameVideos);
	ArrayList<Video> alvideos = videos.getListVideos();
	String strTitle;
	String strUrl;
	Name nameVideo = soapFactory.createName("video");
	Name nameTitle = soapFactory.createName("title");
	Name nameUrl = soapFactory.createName("url");
	SOAPElement elementVideo;
	SOAPElement elementTitle;
	SOAPElement elementurl;
	for (Video video : alvideos) {
		strTitle = video.getTile();
		strUrl = video.getUrl();
		elementVideo = elementVideos.addChildElement(nameVideo);
		elementTitle = elementVideo.addChildElement(nameTitle);
		elementurl = elementVideo.addChildElement(nameUrl);
		elementTitle.addTextNode(strTitle);
		elementurl.addTextNode(strUrl);
		
	}
	//value.addTextNode("spears");
	requestSOAPMessage.saveChanges();
	}catch(SOAPException soapException){
	  soapException.printStackTrace();
	}	return requestSOAPMessage; // the changes are made to the request soap message . so the new message we get is the response message.
	
	}
	
	public void sendResponseSOAPMessage(SOAPMessage responseSOAPMessage){
		   
		try{
		String statusLine = "HTTP/1.1 OK 200"+"\r\n";
			writeString(statusLine);
			
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
		    responseSOAPMessage.writeTo(bout);
		    
		    String strMessage = bout.toString();
		    int length = strMessage.length();
		    //System.out.println("Messsagelength"+strMessage.length());
		    //String initial= "<?xml version=\"1.0\"?>"+"\r\n";
			
		    printHeader(getDate(),"text/xml; charset=utf-8",length);
			writeString("\r\n");
			
			
			responseSOAPMessage.writeTo(os);
			//System.out.print("\nResponse soap message send to client:");
			//responseSOAPMessage.writeTo(System.out);
			
		}catch(SOAPException soapException){
			soapException.printStackTrace();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
	}
	
	
	public String getKeyword(SOAPMessage requestSOAPMessage){
		String keyword = null;
		try{
		SOAPBody soapBody = requestSOAPMessage.getSOAPBody();
		 soapFactory = SOAPFactory.newInstance();
		Name bodyName = soapFactory.createName("GetVideosForKeyword","m","http://www.youtube.com");
		Name name = soapFactory.createName("keyword");
		Iterator iterator = soapBody.getChildElements(bodyName);
		SOAPBodyElement bodyElementResponse = (SOAPBodyElement)iterator.next();
		Iterator iterator1 = bodyElementResponse.getChildElements(name);
		SOAPElement keywordElement = (SOAPElement)iterator1.next();
		keyword = keywordElement.getValue();
		}catch(SOAPException soapException){
			soapException.printStackTrace();
		}
		return keyword;
	}
	public void closeStreams(){
	try{
		br.close();
		isr.close();
		is.close();
		os.close();
    	socket.close();
	   }catch(IOException ioe){
		   ioe.printStackTrace();
	   }
	}
	
	public String getDate() {
		Date curDate = new Date();
		String date;
		TimeZone gmtTz = TimeZone.getTimeZone("GMT");
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy hh:mm:ss zzz");
		sdf.setTimeZone(gmtTz);
		date = sdf.format(curDate);
	    return date;
}

	
	public void writeString(String s){
		  try {
			byte [] b = s.getBytes();
			os.write(b);
//			os.flush();
		  }catch(IOException ioe){
			  System.out.println(ioe);
		  }
		
		}
	public void printHeader(String date,String type,int length){
		  writeString("Date: "+date+"\r\n");
		  writeString("Content-Type: "+type+"\r\n");
		  writeString("Content-Length: "+length+"\r\n");
		  writeString("Conection: keep-alive"+"\r\n");
		   }
	
	
	
}

/********
app.yReqResTable.entries.get(yRequest.getRequestId());
			}
synchronized(yRequest){
			while(!yRequest.isValueSet()){
			   try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			}

********/