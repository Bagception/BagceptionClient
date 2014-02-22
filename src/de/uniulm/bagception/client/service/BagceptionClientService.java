package de.uniulm.bagception.client.service;

import org.json.simple.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import de.philipphock.android.lib.services.observation.ObservableService;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.client.bluetooth.middleware.BluetoothSystem;
import de.uniulm.bagception.client.caching.ImageCachingSystem;
import de.uniulm.bagception.client.notification.NotificationSystem;
import de.uniulm.bagception.client.ui.launcher.MainGUI;
import de.uniulm.bagception.protocol.bundle.constants.Command;

public class BagceptionClientService extends ObservableService {

	private BundleMessageActor bluetoothMessageActor;
	private BundleMessageActor notificationMessageActor;

	private BundleMessageActor imageCachingActor;

	private BundleMessageActor stringNotificationMessageActor;

	public BundleMessageHelper bmHelper;

	private BluetoothSystem btSys;
	private NotificationSystem notifySys;

	@Override
	public String getServiceName() {

		return "de.uniulm.bagception.client.service.BagceptionClientService";
	}

	@Override
	public void onCreate() {

		bmHelper = new BundleMessageHelper(this);

		ImageCachingSystem.initInstance(this);
		imageCachingActor = new BundleMessageActor(
				ImageCachingSystem.getInstance());
		imageCachingActor.register(this);

		btSys = new BluetoothSystem(this);
		notifySys = new NotificationSystem(this);

		bluetoothMessageActor = new BundleMessageActor(btSys);
		bluetoothMessageActor.register(this);

		notificationMessageActor = new BundleMessageActor(notifySys);
		notificationMessageActor.register(this);

		stringNotificationMessageActor = new BundleMessageActor(
				new BundleMessageReactor() {
					@Override
					public void onStatusMessage(Bundle b) {
					}

					@Override
					public void onResponseMessage(Bundle b) {
					}

					@Override
					public void onResponseAnswerMessage(Bundle b) {
					}

					@Override
					public void onError(Exception e) {
					}

					@Override
					public void onCommandMessage(Bundle b) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onBundleMessageSend(Bundle b) {
					}

					@Override
					public void onBundleMessageRecv(Bundle b) {

						switch (BundleMessage.getInstance()
								.getBundleMessageType(b)) {
						case STRING_MESSAGE: {
							JSONObject message = BundleMessage.getInstance().extractObject(b);
							String msg = message.get("msg").toString();
							Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
							break;
						}
						default:
							break;
						}
					}
				});

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		bmHelper.sendCommandBundle(Command.TRIGGER_SCAN_DEVICES.toBundle());
		Intent sa = new Intent(this, MainGUI.class);

		sa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(sa);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		bluetoothMessageActor.unregister(this);
		notificationMessageActor.unregister(this);
		stopForeground(true);
		imageCachingActor.unregister(this);
		super.onDestroy();

	}

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	@Override
	protected void onFirstInit() {

	}

}
