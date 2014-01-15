package de.uniulm.bagception.client.ui.launcher;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import de.uniulm.bagception.client.R;

public class ItemListFragment extends ListFragment {
	
	private ListAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// TODO
		// There should be the database data here 
		String[] items = new String[] {"Item 1", "Item 2", "Item 3"};	//Dummy Content
		
		// ArrayAdapter manage the ListView and add content
		mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_row, R.id.text1, items);
		setListAdapter(mAdapter);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
//		setEmptyText(getResources().getString(R.string.no_items));
	}

}
