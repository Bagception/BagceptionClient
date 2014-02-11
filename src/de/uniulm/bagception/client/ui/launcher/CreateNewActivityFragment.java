package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ActivityCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.CategoryCommand;
import de.uniulm.bagception.client.R;

public class CreateNewActivityFragment extends Fragment {

	EditText editName;
	Button send;
	Button cancel;

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

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Activity activity = new Activity(editName.getText().toString());


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

}
