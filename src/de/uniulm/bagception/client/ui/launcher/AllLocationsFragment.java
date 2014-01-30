package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;

public class AllLocationsFragment extends Fragment{

	ListView listView;
	ArrayAdapter<Item> ad;
	String bla;
	
	public static Fragment newInstance(Context context) {
		AllItemsFragment f = new AllItemsFragment();

		return f;
	}
	
	Item[] test;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_all_locations, null);
		listView = (ListView) root.findViewById(R.id.listViewAllLocations);
		return root;
	}

}
