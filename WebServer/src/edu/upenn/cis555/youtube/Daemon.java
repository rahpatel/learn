package edu.upenn.cis555.youtube;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;



public class Daemon implements Runnable{

	//RequestQueue requestqueue;
	Thread t;
	int port;
    volatile boolean quit ;
    volatile ServerSocket acceptSocket;
    MyApp app;

    Daemon(int port,MyApp app) {
		this.port = port;
		this.app = app;
		t = new Thread(this,"Daemon");
		quit = false;
		t.start();
	}
	
	public void serverSocketClose(){
		
		try {
			acceptSocket.close();
		} catch (IOException e) {
		     e.printStackTrace();
		}
	}
	@Override
	public void run() {
		
		try {
			acceptSocket = new ServerSocket(port);
			Worker worker;
			while(!quit){
				System.out.println("Daemon at port "+port+" waiting for new connection");
				Socket s = acceptSocket.accept();
				System.out.println("Daemon at port"+port+"got connection ..starting a worker");
				worker  = new Worker(s,app);
			}
			
		
		//	worker.t.join();
		
		
		}catch(SocketException se){
			  se.printStackTrace();
		  }
		   catch(IOException e){
				  e.printStackTrace();
		   }
		   
      }
		   
// TODO Auto-generated method stub
		
	}

	
	

