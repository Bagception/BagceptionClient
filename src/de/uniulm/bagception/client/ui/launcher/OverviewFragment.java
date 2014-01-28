package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
import de.uniulm.bagception.protocol.bundle.constants.Command;
import de.uniulm.bagception.protocol.bundle.constants.StatusCode;

public class OverviewFragment extends Fragment implements BundleMessageReactor {

	public static Context appContext;
	private BundleMessageActor bmActor;
	private volatile ContainerStateUpdate statusUpdate;
	
	private ItemsInFragment itemsInFragment;
	private ItemsMissFragment itemsMissFragment;
	//private ItemsNeedlessFragment itemsNeedlessFragment;
	private ItemsSuggFragment itemsSuggFragment;

	
	private BundleMessageHelper bmHelper;
	private TextView currentActivityView;

	private ActionBar.Tab itemsInTab;
	private ActionBar.Tab itemsMissTab;
	//ActionBar.Tab itemsNeedlessTab;
	ActionBar.Tab itemsSuggTab;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_TABS);
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_overview, null);
		getActivity().setTitle("Übersicht");

		currentActivityView = (TextView) root.findViewById(R.id.test);

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.removeAllTabs();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// initiating both tabs and set text to it.
		itemsInTab = actionBar.newTab().setText("Enthalten (0)");
		itemsMissTab = actionBar.newTab().setText("Fehlend (0)");
		//itemsNeedlessTab = actionBar.newTab().setText("�berfl�ssig (0)");
		itemsSuggTab = actionBar.newTab().setText("Vorschlag");

		itemsInFragment = new ItemsInFragment();
		itemsInFragment.setParentFragment(this);
		itemsMissFragment = new ItemsMissFragment();
		itemsMissFragment.setParentFragment(this);
		//itemsNeedlessFragment = new ItemsNeedlessFragment();
		//itemsNeedlessFragment.setParentFragment(this);
		itemsSuggFragment = new ItemsSuggFragment();
		itemsSuggFragment.setParentFragment(this);

		itemsInTab.setTabListener(new ItemTabListener(itemsInFragment));
		itemsMissTab.setTabListener(new ItemTabListener(itemsMissFragment));
		//itemsNeedlessTab.setTabListener(new ItemTabListener(itemsNeedlessFragment));
		itemsSuggTab.setTabListener(new ItemTabListener(itemsSuggFragment));

		actionBar.addTab(itemsInTab);
		actionBar.addTab(itemsMissTab);
		//actionBar.addTab(itemsNeedlessTab);
		actionBar.addTab(itemsSuggTab);
		actionBar.getTabAt(0).select();
		return root;
	}

	List<Item> itemsMust;
	List<Item> itemsIn;
	List<Item> needlessItems;
	List<Item> missingItems;
	List<Item> copiedMustItems;

	public void onBundleMessageRecv(Bundle b) {
		BUNDLE_MESSAGE msg = BundleMessage.getInstance()
				.getBundleMessageType(b);
		switch (msg) {

		case CONTAINER_STATUS_UPDATE:
			itemsInFragment.setParentFragment(this);
			itemsMissFragment.setParentFragment(this);
			//itemsNeedlessFragment.setParentFragment(this);
			
			statusUpdate = ContainerStateUpdate.fromJSON(BundleMessage
					.getInstance().extractObject(b));
			String currentActivity = statusUpdate.getActivity().getName();
			currentActivityView.setText("Aktuelle Aktivität: "
					+ currentActivity);

			itemsIn = statusUpdate.getItemList();
			itemsMust = statusUpdate.getActivity().getItemsForActivity();
			needlessItems = statusUpdate.getNeedlessItems();
			missingItems = statusUpdate.getMissingItems();
			copiedMustItems = new ArrayList<Item>(itemsMust);

			itemsInTab.setText(String.format("Enthalten" + " (%d)",itemsIn.size()));
			//itemsNeedlessTab.setText(String.format("Überflüssig (%d)",needlessItems.size()));
			itemsMissTab.setText(String.format("Fehlend (%d)",missingItems.size()));
			
			itemsInFragment.updateView(statusUpdate);
			itemsMissFragment.updateView(statusUpdate);
			//itemsNeedlessFragment.updateView(statusUpdate);
			
			
			break;
			
		
		default:
			break;
		}
	}

//	private void debugMessage(ContainerStateUpdate update){
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("Update: \n");
//		sb.append("Items in Bag:");
//		sb.append("\n");
//
//		sb.append("\n");
//		sb.append("Activity: ");
//		sb.append(statusUpdate.getActivity().getName());
//		sb.append("\n");
//		sb.append("Items for activity:");
//		sb.append("\n");
//
//		sb.append("\n");
//		Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG)
//				.show();
//	}
	public synchronized ContainerStateUpdate getItemUpdate() {
		return statusUpdate;
	}

	

	@Override
	public void onDetach() {
		bmActor.unregister(getActivity());
		super.onDetach();
	}

	@Override
	public void onBundleMessageSend(Bundle b) {

	}

	@Override
	public void onResponseMessage(Bundle b) {

	}

	@Override
	public void onResponseAnswerMessage(Bundle b) {

	}

	@Override
	public void onStatusMessage(Bundle b) {
		StatusCode status = StatusCode.getStatusCode(b);
		switch (status){
		case CONNECTED:
			bmHelper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.CONTAINER_STATUS_UPDATE_REQUEST, ""));
			break;
		case DISCONNECTED:
			break;
		case ERROR:
			break;
		case SCAN_DEVICES_DONE:
			break;
		default:
			break;
		}
	}

	@Override
	public void onCommandMessage(Bundle b) {

	}

	@Override
	public void onError(Exception e) {

	}

	@Override
	public void onResume() {
		super.onResume();
		
		bmActor = new BundleMessageActor(this);
		bmActor.register(getActivity());
		bmHelper = new BundleMessageHelper(getActivity());
		//bmHelper.sendCommandBundle(Command.RESEND_STATUS.toBundle());
		
		
		
		
	}

}
