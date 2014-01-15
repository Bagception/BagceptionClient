package de.uniulm.bagception.client.debugactivities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.caching.ImageCachingSystem;
import de.uniulm.bagception.client.items.ItemsSystem;

public class Debug extends Activity {

	private Item i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_debug);
		i = ItemsSystem.DEBUGITEM;
		setImage();
		
	}

	
	@Override
	protected void onResume() {
	
		super.onResume();
		ImageCachingSystem.getInstance().registerForImageUpdate(this, imageCachingRecv);
	}
	
	@Override
	protected void onPause() {
	
		super.onPause();
		ImageCachingSystem.getInstance().unregisterForImageUpdate(this, imageCachingRecv);
	}
	
	public void debugClick(View v){
		setImage();
	}
	
	private void setImage(){
		
		Bitmap bmp = ImageCachingSystem.getInstance().getImage(i);
		if (bmp != null){
			ImageView imgV = (ImageView)findViewById(R.id.imageDebugView);
			imgV.setImageBitmap(bmp);
		}
	}

	private final BroadcastReceiver imageCachingRecv = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			//int id = ImageCachingSystem.getInstance().getIdFromIntent(intent);
			ImageView imgV = (ImageView)Debug.this.findViewById(R.id.imageDebugView);
			imgV.setImageBitmap(i.getImage());
			//Bitmap b = ImageCachingSystem.getInstance().getImage(id, i);
			
			
		}
	};
}
