package de.uniulm.bagception.client.ui.launcher;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.bluetooth.pairing.AddNewBagStartActivity;
import de.uniulm.bagception.client.osm.ShowMap;

public class MainGUI extends Activity {

	private ActionBarDrawerToggle mDrawerToggle;

	final String[] data = { "Übersicht", "Item erstellen", "Ort erstellen",
			"Neue Tasche", "Einstellungen" };
	final String[] menueFragments = {
			"de.uniulm.bagception.client.ui.launcher.OverviewFragment",
			"de.uniulm.bagception.client.ui.launcher.CreateNewItemFragment",
			"de.uniulm.bagception.client.ui.launcher.CreateNewPlaceFragment",
			"de.uniulm.bagception.client.ui.launcher.NewBagFragment",
			"de.uniulm.bagception.client.ui.launcher.SettingsFragment" };

	final String[] data2 = { "Test1", "Test2" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_gui);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1, data);

		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		final ListView navListLeft = (ListView) findViewById(R.id.drawer);
		navListLeft.setAdapter(adapter);
		navListLeft.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int pos, long id) {
				drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
					@Override
					public void onDrawerClosed(View drawerView) {
						super.onDrawerClosed(drawerView);
						FragmentTransaction tx = getFragmentManager()
								.beginTransaction();
						tx.replace(R.id.main, Fragment.instantiate(
								MainGUI.this, menueFragments[pos]));
						tx.commit();
					}
				});
				drawer.closeDrawer(navListLeft);
			}
		});

		mDrawerToggle = new ActionBarDrawerToggle(this, drawer,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close);

		Log.d("Mudit",
				"mDrawerToggle" + mDrawerToggle.isDrawerIndicatorEnabled());

		drawer.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		getActionBar().setDisplayShowHomeEnabled(true);

		FragmentTransaction tx = getFragmentManager().beginTransaction();
		tx.replace(R.id.main,
				Fragment.instantiate(MainGUI.this, menueFragments[0]));
		tx.commit();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onScanClick(View view) {
		Intent intent = new Intent(this, AddNewBagStartActivity.class);
		startActivity(intent);
	}

	public void startMap(View view) {
		Intent intent = new Intent(this, ShowMap.class);
		startActivity(intent);
	}
}
