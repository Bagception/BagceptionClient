package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
	TextView latView;
	TextView lngView;
	TextView btView;
	TextView wlanView;
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
		getActivity().getActionBar().setTitle("Ort hinzufügen");
		getActivity().getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#0099CC")));
		editName = (EditText) root.findViewById(R.id.editName);
		editAddress = (EditText) root.findViewById(R.id.editAddress);
		btView = (TextView) root.findViewById(R.id.btView);
		latView = (TextView) root.findViewById(R.id.latitudeView);
		lngView = (TextView) root.findViewById(R.id.longitudeView);
		wlanView = (TextView) root.findViewById(R.id.wlanView);
		send = (Button) root.findViewById(R.id.sendPlaceBtn);
		cancel = (Button) root.findViewById(R.id.cancelPlaceBtn);
		bt = (Button) root.findViewById(R.id.btButton);
		wlan = (Button) root.findViewById(R.id.wlanButton);
		resolveAddress = (Button) root.findViewById(R.id.resolveAddress);
		showMap = new ShowMap();
		actor = new BundleMessageActor(this);

		resolveAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Location locAddress = new Location(editAddress.getText()
						.toString(), "");
				Log.d("TEST", "Adresse: " + editAddress.getText().toString());
				new BundleMessageHelper(getActivity())
						.sendMessageSendBundle(BundleMessage.getInstance()
								.createBundle(
										BUNDLE_MESSAGE.RESOLVE_ADDRESS_REQUEST,
										locAddress));

			}
		});

		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btDevices.clear();
				btAlert = new AlertDialog.Builder(getActivity());
				btAlert.setTitle("Bluetooth Devices");

				btArrayAdapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_selectable_list_item, btDevices);

				new BundleMessageHelper(getActivity())
						.sendMessageSendBundle(BundleMessage
								.getInstance()
								.createBundle(
										BUNDLE_MESSAGE.BLUETOOTH_SEARCH_REQUEST,
										null));
				btAlert.setAdapter(btArrayAdapter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								btView.setText("BT: "
										+ btDevices.get(which).toString());
							}
						});
				btAlert.create().show();
			}
		});

		wlan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wifiDevices.clear();
				wifiAlert = new AlertDialog.Builder(getActivity());

				wifiAlert.setTitle("Access Points");
				wifiArrayAdapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_selectable_list_item,
						wifiDevices);

				new BundleMessageHelper(getActivity())
						.sendMessageSendBundle(BundleMessage.getInstance()
								.createBundle(
										BUNDLE_MESSAGE.WIFI_SEARCH_REQUEST,
										null));
				wifiAlert.setAdapter(wifiArrayAdapter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								wlanView.setText("WLAN: "
										+ wifiDevices.get(which).toString());
							}
						});
				wifiAlert.create().show();
			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// float lat = -1;
				// float lon = -1;
				//
				// if(latView.getText().toString() != null||
				// lngView.getText().toString() != null){
				//
				// Log.w("TEST", "Latitude: " + latView.getText());
				// Log.w("TEST", "Longitude: " + lngView.getText());
				//
				//
				// lat = Float.parseFloat(latView.getText().toString());
				// lon = Float.parseFloat(lngView.getText().toString());
				// }
				if ("".equals(editName.getText().toString().trim())
						|| resultLocation == null) {
					AlertDialog.Builder dialogAlert = new AlertDialog.Builder(
							getActivity());
					dialogAlert.setTitle("Bitte alle Felder ausfüllen");
					dialogAlert.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					dialogAlert.create().show();
				} else {
					String mac;
					if (device == null) {
						mac = "";
					}else {
						mac = device.getMac();
					}
					Location location = new Location(-1, editName.getText()
							.toString(), lat,
							lng,
							resultLocation.getRadius(), mac);
					
					Log.d("TEST", "loc alt: " + location.toString());
					BundleMessageHelper helper = new BundleMessageHelper(
							getActivity());
					helper.sendMessageSendBundle(BundleMessage.getInstance()
							.createBundle(
									BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
									LocationCommand.add(location)));

					getActivity().finish();
					// Intent intent = new Intent(getActivity(), MainGUI.class);
					// startActivity(intent);
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editName.setText("");

				getActivity().finish();
				// Intent intent = new Intent(getActivity(), MainGUI.class);
				// startActivity(intent);
			}
		});

		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	Float lat;
	Float lng;
	
	@Override
	public void onBundleMessageRecv(Bundle b) {
		switch (BundleMessage.getInstance().getBundleMessageType(b)) {
		case WIFI_SEARCH_REPLY: {
			device = WifiBTDevice.fromJSON(BundleMessage.getInstance()
					.extractObject(b));
			Log.d("TEST", device.getName() + " " + device.getMac());
			wifiDevices.add(device.getName() + " " + device.getMac());
			wifiArrayAdapter.notifyDataSetChanged();

			break;
		}
		case BLUETOOTH_SEARCH_REPLY: {
			device = WifiBTDevice.fromJSON(BundleMessage.getInstance()
					.extractObject(b));
			Log.d("TEST", device.getName() + " " + device.getMac());
			btDevices.add(device.getMac());
			btArrayAdapter.notifyDataSetChanged();
			break;
		}
		case RESOLVE_ADDRESS_REPLY: {
			// Location location
			Log.w("TEST", "Hole mir jetzt die Location");
			resultLocation = Location.fromJSON(BundleMessage.getInstance()
					.extractObject(b));
			latView.setText("Lat: " + resultLocation.getLat());
			lngView.setText("Long: " + resultLocation.getLng());
			lng = resultLocation.getLng();
			lat = resultLocation.getLat();
			Log.w("TEST",
					resultLocation.getLat() + " und " + resultLocation.getLng());
			break;
		}
		case RESOLVE_COORDS_REPLY: {
			Log.d("TEST", "received");
			resultLocation = Location.fromJSON(BundleMessage.getInstance()
					.extractObject(b));
			Log.d("TEST", "result" + resultLocation.getName());
			editAddress.setText(resultLocation.getName());
			lng = resultLocation.getLng();
			lat = resultLocation.getLat();
			Log.d("TEST", latView.getText() + "and" + lng.toString());
			
			Location locAddress = new Location(editAddress.getText()
					.toString(), "");
			Log.d("TEST", "Adresse: " + editAddress.getText().toString());
			new BundleMessageHelper(getActivity())
					.sendMessageSendBundle(BundleMessage.getInstance()
							.createBundle(
									BUNDLE_MESSAGE.RESOLVE_ADDRESS_REQUEST,
									locAddress));
			break;

		}
		default:
			break;
		}
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
	}

	@Override
	public void onCommandMessage(Bundle b) {
	}

	@Override
	public void onError(Exception e) {
	}
}
