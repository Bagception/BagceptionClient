package de.uniulm.bagception.client.items;

import java.util.List;

<<<<<<< HEAD
import android.content.Context;
=======
import android.content.Intent;
>>>>>>> 4ddefaf311ef9f0b75fcd15f744bc8a568452ad9
import android.os.Bundle;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
<<<<<<< HEAD
=======
import de.uniulm.bagception.client.debugactivities.Debug;
import de.uniulm.bagception.client.service.BagceptionClientService;
>>>>>>> 4ddefaf311ef9f0b75fcd15f744bc8a568452ad9

public class ItemsSystem implements BundleMessageReactor{

	
	public static Item DEBUGITEM;
	private final Context context;
	public ItemsSystem(Context c) {
		this.context = c;
	}

	@Override
	public void onBundleMessageRecv(Bundle b) {
		BUNDLE_MESSAGE msg = BundleMessage.getInstance().getBundleMessageType(b);
		switch (msg){

			
			case CONTAINER_STATUS_UPDATE:
				ContainerStateUpdate statusUpdate = ContainerStateUpdate.fromJSON(BundleMessage.getInstance().extractObject(b));
				
				StringBuilder sb = new StringBuilder();
				List<Item> itemsIs = statusUpdate.getItemList();
				List<Item> itemsMust = statusUpdate.getActivity().getItemsForActivity();
				sb.append("Update: \n");
				sb.append("Items in Bag:");
				sb.append("\n");
				for(Item item:itemsIs){
					sb.append(item.getName());
					sb.append("\n");
					if(item.getImageHash()>0){
						DEBUGITEM=item;
					}
				}
				sb.append("\n");
				sb.append("Activity: ");
				sb.append(statusUpdate.getActivity().getName());
				sb.append("\n");
				sb.append("Items for activity:");
				sb.append("\n");
				
				for(Item item:itemsMust){
					sb.append(item.getName());
					sb.append("\n");
				}
				sb.append("\n");
<<<<<<< HEAD
				Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
=======
				Toast.makeText(mainService, sb.toString(), Toast.LENGTH_LONG).show();
				Intent sa = new Intent(mainService, Debug.class);
				sa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mainService.startActivity(sa);
>>>>>>> 4ddefaf311ef9f0b75fcd15f744bc8a568452ad9
				break;
			

			default:break;
				
			
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
