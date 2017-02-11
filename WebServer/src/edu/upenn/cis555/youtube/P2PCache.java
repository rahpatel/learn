package edu.upenn.cis555.youtube;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import edu.upenn.cis555.webserver.Consumer;


import rice.environment.Environment;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.pastry.NodeIdFactory;
import rice.pastry.leafset.LeafSet;
import rice.pastry.standard.RandomNodeIdFactory;


public class P2PCache {

	public static void main(String args[]){
		System.out.println("start");
		InetAddress localhost = null;
	
		int numOfNodes = Integer.parseInt(args[0]);
		String bootstrapIPAddress = args[1];
		int port = Integer.parseInt(args[2]);
		try {
			localhost = InetAddress.getLocalHost();
			System.out.println("Localhost"+localhost);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("localhost"+localhost);
		InetSocketAddress bootaddress = new InetSocketAddress(localhost, port);
		
		//	NodeFactory nodeFactory = new NodeFactory(port,bootaddress);
		 Environment env = new Environment();
		NodeFactory nodeFactory = new NodeFactory(port,bootaddress,env);
		
		// construct a new MyApp
		ArrayList<MyApp> apps = new ArrayList<MyApp>();
	
		int daemonPort = port + 1000;
		for(int i = 0 ;i< numOfNodes;i++){
			 MyApp app = new MyApp(nodeFactory,daemonPort); 
			apps.add(app);
		    daemonPort = daemonPort+1;
		}
		

	  	  try {
	    		env.getTimeSource().sleep(5000);
	    	} catch (InterruptedException e) {
	    		// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	}
		
		
		 String key ="britney";
		 String val = "popspears";
	
		// apps.get(0).sendMessage(MsgType.PUT,key,val);
			
		
		 
		 
		for(int i=0;i<numOfNodes;i++){
			try {
				(apps.get(i)).daemon.t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
		
		
		// app.sendMessage(randId,"Hi");
	//	 String key ="britney";
	//	 String val = "spears";
	//	 MyApp app2 = new MyApp(nodeFactory); 
		// app2.sendMessage(MsgType.PUT,key,val);    
		// randId = nidFactory.generateNodeId();
		 //System.out.println("RandId"+randId);
			
		 //app.sendMessage(randId,"Hi1");
	
		
	  	  try {
	    		env.getTimeSource().sleep(10000);
	    	} catch (InterruptedException e) {
	    		// TODO Auto-generated catch block
	    		e.printStackTrace();
	    	}
		
	    //	 app.sendMessage(MsgType.GET,key);
	 		
	    	
        // NodeHandle nh = node1.getLocalNodeHandle();
        //GET /busy HTTP/1.1

        // send the message directly to the node
        //app.routeMyMsgDirect(nh);   
        //app.routeMyMsg(node1.getId());
	    //app.sendMessage(node1.getId(),"Hi");
    	// wait 10 seconds
  	  try {
  		env.getTimeSource().sleep(10000);
  	} catch (InterruptedException e) {
  		// TODO Auto-generated catch block
  		e.printStackTrace();
  	}
  	
  	  	
  	
		System.out.println("over");
	}
	}



/******
 * 
 * 	MyApp app = new MyApp(node);
		MyApp app1 = new MyApp(node1);
	
 *	NodeIdFactory nidFactory = new RandomNodeIdFactory(env);
		 Id randId = nidFactory.generateNodeId();
		 System.out.println("RandId"+randId);
	 

Node node= nodeFactory.getNode();
		System.out.println("Node: "+node+"id: "+node.getId());
        
		Node node1= nodeFactory.getNode();
		System.out.println("Node1"+node1+"id: "+node1.getId());
    
		
		Node node2= nodeFactory.getNode();
		System.out.println("Node2"+node2+"id: "+node2.getId());
    
		/*
		node= nodeFactory.getNode();
		System.out.println("Node"+node+"id: "+node.getId());
        node= nodeFactory.getNode();
		System.out.println("Node"+node+"id: "+node.getId());
		
	

	// route 10 messages
	NodeIdFactory nidFactory = new RandomNodeIdFactory(env);
		for(int i = 0;i<2;i++){
			//pick a key at random
         Id randId = nidFactory.generateNodeId();
        System.out.println("RandId"+randId);
         app.routeMyMsg(randId);
         // wait a second
         try {
			env.getTimeSource().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
		
		// wait 10 seconds
		// wait 10 seconds
	  try {
		env.getTimeSource().sleep(10000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		  LeafSet leafSet = node.getLeafSet();

		 for (int i=-leafSet.ccwSize(); i<=leafSet.cwSize(); i++) {
		      if (i != 0) { // don't send to self
		        // select the item
		        NodeHandle nh = leafSet.get(i);
		        
		        // send the message directly to the node
		        app.routeMyMsgDirect(nh);   
		        
		        // wait a sec
		        env.getTimeSource().sleep(1000);
		      }

		    
		    }
		    
		    
		    try {
			env.getTimeSource().sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

*******/