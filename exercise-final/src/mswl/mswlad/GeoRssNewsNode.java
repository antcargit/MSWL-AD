
package mswl.mswlad;

import java.io.Serializable;

public class GeoRssNewsNode implements Serializable{

	String mPubDate = null;
	String mTitle = null;
	String mDescription = null;
	String mLink= null;	
	Double mLatitude = 0.0;
	Double mLongitude = 0.0;
	int mDistance= 0;	
	
	public GeoRssNewsNode (String pubDate,String title, String description, Double latitude, Double longitude,String link,int distance)
	{
		mPubDate = pubDate;
		mTitle = title;
		mDescription = description;
		mLatitude = latitude;
		mLongitude = longitude;
		mLink = link;
		mDistance = distance;
	}
	
	
}
