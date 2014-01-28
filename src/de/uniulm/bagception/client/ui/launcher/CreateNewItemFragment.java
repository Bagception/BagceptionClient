package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ItemCommand;
import de.uniulm.bagception.client.R;

public class CreateNewItemFragment extends Fragment {
	
	EditText editName;
	Button send;

	public static Fragment newInstance(Context context) {
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
		
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Item item = new Item();
				item.setName(editName.getText().toString());
				item.setImage(((MainGUI)getActivity()).currentPicturetaken);
				
				
				BundleMessageHelper helper = new BundleMessageHelper(getActivity());
				helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ItemCommand.add(item)));
			}
		});
		return root;
	}
	

	

}
