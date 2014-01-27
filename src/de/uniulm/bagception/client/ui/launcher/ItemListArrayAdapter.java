package de.uniulm.bagception.client.ui.launcher;

import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.items.AutoUpdateableItemView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemListArrayAdapter extends ArrayAdapter<Item> {

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
            TextView itemView = (TextView) view.findViewById(R.id.itemName);
            AutoUpdateableItemView autoItemView = (AutoUpdateableItemView) view.findViewById(R.id.itemIcon);
            autoItemView.setItem(item);
            
            if (itemView != null) {
                itemView.setText(item.getName());
            }
         }

        return view;
    }

}
