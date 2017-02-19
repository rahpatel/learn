package servlets;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Hashtable;
public class WebServer {
    static int portno;
    static String rootfolder;
    static String webdotxml;
    static Hashtable<String,MyHttpSession> sessions;
    static SessionIdGenerator sidgen;
    public static void main(String[] args) {
	//int portno;
    sessions = new 	Hashtable<String,MyHttpSession>();
	String rootfile;
	portno = Integer.parseInt(args[0]);
	//System.out.println("Portno:"+portno);
	rootfolder = args[1];
	//System.out.println("RootFile: "+rootfolder);
	webdotxml = args[2];
	sidgen = new SessionIdGenerator();
	 RequestQueue requestqueue = new RequestQueue();
	 Control control = new Control();
	 Producer producer = new Producer(requestqueue);
	 Consumer [] consumer = new Consumer[10];
	
	 for(int i=0;i<10;i++){
		consumer[i] = new Consumer(requestqueue,i);
	 }
	 
	 
	 try {
		
		 control.t.join();
		// System.out.println(" controller stopped");
		// System.out.println(" waiting for producer to stop.....");
			
		 producer.quit();
		 producer.serverSocketClose();
				
		 producer.t.join();
		 //System.out.println(" producer stopped");
						
	     //System.out.println(" waiting for consumers to stop.....");
					
		 for(int i=0;i<10;i++){
		     consumer[i].quit();		
			 consumer[i].t.interrupt();
		 }
		 
		 for(int i=0;i<10;i++){
			 (consumer[i]).t.join();
		 }
		
		 //System.out.println("  consumers stopped.....");
			
	    }catch(InterruptedException e){
		 //System.out.println(e);
	 }
	 
	}
	
	}
		


     
