package edu.upenn.cis555.youtube;

public class YResponse {

	String value;
	ListVideos videos;
    YResponse(){
		
	}
	
    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ListVideos getListVideos() {
		return videos;
	}

	public void setListVideo(ListVideos videos) {
		this.videos = videos;
	}

	
	
	
}
