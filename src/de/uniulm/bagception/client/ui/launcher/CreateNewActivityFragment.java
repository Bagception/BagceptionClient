package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.HashSet;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ActivityCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ItemCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.LocationCommand;
import de.uniulm.bagception.client.R;

public class CreateNewActivityFragment extends Fragment implements
		BundleMessageReactor {

	ArrayList<Item> itemsForActivity;
	Location locationForActivity;
	EditText editName;
	Button send;
	Button cancel;
	Button addPlace;
	Button addActivityItems;
	BundleMessageActor bmActor;
	ListView listView;
	ArrayAdapter<String> listadapter;
	TextView placeView;

	public static Fragment newInstance(Context context) {
		CreateNewItemFragment f = new CreateNewItemFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_create_new_activity, null);
		editName = (EditText) root.findViewById(R.id.editActivity);
		send = (Button) root.findViewById(R.id.sendActivity);
		cancel = (Button) root.findViewById(R.id.cancelActivity);
		addPlace = (Button) root.findViewById(R.id.addLocation);

		listView = (ListView) root.findViewById(R.id.itemView);
		placeView = (TextView) root.findViewById(R.id.viewPlace);
		listadapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1);
		listView.setAdapter(listadapter);

		bmActor = new BundleMessageActor(this);

		addPlace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new BundleMessageHelper(getActivity())
						.sendMessageSendBundle(BundleMessage.getInstance()
								.createBundle(
										BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
										LocationCommand.list()));

			}
		});

		addActivityItems = (Button) root.findViewById(R.id.addActivityItem);

		addActivityItems.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new BundleMessageHelper(getActivity())
						.sendMessageSendBundle(BundleMessage.getInstance()
								.createBundle(
										BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
										ItemCommand.list()));

			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.w("TEST", "EditName: " + editName.getText());
				if ("".equals(editName.getText().toString().trim())) {

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
					String name = editName.getText().toString();

					Activity activity = new Activity(name, itemsForActivity,
							locationForActivity);
					Log.w("TEST", "Die erstellte Activity: " + activity);

					BundleMessageHelper helper = new BundleMessageHelper(
							getActivity());
					helper.sendMessageSendBundle(BundleMessage.getInstance()
							.createBundle(
									BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
									ActivityCommand.add(activity)));

					Intent intent = new Intent(getActivity(), MainGUI.class);
					startActivity(intent);
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editName.setText("");

			}
		});

		return root;
	}

	@Override
	public void onBundleMessageRecv(Bundle b) {
		switch (BundleMessage.getInstance().getBundleMessageType(b)) {
		case ADMINISTRATION_COMMAND: {
			AdministrationCommandProcessor p = new AdministrationCommandProcessor() {

				@Override
				public void onLocationList(
						de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand<de.uniulm.bagception.bundlemessageprotocol.entities.Location> i) {
					final Location[] locations = i.getPayloadObjects();
					final long[] locationIDs = new long[locations.length];
					final String[] locationNames = new String[locations.length];
					final float[] locationLat = new float[locations.length];
					final float[] locationLon = new float[locations.length];
					final int[] locationRadius = new int[locations.length];
					final String[] locationMAC = new String[locations.length];

					for (int iter = 0; iter < locationNames.length; iter++) {
						locationIDs[iter] = locations[iter].getId();
						locationNames[iter] = locations[iter].getName();
						locationLat[iter] = locations[iter].getLat();
						locationLon[iter] = locations[iter].getLng();
						locationRadius[iter] = locations[iter].getRadius();
						locationMAC[iter] = locations[iter].getMac();
					}

					AlertDialog.Builder locationAlert = new AlertDialog.Builder(
							getActivity());
					locationAlert.setTitle("Ort zur Activity hinzufügen");

					locationAlert.setItems(locationNames,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									locationForActivity = new Location(
											locationNames[which], null);
									locationForActivity = new Location(
											locationIDs[which],
											locationNames[which],
											locationLat[which],
											locationLon[which],
											locationRadius[which],
											locationMAC[which]);

									placeView.setText(locationNames[which]);
								}
							});
					locationAlert.create().show();

				}

				public void onItemList(
						de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand<de.uniulm.bagception.bundlemessageprotocol.entities.Item> i) {
					final Item[] items = i.getPayloadObjects();
					final HashSet<Integer> checkedItems = new HashSet<Integer>();
					String[] itemStrings = new String[items.length];
					for (int iter = 0; iter < itemStrings.length; iter++) {
						itemStrings[iter] = items[iter].getName();
					}

					AlertDialog.Builder itemAlert = new AlertDialog.Builder(
							getActivity());
					itemAlert.setTitle("Items zur Activity hinzufügen");
					itemAlert.setMultiChoiceItems(itemStrings, null,
							new DialogInterface.OnMultiChoiceClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									if (isChecked) {
										checkedItems.add(which);
									} else {
										checkedItems.remove(which);
									}
								}
							});
					itemAlert.setPositiveButton("ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									ArrayList<String> selectedItems = new ArrayList<String>();
									ArrayList<Item> itemsSelected = new ArrayList<Item>();

									for (int checked : checkedItems) {
										selectedItems.add(items[checked]
												.getName());
										itemsSelected.add(items[checked]);

										Log.d("TEST", selectedItems.toString());
									}
									Toast.makeText(getActivity(),
											selectedItems.toString(),
											Toast.LENGTH_LONG).show();
									listadapter.addAll(selectedItems);
									listView.invalidate();
									itemsForActivity = itemsSelected;
									Log.w("TEST",
											"ItemsForActivity wird gefüllt mit: "
													+ itemsForActivity);
								}

							});
					itemAlert.create().show();

				}

			};
			AdministrationCommand.fromJSONObject(
					BundleMessage.getInstance().extractObject(b)).accept(p);

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

	}

	@Override
	public void onCommandMessage(Bundle b) {

	}

	@Override
	public void onError(Exception e) {

	}

	@Override
	public void onResume() {
		bmActor.register(getActivity());
		super.onResume();
	}

	@Override
	public void onPause() {
		bmActor.unregister(getActivity());
		super.onPause();
	}

}
