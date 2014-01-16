package de.uniulm.bagception.client.debugactivities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.caching.ImageCachingSystem;
import de.uniulm.bagception.client.items.AutoUpdateableItemView;
import de.uniulm.bagception.client.items.ItemsSystem;

public class Debug extends Activity {

	private Item i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_debug);
		i = ItemsSystem.DEBUGITEM;
		
		AutoUpdateableItemView l = (AutoUpdateableItemView) findViewById(R.id.autoUpdateableItemView1);
		l.setItem(i);	
		
	}

		

	
}
