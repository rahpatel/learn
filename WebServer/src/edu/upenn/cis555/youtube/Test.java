package edu.upenn.cis555.youtube;

public class Test {

	
	
	public void testvideo(){
		
		Video video= new Video();
		video.setTile("mickey");
		video.setTile("www.upenn.edu");
		System.out.println(video);
	}
	
	public void testvideos(){
		ListVideos videos = new ListVideos();
		Video video= new Video();
		video.setTile("mickey");
		video.setUrl("www.upenn.edu");
		videos.addVideo(video);
		//System.out.println(videos.toString()); 
		video= new Video();
			video.setTile("donald");
			video.setUrl("www.upenn.edu");
			videos.addVideo(video);
			System.out.println(videos.toString()); 
						
	}
	
	public static void main(String [] args){
		Test test = new Test();
		//test.testvideo();
		test.testvideos();
		
	}
}
