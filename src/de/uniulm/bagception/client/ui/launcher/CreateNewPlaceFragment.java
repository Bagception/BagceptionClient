package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.uniulm.bagception.client.R;

public class CreateNewPlaceFragment extends Fragment {
	
	public static Fragment newInstance(Context context) {
		CreateNewPlaceFragment f = new CreateNewPlaceFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_create_new_place, null);
		return root;
	}

}
