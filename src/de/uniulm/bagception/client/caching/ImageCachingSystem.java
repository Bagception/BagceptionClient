package de.uniulm.bagception.client.caching;

import java.util.HashSet;

import org.json.simple.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseArray;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.serializer.PictureSerializer;
import de.uniulm.bagception.client.service.BagceptionClientService;

public class ImageCachingSystem implements BundleMessageReactor{


	private BagceptionClientService mainService;
	private final SparseArray<Bitmap> imageCache = new SparseArray<Bitmap>();
	private final SparseArray<HashSet<Item>> pendingImages = new SparseArray<HashSet<Item>>();
	
	private static final String INTENT_EXTRA_IMAGE_ID = "de.uniulm.bagception.image.id";
	private static final String INTENT_IMAGE_ID_BROADCAST = "de.uniulm.bagception.image_id.broadcast";
	
	public void registerForImageUpdate(Context c,BroadcastReceiver bcr){
		LocalBroadcastManager.getInstance(c).registerReceiver(bcr, new IntentFilter(INTENT_IMAGE_ID_BROADCAST));
	}
	public void unregisterForImageUpdate(Context c,BroadcastReceiver bcr){
		LocalBroadcastManager.getInstance(c).unregisterReceiver(bcr);
	}
	
	public int getIdFromIntent(Intent i){
		return i.getIntExtra(INTENT_EXTRA_IMAGE_ID,-1);
	}
	
	private ImageCachingSystem(){
		
	}
	
	
	public static final ImageCachingSystem instance = new ImageCachingSystem();
	
	public static ImageCachingSystem getInstance(){
		return instance;
	}
	public static void initInstance(BagceptionClientService mainService){
		ImageCachingSystem.getInstance().mainService = mainService;
	}
	
	@SuppressWarnings("unchecked")
	public Bitmap getImage(Item i){
		if (instance == null) return null;
		Bitmap img = imageCache.get(i.getImageHash());
		if (img == null){
			JSONObject jo = new JSONObject();
			jo.put("img", i.getImageHash());
			 Bundle imageRequest = BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.IMAGE_REQUEST,jo);
			 mainService.bmHelper.sendMessageSendBundle(imageRequest);
			 if (i != null){
				 HashSet<Item> items = pendingImages.get(i.getImageHash());
				 if (items == null){
					 items = new HashSet<Item>();
					 pendingImages.put(i.getImageHash(), items);
				 }
				 
				 items.add(i);
			 }
			 
			 return null;
		}
		return img;
	}
	
	private void setImage(Bitmap i){
		int hash =PictureSerializer.serialize(i).hashCode();
		imageCache.put(hash, i);
	}
	@Override
	public void onBundleMessageRecv(Bundle b) {
		switch (BundleMessage.getInstance().getBundleMessageType(b)){
			case IMAGE_REPLY:
				Log.d("debug", "image reply");
				JSONObject obj = BundleMessage.getInstance().extractObject(b);
				String deserImg = obj.get("img").toString();
				Bitmap bmp = PictureSerializer.deserialize(deserImg);
				setImage(bmp);
				HashSet<Item> items = pendingImages.get(deserImg.hashCode());
				if (items != null){
					for (Item item_:items){
						item_.setImage(bmp);
					}
					pendingImages.remove(deserImg.hashCode());
				}
				Intent intent = new Intent(INTENT_IMAGE_ID_BROADCAST);
				intent.putExtra(INTENT_EXTRA_IMAGE_ID, deserImg.hashCode());
				LocalBroadcastManager.getInstance(mainService).sendBroadcast(intent);
			default: break;
		}
	}
	@Override
	public void onBundleMessageSend(Bundle b) {
		
	}
	@Override
	public void onResponseMessage(Bundle b) {
		
	}
	@Override
	public void onResponseAnswerMessage(Bundle b) {
		
	}
	@Override
	public void onStatusMessage(Bundle b) {
		
	}
	@Override
	public void onCommandMessage(Bundle b) {
		
	}
	@Override
	public void onError(Exception e) {
		
	}

}
