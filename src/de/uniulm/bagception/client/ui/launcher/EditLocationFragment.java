package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class EditLocationFragment extends Fragment implements
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
		getActivity().getActionBar().setTitle("Ort bearbeiten");
		getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));
		editName = (EditText) root.findViewById(R.id.editName);
		editAddress = (EditText) root.findViewById(R.id.editAddress);
		latView = (TextView) root.findViewById(R.id.latitudeView);
		lngView = (TextView) root.findViewById(R.id.longitudeView);
		wlanView = (TextView) root.findViewById(R.id.wlanView);
		send = (Button) root.findViewById(R.id.sendPlaceBtn);
		cancel = (Button) root.findViewById(R.id.cancelPlaceBtn);
		wlan = (Button) root.findViewById(R.id.wlanButton);
		resolveAddress = (Button) root.findViewById(R.id.resolveAddress);
		showMap = new ShowMap();
		actor = new BundleMessageActor(this);

		Location location = null;
		String i = getArguments().getString("ENTITYSTRING");
		org.json.simple.JSONObject obj = new org.json.simple.JSONObject();
		JSONParser p = new JSONParser();
		try {
			obj = (org.json.simple.JSONObject) p.parse(i);
			location = Location.fromJSON(obj);
			Log.d("TEST", "loc: " + location.toString());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		final Location oldLocation = location;

		editName.setText(location.getName());

		new BundleMessageHelper(getActivity())
				.sendMessageSendBundle(BundleMessage.getInstance()
						.createBundle(BUNDLE_MESSAGE.RESOLVE_COORDS_REQUEST,
								oldLocation));

		Log.d("TEST", location.getLat().toString());
		latView.setText(location.getLat().toString());
		lngView.setText(location.getLng().toString());
		
		Log.w("DEBUG", "Location beim Editieren: " + location);
		if(location.getMac() != null){
			wlanView.setText("WLAN: " + location.getMac().toString());
		}
		
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

			Location newLocation = null;

			@Override
			public void onClick(View v) {

				if ("".equals(editName.getText().toString().trim()) || resultLocation == null) {
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
					String mac = null;
					if(device != null){
						mac = device.getMac();
					}
					newLocation = new Location(-1, editName.getText()
							.toString(), resultLocation.getLat(), resultLocation.getLng(), resultLocation.getRadius(),
							mac);
					BundleMessageHelper helper = new BundleMessageHelper(
							getActivity());
					helper.sendMessageSendBundle(BundleMessage.getInstance()
							.createBundle(
									BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
									LocationCommand.edit(oldLocation,
											newLocation)));

					getActivity().finish();
//					Intent intent = new Intent(getActivity(), MainGUI.class);
//					startActivity(intent);
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editName.setText("");
				getActivity().finish();
			}
		});

		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

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
			Log.w("TEST", "Hole mir jetzt die Location");
			resultLocation = Location.fromJSON(BundleMessage.getInstance()
					.extractObject(b));
			latView.setText("" + resultLocation.getLat());
			lngView.setText("" + resultLocation.getLng());
			Log.w("TEST",
					resultLocation.getLat() + " und " + resultLocation.getLng());
			break;
		}
		case RESOLVE_COORDS_REPLY: {
			Log.d("TEST", "received");
			resultLocation = Location.fromJSON(BundleMessage.getInstance()
					.extractObject(b));
			Log.d("TEST", "result" + resultLocation.getName());
			latView.setText("" + resultLocation.getLat());
			lngView.setText("" + resultLocation.getLng());
			editAddress.setText(resultLocation.getName());
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
