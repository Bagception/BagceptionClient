package de.uniulm.bagception.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends Activity {

	EditText editName;
	Button send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);

		editName = (EditText) findViewById(R.id.editName);
		send = (Button) findViewById(R.id.save);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

//				Item item = new Item(editName.getText().toString());
//
//				item.setImage(((MainGUI) getActivity()).currentPicturetaken);
//
//				BundleMessageHelper helper = new BundleMessageHelper(
//						getActivity());
//				helper.sendMessageSendBundle(BundleMessage.getInstance()
//						.createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
//								ItemCommand.add(item)));
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

}
