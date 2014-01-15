package de.uniulm.bagception.client.ui.launcher;

import de.uniulm.bagception.client.Loader;
import de.uniulm.bagception.client.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartServiceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_service);
		Loader.startService(this);
		
		
		
		finish();
	}


}
