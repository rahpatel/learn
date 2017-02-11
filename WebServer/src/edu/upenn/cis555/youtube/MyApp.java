package edu.upenn.cis555.youtube;

import java.util.Hashtable;

import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;

public class MyApp implements Application {
    NodeFactory nodeFactory;
	Node node;
	Endpoint endpoint;
    Hashtable<String,ListVideos> cache;
	Daemon daemon;
	YReqResTable yReqResTable;
    MsgIdGen msgIdGen;
	public  MyApp(Node node) {
		this.cache = new Hashtable<String,ListVideos>();
		yReqResTable = new YReqResTable();
		msgIdGen = new MsgIdGen();
		this.node = node;
		this.endpoint = node.buildEndpoint( this,"myinstance");
		this.endpoint.register();
		
	}

	public MyApp(NodeFactory nodeFactory,int daemonPort) {
		this.nodeFactory = nodeFactory;
		this.cache = new Hashtable<String,ListVideos>();
		yReqResTable = new YReqResTable();
		msgIdGen = new MsgIdGen();
	
		this.node = nodeFactory.getNode();
		System.out.println("Node:"+node.getId());
		this.endpoint = node.buildEndpoint( this,"My Application");
		this.endpoint.register();
		daemon = new Daemon(daemonPort,this);
		
		//this.endpoint = node.registerApplication(this,"My Application");
	}

	
	public Node getNode() {
		return node;
	}
	
	void sendMessage(Id idToSendTo,String messageToSend){
		MyMsg msg = new MyMsg(node.getLocalNodeHandle());
		msg.setContent(messageToSend);
		System.out.println(this+"sending message:"+msg.getContent()+" :to "+idToSendTo);

		endpoint.route(idToSendTo,msg,null);
	}

	void sendMessage(MsgType msgType,String key,int msgId){
		MyMsg msg = new MyMsg(node.getLocalNodeHandle());
		msg.setMsgType(msgType);
		msg.setKey(key);
		msg.setMessageId(msgId);
		Id idToSendTo = NodeFactory.getIdFromKey(key);
		System.out.println(node.getId()+"sending '" +msg.getMsgType()+"' message:"+" Key:"+msg.getKey()+" :to "+idToSendTo);
		endpoint.route(idToSendTo,msg,null);
	}

	void sendMessage(MsgType msgType,String key,String value){
		MyMsg msg = new MyMsg(node.getLocalNodeHandle());
		msg.setMsgType(msgType);
		msg.setKey(key);
		msg.setValue(value);
		msg.setWantResponse(false);
		Id idToSendTo = NodeFactory.getIdFromKey(key);
		System.out.println(node.getId()+"sending '" +msg.getMsgType()+"' message:"+" Key:"+msg.getKey()+" Value:"+msg.getValue()+" :to "+idToSendTo);
		endpoint.route(idToSendTo,msg,null);
	}

	
	public void routeMyMsg(Id id){
		System.out.println(this+"sending to "+id);
		Message msg = new MyMsg(endpoint.getId(),id);
		endpoint.route(id,msg,null);
	}
	
	public void routeMyMsgDirect(NodeHandle nh){
		System.out.println(this+" sending direct to "+nh);
		Message msg = new MyMsg(endpoint.getId(), nh.getId());
		endpoint.route(null,msg,nh);
	}
	@Override
	public boolean forward(RouteMessage arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void update(rice.p2p.commonapi.NodeHandle arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
		@Override
	public void deliver(Id id, Message message) {
		// TODO Auto-generated method stub
	MyMsg rcvdMsg = (MyMsg) message;
    //	String rcvdMsgContents = rcvdMsg.getContent();
	String rcvdKey;// = rcvdMsg.getKey();
    //String rcvdValue;// = rcvdMsg.getValue();
    int rcvdMsgId;// = rcvdMsg.getMessageId();
 	MsgType msgType = rcvdMsg.getMsgType();
 	boolean wantResponse;// = rcvdMsg.isWantResponse();
 	ListVideos videos;
 	
 	
 	switch(msgType){
 	case PUT :  rcvdKey = rcvdMsg.getKey();
 	             videos = rcvdMsg.getListVideos();
 	            	 //value =rcvdMsg.getValue();
 		        cache.put(rcvdKey,videos);
 		       // System.out.println(node.getId()+" received '"+ msgType+ "' message: "+"key:"+rcvdKey+"value:"+videos+" from "+rcvdMsg.hfrom);
 		  	
 		        break;
 	
 	case GET :  rcvdKey = rcvdMsg.getKey();
 		        if(cache.containsKey(rcvdKey)){
 	            videos = cache.get(rcvdKey);
 		        System.out.println("Fetching from cache");
 		        }
 		        else {
 		        videos = SearchYouTubeVideos.search(rcvdKey);
 		        cache.put(rcvdKey, videos);
 		        System.out.println("Fetching from youtube");
 	 		   
 		        }
 		      
 		       // System.out.println(videos);
 		        rcvdMsgId = rcvdMsg.getMessageId();
 		       // System.out.println(node.getId()+" received '"+ msgType+ "' message: "+"key:"+rcvdKey+" from "+rcvdMsg.hfrom);
 	 		  	MyMsg replyMsg = new MyMsg(node.getLocalNodeHandle());
 		 		replyMsg.setMsgType(MsgType.RESULT);
 		  	    replyMsg.setMessageId(rcvdMsgId);
 		  	    replyMsg.setKey(rcvdKey);
 		  	    //replyMsg.setValue(replyValue);
 		  	    replyMsg.setListVideos(videos);
 		  	    replyMsg.wantResponse = false;
 		  		//System.out.println(node.getId()+"sending '" +replyMsg.getMsgType()+"' message:"+" Key:"+replyMsg.getKey()+" Value:"+replyMsg.getListVideos()+" :to "+rcvdMsg.hfrom);
 		  		endpoint.route(null,replyMsg,rcvdMsg.hfrom);
 		        break;
 	
 	case RESULT : rcvdKey = rcvdMsg.getKey();
                  videos = rcvdMsg.getListVideos();              
 	              //rcvdValue = rcvdMsg.getValue();
                  rcvdMsgId = rcvdMsg.getMessageId();
                 // System.out.println(node.getId()+" received '"+ msgType+ "' message: "+"key:"+rcvdKey+"value:"+videos+" from "+rcvdMsg.hfrom);
       		  	  YRequest yRequest = yReqResTable.get(rcvdMsgId);
                  YResponse yResponse = new YResponse();
                  //yResponse.setValue(rcvdValue);
                  yResponse.setListVideo(videos);
                  yRequest.setYResponse(yResponse);
                  }
                  
 		}
	
	public String toString(){
       return "MyApp "+endpoint.getId();		
	}

}
