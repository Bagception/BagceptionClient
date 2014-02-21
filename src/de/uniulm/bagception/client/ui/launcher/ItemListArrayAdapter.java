package de.uniulm.bagception.client.ui.launcher;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion.CONTEXT;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.items.AutoUpdateableItemView;

public class ItemListArrayAdapter extends ArrayAdapter<Item> {

	
	private HashMap<Long,Integer> colorCodes = new HashMap<Long,Integer>(); 
	private HashMap<Long,List<CONTEXT>> context = new HashMap<Long, List<CONTEXT>>();
	private final Bitmaps bitmaps;
	public void clearColorCodeItems(){
		colorCodes.clear();
	}
	public void putColorCodeItems(int c,Item... items){
		for (Item i:items){
			colorCodes.put(i.getId(), c);
		}
	}
	
	public void clearContextInfo(){
		context.clear();
	}
	
	public void putContextItem(List<CONTEXT> c,Item... items){
		for (Item i:items){
			context.put(i.getId(), c);
		}
	}
	
	public ItemListArrayAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1);
		bitmaps=Bitmaps.getInstance(context);

		
	}
	
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list_layout, null);
        }

        Item item = getItem(position);
        if (item!= null) {
            TextView itemView = (TextView) view.findViewById(R.id.eventNameTextView);
            AutoUpdateableItemView autoItemView = (AutoUpdateableItemView) view.findViewById(R.id.itemIcon);
            autoItemView.setItem(item);
            
            if (itemView != null) {
                itemView.setText(item.getName());
            }
            Integer col = colorCodes.get(item.getId());
            if (col!=null){
            	view.setBackgroundColor(col);
            }else{
            	view.setBackgroundColor(Color.WHITE);
            }
            List<CONTEXT> ctx = context.get(item);
            ImageView contextImg = (ImageView)view.findViewById(R.id.contextIcon);
            if (ctx!=null){
	            Bitmap bmp=null;
            	for(CONTEXT c:ctx){
            		bmp=bitmaps.getContextIcon(c);
            	}
            	contextImg.setImageBitmap(bmp);
            }
         }

        return view;
    }
	


}
