package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
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
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.client.R;

public abstract class BasicListEntitiesFragment<E> extends Fragment implements BundleMessageReactor{


	private ListView listView;
	protected ArrayAdapter<E> listAdapter;
	
	private BundleMessageActor actor;
	private BundleMessageHelper helper;

	/**
	 * 
	 * @return  the ArrayAdapter for the corresponding entity 
	 */
	protected abstract ArrayAdapter<E> getEntityAdapter();
	
	
	protected abstract AdministrationCommand<E> getAdminCommandRequest();
	
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
		listAdapter = getEntityAdapter();
		listView.setAdapter(listAdapter);
		return root;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		actor.register(getActivity()); //necessary?
		AdministrationCommand<?> cmd =getAdminCommandRequest();
		helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,cmd ));
		
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
				onAdminCommand(a_cmd);
			break;
			
			default: break;
			}
		}
	
	
	public abstract void onAdminCommand(AdministrationCommand<?> a_cmd);

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
		
	}

	@Override
	public void onCommandMessage(Bundle b) {
		
	}

	@Override
	public void onError(Exception e) {
		
	}

}
