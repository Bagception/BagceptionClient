package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandReceiverCallbacks;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ItemCommand;
import de.uniulm.bagception.client.R;

public class AllItemsFragment extends Fragment implements BundleMessageReactor{
	
	ListView listView;
	ItemListArrayAdapter ad;
	String bla;
	BundleMessageActor actor;
	BundleMessageHelper helper;
	public static Fragment newInstance(Context context) {
		AllItemsFragment f = new AllItemsFragment();

		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actor = new BundleMessageActor(this);
		helper = new BundleMessageHelper(getActivity());
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_all_items, null);
		listView = (ListView) root.findViewById(R.id.listViewAllItems);
		ad = new ItemListArrayAdapter(getActivity());
		listView.setAdapter(ad);
		return root;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		actor.register(getActivity()); //necessary?
		helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ItemCommand.list()));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		actor.unregister(getActivity()); //necessary?
	}
	
	//BundleMessageReactor

	@Override
	public void onBundleMessageRecv(Bundle b) {
		BUNDLE_MESSAGE msg = BundleMessage.getInstance().getBundleMessageType(b);
		switch (msg){
		case ADMINISTRATION_COMMAND:
			AdministrationCommand<?> a_cmd = AdministrationCommand.fromJSONObject(BundleMessage.getInstance().extractObject(b));
			AdministrationCommandProcessor adminCommandProcessor = new AdministrationCommandProcessor(){
				@Override
				public void onItemList(AdministrationCommand<Item> i) {
					//item list
					Item[] theItemsWeWantToDisplay = i.getPayloadObjects();
					ad.clear();
					ad.addAll(theItemsWeWantToDisplay);
				}
			};
			a_cmd.accept(adminCommandProcessor);

			break;
			
			default: break;
		}
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
