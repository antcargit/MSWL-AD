package mswl.mswlad;

import java.util.ArrayList;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GeoRssLocationService extends Service {

	private static Location mLoc=null;	
	private static ArrayList<ILocationService> serviceListenerArray=new ArrayList<ILocationService>(); // Array de Listeners
	private LocationManager gpsLocationManager; 
    private LocationListener gpsLocationListener;
    
	@Override
	public IBinder onBind(Intent arg0) {return null;}	
	@Override
	public void onCreate(){		
		Log.d("LocationService:","Iniciando Servicio...");		
		configLocation();						
	}	
	@Override
	public void onDestroy() {
		Log.d("LocService:","Deteniendo!!!! Servicio...");
		serviceListenerArray.clear();
		this.gpsLocationManager.removeUpdates(gpsLocationListener); // Detengo con el GPS
		super.onDestroy();
	}
    public static void regListener(ILocationService iLocService){
		  serviceListenerArray.add(iLocService);
	}	
    private void configLocation ()
    {    
    	Log.d("LocationService:","configLocation");
    	this.gpsLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	this.gpsLocationListener = new gpsListener();
    	this.gpsLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,5000, 15, gpsLocationListener);  	
    }    
    
    /////// LISTENER DEL GPS
    private class gpsListener implements LocationListener
    {
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub			
			mLoc = location;
			Log.d("LocService:", String.valueOf(mLoc.getLatitude()) + " " + String.valueOf(mLoc.getLongitude()));			
			for(int i=0;i<serviceListenerArray.size();i++){
			   serviceListenerArray.get(i).updateCurrentLocation(location);
			}
		}
		@Override
		public void onProviderDisabled(String provider) {}
		@Override
		public void onProviderEnabled(String provider) {}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}	
    }        	
}
