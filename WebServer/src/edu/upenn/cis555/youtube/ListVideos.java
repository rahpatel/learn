package edu.upenn.cis555.youtube;

import java.io.Serializable;
import java.util.ArrayList;

public class ListVideos implements Serializable {
    ArrayList<Video> videos;
    
    ListVideos(){
    	videos = new ArrayList<Video>();
        	
    }

	public ArrayList<Video> getListVideos() {
		return videos;
	}

	public void setListVideos(ArrayList<Video> videos) {
		this.videos = videos;
	}
    
	public void addVideo(Video video){
		videos.add(video);
	}
    
	public String toString(){
		String strvideos = null;
		String startstr = "<videos>";
		String endstr = "</videos>";
		strvideos = startstr+"\n"+
		            "    ";
		for (Video video :videos) {
			strvideos = strvideos+video.toString();
		}
		strvideos = strvideos+endstr+"\n";
	return strvideos;
	}
}


















