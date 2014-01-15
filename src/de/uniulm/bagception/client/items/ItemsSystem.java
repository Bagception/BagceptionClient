package de.uniulm.bagception.client.items;

import org.json.JSONException;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.service.BagceptionClientService;

public class ItemsSystem implements BundleMessageReactor{

	private final BagceptionClientService mainService;
	
	public ItemsSystem(BagceptionClientService mainService) {
		this.mainService = mainService;
	}

	@Override
	public void onBundleMessageRecv(Bundle b) {
		Log.d("bag","bundlemessagerecv12");
		BUNDLE_MESSAGE msg = BundleMessage.getInstance().getBundleMessageType(b);
		switch (msg){
			case ITEM_FOUND:
			Item i;
			try {
				i = BundleMessage.getInstance().toItemFound(b);
				
				Toast.makeText(mainService,"Item found: "+i.getName() , Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
			case ITEM_NOT_FOUND:
			try {
				i = BundleMessage.getInstance().toItemFound(b);
				Toast.makeText(mainService, "item not found: "+i.getIds().get(0), Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
			default:break;
				
			
		}
	}

	@Override
	public void onBundleMessageSend(Bundle b) {
		Log.d("bag","bundlemessagesend");
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
