package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ToggleButton;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ItemCommand;
import de.uniulm.bagception.client.R;

public class CreateNewItemFragment extends Fragment {

	EditText editName;
	Button send;
	Button cancel;
	ToggleButton warm;
	ToggleButton cold;
	ToggleButton sunny;
	ToggleButton rainy;
	ToggleButton light;
	ToggleButton dark;
	Spinner spinner;
	ImageView iv;

	static Fragment newInstance(Context context) {
		CreateNewItemFragment f = new CreateNewItemFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_create_new_item, null);
		editName = (EditText) root.findViewById(R.id.editName);
		send = (Button) root.findViewById(R.id.sendItem);
		cancel = (Button) root.findViewById(R.id.cancelItem);
		iv = (ImageView) root.findViewById(R.id.itemIcon);
		warm = (ToggleButton) root.findViewById(R.id.warmButton);
		cold = (ToggleButton) root.findViewById(R.id.coldButton);
		sunny = (ToggleButton) root.findViewById(R.id.sunButton);
		rainy = (ToggleButton) root.findViewById(R.id.rainButton);
		light = (ToggleButton) root.findViewById(R.id.lightButton);
		dark = (ToggleButton) root.findViewById(R.id.darkButton);

		warm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean on = warm.isChecked();

				if (on) {
					// Enable vibrate
				} else {
					// Disable vibrate
				}

			}
		});

		cold.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean on = cold.isChecked();

				if (on) {
					// Enable vibrate
				} else {
					// Disable vibrate
				}

			}
		});

		sunny.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean on = sunny.isChecked();

				if (on) {
					// Enable vibrate
				} else {
					// Disable vibrate
				}

			}
		});

		rainy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean on = rainy.isChecked();

				if (on) {
					// Enable vibrate
				} else {
					// Disable vibrate
				}

			}
		});

		light.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean on = light.isChecked();

				if (on) {
					// Enable vibrate
				} else {
					// Disable vibrate
				}

			}
		});

		dark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean on = dark.isChecked();

				if (on) {
					// Enable vibrate
				} else {
					// Disable vibrate
				}

			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Item item = new Item(editName.getText().toString());

				item.setImage(((MainGUI) getActivity()).currentPicturetaken);

				BundleMessageHelper helper = new BundleMessageHelper(
						getActivity());
				helper.sendMessageSendBundle(BundleMessage.getInstance()
						.createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
								ItemCommand.add(item)));

				Intent intent = new Intent(getActivity(), MainGUI.class);
				startActivity(intent);
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editName.setText("");
				iv.setImageResource(R.drawable.ic_launcher);
				
			}
		});
		
		
		return root;
	}

}
