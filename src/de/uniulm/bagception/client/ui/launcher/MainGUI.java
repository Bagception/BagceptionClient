package de.uniulm.bagception.client.ui.launcher;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
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

public class MainGUI extends Activity {

	private ActionBarDrawerToggle mDrawerToggle;

	final String[] data = { "�bersicht", "Alle Items", "Gefundene Items",
			"Item erstellen", "Ort erstellen", "Neue Tasche", "Einstellungen" };
	final String[] fragments = {
			"de.uniulm.bagception.client.ui.launcher.OverviewFragment",
			"de.uniulm.bagception.client.ui.launcher.AllItemsFragment",
			"de.uniulm.bagception.client.ui.launcher.ItemsFoundFragment",
			"de.uniulm.bagception.client.ui.launcher.CreateNewItemFragment",
			"de.uniulm.bagception.client.ui.launcher.CreateNewPlaceFragment",
			"de.uniulm.bagception.client.ui.launcher.NewBagFragment",
			"de.uniulm.bagception.client.ui.launcher.SettingsFragment" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_gui);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1, data);

		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		final ListView navList = (ListView) findViewById(R.id.drawer);
		navList.setAdapter(adapter);
		navList.setOnItemClickListener(new OnItemClickListener() {
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
								MainGUI.this, fragments[pos]));
						tx.commit();
					}
				});
				drawer.closeDrawer(navList);
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
		tx.replace(R.id.main, Fragment.instantiate(MainGUI.this, fragments[0]));
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
	
	// public void createNewPlace(View view){
	// Intent placeIntent = new Intent(this, CreateNewPlace.class);
	// startActivity(placeIntent);
	// }
	//
	// public void createNewItem(View view){
	// Intent itemIntent = new Intent(this, CreateNewItem.class);
	// startActivity(itemIntent);
	// }
	//
	// public void searchForNewBag(View view){
	// Intent searchBagIntent = new Intent(this, AddNewBagStartActivity.class);
	// startActivity(searchBagIntent);
	// }
	//
	// public void startSettings(View view){
	// Intent settingsIntent = new Intent(this, SettingsActivity.class);
	// startActivity(settingsIntent);
	// }

}
