package de.uniulm.bagception.client;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class CreateNewItem extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_item, menu);
		return true;
	}
	
	private final int REQUEST_IMAGE_CAPTURE = 1;
	
	public void takePicture(View view){
		
		
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
			//Intent i = new Intent(this, PictureSerializing.class);
			//i.putExtra("img", PictureSerializer.serialize(imageBitmap));
			
		}
	}
	

}
