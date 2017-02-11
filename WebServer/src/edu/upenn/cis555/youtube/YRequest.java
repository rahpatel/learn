package edu.upenn.cis555.youtube;


import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

 public class YRequest {
       boolean valueSet = false;
       String keyword;
	   YResponse yResponse;
       public YRequest(){
	      
       }

	public boolean isValueSet() {
		return valueSet;
	}

	public void setValueSet(boolean valueSet) {
		this.valueSet = valueSet;
	}

	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public synchronized YResponse getYResponse() {
		while(!valueSet){
			try {
				System.out.println("Response not received. Going to sleep...");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Response received. Send response...");
		
		return yResponse;
	}

	public synchronized void  setYResponse(YResponse yResponse) {
		this.yResponse = yResponse;
		valueSet = true;
	    System.out.println("Response set..Notify waiting thread....");
		notify();
	}

	  
	         
				
 }
/*******
 
 public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

 
 ********/