package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.bluetooth.pairing.BluetoothDeviceArrayAdapter;

public class OverviewFragment extends Fragment implements BundleMessageReactor {
	
//	private BundleMessageActor bmActor;
//	private ListView itemsStatusView;
//	private ItemListArrayAdapter itemListAd;
//	private int count = 0;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_overview, null);
		getActivity().setTitle("Übersicht");
		
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//initiating both tabs and set text to it.
		ActionBar.Tab ItemsInTab = actionBar.newTab().setText("Enthalten");
		ActionBar.Tab ItemsMissTab = actionBar.newTab().setText("Fehlend");
		ActionBar.Tab ItemsRedundantTab = actionBar.newTab().setText("Doppelt");
		ActionBar.Tab ItemsSuggTab = actionBar.newTab().setText("Vorschlag");
		
//		Fragment ItemsInFragment = new ItemsInFragment();
//		Fragment ItemsMissFragment = new ItemsMissFragment();
//		Fragment ItemsRedundantFragment = new ItemsRedundantFragment();
//		Fragment ItemsSuggFragment = new ItemsSuggFragment();
//		
//		ItemsInTab.setTabListener(new ItemTabListener(ItemsInFragment));
//		ItemsMissTab.setTabListener(new ItemTabListener(ItemsMissFragment));
//		ItemsRedundantTab.setTabListener(new ItemTabListener(ItemsRedundantFragment));
//		ItemsInTab.setTabListener(new ItemTabListener(ItemsInFragment));
		
//		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
//			
//			@Override
//			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onTabSelected(Tab tab, FragmentTransaction ft) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onTabReselected(Tab tab, FragmentTransaction ft) {
//				// TODO Auto-generated method stub
//				
//			}
//		};
//
//		getActivity().getActionBar().addTab(
//	            getActivity().getActionBar().newTab()
//	            .setText("Enthalten")
//	            .setTabListener(tabListener));
//		
//		getActivity().getActionBar().addTab(
//	            getActivity().getActionBar().newTab()
//	            .setText("Vorschlag")
//	            .setTabListener(tabListener));
//		
//		getActivity().getActionBar().addTab(
//	            getActivity().getActionBar().newTab()
//	            .setText("Doppelt")
//	            .setTabListener(tabListener));
//		
//		getActivity().getActionBar().addTab(
//	            getActivity().getActionBar().newTab()
//	            .setText("Fehlend")
//	            .setTabListener(tabListener));
//		
		// Add 3 tabs, specifying the tab's text and TabListener
		/*for (int i1 = 0; i1 < 4; i1++) {
		    getActivity().getActionBar().addTab(
		            getActivity().getActionBar().newTab()
		            .setText("Tab " + (i1 + 1))
		            .setTabListener(tabListener));
		}*/
		

//		itemsStatusView = (ListView) root.findViewById(R.id.itemsOverview);
//		itemListAd = new ItemListArrayAdapter(getActivity());
//		itemsStatusView.setAdapter(itemListAd);
		
		return root;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		bmActor = new BundleMessageActor(this);
//		bmActor.register(getActivity());
	}

	@Override
	public void onDetach() {
//		bmActor.unregister(getActivity());
		super.onDetach();
	}

//	@Override
//	public void onBundleMessageRecv(Bundle b) {
//		BUNDLE_MESSAGE msg = BundleMessage.getInstance()
//				.getBundleMessageType(b);
//		switch (msg) {
//		case ITEM_FOUND:
//			Item i;
//			try {
//				i = BundleMessage.getInstance().toItemFound(b);
//
//				Toast.makeText(getActivity(), "Item found: " + i.getName(),
//						Toast.LENGTH_SHORT).show();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			break;
//		case ITEM_NOT_FOUND:
//			try {
//				i = BundleMessage.getInstance().toItemFound(b);
//				Toast.makeText(getActivity(),
//						"item not found: " + i.getIds().get(0),
//						Toast.LENGTH_SHORT).show();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			break;
//
//		case CONTAINER_STATUS_UPDATE:
//			ContainerStateUpdate statusUpdate = ContainerStateUpdate
//					.fromJSON(BundleMessage.getInstance().extractObject(b));
//
//			StringBuilder sb = new StringBuilder();
//			List<Item> itemsIs = statusUpdate.getItemList();
//			List<Item> itemsMust = statusUpdate.getActivity()
//					.getItemsForActivity();
//			sb.append("Update: \n");
//			sb.append("Items in Bag:");
//			sb.append("\n");
//			for (Item item : itemsIs) {
//				sb.append(item.getName());
//				sb.append("\n");
//				itemListAd.clear();
//				itemListAd.add(item);
//				//itemListAd.add(count);
//			}
//			sb.append("\n");
//			sb.append("Activity: ");
//			sb.append(statusUpdate.getActivity().getName());
//			sb.append("\n");
//			sb.append("Items for activity:");
//			sb.append("\n");
//
//			for (Item item : itemsMust) {
//				sb.append(item.getName());
//				sb.append("\n");
//			}
//			sb.append("\n");
//			Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG)
//					.show();
//			break;
//
//		default:
//			break;
//
//		}
//	}

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
