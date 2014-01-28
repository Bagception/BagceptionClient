package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;

public class OverviewFragment extends Fragment implements BundleMessageReactor {

	public static Context appContext;
	private BundleMessageActor bmActor;
	private ContainerStateUpdate statusUpdate;
	private ItemsInFragment itemsInFragment;
	private ItemsMissFragment itemsMissFragment;
	private ItemsNeedlessFragment itemsNeedlessFragment;
	private ItemsSuggFragment itemsSuggFragment;

	private TextView currentActivityView;

	private ActionBar.Tab itemsInTab;
	private ActionBar.Tab itemsMissTab;
	ActionBar.Tab itemsNeedlessTab;
	ActionBar.Tab itemsSuggTab;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_TABS);
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_overview, null);
		getActivity().setTitle("�bersicht");

		currentActivityView = (TextView) root.findViewById(R.id.test);

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// initiating both tabs and set text to it.
		itemsInTab = actionBar.newTab().setText("Enthalten (0)");
		itemsMissTab = actionBar.newTab().setText("Fehlend (0)");
		itemsNeedlessTab = actionBar.newTab().setText("�berfl�ssig (0)");
		itemsSuggTab = actionBar.newTab().setText("Vorschlag");

		itemsInFragment = new ItemsInFragment();
		itemsInFragment.setParentFragment(this);
		itemsMissFragment = new ItemsMissFragment();
		itemsMissFragment.setParentFragment(this);
		itemsNeedlessFragment = new ItemsNeedlessFragment();
		itemsNeedlessFragment.setParentFragment(this);
		itemsSuggFragment = new ItemsSuggFragment();
		itemsSuggFragment.setParentFragment(this);

		itemsInTab.setTabListener(new ItemTabListener(itemsInFragment));
		itemsMissTab.setTabListener(new ItemTabListener(itemsMissFragment));
		itemsNeedlessTab.setTabListener(new ItemTabListener(
				itemsNeedlessFragment));
		itemsSuggTab.setTabListener(new ItemTabListener(itemsSuggFragment));

		actionBar.addTab(itemsInTab);
		actionBar.addTab(itemsMissTab);
		actionBar.addTab(itemsNeedlessTab);
		actionBar.addTab(itemsSuggTab);

		return root;
	}

	List<Item> itemsMust;
	List<Item> itemsIn;
	List<Item> needlessItems;
	List<Item> copiedMustItems;

	public void onBundleMessageRecv(Bundle b) {
		BUNDLE_MESSAGE msg = BundleMessage.getInstance()
				.getBundleMessageType(b);
		switch (msg) {

		case CONTAINER_STATUS_UPDATE:
			statusUpdate = ContainerStateUpdate.fromJSON(BundleMessage
					.getInstance().extractObject(b));
			String currentActivity = statusUpdate.getActivity().getName();
			currentActivityView.setText("Aktuelle Aktivit�t: "
					+ currentActivity);
			itemsInFragment.updateView(statusUpdate);
			itemsMissFragment.updateView(statusUpdate);
			itemsNeedlessFragment.updateView(statusUpdate);

			StringBuilder sb = new StringBuilder();
			itemsIn = statusUpdate.getItemList();
			itemsMust = statusUpdate.getActivity().getItemsForActivity();
			needlessItems = statusUpdate.getNeedlessItems();
			copiedMustItems = new ArrayList<Item>(itemsMust);

			if (itemsIn.size() == 0) {
				itemsInTab.setText("Enthalten" + " (0)");
				itemsNeedlessTab.setText("�berfl�ssig (0)");
				itemsMissTab.setText("Fehlend " + "(" + copiedMustItems.size()
						+ ")");
			} else {
				itemsInTab.setText("Enthalten" + " (" + itemsIn.size() + ")");
				itemsMissTab.setText("Fehlend " + "(" + copiedMustItems.size()
						+ ")");
				itemsNeedlessTab.setText("�berfl�ssig " + "("
						+ needlessItems.size() + ")");
			}

			for (Item item : itemsIn) {

				// boolean bb = copiedMustItems.remove(item);
				boolean bbb = copiedMustItems.contains(item);
				if (bbb == false) {

					Log.d("itemsIn size: ", "" + itemsIn.size());
					if (bbb == false) {
						needlessItems.add(item);
						itemsNeedlessTab.setText("�berfl�ssig " + "("
								+ needlessItems.size() + ")");
					}
				} else if (bbb == true) {
					copiedMustItems.remove(item);
					itemsNeedlessTab.setText("�berfl�ssig " + "("
							+ needlessItems.size() + ")");
					itemsMissTab.setText("Fehlende " + "("
							+ copiedMustItems.size() + ")");
				}

			}
			if (copiedMustItems.size() == 0) {
				itemsMissTab.setText("Fehlende (0)");
			}
			sb.append("Update: \n");
			sb.append("Items in Bag:");
			sb.append("\n");

			sb.append("\n");
			sb.append("Activity: ");
			sb.append(statusUpdate.getActivity().getName());
			sb.append("\n");
			sb.append("Items for activity:");
			sb.append("\n");

			sb.append("\n");
			Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG)
					.show();
			break;
		default:
			break;
		}
	}

	public ContainerStateUpdate getItemUpdate() {
		return statusUpdate;
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
	public void onResume() {
		itemsInFragment.setParentFragment(this);
		super.onResume();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(getActivity(), "requesting update", Toast.LENGTH_SHORT).show();
				new BundleMessageHelper(getActivity()).sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.CONTAINER_STATUS_UPDATE_REQUEST, ""));
				
			}
		}, 1500);
		


	}

}
