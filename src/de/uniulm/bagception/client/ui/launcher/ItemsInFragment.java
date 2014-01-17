package de.uniulm.bagception.client.ui.launcher;

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

	public void onBundleMessageRecv(Bundle b) {
		BUNDLE_MESSAGE msg = BundleMessage.getInstance()
				.getBundleMessageType(b);
		switch (msg) {
		case ITEM_FOUND:
			Item i;
			try {
				i = BundleMessage.getInstance().toItemFound(b);
				Log.d("DAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "WAAAAAAAAAAAAAAAAAAAAAAAAAAH");
				Toast.makeText(getActivity(), "Item found: " + i.getName(),
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case ITEM_NOT_FOUND:
			try {
				i = BundleMessage.getInstance().toItemFound(b);
				Toast.makeText(getActivity(),
						"item not found: " + i.getIds().get(0),
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case CONTAINER_STATUS_UPDATE:
			itemListAd.clear();
			ContainerStateUpdate statusUpdate = ContainerStateUpdate
					.fromJSON(BundleMessage.getInstance().extractObject(b));
			StringBuilder sb = new StringBuilder();
			List<Item> itemsIs = statusUpdate.getItemList();
			List<Item> itemsMust = statusUpdate.getActivity()
					.getItemsForActivity();
			sb.append("Update: \n");
			sb.append("Items in Bag:");
			sb.append("\n");
			for (Item item : itemsIs) {
				sb.append(item.getName());
				sb.append("\n");
				//itemListAd.clear();
				itemListAd.add(item);
				// itemListAd.add(count);
			}
			sb.append("\n");
			sb.append("Activity: ");
			sb.append(statusUpdate.getActivity().getName());
			sb.append("\n");
			sb.append("Items for activity:");
			sb.append("\n");

			for (Item item : itemsMust) {
				sb.append(item.getName());
				sb.append("\n");
			}
			sb.append("\n");
			Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG)
					.show();
			break;

		default:
			break;

		}

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

}
