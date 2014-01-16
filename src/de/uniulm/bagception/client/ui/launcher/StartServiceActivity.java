package de.uniulm.bagception.client.ui.launcher;

import android.app.Activity;
import android.os.Bundle;
import de.uniulm.bagception.client.Loader;
import de.uniulm.bagception.client.R;

public class StartServiceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_service);
		Loader.startService(this);
		
		
		
		finish();
	}


}
