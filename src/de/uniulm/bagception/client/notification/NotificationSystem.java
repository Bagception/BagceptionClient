package de.uniulm.bagception.client.notification;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import de.philipphock.android.lib.services.observation.ConstantFactory;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.client.ConnectToDevice;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.service.BagceptionClientService;
import de.uniulm.bagception.protocol.bundle.constants.Command;
import de.uniulm.bagception.protocol.bundle.constants.Response;
import de.uniulm.bagception.services.ServiceNames;

public class NotificationSystem implements BundleMessageReactor{
	
	private final BagceptionClientService mainService;
	private int iconID = R.drawable.noconnectionicon;
	public NotificationSystem(BagceptionClientService mainService) {
		this.mainService = mainService;
		
		Notification foregroundNotification = new Notification();
		mainService.startForeground(1, foregroundNotification);
				
		updateNotification("touch to connect");
		
		
		
		
		
		Intent broadcastRequest = new Intent();
		//broadcast answer is handled by ServiceObservationReactor
		//with this, we foce the BluetoothMiddleware to resent if it is alive
		broadcastRequest
				.setAction(ConstantFactory 
						.getForceResendStatusString(ServiceNames.BLUETOOTH_CLIENT_SERVICE));  
		
		LocalBroadcastManager.getInstance(mainService).sendBroadcast(broadcastRequest);
		//when we reconnect with the bluetoothMiddleware, we ask if the btclient is connected
		mainService.bmHelper.sendCommandBundle(Command.getCommandBundle(Command.RESEND_STATUS));
		mainService.bmHelper.sendCommandBundle(Command.POLL_ALL_RESPONSES.toBundle());
		
	}
	
	public void updateNotification(String text){
		updateNotification(null,text,BagceptionClientService.class,true);
	}
	public void updateNotification(String text,Bundle b){
		updateNotification(b,text,BagceptionClientService.class,true);
	}
	public void updateNotification(String text,Bundle b,Class<?> c){
		updateNotification(b,text,c,true);
	}
	public void updateNotification(Bundle response,String text,Class<?> targetOnTouch,boolean targetIsService){
		NotificationManager notificationManager = (NotificationManager) 
				  mainService.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notificationIntent = new Intent(mainService, targetOnTouch);
		if (response != null){
			notificationIntent.putExtras(response);
		}
		PendingIntent pendingIntent;
		if (targetIsService){
			pendingIntent = PendingIntent.getService(mainService, 0, notificationIntent, 0);
		}else{
			pendingIntent = PendingIntent.getActivity(mainService, 0, notificationIntent, 0);
		}
		

		Notification n = new Notification.Builder(
				mainService).setSmallIcon(iconID)
				.setContentTitle("Bagception")
				.setContentIntent(pendingIntent)
				.setLargeIcon(BitmapFactory.decodeResource(mainService.getResources(),iconID))
				.setContentText(text).build();
		
		notificationManager.notify(1, n);
	}
	
	
	
	
	// BundleMessageReactor
	
	@Override
	public void onResponseMessage(Bundle b) {
		
		Response r = Response.getResponse(b);
		
		//Bundle ack=null;
		switch (r) {
		case Ask_For_Specific_Device:
			//ack = ResponseAnswer.Ask_For_Specific_Device.getACK();
			ArrayList<BluetoothDevice> ds =  b.getParcelableArrayList(Response.EXTRA_KEYS.PAYLOAD);
			updateNotification(b, "Es wurden " +ds.size()+ " Taschen gefunden", ConnectToDevice.class, false);
			break;

		case Confirm_Established_Connection:
			//ack = ResponseAnswer.Confirm_Established_Connection.getACK();
			BluetoothDevice d = (BluetoothDevice) b.getParcelable(Response.EXTRA_KEYS.PAYLOAD);
			updateNotification("Es wurde die Tasche \"" + d.getName() + "\" gefunden",b);

			break;
			
		case CLEAR_RESPONSES:
			ArrayList<Integer> ids=b.getIntegerArrayList(Response.EXTRA_KEYS.NOTIFICATIONS_TO_CLEAR);
			if (ids == null){
				clearNotifications();
			}else{
				clearNotifications(ids);	
			}
			
		
		case BLUETOOTH_CONNECTION:
			boolean connected = b.getBoolean(Response.EXTRA_KEYS.PAYLOAD);
			boolean valChanged = b.getBoolean(Response.EXTRA_KEYS.VALUE_HAS_CHANGED);
			if (!valChanged){
				//bluetooth state not changed
				return;
			}
			String s = "lost: touch to connect";
			iconID = R.drawable.noconnectionicon;
			if (connected){
				iconID = R.drawable.connectedicon;
				s="established";
			}
			updateNotification("Connection "+s);
			
			break;
		default:
			break;
		}
		
		//send ack = tells the service that we handled the message and it does not have to retransmit it ever again
//		if (ack != null)
//			bmHelper.sendResponseAnswerBundle(ack);
		//we don't do this anymore, this is confirmed by the answer itself
		

	}

	

	private void clearNotifications(){
//		NotificationManager notificationManager = (NotificationManager)mainService.getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationManager.cancel(0);
	}
	
	private void clearNotifications(ArrayList<Integer> ids){
//		NotificationManager notificationManager = (NotificationManager)mainService.getSystemService(Context.NOTIFICATION_SERVICE);
//		for (int id:ids){
//			notificationManager.cancel(id);	
//		}
		
	}

		

	@Override
	public void onStatusMessage(Bundle b) {
		
	}

	@Override
	public void onCommandMessage(Bundle b) {
		Command c = Command.getCommand(b);
		if (c == Command.PONG) {
			Toast.makeText(mainService, "PONG: Service",
					Toast.LENGTH_SHORT).show();

		}
	}

	@Override
	public void onError(Exception e) {
		//error?
	}





	@Override
	public void onResponseAnswerMessage(Bundle b) {
		//nothing todo here
	}



	@Override
	public void onBundleMessageRecv(Bundle b) {
		//nothing to do here
		
	}



	@Override
	public void onBundleMessageSend(Bundle b) {
		//nothing todo here, we do not care about bundle messages at all
	}


}
