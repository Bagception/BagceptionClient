package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TwoLineListItem;
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

public class CreateNewPlaceFragment extends Fragment implements
		BundleMessageReactor {

	EditText editName;
	EditText editAddress;
	Button send;
	ShowMap showMap;
	Button cancel;
	Button bt;
	Button wlan;
	Button resolveAddress;
	BundleMessageActor actor;
	WifiBTDevice device;
	Location resultLocation;
	AlertDialog.Builder btAlert;
	AlertDialog.Builder wifiAlert;
	ArrayAdapter<String> btArrayAdapter;
	ArrayAdapter<String> wifiArrayAdapter;
	ArrayList<String> btDevices = new ArrayList<String>();
	ArrayList<String> wifiDevices = new ArrayList<String>();

	static Fragment newInstance(Context context) {
		CreateNewPlaceFragment f = new CreateNewPlaceFragment();

		return f;
	}
	
	@Override
	public void onStart() {
		
		super.onStart();
		actor.register(getActivity());
	}

	@Override
	public void onStop() {
		
		super.onStop();
		actor.unregister(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_create_new_place, null);
		editName = (EditText) root.findViewById(R.id.editName);
		editAddress = (EditText) root.findViewById(R.id.editAddress);
		send = (Button) root.findViewById(R.id.send);
		cancel = (Button) root.findViewById(R.id.cancelPlace);
		bt = (Button) root.findViewById(R.id.btButton);
		wlan = (Button) root.findViewById(R.id.wlanButton);
		resolveAddress = (Button) root.findViewById(R.id.resolveAddress);
		showMap = new ShowMap();
		actor = new BundleMessageActor(this);
		
		resolveAddress.setOnClickListener(new OnClickListener(
				) {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Location locAddress = new Location(editAddress.getText().toString(), "");
				Log.d("TEST", "Adresse: " + editAddress.getText().toString());
				new BundleMessageHelper(getActivity())
				.sendMessageSendBundle(BundleMessage.getInstance()
						.createBundle(BUNDLE_MESSAGE.RESOLVE_ADDRESS_REQUEST, locAddress));
				
			}
		});

		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btDevices.clear();
				btAlert = new AlertDialog.Builder(getActivity());
				btAlert.setTitle("Bluetooth Devices");
				
				btArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item, btDevices);
				
				new BundleMessageHelper(getActivity())
						.sendMessageSendBundle(BundleMessage.getInstance()
								.createBundle(BUNDLE_MESSAGE.BLUETOOTH_SEARCH_REQUEST, null));
				btAlert.setAdapter(btArrayAdapter, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d("TEST", "item clicked:" + which);
						Log.d("TEST", "items name: " + btDevices.get(which));
					}
				});
				btAlert.create().show();
			}
		});

		wlan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wifiDevices.clear();
				wifiAlert = new AlertDialog.Builder(
						getActivity());

				wifiAlert.setTitle("Access Points");
				wifiArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item, wifiDevices);
				
				new BundleMessageHelper(getActivity())
						.sendMessageSendBundle(BundleMessage.getInstance()
								.createBundle(BUNDLE_MESSAGE.WIFI_SEARCH_REQUEST,	null));
				wifiAlert.setAdapter(wifiArrayAdapter, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d("TEST", "item clicked:" + which);
						Log.d("TEST", "items name: " + wifiDevices.get(which));
					}
				});
				wifiAlert.create().show();
			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO !!!
				
				Location location = new Location(-1, editName.getText().toString(), resultLocation.getLat(), resultLocation.getLng(), resultLocation.getRadius(), "5");
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
		Log.d("TEST", "Kam was an");
		switch (BundleMessage.getInstance().getBundleMessageType(b)) {
		case WIFI_SEARCH_REPLY: {
			device = WifiBTDevice.fromJSON(BundleMessage.getInstance()
					.extractObject(b));
			Log.d("TEST", device.getName() + " " + device.getMac());
			wifiDevices.add(device.getName() + " " + device.getMac());
			wifiArrayAdapter.notifyDataSetChanged();

			break;
		}
		case BLUETOOTH_SEARCH_REPLY:{
			device = WifiBTDevice.fromJSON(BundleMessage.getInstance()
					.extractObject(b));
			Log.d("TEST", device.getName() + " " + device.getMac());
			btDevices.add(device.getMac());
			btArrayAdapter.notifyDataSetChanged();
			break;
		}
		case RESOLVE_ADDRESS_REPLY:{
			//Location location 
			resultLocation = Location.fromJSON(BundleMessage.getInstance()
					.extractObject(b));
			Log.d("TEST", resultLocation.getLat() + " " + resultLocation.getLng());
			break;
		}
		case RESOLVE_COORDS_REPLY:{
			resultLocation = Location.fromJSON(BundleMessage.getInstance()
					.extractObject(b));
			Log.d("TEST", resultLocation.getName());
			editAddress.setText(resultLocation.getName());
			break;

		}
		default:
			break;
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
