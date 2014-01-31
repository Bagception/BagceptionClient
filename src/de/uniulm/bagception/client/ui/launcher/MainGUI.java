package de.uniulm.bagception.client.ui.launcher;

import org.json.simple.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.bluetooth.pairing.AddNewBagStartActivity;
import de.uniulm.bagception.client.osm.ShowMap;
import de.uniulm.bagception.protocol.bundle.constants.StatusCode;

public class MainGUI extends Activity implements BundleMessageReactor {

	private ActionBarDrawerToggle mDrawerToggle;
	CreateNewItemFragment newItemfragment;
	private BundleMessageActor bmActor;

	private boolean lastConnected = false;

	private BundleMessageHelper bmHelper;
	private DrawerLayout drawer;
	private View drawRightLayout;
	public Bitmap currentPicturetaken = null;

	final String[] data = { "Übersicht", "Alle Items", "Alle Locations",
			"Alle Kategorien", "Alle Aktivitäten", "Item erstellen",
			"Ort erstellen", "Kategorie erstellen", "Aktivität erstellen",
			"Neue Tasche", "Einstellungen" };
	final String[] menueFragments = {
			"de.uniulm.bagception.client.ui.launcher.OverviewFragment",
			"de.uniulm.bagception.client.ui.launcher.AllItemsFragment",
			"de.uniulm.bagception.client.ui.launcher.AllLocationsFragment",
			"de.uniulm.bagception.client.ui.launcher.AllCategoriesFragment",
			"de.uniulm.bagception.client.ui.launcher.AllActivitiesFragment",
			"de.uniulm.bagception.client.ui.launcher.CreateNewItemFragment",
			"de.uniulm.bagception.client.ui.launcher.CreateNewPlaceFragment",
			"de.uniulm.bagception.client.ui.launcher.CreateNewCategoryFragment",
			"de.uniulm.bagception.client.ui.launcher.CreateNewActivityFragment",
			"de.uniulm.bagception.client.ui.launcher.NewBagFragment",
			"de.uniulm.bagception.client.ui.launcher.SettingsFragment" };

	final String[] data2 = { "Test1", "Test2" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bmActor = new BundleMessageActor(this);
		bmHelper = new BundleMessageHelper(this);
		setContentView(R.layout.activity_main_gui);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActionBar()
				.getThemedContext(), android.R.layout.simple_list_item_1, data);

		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawRightLayout = findViewById(R.id.drawerRight);
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
						getActionBar().setNavigationMode(
								ActionBar.NAVIGATION_MODE_STANDARD);
						tx.replace(R.id.main, Fragment.instantiate(
								MainGUI.this, menueFragments[pos]));
						getActionBar().setTitle(data[pos]);
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

	private final int REQUEST_IMAGE_CAPTURE = 1;

	public void ontakePictureButtonClick(View v) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE) {
			Bundle extras = data.getExtras();
			if (extras == null)
				return;
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			if (imageBitmap == null)
				return;
			// send as string (this would work over bluetooth)
			Toast.makeText(this, "picture taken", Toast.LENGTH_SHORT).show();
			ImageView img = (ImageView) findViewById(R.id.itemIcon);
			img.setImageBitmap(imageBitmap);
			currentPicturetaken = imageBitmap;
		}
	}

	@Override
	public void onBundleMessageRecv(Bundle b) {
		BUNDLE_MESSAGE msg = BundleMessage.getInstance()
				.getBundleMessageType(b);
		JSONObject obj = BundleMessage.getInstance().extractObject(b);

		switch (msg) {
		case ADMINISTRATION_COMMAND:
			AdministrationCommand<?> cmd = AdministrationCommand
					.fromJSONObject(obj);
			AdministrationCommandProcessor p = new AdministrationCommandProcessor() {
				@Override
				public void onItemAdd(Item a, AdministrationCommand<Item> i) {
					if (i.isSuccessful()) {
						Toast.makeText(
								MainGUI.this,
								"item : " + a.getName()
										+ "erfolgreich angelegt ",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(
								MainGUI.this,
								"item : " + a.getName()
										+ "nicht erfolgreich angelegt ",
								Toast.LENGTH_SHORT).show();

					}
				}
			};
			cmd.accept(p);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBundleMessageSend(Bundle b) {

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
		StatusCode c = StatusCode.getStatusCode(b);
		switch (c) {
		case CONNECTED: {
			if (lastConnected)
				return;
			drawer.openDrawer(drawRightLayout);
			Handler h = new Handler();
			h.postDelayed(new Runnable() {

				@Override
				public void run() {
					drawer.closeDrawer(drawRightLayout);

				}
			}, 1500);

			lastConnected = true;
			break;
		}
		case DISCONNECTED: {
			if (!lastConnected)
				return;
			drawer.openDrawer(drawRightLayout);
			lastConnected = false;
			break;
		}
		default:
			break;
		}

	}

	@Override
	public void onCommandMessage(Bundle b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onStop() {

		super.onStop();
		bmActor.unregister(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		bmActor.register(this);
		// bmHelper.sendCommandBundle(Command.RESEND_STATUS.toBundle());

	}

	@Override
	protected void onResume() {

		super.onResume();
	}
	// @Override
	// protected void onResume() {
	// super.onResume();
	// new
	// BundleMessageHelper(this).sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.CONTAINER_STATUS_UPDATE_REQUEST,
	// ""));
	// }
}
