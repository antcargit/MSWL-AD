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

public class LocService extends Service {

	private static Location mLoc=null; 	
	private static ArrayList<ILocationService> mArray=new ArrayList<ILocationService>();
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		
		Log.d("LogService:","Iniciando Servicio...");		
		configLocation();
		
	}
	  public static void regListener(ILocationService iLocService){
	    	mArray.add(iLocService);
	    }
	
    private void configLocation ()
    {    
    	LocationManager mLocationManager;
    	LocationListener mLocationListener;
    	mLocationManager = (LocationManager)
    	         getSystemService(Context.LOCATION_SERVICE);
    	mLocationListener = new MyLocationListener();
    	mLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
    											 5000, 15, mLocationListener);  	
    }
    
    public static Location getLoc(){
    	return mLoc;  
    }
    private class MyLocationListener implements LocationListener
    {
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			mLoc = location;
			Log.e("LocService:", String.valueOf(mLoc.getLatitude()) + 
					           " " + 
					           String.valueOf(mLoc.getLongitude()));			
			// Recorro los interfaces
			for(int i=0;i<mArray.size();i++){
				mArray.get(i).updateCurrentLocation(mLoc);
			}
		}
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}	
    }
    
    
	
}
