package de.uniulm.bagception.client.ui.launcher;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ActivityCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.LocationCommand;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.osm.ShowMap;

public class CreateNewPlaceFragment extends Fragment {

	EditText editName;
	Button send;
	ShowMap showMap;
	Button cancel;
	Button bt;
	Button wlan;
	ListView btListView;
	ListAdapter adapter;

	public static Fragment newInstance(Context context) {
		CreateNewPlaceFragment f = new CreateNewPlaceFragment();

		return f;
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
		
		final String[] test = {"1", "2"};

		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String names[] = {"1", "2"};
				// TODO Auto-generated method stub
				AlertDialog.Builder dialogAlert = new AlertDialog.Builder(
						getActivity());
				dialogAlert.setTitle("BT");
				ListView lv = (ListView) root.findViewById(R.id.btListview);
				ListAdapter ladapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, test);
				lv.setAdapter(ladapter);
				dialogAlert.create().show();
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

}
