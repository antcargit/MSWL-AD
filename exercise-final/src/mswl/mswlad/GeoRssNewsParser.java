
package mswl.mswlad;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.location.Location;
import android.util.Log;

public class GeoRssNewsParser{

	private static ArrayList<GeoRssNewsNode> geoNewsArray = new ArrayList<GeoRssNewsNode>(); //Array de noticias
	private static ArrayList<GeoRssNewsNode> geoNewsNearArray = new ArrayList<GeoRssNewsNode>(); // Array de noticias cercanas
	
	public ArrayList<GeoRssNewsNode> getNews(String url,Location currentLocation,Integer maxDistance){	
        geoNewsArray=this.parseGeoRssURL(url);
		for(int i=0;i<geoNewsArray.size();i++){
			GeoRssNewsNode targetNode=geoNewsArray.get(i);
			Double targetLatitude=targetNode.mLatitude;
			Double targetLongitude=targetNode.mLongitude;
			String targetTitle=targetNode.mTitle;
			Location destinyLocation=new Location("");
			destinyLocation.setLatitude(targetLatitude);
			destinyLocation.setLongitude(targetLongitude);
							
			float distanceBetween=currentLocation.distanceTo(destinyLocation);
			distanceBetween=distanceBetween / 1000; // Paso a kilometros
			// Añado distancia al suceso desde mi posicion actual
			targetNode.mDistance=(int)distanceBetween;
			geoNewsArray.set(i, targetNode);
							
			if (distanceBetween < maxDistance){
				Log.d("Distancia!!:",String.valueOf(distanceBetween)+": "+targetTitle);
				geoNewsNearArray.add(geoNewsArray.get(i));
			}					
		}
		return geoNewsNearArray; 
	}
			
    public ArrayList<GeoRssNewsNode> parseGeoRssURL (String url) {    	
    	ArrayList<GeoRssNewsNode> result = null;
    	try{
            SAXParserFactory spf = SAXParserFactory.newInstance();  
            SAXParser sp = spf.newSAXParser();
            GeoRSSReader reader = new GeoRSSReader();
           	sp.parse(url, reader);  
           	result = reader.getArrayGeoNode(); 
        }catch(ParserConfigurationException pcex){
        	Log.e("ParserConfigurationException!", pcex.toString());
        	pcex.printStackTrace();
        	//throw pcex;
        }catch(SAXException saxex){
        	Log.e("SAXException", saxex.toString());
        	saxex.printStackTrace();
        	//throw saxex;
        } catch (IOException ioex) {
        	Log.e("IOException", ioex.toString());
        	ioex.printStackTrace();
        	//throw ioex;
        }
        return result; 
    }
    
    /**
     * GPXReader extends the DefaultHandler for a SAX parser, implementing
     * the specific parsing of labels for a GPX document.  
     */
    private class GeoRSSReader extends DefaultHandler {
    	private StringBuilder contentBuffer; 
    	private ArrayList<GeoRssNewsNode> aGeoRSSNode = new ArrayList<GeoRssNewsNode>();
    	    	
    	private String pubDate = null;
    	private String name = null;
    	private String description = null;
    	private String link = null;
    	private double latitude = -1.0;
    	private double longitude = -1.0;
    	private double altitude = -1.0;
    	
    	public GeoRSSReader() {
    		clear();
    	}    	   
    	public void clear() {
    		aGeoRSSNode.clear();
    		contentBuffer = new StringBuilder();
    	}    	
    	public ArrayList<GeoRssNewsNode> getArrayGeoNode(){
    		return aGeoRSSNode;    		
    	}
    	public void startDocument() throws SAXException{
    	}  
    	public void endDocument()throws SAXException{
    	}
    	public void startElement(String uri, String localName, 
    							String qName, Attributes attributes){
    		contentBuffer.delete(0, contentBuffer.length());    		
    	} 
    	public void characters(char buf[], int offset, int len) throws SAXException{
    		contentBuffer.append(String.copyValueOf(buf, offset, len));
    	}
    	public void endElement(String uri, String localName, String qName) {       	
    		if("title".equals(localName)) 
    			name = contentBuffer.toString();
    		else if("description".equals(localName))
    			description = contentBuffer.toString(); 		
    		else if ("geo:lat".equals(qName)) 
    			latitude = new Double(contentBuffer.toString()).doubleValue();
    		else if ("geo:long".equals(qName))
    			longitude = new Double(contentBuffer.toString()).doubleValue();
    		else if("geo:rsspoint".equals(qName)) {
    			String coordinate[] = contentBuffer.toString().split(" ");   			
    			latitude = new Double(coordinate[0]).doubleValue(); 
    			longitude = new Double(coordinate[1]).doubleValue();   
    		}
        	else if("link".equals(localName))
        			link = contentBuffer.toString(); 
            else if("pubDate".equals(localName))
            	pubDate = contentBuffer.toString();  
    		else if("item".equals(localName)) 
    		{
    			aGeoRSSNode.add(new GeoRssNewsNode(pubDate,name,description,latitude,longitude,link,0));
    		}
    		
    	}
    }
}