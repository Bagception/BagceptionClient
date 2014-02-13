package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.HashSet;

import android.R.integer;
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
import android.widget.ListAdapter;
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
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ActivityCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ItemCommand;
import de.uniulm.bagception.client.R;

public class CreateNewActivityFragment extends Fragment implements BundleMessageReactor{

	ArrayList<Item> itemsForActivity;
	EditText editName;
	Button send;
	Button cancel;
	Button addPlace;
	Button addActivityItems;
	BundleMessageActor bmActor;
	ListView listView;
	ArrayAdapter<Item> listadapter;
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

		listView.setAdapter(listadapter);
		
		bmActor = new BundleMessageActor(this);
		
		addPlace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String names[] = { "1", "2" };
				// TODO Auto-generated method stub
				AlertDialog.Builder placeAlert = new AlertDialog.Builder(
						getActivity());

				placeAlert.setTitle("BT");

				final CharSequence[] test = { "1", "2" };
				placeAlert.setItems(test, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
				placeAlert.create().show();
			}
		});
		
		
		addActivityItems = (Button) root.findViewById(R.id.addActivityItem);
		
		addActivityItems.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				
				new BundleMessageHelper(getActivity()).sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ItemCommand.list()));
				
			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Activity activity = new Activity(editName.getText().toString(), itemsForActivity);

				BundleMessageHelper helper = new BundleMessageHelper(
						getActivity());
				helper.sendMessageSendBundle(BundleMessage.getInstance()
						.createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
								ActivityCommand.add(activity)));

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
		case ADMINISTRATION_COMMAND:{
			AdministrationCommandProcessor p = new AdministrationCommandProcessor(){
				public void onItemList(de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand<de.uniulm.bagception.bundlemessageprotocol.entities.Item> i) {
					final Item[] items = i.getPayloadObjects();
					final HashSet<Integer> checkedItems = new HashSet<Integer>();
					String[] itemStrings = new String[items.length];
					for (int iter=0;iter<itemStrings.length;iter++){
						itemStrings[iter] = items[iter].getName();
					}
					
					AlertDialog.Builder itemAlert = new AlertDialog.Builder(getActivity());
					itemAlert.setTitle("Items zur Activity hinzufügen");
					itemAlert.setMultiChoiceItems(itemStrings, null, new DialogInterface.OnMultiChoiceClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							if (isChecked){
								checkedItems.add(which);
							}else{
								checkedItems.remove(which);
							}
						}
					});
					itemAlert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ArrayList<Item> selectedItems  = new ArrayList<Item>();
							for (int checked: checkedItems){
								selectedItems.add(items[checked]);
							}
							itemsForActivity = selectedItems;
						
							Log.w("TEST", "ItemsForActivity: " + itemsForActivity);
							
							listadapter = new ArrayAdapter<Item>(getActivity(), R.layout.fragment_create_new_activity, itemsForActivity);
							Log.w("TEST", "ListAdapter: " + listadapter);
						}
					});
					itemAlert.create().show();
					
					
				}
				
			};
			AdministrationCommand.fromJSONObject(BundleMessage.getInstance().extractObject(b)).accept(p);
			
			//listadapter = new ArrayAdapter<Item>(getActivity(), R.layout.fragment_create_new_activity, R.id.itemTextView, itemsForActivity);
			
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
