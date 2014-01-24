package de.uniulm.bagception.client.ui.launcher;

import de.uniulm.bagception.client.R;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class ItemTabListener extends FragmentActivity implements TabListener {

	public Fragment fragment;

	public ItemTabListener(Fragment fragment) {
		this.fragment = fragment;
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.fragment_container, fragment);
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}

}
