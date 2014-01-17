package de.uniulm.bagception.client.ui.launcher;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.client.R;

public class OverviewFragment extends Fragment implements BundleMessageReactor {

	public static Context appContext;
	// private BundleMessageActor bmActor;
	// private ListView itemsStatusView;
	// private ItemListArrayAdapter itemListAd;
	// private int count = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_TABS);
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_overview, null);
		getActivity().setTitle("Übersicht");

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// initiating both tabs and set text to it.
		ActionBar.Tab ItemsInTab = actionBar.newTab().setText("Enthalten");
		ActionBar.Tab ItemsMissTab = actionBar.newTab().setText("Fehlend");
		ActionBar.Tab ItemsRedundantTab = actionBar.newTab().setText("Doppelt");
		ActionBar.Tab ItemsSuggTab = actionBar.newTab().setText("Vorschlag");

		Fragment ItemsInFragment = new ItemsInFragment();
		Fragment ItemsMissFragment = new ItemsMissFragment();
		Fragment ItemsRedundantFragment = new ItemsRedundantFragment();
		Fragment ItemsSuggFragment = new ItemsSuggFragment();

		ItemsInTab.setTabListener(new ItemTabListener(ItemsInFragment));
		ItemsMissTab.setTabListener(new ItemTabListener(ItemsMissFragment));
		ItemsRedundantTab.setTabListener(new ItemTabListener(
				ItemsRedundantFragment));
		ItemsSuggTab.setTabListener(new ItemTabListener(ItemsSuggFragment));
		
		actionBar.addTab(ItemsInTab);
		actionBar.addTab(ItemsMissTab);
		actionBar.addTab(ItemsRedundantTab);
		actionBar.addTab(ItemsSuggTab);

		// itemsStatusView = (ListView) root.findViewById(R.id.itemsOverview);
		// itemListAd = new ItemListArrayAdapter(getActivity());
		// itemsStatusView.setAdapter(itemListAd);

		return root;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// bmActor = new BundleMessageActor(this);
		// bmActor.register(getActivity());
	}

	@Override
	public void onDetach() {
		// bmActor.unregister(getActivity());
		super.onDetach();
	}

//	 @Override
//	 public void onBundleMessageRecv(Bundle b) {
//	 BUNDLE_MESSAGE msg = BundleMessage.getInstance()
//	 .getBundleMessageType(b);
//	 switch (msg) {
//	 case ITEM_FOUND:
//	 Item i;
//	 try {
//	 i = BundleMessage.getInstance().toItemFound(b);
//	
//	 Toast.makeText(getActivity(), "Item found: " + i.getName(),
//	 Toast.LENGTH_SHORT).show();
//	 } catch (Exception e) {
//	 e.printStackTrace();
//	 }
//	 break;
//	 case ITEM_NOT_FOUND:
//	 try {
//	 i = BundleMessage.getInstance().toItemFound(b);
//	 Toast.makeText(getActivity(),
//	 "item not found: " + i.getIds().get(0),
//	 Toast.LENGTH_SHORT).show();
//	 } catch (Exception e) {
//	 e.printStackTrace();
//	 }
//	 break;
//	
//	 case CONTAINER_STATUS_UPDATE:
//	 ContainerStateUpdate statusUpdate = ContainerStateUpdate
//	 .fromJSON(BundleMessage.getInstance().extractObject(b));
//	
//	 StringBuilder sb = new StringBuilder();
//	 List<Item> itemsIs = statusUpdate.getItemList();
//	 List<Item> itemsMust = statusUpdate.getActivity()
//	 .getItemsForActivity();
//	 sb.append("Update: \n");
//	 sb.append("Items in Bag:");
//	 sb.append("\n");
//	 for (Item item : itemsIs) {
//	 sb.append(item.getName());
//	 sb.append("\n");
//	 itemListAd.clear();
//	 itemListAd.add(item);
//	 //itemListAd.add(count);
//	 }
//	 sb.append("\n");
//	 sb.append("Activity: ");
//	 sb.append(statusUpdate.getActivity().getName());
//	 sb.append("\n");
//	 sb.append("Items for activity:");
//	 sb.append("\n");
//	
//	 for (Item item : itemsMust) {
//	 sb.append(item.getName());
//	 sb.append("\n");
//	 }
//	 sb.append("\n");
//	 Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG)
//	 .show();
//	 break;
//	
//	 default:
//	 break;
//	
//	 }
//	 }

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
