package de.uniulm.bagception.client.ui.launcher;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.CategoryCommand;
import de.uniulm.bagception.client.R;

public class EditCategoryFragment extends Fragment implements
		BundleMessageReactor {

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
		
		Category category = null;
		String i = getArguments().getString("ENTITYSTRING");
		// String i = intent.getStringExtra("ITEMSTRING");
		org.json.simple.JSONObject obj = new org.json.simple.JSONObject();
		JSONParser p = new JSONParser();
		try {
			obj = (org.json.simple.JSONObject) p.parse(i);
			category = Category.fromJSON(obj);
			Log.d("TEST", category.getName().toString());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		final Category oldCategory = category;
		
		editName.setText(oldCategory.getName());
		
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Category newCategory;
				

				if ("".equals(editName.getText().toString().trim())) {
					AlertDialog.Builder dialogAlert = new AlertDialog.Builder(
							getActivity());
					dialogAlert.setTitle("Bitte alle Felder ausfüllen");
					dialogAlert.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});
					dialogAlert.create().show();
				} else {
					newCategory = new Category(editName.getText().toString());
					BundleMessageHelper helper = new BundleMessageHelper(
							getActivity());
					helper.sendMessageSendBundle(BundleMessage.getInstance()
							.createBundle(
									BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
									CategoryCommand.edit(oldCategory, newCategory)));

					Intent intent = new Intent(getActivity(), MainGUI.class);
					startActivity(intent);
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// getFragmentManager().popBackStack();
				editName.setText("");

			}
		});

		
		return root;
	}

	@Override
	public void onBundleMessageRecv(Bundle b) {
		// TODO Auto-generated method stub

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
