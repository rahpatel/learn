package edu.upenn.cis555.webserver;

import java.util.*;

public class RequestQueue  {
   LinkedList<SocketRequest> queue;
	
   public RequestQueue(){
		queue = new LinkedList<SocketRequest>();
	}
	public  synchronized void enqueue(SocketRequest request){
		if(queue.isEmpty()) {
			queue.offer(request);
			notify();	
	//		System.out.println("added request and notified");
		 }
		else {
			queue.offer(request);
		}
	}
	
    public  synchronized SocketRequest dequeue() throws InterruptedException{
		if(queue.isEmpty()){
        		wait();
           }
    	    return queue.poll();
    	}
}





/************Junk
 
    	//	try {

        	//	}catch(InterruptedException ie){
        		//	System.out.println("Exception handled in deque");
            		//throw new InterruptedException() ;
        		   //}
   	    System.out.println(Thread.currentThread()+ " Out of wait : Got the lock ");
			System.out.println(Thread.currentThread()+"  added request ");
		System.out.println(Thread.currentThread()+ "Entered Enque");
 

*/