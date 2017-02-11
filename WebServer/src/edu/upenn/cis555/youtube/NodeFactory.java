package edu.upenn.cis555.youtube;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import rice.p2p.commonapi.Node;
import rice.environment.Environment;
import rice.pastry.NodeHandle;
//import rice.pastry.NodeId;
import rice.pastry.Id;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

/**
 * A simple class for creating multiple Pastry nodes in the same
 * ring
 * 
 * 
 * @author Nick Taylor
 *
 */
public class NodeFactory {
	Environment env;
	NodeIdFactory nidFactory;
	SocketPastryNodeFactory factory;
	NodeHandle bootHandle =null;
	int createdCount = 0;
	int port;
	//InetSocketAddress bootaddress;
	NodeFactory(int port) {
		this(new Environment(), port);
	}	
	
	NodeFactory(int port, InetSocketAddress bootPort) {
		this(port);
		
		
		//this.bootaddress = bootPort;
		bootHandle = factory.getNodeHandle(bootPort);
		//System.out.println("boothandle:"+bootHandle);
		
	}
	
	NodeFactory(Environment env, int port) {
		this.env = env;
		this.port = port;
		nidFactory = new RandomNodeIdFactory(env);		
		try {
			factory = new SocketPastryNodeFactory(nidFactory, port, env);
		} catch (java.io.IOException ioe) {
			throw new RuntimeException(ioe.getMessage(), ioe);
		}
		
	}
	
	NodeFactory(int port, InetSocketAddress bootPort,Environment env) {
		this.env = env;
		this.port = port;
		nidFactory = new RandomNodeIdFactory(env);		
		try {
			factory = new SocketPastryNodeFactory(nidFactory, port, env);
		} catch (java.io.IOException ioe) {
			throw new RuntimeException(ioe.getMessage(), ioe);
		}
		bootHandle = factory.getNodeHandle(bootPort);
		
	}
	
	
	public Node getNode() {
		try {
			synchronized (this) {
		//		System.out.println("boothandle"+bootHandle);
				if (bootHandle == null && createdCount > 0) {
					InetAddress localhost = InetAddress.getLocalHost();
					InetSocketAddress bootaddress = new InetSocketAddress(localhost, port);
				    bootHandle = factory.getNodeHandle(bootaddress);
				    
				}
			}
			PastryNode node =  factory.newNode(bootHandle);
			
			while (! node.isReady()) {
				Thread.sleep(100);
			}
			synchronized (this) {
				++createdCount;
			}
			 if (node.joinFailed()) {
		          throw new IOException("Could not join the FreePastry ring.  Reason:"+node.joinFailedReason()); 
		        }
			 /*
			 if (node.isReady()) {
		          throw new IOException("Is not ready.  Reason:"); 
		        }
			 */
			return node;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public void shutdownNode(Node n) {
		((PastryNode) n).destroy();
		
	}

	public static Id getIdFromKey(String key){
		String myString = key;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] content = myString.getBytes();
		md.update(content);
		byte shaDigest[] = md.digest();
		Id id = Id.build(shaDigest);
		return id;
	}
	
	/*
	public NodeId getIdFromBytes(byte[] material) {
	
		return NodeId.buildNodeId(material);
	}
		*/
	
	
}
/***

		bootHandle = new InetSocketAddress(localhost, port);
			
**/