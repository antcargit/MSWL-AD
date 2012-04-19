package mswl.mswlad;

import java.util.ArrayList;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GeoRssNewsList extends ListActivity {
		
	private MyAdapter mAdapter = null;
	private Context mContext;
	private Location mLoc;
	private ProgressDialog pd=null;
    private Boolean isServiceUp=null;    
	private static ArrayList<GeoRssNewsNode> geoNewsNearArray = new ArrayList<GeoRssNewsNode>();
	private Intent intentNews;
	private static Integer RES_GEORSSNEWS = 10; // Para comunicación con GeoRssNewsCard	
    private static final int MENU_SERVICE_CONTROL = Menu.FIRST + 1;
	private LocationManager gpsLocationManager; // Acceso al GPS sin Servicio
    private LocationListener gpsLocationListener; 
    private String rssUrl="";
      
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        mContext = this.getApplicationContext();
        Log.d("GeoRssNewsList:","ON-CREATE");
        mAdapter = new MyAdapter(this);           
        Intent i = getIntent();
        Boolean notify=i.getBooleanExtra("VieneDeNotificacion",false);
        pd = new ProgressDialog(this);
        pd.setTitle(mContext.getString(R.string.progress_dialog_tit));
        pd.setMessage(mContext.getString(R.string.progress_dialog_text));        
		String convertGeoUrl=mContext.getString(R.string.convert_togeorss_URL);
		rssUrl=convertGeoUrl+mContext.getString(R.string.madrid_elmundo_URL);
        if ( !notify ){  
        	connectNewsService();
        	pd.show();
        }else{ 
        	getNotification(i);
        }       
    }    	
	
	@Override
	public void onDestroy() {
		Log.d("GeoRssNewsList:","ON-DESTROY!!!");
		saveStatusService(isServiceUp);		
		super.onDestroy();
	}
	
	public void getNotification(Intent i){
	    Log.d("GeoRssNewsList:","Entrando desde Notification con Intent");
        geoNewsNearArray=(ArrayList<GeoRssNewsNode>)i.getSerializableExtra("arraynews");
    	NotificationManager mNM=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);        	        	                	
    	mNM.cancel(1); 
    	this.intentNews=new Intent(this,GeoRssNewsService.class); //Comunicacion con el Servicio
    	this.isServiceUp=loadStatusService();
    	setListAdapter(mAdapter);
	}
	
	public void connectNewsService(){
		INewsService ils=new INewsService() {
			@Override
			public void updateNearNews(ArrayList<GeoRssNewsNode> listNearNews){
				Log.d("GeoRssNewsList:","Llamado a INTERFACE!");
				if (pd!=null)
				     pd.dismiss();		
				geoNewsNearArray=listNearNews;	
				setListAdapter(mAdapter);
			}
		};
		Log.d("GeoRssNewsList:","connectNewsService");
        GeoRssNewsService.regListener(ils); 		
    	this.intentNews=new Intent(this,GeoRssNewsService.class); //Comunicacion con el Servicio
    	startService(this.intentNews);
    	this.isServiceUp=true; 
    	saveStatusService(isServiceUp);		
	}
		
    private Boolean loadStatusService(){ // Restaura el estado del servicio.
    	SharedPreferences settings = getSharedPreferences("GeoRssNewsList", MODE_PRIVATE);
    	Boolean targetState = settings.getBoolean("serviceStatus", false);
    	return targetState;
    }
    private void saveStatusService(Boolean targetState){ // Almacena el estado del servicio
    	SharedPreferences settings = getSharedPreferences("GeoRssNewsList", MODE_PRIVATE);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putBoolean("serviceStatus",targetState);
    	editor.commit();    	
    }  
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
	{
    	Intent i=new Intent(this,GeoRssNewsCard.class);    	
    	i.putExtra("targetNode",geoNewsNearArray.get(position));
    	startActivityForResult(i,RES_GEORSSNEWS);  
	}
    
	public void connectLocationService(){ //Conexion al GPS desde el ASYNC TASK
        ILocationService ils=new ILocationService() {
			@Override
			public void updateCurrentLocation(Location location){	
				mLoc=location;		
			}
		};
        GeoRssLocationService.regListener(ils); 
	}
    // ASYNC TASK
    private class onChangedGpsPosition extends AsyncTask<Void, Void, Void>
    {    			
    	protected void onPreExecute() 
    	{    
    		connectLocationService();    		
    		pd.show();	
    		geoNewsNearArray.clear();
    	}    	 
		protected Void doInBackground(Void... arg0)
		{		
			Log.d("onChangedGpsPosition:","Entrando en ASYNC TASK!");
			GeoRssNewsParser myRssParser = new GeoRssNewsParser();	
			Integer maxDistance=mContext.getResources().getInteger(R.integer.max_distance);
			geoNewsNearArray=myRssParser.getNews(rssUrl,mLoc,maxDistance);
			return null;	       	        
		}		
		protected void onPostExecute(Void unused) 
		{
			if (pd!=null)
				pd.dismiss();	
		    setListAdapter(mAdapter);
		}    	
    }
    // END ASYNC TASK
    
    // ADAPTADOR DE LA LISTA
	public static class MyAdapter extends BaseAdapter 
	{
		private Context mContext;		
		public MyAdapter(Context c) {
			mContext = c; // Para acceder a los atributos de la clase padre.
		}
		public int getCount() {return geoNewsNearArray.size();}
		public Object getItem(int position) {return geoNewsNearArray.get(position);}
		public long getItemId(int position) {return position;}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			// This IF ELSE build a new view if it's necessary o re-use a view			
			if (convertView == null) {
				// Make up a new view
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.main, null);
			}else {
				// Use convertView if it is available
				view = convertView;
			}											
			ImageView img = (ImageView) view.findViewById(R.id.iconRSS);				
			img.setImageResource(R.drawable.iconorss);
			TextView tdesc = (TextView) view.findViewById(R.id.titulo);			
			tdesc.setText(geoNewsNearArray.get(position).mPubDate+" a "+geoNewsNearArray.get(position).mDistance+" Km.");
			TextView t = (TextView) view.findViewById(R.id.shortDescription);			
			t.setText(geoNewsNearArray.get(position).mTitle);		
			return view;
		}

	}
    // END ADAPTADOR DE LA LISTA	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);    	
     	// Como uso botones dinamicos, los botones del menu debo de crearlos en onPrepareOptionsMenu
        return true;
    }
    public boolean onOptionsItemSelected (MenuItem item) {
    	switch (item.getItemId()) {   
    		case MENU_SERVICE_CONTROL:
    			String messageStatus="";
    			if (isServiceUp){
    				stopService(this.intentNews);
    				this.isServiceUp=false;
    				saveStatusService(isServiceUp);
    				messageStatus=mContext.getResources().getString(R.string.messageservice_stopped);
    		        // Arranco el Listener local del GPS
    				this.gpsconfigLocation();    			
    			}else{ 
    				connectNewsService(); //Arranco servicio de Noticias
    		        messageStatus=mContext.getResources().getString(R.string.messageservice_started);
    		        // Detenco el Listener local del GPS
    		        this.gpsLocationManager.removeUpdates(gpsLocationListener); // Detengo escucha directa con el GPS
    			}
    			Toast.makeText(getBaseContext(),messageStatus,Toast.LENGTH_LONG).show();
    			break;	
    	}
    	return true;
    }    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	menu.clear();
    	String textMenu="";
    	int targetIcon;
    	if(this.isServiceUp){
    		textMenu=mContext.getResources().getString(R.string.button_stopserv);
    		targetIcon=R.drawable.botonon;
    	}else{ 
    		textMenu=mContext.getResources().getString(R.string.button_startserv);
    		targetIcon=R.drawable.botonoff;
    	}    
		menu.add(0, MENU_SERVICE_CONTROL, Menu.NONE ,textMenu).setIcon(targetIcon).setAlphabeticShortcut('S');
    	return super.onPrepareOptionsMenu(menu);    	
    }    
    /////// GPS: Para cuando el servicio este parado:
    private void gpsconfigLocation ()
    {    
    	Log.d("GeoRssNewsList:","Configurando GPS...");
    	this.gpsLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	this.gpsLocationListener = new gpsListener();
    	this.gpsLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,5000, 15, gpsLocationListener);    	
    }    
    private class gpsListener implements LocationListener
    {
		@Override
		public void onLocationChanged(Location location) {			
			mLoc = location;
			Log.d("ActivityList-GPSConnection:", String.valueOf(mLoc.getLatitude()) + " " +String.valueOf(mLoc.getLongitude()));
			if (pd!=null)
				     pd.dismiss();	
			onChangedGpsPosition asyncTaskGps=(onChangedGpsPosition)new onChangedGpsPosition();
			asyncTaskGps.execute(); //Ejecuto la Async Task
		}
		@Override
		public void onProviderDisabled(String provider) {}
		@Override
		public void onProviderEnabled(String provider) {}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}	
    }   
}