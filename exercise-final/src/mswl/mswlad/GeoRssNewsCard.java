package mswl.mswlad;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class GeoRssNewsCard extends Activity {
	 private Integer token = 0;		 
	 private GeoRssNewsNode NewsNode;
	 private static Integer RES_GEORSSNEWSMAP = 10;
	 private static final int MENU_MAP = Menu.FIRST + 1;
	 private static final int MENU_LINK = Menu.FIRST + 2;
	 
	 public void onCreate(Bundle savedInstanceState) {	  
		 super.onCreate(savedInstanceState);
		 Log.d("GeoRssNewsCard", "Inicio GeoRssNewsCard OnCreate");		 
	     setContentView(R.layout.card);	    
	     
	     Intent i = this.getIntent();   
	     NewsNode=(GeoRssNewsNode)i.getSerializableExtra("targetNode");
	     token = i.getIntExtra("TOKEN", -1);
	     
		 ImageView img = (ImageView) this.findViewById(R.id.iconRSS);				
		 img.setImageResource(R.drawable.iconorss);
	     
		 TextView t1 = (TextView) this.findViewById(R.id.pubDate);			
		 t1.setText(NewsNode.mPubDate);
		 TextView t2 = (TextView) this.findViewById(R.id.titulo);			
		 t2.setText(NewsNode.mTitle);
		 TextView t3 = (TextView) this.findViewById(R.id.descripcion);		
		 // PAra poder mostrar Texto HTML uso Html.fromHtml
		 t3.setText(Html.fromHtml(NewsNode.mDescription));
		 TextView t4 = (TextView) this.findViewById(R.id.latitud);			
		 t4.setText("Latitud: "+String.valueOf(NewsNode.mLatitude));
		 TextView t5 = (TextView) this.findViewById(R.id.longitud);			
		 t5.setText("Longitud: "+String.valueOf(NewsNode.mLongitude));
		 TextView t6 = (TextView) this.findViewById(R.id.link);			
		 t6.setText(NewsNode.mLink);		 
		 TextView t7 = (TextView) this.findViewById(R.id.distance);			
		 t7.setText("Distancia : "+String.valueOf(NewsNode.mDistance)+" Km");
			
     	 this.NewsNode=new GeoRssNewsNode(NewsNode.mPubDate,NewsNode.mTitle,NewsNode.mDescription,NewsNode.mLatitude,NewsNode.mLongitude,NewsNode.mLink,NewsNode.mDistance);    	     
	 }
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {	     
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
			 onFinish();
	         return true;
	     }
	     return super.onKeyDown(keyCode, event);
	 }
	 private void onFinish()
	 { 
	        Intent resultIntent = new Intent();
	        resultIntent.putExtra("RES", 150);	        	       
	        setResult(token, resultIntent);	        
	        finish();		 		 
	 }
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) 
	    {
	    	super.onCreateOptionsMenu(menu);
	        menu.add(0, MENU_MAP, Menu.NONE ,R.string.button_map)
	        .setIcon(R.drawable.mapicon)
	        .setAlphabeticShortcut('S');
	        menu.add(0, MENU_LINK, Menu.NONE ,R.string.button_web_browser)
	        .setIcon(R.drawable.wwwicon)
	        .setAlphabeticShortcut('A');	        
	        return true;
	    }
	    public boolean onOptionsItemSelected (MenuItem item) {
	    	switch (item.getItemId()) {
	    		case MENU_MAP:
	    	    	Intent i=new Intent(this,GeoRssNewsMap.class);
	    	    	i.putExtra("NewsNode",this.NewsNode);
	    	    	i.putExtra("TOKEN",RES_GEORSSNEWSMAP);
	    	    	startActivityForResult(i,RES_GEORSSNEWSMAP);  	    			
	    			break;
	    		case MENU_LINK:
	    			Intent iweb = new Intent(Intent.ACTION_VIEW);
	    			iweb.setData(Uri.parse(this.NewsNode.mLink));
	    			startActivity(iweb);
	    			break;	
	    	}
	    	return true;
	    }
}