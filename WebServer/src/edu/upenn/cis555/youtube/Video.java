package edu.upenn.cis555.youtube;

import java.io.Serializable;

public class Video implements Serializable{

	String title;
	String url;
	
	public Video(){
		
	}

	public String getTile() {
		return title;
	}

	public void setTile(String tile) {
		this.title = tile;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	public String toString(){
		
		String strtitle = "<title>"+title+"</title>";
		String strurl =  "<url>"+url+"</url>";
		String startstr ="<video>";
		String endstr = "</video>";
		String videostr = startstr+"\n"+
		                  "        "+strtitle+"\n"+
		                  "        "+strurl+"\n"+
		                  endstr+"\n";
	 
	  return videostr;
		
	}
	
}


















