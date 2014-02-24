package de.uniulm.bagception.client.ui.launcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion.CONTEXT;
import de.uniulm.bagception.bundlemessageprotocol.entities.RichItem;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.items.AutoUpdateableItemView;

public class RichItemArrayAdapter extends ArrayAdapter<RichItem>{
	private final Bitmaps bitmaps;

	public RichItemArrayAdapter(Context context) {
		super(context, R.layout.item_list_layout);
        Log.d("CTX", "init RichArrayAdapter");

		bitmaps=Bitmaps.getInstance(context);
	}
	
	  public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.item_list_layout, null);
	        }

	        RichItem item = getItem(position);
	        if (item!= null) {
	            
	        	TextView itemName = (TextView) view.findViewById(R.id.eventNameTextView);
	            AutoUpdateableItemView autoItemView = (AutoUpdateableItemView) view.findViewById(R.id.itemIcon);
	            ImageView contextImg = (ImageView)view.findViewById(R.id.contextIcon);
	            autoItemView.setItem(item);
	            CONTEXT ctx=null;
	            if (itemName != null) {
	            	itemName.setText(item.getName());
	            }
	            if(item.getContextSuggestion() != null){
	            	ctx = item.getContextSuggestion().getReason();
	            }
	            

            	if (ctx!=null){
	            	view.setBackgroundColor(Color.argb(10, 255, 250, 50));
	            	Bitmap bmp=null;
            		bmp=bitmaps.getContextIcon(ctx);
            		Log.d("CONTEXT",item.getName()+" kontext: "+ctx.name());
	            	contextImg.setVisibility(View.VISIBLE);
	            	contextImg.setImageBitmap(bmp);
	            }else{
	            	view.setBackgroundColor(Color.WHITE);
	            	contextImg.setVisibility(View.INVISIBLE);
	            }
            	if (item.isNeedless()){
	            	view.setBackgroundColor(Color.argb(50, 255, 0, 0));
            	}
            }
	            
	            

	        return view;
	    }

}
