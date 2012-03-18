package mswl.mswlad;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GeoRssNewsList extends ListActivity {
	
	private MyAdapter mAdapter = null;
	private Location mLoc;
	private ProgressDialog pd=null;
	private static ArrayList<GeoRSSNode> mArray = new ArrayList<GeoRSSNode>();
	private Intent myIntent;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                         
        LocService.getLoc(); 
       
        mAdapter = new MyAdapter(this);       
        
        ILocationService ils=new ILocationService() {
			@Override
			public void updateCurrentLocation(Location loc) {
				// TODO Auto-generated method stub
				mLoc=loc; // Antes de refrescar debo de guardarme la nueva localizacion
				if (pd!=null)
				      pd.dismiss();							
				new onChangedGpsPosition().execute();				
			}
		};
        LocService.regListener(ils);        
        pd=ProgressDialog.show(this,"Please Wait","waiting for location",true);        
        //startService(new Intent(this,LocService.class)); 
        this.myIntent=new Intent(this,LocService.class);
        startService(this.myIntent);
    }    
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mArray.clear();
		stopService(this.myIntent);
		super.onDestroy();
	}

    protected void onListItemClick(ListView l, View v, int position, long id) 
	{
    	// Create a new intent to call other Activity. 
    	// Using the methods "putExtra" we can
    	// send data to the new activity	
	}
    
    private class onChangedGpsPosition extends AsyncTask<Void, Void, Void>
    {    
    	// This method can access to graphic widgets
    	protected void onPreExecute() 
    	{    		
    	}    	
    	// This method NEVER can access to graphic widgets
		@Override
		protected Void doInBackground(Void... arg0) 
		{				
			GeoRssParser myRssParser = new GeoRssParser();
	        try {
	        	mArray=myRssParser.parseGeoRssURL("http://earthquake.usgs.gov/earthquakes/catalogs/eqs7day-M5.xml");
			} catch (ParserConfigurationException e) {			
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;	       
	        
		}		
		// This method can access to graphic widgets
		protected void onPostExecute(Void unused) 
		{
			if (!isFinishing()) 
			{
				pd.dismiss();							   
			        setListAdapter(mAdapter);
			}
		}    	
    }
		
	public static class MyAdapter extends BaseAdapter 
	{
		private Context mContext;		
		public MyAdapter(Context c) {
			mContext = c;
		}
		//@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArray.size();
		}
		//@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mArray.get(position);
		}
		//@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		//@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			// This IF ELSE build a new view if it's necessary o re-use a view			
			if (convertView == null) {
				// Make up a new view
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.main, null);
			} else {
				// Use convertView if it is available
				view = convertView;
			}								
			TextView tpubDate = (TextView) view.findViewById(R.id.pubDate);			
			tpubDate.setText(mArray.get(position).mPubDate);
			
			TextView t = (TextView) view.findViewById(R.id.titulo);			
			t.setText(mArray.get(position).mTitle);
			
			TextView tdesc = (TextView) view.findViewById(R.id.descripcion);			
			tdesc.setText(mArray.get(position).mDescription);
						
			TextView tlink = (TextView) view.findViewById(R.id.link);			
			tlink.setText(mArray.get(position).mLink);
			
			TextView tlatitud = (TextView) view.findViewById(R.id.latitud);			
			tlatitud.setText(String.valueOf(mArray.get(position).mLatitude));
			
			TextView tlongitud = (TextView) view.findViewById(R.id.longitud);			
			tlongitud.setText(String.valueOf(mArray.get(position).mLongitude));
			return view;
		}

	}
}