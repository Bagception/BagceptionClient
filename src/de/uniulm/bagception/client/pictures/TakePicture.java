package de.uniulm.bagception.client.pictures;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import de.uniulm.bagception.client.R;

public class TakePicture extends Activity {

	private final int REQUEST_IMAGE_CAPTURE = 1;
	ImageView imVCature_pic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_picture);

	}

	public void ontakePictureButtonClick(View v) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
	    /*put uri as extra in intent object*/
	    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
	    /*start activity for result pass intent as argument and request code */
	    startActivityForResult(takePictureIntent, 1);
//		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//
//		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == REQUEST_IMAGE_CAPTURE) {
			super.onActivityResult(requestCode, resultCode, data);
			 if(requestCode==1){
				   //create instance of File with same name we created before to get image from storage
				   File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
				   //Crop the captured image using an other intent
				   try {
				    /*the user's device may not support cropping*/
				    cropCapturedImage(Uri.fromFile(file));
				   }
				   catch(ActivityNotFoundException aNFE){
				    //display an error message if user device doesn't support
				    String errorMessage = "Sorry - your device doesn't support the crop action!";
				    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
				    toast.show();
				   }
				  }
			 if(requestCode==2){
				   //Create an instance of bundle and get the returned data
				   Bundle extras = data.getExtras();
				   if(extras == null) return;
				   //get the cropped bitmap from extras
				   Bitmap thePic = (Bitmap) extras.get("data");
				   if(thePic == null) return;
				   //set image bitmap to image view
				   imVCature_pic.setImageBitmap(thePic);
				  }
//			Bundle extras = data.getExtras();
//			if (extras == null)
//				return;
//			Bitmap imageBitmap = (Bitmap) extras.get("data");
//			if (imageBitmap == null)
//				return;
			// send as string (this would work over bluetooth)
//			Toast.makeText(this, "picture taken", Toast.LENGTH_SHORT).show();
			//Intent i = new Intent(this, PictureSerializing.class);
			//i.putExtra("img", PictureSerializer.serialize(imageBitmap));
			
//		}
	}

	 public void cropCapturedImage(Uri picUri){
		  //call the standard crop action intent 
		  Intent cropIntent = new Intent("com.android.camera.action.CROP");
		  //indicate image type and Uri of image
		  cropIntent.setDataAndType(picUri, "image/*");
		  //set crop properties
		  cropIntent.putExtra("crop", "true");
		  //indicate aspect of desired crop
		  cropIntent.putExtra("aspectX", 1);
		  cropIntent.putExtra("aspectY", 1);
		  //indicate output X and Y
		  cropIntent.putExtra("outputX", 256);
		  cropIntent.putExtra("outputY", 256);
		  //retrieve data on return
		  cropIntent.putExtra("return-data", true);
		  //start the activity - we handle returning in onActivityResult
		  startActivityForResult(cropIntent, 2);
	 }

}
