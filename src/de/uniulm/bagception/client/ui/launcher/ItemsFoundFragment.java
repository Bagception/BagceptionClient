package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.uniulm.bagception.client.R;

public class ItemsFoundFragment extends Fragment {

	public static Fragment newInstance(Context context) {
		ItemsFoundFragment f = new ItemsFoundFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_items_found, null);
		return root;
	}

}
