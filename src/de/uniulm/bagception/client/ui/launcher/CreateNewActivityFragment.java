package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import de.uniulm.bagception.client.R;

public class CreateNewActivityFragment extends Fragment {

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
				R.layout.fragment_create_new_activity, null);
		editName = (EditText) root.findViewById(R.id.editActivity);
		send = (Button) root.findViewById(R.id.sendActivity);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		return root;
	}

}
