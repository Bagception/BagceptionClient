package de.uniulm.bagception.client.ui.launcher;

import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class ItemEditActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_edit_activity);
		
		// Look if a fragment already exists
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(ItemEditFragment.DEFAULT_EDIT_FRAGMENT_TAG);
		
		// create new fragment if no other fragment can be found
		if(fragment == null) {
			fragment = new ItemEditFragment();
			Bundle args = new Bundle();
			
			// TODO
			// Hier m�ssen die Daten aus der Datenbank geholt werden
			
			args.putString("Test", null); // DummyContent!
			
			fragment.setArguments(args);
			
			// To make fragment transactions in your activity (such as add, remove, or replace a fragment),
			// you must use FragmentTransaction. All fragment interact between the beginTransaction() and
			// commit() calls
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			
			// Add the fragment to the activity. The fragment will be placed in R.id.edit_container
			// and name it DEFAULT_EDIT_FRAGMENT_TAG
			transaction.add(R.id.edit_container, fragment, ItemEditFragment.DEFAULT_EDIT_FRAGMENT_TAG);
			
			// End the transaction
			transaction.commit();
			
		}
	}
}
