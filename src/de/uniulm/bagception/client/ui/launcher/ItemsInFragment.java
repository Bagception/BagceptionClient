package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ItemsInFragment extends Fragment implements BundleMessageReactor {

	private ListView itemsStatusView;
	private ItemListArrayAdapter itemListAd;
	private BundleMessageActor bmActor;
	private OverviewFragment fragment;

	public void setParentFragment(OverviewFragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (fragment == null) {
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
		itemListAd.clear();
		
		if(itemsMustBeIn.contains(itemsIn)){
			Toast.makeText(getActivity(), "List is in list", Toast.LENGTH_SHORT).show();
		}else{
//			Log.d("isIn", itemsIn.toString());
//			Log.d("mustBeIn", itemsMustBeIn.toString());
		}
		
		for (Item item : itemsIn) {
			// sb.append(item.getName());
			// sb.append("\n");
			// itemListAd.clear();

			itemListAd.add(item);
			//if()
			// itemListAd.add(count);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_items_in, null);
		// return inflater.inflate(R.layout.fragment_items_in, container,
		// false);

		itemsStatusView = (ListView) root.findViewById(R.id.itemsIn);
		itemListAd = new ItemListArrayAdapter(getActivity());
		itemsStatusView.setAdapter(itemListAd);

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

	@Override
	public void onBundleMessageRecv(Bundle b) {
		// TODO Auto-generated method stub

	}

}
