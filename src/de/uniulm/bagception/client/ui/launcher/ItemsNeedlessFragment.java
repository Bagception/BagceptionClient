package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;

public class ItemsNeedlessFragment extends Fragment implements
		BundleMessageReactor {

	private OverviewFragment fragment;
	private ListView itemsRedundantView;
	private ItemListArrayAdapter arrayAdapter;
	private BundleMessageActor bmActor;
	
	public void setParentFragment(OverviewFragment fragment) {
		this.fragment = fragment;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (fragment == null) {
			// super.onResume();
			return;
		}
		ContainerStateUpdate update = fragment.getItemUpdate();
		if (update != null) {
			updateView(update);
		}
		super.onResume();
	}
	
	public void updateView(ContainerStateUpdate update) {
		// TODO view
		List<Item> itemsIn = update.getItemList();
		List<Item> itemsMustBeIn = update.getActivity().getItemsForActivity();

		if (arrayAdapter == null) {
			return;
		} else {
			arrayAdapter.clear();
		}

		ArrayList<Item> copiedMustItems = new ArrayList<Item>(itemsMustBeIn);
		
		for(Item item : copiedMustItems){
			Log.d("Item in copiedMust: ", item.getName());
		}
		Log.d("das ist in mustList drin: ", "" + copiedMustItems.toString());
		for (Item item : itemsIn) {
			//boolean b = copiedMustItems.remove(item);
			Log.d("Item Name: ", item.getName());
			boolean bb = copiedMustItems.contains(item);
			//arrayAdapter.add(item);
			
			if(bb == false){
				Log.d("bb Wert: ", "" + bb);
				arrayAdapter.add(item);
			}
			
		}
		
		
		//arrayAdapter.addAll(copiedMustItems);
	
		if(itemsIn.size() == 0){
			arrayAdapter.clear();
		}

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_items_needless, null);

		itemsRedundantView = (ListView) root.findViewById(R.id.itemsNeedless);
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
