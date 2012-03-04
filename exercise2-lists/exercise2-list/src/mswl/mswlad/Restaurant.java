package mswl.mswlad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class Restaurant extends Activity {
	 Integer token = 0;	
	 public void onCreate(Bundle savedInstanceState) {	  
		 super.onCreate(savedInstanceState);
		 Log.d("Restaurant", "Restaurante: OnCreate");		 
	     setContentView(R.layout.main);	    
	     
	     Intent i = this.getIntent();
	     	     
	     String paramTitulo = i.getStringExtra("titulo");
	     String paramTexto = i.getStringExtra("texto");
	     Integer paramImagen = i.getIntExtra("imagen", -1);
	     token = i.getIntExtra("TOKEN", -1);
	        	     	     
		 ImageView img = (ImageView) this.findViewById(R.id.fotoplato);				
		 img.setImageDrawable(this.getResources().getDrawable(paramImagen));
			
		 TextView t = (TextView) this.findViewById(R.id.titulorest);			
		 t.setText(paramTitulo);
			
		 TextView tdesc = (TextView) this.findViewById(R.id.textorest);			
		 tdesc.setText(paramTexto);	     	     	     
	 }
	 // We capture the onKeyDown
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	     
		 // We know that KEY BACK is pressed
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
			 onFinish();
	         return true;
	     }
		 // If we don't handle this keyCode is necessary
		 // that we call to parent to handle this keyCode
	     return super.onKeyDown(keyCode, event);
	 }
	 private void onFinish()
	 { 
	        // Built a new Intent to return data
	        Intent resultIntent = new Intent();
	        resultIntent.putExtra("RES", 150);	        
	       
	        // Set the intent to return
	        setResult(token, resultIntent);
	        
	        // This method propagates the call via onActivityResult()
	        finish();		 
		 
	 }
}