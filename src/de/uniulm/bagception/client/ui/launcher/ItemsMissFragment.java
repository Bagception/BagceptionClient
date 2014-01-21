package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;

public class ItemsMissFragment extends Fragment implements BundleMessageReactor {

	private OverviewFragment fragment;
	private ListView missedItemView;
	private ItemListArrayAdapter arrayAdapter;
	private BundleMessageActor bmActor;

	public void setParentFragment(OverviewFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (fragment == null) {
//			super.onResume();
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
		Log.d("Itemlist Inhalt: ", itemsIn.size() + "");
		List<Item> itemsMustBeIn = update.getActivity().getItemsForActivity();
		arrayAdapter.clear();


		ArrayList<Item> copiedMustItems = new ArrayList<Item>(itemsMustBeIn);
		
		
//		Collections.copy(copiedMustItems, itemsMustBeIn);
		for (Item item : itemsIn) {
			// sb.append(item.getName());
			// sb.append("\n");
			// itemListAd.clear();
			boolean b = copiedMustItems.remove(item);
			boolean bb = copiedMustItems.contains(item);
			
			Log.d("gelöscht: ", item.getName()+ " deleted: "+b+ " contains: "+bb);
			// if()
			// itemListAd.add(count);
		}
		arrayAdapter.addAll(copiedMustItems);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		// return inflater.inflate(R.layout.fragment_items_miss, container,
		// false);
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_items_miss, null);

		missedItemView = (ListView) root.findViewById(R.id.itemsMiss);
		arrayAdapter = new ItemListArrayAdapter(getActivity());
		missedItemView.setAdapter(arrayAdapter);

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
