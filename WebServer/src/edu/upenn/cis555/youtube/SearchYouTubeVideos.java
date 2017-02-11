package edu.upenn.cis555.youtube;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.geo.impl.GeoRssWhere;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaPlayer;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeMediaRating;
import com.google.gdata.data.youtube.YtPublicationState;
import com.google.gdata.data.youtube.YtStatistics;
import com.google.gdata.util.ServiceException;

public class SearchYouTubeVideos {

	
	public SearchYouTubeVideos() {
		
		// TODO Auto-generated constructor stub
	}

	public static ListVideos search(String keyword){
		ListVideos videos = null;
		
		try{
			YouTubeService service = new YouTubeService("my service");
			YouTubeQuery query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
			// order results by the number of views (most viewed first)
			query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);

			// search for puppies and include restricted content in the search results
			query.setFullTextQuery(keyword);
			query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);

			VideoFeed videoFeed = service.query(query, VideoFeed.class);
			//printVideoFeed(videoFeed, true);
            videos = getVideoFeed(videoFeed);
			}catch(ServiceException se){
				se.printStackTrace();
			}
			
			catch(MalformedURLException urle){
				urle.printStackTrace();
			}
			catch(IOException ioe){
				ioe.printStackTrace();
			}
           return videos;
		}
	
	public static ListVideos getVideoFeed(VideoFeed videoFeed) {
		ListVideos videos = new ListVideos();
		Video video; 
		for(VideoEntry videoEntry : videoFeed.getEntries() ) {
		    video = getVideoEntry(videoEntry);
		    videos.addVideo(video);
		 }
		return videos;
	   }
	
	public static Video getVideoEntry(VideoEntry videoEntry) {
		Video video;
		video = new Video();
		String title = videoEntry.getTitle().getPlainText().trim();
	//	System.out.println("Title: " +title);
		video.setTile(title);    
		YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
		MediaPlayer mediaPlayer = mediaGroup.getPlayer();
		    String url = mediaPlayer.getUrl().trim();
			//int index = url.indexOf("&");
			//if(index != -1 )url = url.substring(0,index); // was giving error in parsing xml for seconds equal sign in url
		    video.setUrl(url);    
		//	System.out.println("Web Player URL: " +url);
	return video;		   
	}
	
	}
	
	

/****
		 // printVideoEntry(videoEntry, detailed);
	

**/