package edu.upenn.cis555.youtube;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;


public class YReqResTable {
	Hashtable<Integer,YRequest> entries;
	
	   public YReqResTable(){
			entries  = new Hashtable<Integer,YRequest>();
		}
		
	   public  synchronized void put(int id,YRequest yRequest){
		      entries.put(id,yRequest);	
		  	
			}
		
		
	    public  synchronized YRequest get(int id) {
			    return entries.get(id);
	    	}

	 

}
/********

   public  synchronized YResponse getYResponse(int requestId) {
			Enumeration<YRequest> keys;
			keys = entries.keys();
			YResponse yResponse = null;
			while(keys.hasMoreElements()){
				YRequest yRequest = keys.nextElement();
				if(yRequest.getRequestId() == requestId) {
					 yResponse = entries.get(yRequest);
					break;
				}
			}
			
	    	
            return yResponse;
    	}

	    public  synchronized YRequest getYRequest(int requestId) {
			Enumeration<YRequest> keys;
			keys = entries.keys();
			YRequest yRequest = null;
			while(keys.hasMoreElements()){
				yRequest = keys.nextElement();
				if(yRequest.getRequestId() == requestId) {
					break;
				}
			}
			
	    	
            return yRequest;
    	}


******/