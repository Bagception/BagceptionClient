package de.uniulm.bagception.client.ui.launcher;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.CategoryCommand;
import de.uniulm.bagception.client.R;

public class CreateNewCategoryFragment extends Fragment {

	EditText editName;
	Button send;
	Button cancel;

	public static Fragment newInstance(Context context) {
		CreateNewCategoryFragment f = new CreateNewCategoryFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_create_new_category, null);
		editName = (EditText) root.findViewById(R.id.editCategory);
		send = (Button) root.findViewById(R.id.sendCategory);
		cancel = (Button) root.findViewById(R.id.cancelCategory);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Toast.makeText(getActivity(), "RLY FU", Toast.LENGTH_LONG).show();

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
					Category category = new Category(editName.getText().toString());
					BundleMessageHelper helper = new BundleMessageHelper(
							getActivity());
					helper.sendMessageSendBundle(BundleMessage.getInstance()
							.createBundle(
									BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
									CategoryCommand.add(category)));
					
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
//				Intent intent = new Intent(getActivity(), MainGUI.class);
//				startActivity(intent);
			}
		});

		return root;
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
