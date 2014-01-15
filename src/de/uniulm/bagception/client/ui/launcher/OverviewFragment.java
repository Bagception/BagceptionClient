package de.uniulm.bagception.client.ui.launcher;

import de.uniulm.bagception.client.R;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OverviewFragment extends Fragment {

	public static Fragment newInstance(Context context) {
		OverviewFragment f = new OverviewFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_overview, null);
		return root;
	}

}
