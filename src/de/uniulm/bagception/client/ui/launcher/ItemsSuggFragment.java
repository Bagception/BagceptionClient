package de.uniulm.bagception.client.ui.launcher;

import android.app.Activity;
import android.app.Fragment;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;

public class ItemsSuggFragment extends Fragment implements BundleMessageReactor{

	private OverviewFragment fragment;
	private ListView itemsRedundantView;
	private ItemListArrayAdapter arrayAdapter;
	private BundleMessageActor bmActor;

	public void setParentFragment(OverviewFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_items_sugg, null);


		itemsRedundantView = (ListView) root.findViewById(R.id.itemsSugg);
		arrayAdapter = new ItemListArrayAdapter(getActivity());
		itemsRedundantView.setAdapter(arrayAdapter);

		return root;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		bmActor = new BundleMessageActor(this);
		bmActor.register(getActivity());
	}

	@Override
	public void onDetach() {
		bmActor.unregister(getActivity());
		super.onDetach();
	}

	@Override
	public void onBundleMessageRecv(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBundleMessageSend(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponseMessage(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponseAnswerMessage(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusMessage(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommandMessage(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Exception e) {
		// TODO Auto-generated method stub
		
	}

	
}
