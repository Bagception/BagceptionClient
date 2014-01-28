package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.osm.ShowMap;

public class CreateNewPlaceFragment extends Fragment {
	
	EditText editName;
	Button send;
	ShowMap showMap;
	
	public static Fragment newInstance(Context context) {
		CreateNewPlaceFragment f = new CreateNewPlaceFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_create_new_place, null);
		editName = (EditText) root.findViewById(R.id.editName);
		send = (Button) root.findViewById(R.id.send);
		showMap = new ShowMap();
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				String locName =  editName.getText().toString();
				//Location location = new Location(1, locName, lat, lng, radius, mac);
				
				BundleMessageHelper helper = new BundleMessageHelper(getActivity());
				//helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, LocationCommand.add(location)));
				
			}
		});
		
		return root;
	}

}
