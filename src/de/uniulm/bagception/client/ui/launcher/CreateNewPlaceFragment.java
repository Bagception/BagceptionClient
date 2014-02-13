package de.uniulm.bagception.client.ui.launcher;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.bundlemessageprotocol.entities.WifiBTDevice;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.LocationCommand;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.osm.ShowMap;

public class CreateNewPlaceFragment extends Fragment implements BundleMessageReactor{

	EditText editName;
	Button send;
	ShowMap showMap;
	Button cancel;
	Button bt;
	Button wlan;
	BundleMessageActor actor;

	
	static Fragment newInstance(Context context) {
		CreateNewPlaceFragment f = new CreateNewPlaceFragment();

		return f;
	}

	@Override
	public void onResume() {
		actor.register(getActivity());
		super.onResume();
	}
	
	@Override
	public void onPause() {
		actor.unregister(getActivity());
		super.onPause();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_create_new_place, null);
		editName = (EditText) root.findViewById(R.id.editName);
		send = (Button) root.findViewById(R.id.send);
		cancel = (Button) root.findViewById(R.id.cancelPlace);
		bt = (Button) root.findViewById(R.id.btButton);
		wlan = (Button) root.findViewById(R.id.wlanButton);
		showMap = new ShowMap();
		actor = new BundleMessageActor(this);
		

			
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String names[] = {"1", "2"};
				// TODO Auto-generated method stub
				new BundleMessageHelper(getActivity()).sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.WIFI_SEARCH_REQUEST, null));
				
				
				AlertDialog.Builder btAlert = new AlertDialog.Builder(
						getActivity());
				
				
				
				btAlert.setTitle("BT");

				final CharSequence[] test = {"1", "2"};
				btAlert.setItems(test, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						
					}
				});
				btAlert.create().show();
			}
		});
		
		wlan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String names[] = {"1", "2"};
				// TODO Auto-generated method stub
				AlertDialog.Builder wlanAlert = new AlertDialog.Builder(
						getActivity());
				
				wlanAlert.setTitle("BT");

				final CharSequence[] test = {"1", "2"};
				wlanAlert.setItems(test, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				wlanAlert.create().show();
			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Location location = new Location(editName.getText().toString(),
						"testMAC");

				BundleMessageHelper helper = new BundleMessageHelper(
						getActivity());
				helper.sendMessageSendBundle(BundleMessage.getInstance()
						.createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
								LocationCommand.add(location)));

				Intent intent = new Intent(getActivity(), MainGUI.class);
				startActivity(intent);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editName.setText("");
			}
		});

		return root;
	}


	
	@Override
	public void onBundleMessageRecv(Bundle b) {
		switch(BundleMessage.getInstance().getBundleMessageType(b)){
		case WIFI_SEARCH_REPLY:{
			WifiBTDevice device = WifiBTDevice.fromJSON(BundleMessage.getInstance().extractObject(b));
			break;
		}
		default:break;
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
