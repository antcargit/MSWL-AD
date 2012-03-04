package mswl.mswlad;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RestaurantList extends ListActivity 
{
	private static Integer RES_RESTAURANT = 10;
	private MyAdapter mAdapter = null;
	// We define a structure to save the data
	public class Node 
	{
		public String mTitle;
		public String mDescription;
		public Integer mImageResource;	
		
		public Node(String title,String desc,Integer image){
			mTitle=title;
			mDescription=desc;
			mImageResource=image;
		}
	}
	// ArrayList
	private static ArrayList<Node> mArray = new ArrayList<Node>();
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		Log.d("PPal", "He entrado en onCreate");
        super.onCreate(savedInstanceState); 
        Node node1=new Node(this.getString(R.string.TitCabana),this.getString(R.string.TextCabana),R.drawable.cabanargentina);
        Node node2=new Node(this.getString(R.string.TitArroz),this.getString(R.string.TextArroz),R.drawable.arroceria);        
        Node node3=new Node(this.getString(R.string.TitSushi),this.getString(R.string.TextSushi),R.drawable.sushi);

        mArray.add(node1);
        mArray.add(node2);
        mArray.add(node3);
        
        mAdapter = new MyAdapter(this);
		setListAdapter(mAdapter);	     	   
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mArray.clear();
		super.onDestroy();
	}

    protected void onListItemClick(ListView l, View v, int position, long id) 
	{
    	// Create a new intent to call other Activity. 
    	// Using the methods "putExtra" we can
    	// send data to the new activity	
    	Intent i=new Intent(this,Restaurant.class);
    	i.putExtra("titulo",mArray.get(position).mTitle);
    	i.putExtra("texto",mArray.get(position).mDescription);
    	i.putExtra("imagen",mArray.get(position).mImageResource);
    	i.putExtra("TOKEN",RES_RESTAURANT);
    	startActivityForResult(i,RES_RESTAURANT);    	
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
			Log.d("Adaptador", "He entrado en getView Iteración:" + String.valueOf(position));
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
			ImageView img = (ImageView) view.findViewById(R.id.fotoplato);				
			img.setImageDrawable(mContext.getResources().getDrawable(mArray.get(position).mImageResource));
			
			TextView t = (TextView) view.findViewById(R.id.titulorest);			
			t.setText(mArray.get(position).mTitle);
			
			TextView tdesc = (TextView) view.findViewById(R.id.textorest);			
			tdesc.setText(mArray.get(position).mDescription);
						
			return view;
		}

	}
}