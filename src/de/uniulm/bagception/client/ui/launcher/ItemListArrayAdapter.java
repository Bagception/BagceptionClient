package de.uniulm.bagception.client.ui.launcher;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion.CONTEXT;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.items.AutoUpdateableItemView;

public class ItemListArrayAdapter extends ArrayAdapter<Item> {

	
	private HashMap<Item,Integer> colorCodes = new HashMap<Item,Integer>(); 
	private HashMap<Item,List<CONTEXT>> context = new HashMap<Item, List<CONTEXT>>();
	
	public void clearColorCodeItems(){
		colorCodes.clear();
	}
	public void putColorCodeItems(int c,Item... items){
		for (Item i:items){
			colorCodes.put(i, c);
		}
	}
	
	public void clearContextInfo(){
		context.clear();
	}
	
	public void putContextItem(List<CONTEXT> c,Item... items){
		for (Item i:items){
			context.put(i, c);
		}
	}
	
	public ItemListArrayAdapter(Context newBagFragment) {
		super(newBagFragment, android.R.layout.simple_list_item_1);
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
            Integer col = colorCodes.get(item);
            
            if (col!=null){
            	view.setBackgroundColor(col);
            }else{
            	view.setBackgroundColor(Color.WHITE);
            }
            //TODO render context image
            List<CONTEXT> ctx = context.get(item);
            if (ctx!=null){
            	for(CONTEXT c:ctx){
            		switch (c) {
					case BRIGHT:
						//TODO show bright image
						break;

					default:
						break;
					}
            	}
            }
         }

        return view;
    }

}
