
package mswl.mswlad;

import java.io.IOException;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class GeoRssNewsMap extends MapActivity {
	private Context mContext;
    private Location mLoc=new Location("MyLocation");
    private MapView mapview = null;
    private MapController mapControl = null; 
    private TextView tvlocation;
    private GeoRssNewsNode NewsNode;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();  
        setContentView(R.layout.map);
        Log.d("GeoRssNewsMap", "Inicio GeoRssNewsMap OnCreate");        
        mapview = (MapView) findViewById(R.id.myMapView);
        tvlocation = (TextView) findViewById(R.id.tvlocation);
        mapview.setBuiltInZoomControls(true);
        mapControl = mapview.getController();                      
        Intent i = getIntent();
        this.NewsNode=(GeoRssNewsNode)i.getSerializableExtra("NewsNode");        	
        Log.d("Map-Lati",String.valueOf(NewsNode.mLatitude));
        Log.d("Map-Long",String.valueOf(NewsNode.mLongitude));        			
        mLoc.setLatitude(NewsNode.mLatitude);
        mLoc.setLongitude(NewsNode.mLongitude);
        refreshMap(NewsNode.mTitle); 
        }
     
    private void refreshMap(String mapText)
    {
    	Log.d("GeoRssNewsMap", "RefreshMap");
    	if (mLoc == null)
    	{    		
    		Toast.makeText(getBaseContext(),
    				mContext.getResources().getString(R.string.messagelocationnotavailable),
                    Toast.LENGTH_LONG).show();
    		
    		return;
    	}
        GeoPoint geoPoint = new GeoPoint ( (int) (mLoc.getLatitude() * 1000000),
				                           (int) (mLoc.getLongitude() * 1000000));
        mapControl.setZoom(mContext.getResources().getInteger(R.integer.map_zoom));
		mapControl.animateTo(geoPoint);
		MapOverlay myMapOver = new MapOverlay(getBaseContext(),mapText);
		myMapOver.setDrawable(getResources().getDrawable(R.drawable.drawingpin));
		myMapOver.setGeoPoint(geoPoint); // Le paso el Geopoint
		final List<Overlay> overlays = mapview.getOverlays();
		overlays.clear();
		overlays.add(myMapOver);
		mapview.setClickable(true);		
		tvlocation.setText(mapText + ": \n" + String.valueOf(mLoc.getLatitude()) + " , " +String.valueOf(mLoc.getLongitude()));    	
    }
   
    public void searchManual(String address){
        Geocoder gcod = new Geocoder(getApplicationContext());
        try {
                List<Address> addressList = gcod.getFromLocationName(address, 1);
                if (addressList.isEmpty()){                	
                        Toast.makeText(getBaseContext(),
                        		mContext.getResources().getString(R.string.messagetheresnoaddress),
                                        Toast.LENGTH_LONG).show();
                }
                if (addressList.get(0).hasLatitude() && addressList.get(0).hasLongitude()){
                        Location loc = new Location("MANUAL_PROVIDER");
                        loc.setLatitude(addressList.get(0).getLatitude());
                        loc.setLongitude(addressList.get(0).getLongitude());
                        mLoc = loc;
                        refreshMap("Location");
                        
                        //Send coordinates to the server
                        Toast.makeText(getBaseContext(), 
                        		mContext.getResources().getString(R.string.messagelocationupdate), 
                        		       Toast.LENGTH_SHORT).show();
                } else 
                        Toast.makeText(getBaseContext(),
                        		mContext.getResources().getString(R.string.messagenocoordinates),
                                        Toast.LENGTH_LONG).show();
        } catch (IOException e) {
                e.printStackTrace();
        }
}

    private Location getLocationByCriteria ()
    {
    	LocationManager mLocationManager;
    	mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	Criteria criteria = new Criteria();
    	criteria.setAccuracy(Criteria.ACCURACY_FINE);
    	criteria.setAltitudeRequired(false);
    	criteria.setBearingRequired(false);
    	criteria.setPowerRequirement(Criteria.POWER_LOW);
    	String provider = mLocationManager.getBestProvider(criteria, true);
    	if (provider==null)
    		return null;
    	return mLocationManager.getLastKnownLocation(provider);
    }
	@Override	
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}  
}

