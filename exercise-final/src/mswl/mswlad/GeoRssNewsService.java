package mswl.mswlad;

import java.util.ArrayList;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

public class GeoRssNewsService extends Service {

	private Context mContext;
	private static ArrayList<GeoRssNewsNode> geoNewsNearArray = new ArrayList<GeoRssNewsNode>(); // Array de noticias cercanas
	private static ArrayList<INewsService> serviceListenerArray=new ArrayList<INewsService>(); // Array de Listeners
    private Intent intentLocation;
	private NotificationManager mNM=null;	
	private String rssUrl="";
	@Override
	public IBinder onBind(Intent arg0) {return null;}	
	@Override
	public void onCreate(){		
		Log.d("NewsService:","Iniciando Servicio...");
		mContext = this.getApplicationContext();
		mNM=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		String convertGeoUrl=mContext.getString(R.string.convert_togeorss_URL);
		rssUrl=convertGeoUrl+mContext.getString(R.string.madrid_elmundo_URL);
		connectLocationService();
	}
	
	@Override
	public void onDestroy() {
		Log.d("NewsService:","Deteniendo!!!! Servicio...");
		serviceListenerArray.clear();
		mNM.cancelAll();
		stopService(this.intentLocation);
		super.onDestroy();
	}
	
	public void connectLocationService(){
		ILocationService ils=new ILocationService() {
			@Override
			public void updateCurrentLocation(Location location){				
				GeoRssNewsParser myRssParser = new GeoRssNewsParser();	
				Integer maxDistance=mContext.getResources().getInteger(R.integer.max_distance);
				geoNewsNearArray=myRssParser.getNews(rssUrl,location,maxDistance);
				CharSequence textNotif=mContext.getString(R.string.notification_text)+String.valueOf(geoNewsNearArray.size());
				showNotification(textNotif);
				
				for(int i=0;i<serviceListenerArray.size();i++){
					serviceListenerArray.get(i).updateNearNews(geoNewsNearArray); // Le paso array de noticias cercanas
				}			
			}
		};
		Log.d("NewsService:","connectLocationService");
		GeoRssLocationService.regListener(ils); 
		this.intentLocation=new Intent(this,GeoRssLocationService.class);
		startService(this.intentLocation);	
	}
		
    public static void regListener(INewsService iNService){
		  serviceListenerArray.add(iNService);
	}	
  
    private void showNotification(CharSequence text){
    	mNM.cancel(1);    	
    	Notification notification=new Notification(R.drawable.drawingpin,text,System.currentTimeMillis());
    	Intent iNotification=new Intent(this,new GeoRssNewsList().getClass());
    	iNotification.putExtra("VieneDeNotificacion", true);
    	iNotification.putExtra("arraynews",geoNewsNearArray);   
    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, iNotification, PendingIntent.FLAG_UPDATE_CURRENT);
    	notification.setLatestEventInfo(this, "Alarm Notification", text, contentIntent);
    	mNM.notify(1, notification);
    }     
}
