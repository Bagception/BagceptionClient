package de.uniulm.bagception.client.pictures;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import de.uniulm.bagception.client.R;

public class TakePicture extends Activity {

	private final int REQUEST_IMAGE_CAPTURE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_picture);

	}

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
			//Intent i = new Intent(this, PictureSerializing.class);
			//i.putExtra("img", PictureSerializer.serialize(imageBitmap));
			
		}
	}

}
