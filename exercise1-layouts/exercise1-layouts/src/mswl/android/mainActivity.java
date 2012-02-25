package mswl.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Rect;
import android.util.AttributeSet;

public class mainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView tv = (TextView) this.findViewById(R.id.miMarquesina);  
        tv.setSelected(true);  
    }

}