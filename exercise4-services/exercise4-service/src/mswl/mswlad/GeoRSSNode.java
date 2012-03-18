/*
 *
 *  Copyright (C) 2012 GSyC/LibreSoft, Universidad Rey Juan Carlos.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/. 
 *
 *  Author : Roberto Calvo Palomino <rocapal at libresoft dot es>
 *
 */

package mswl.mswlad;

public class GeoRSSNode {

	String mPubDate = null;
	String mTitle = null;
	String mDescription = null;
	String mLink= null;	
	Double mLatitude = 0.0;
	Double mLongitude = 0.0;
	
	public GeoRSSNode (String pubDate,String title, String description, Double latitude, Double longitude,String link)
	{
		mPubDate = pubDate;
		mTitle = title;
		mDescription = description;
		mLatitude = latitude;
		mLongitude = longitude;
		mLink = link;
	}
	
	
}
