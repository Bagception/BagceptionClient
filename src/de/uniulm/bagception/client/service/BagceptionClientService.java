package de.uniulm.bagception.client.service;

import android.content.Intent;
import android.os.IBinder;
import de.philipphock.android.lib.services.observation.ObservableService;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.client.bluetooth.middleware.BluetoothSystem;
import de.uniulm.bagception.client.caching.ImageCachingSystem;
import de.uniulm.bagception.client.items.ItemsSystem;
import de.uniulm.bagception.client.notification.NotificationSystem;
import de.uniulm.bagception.client.ui.launcher.MainGUI;
import de.uniulm.bagception.protocol.bundle.constants.Command;

public class BagceptionClientService extends ObservableService{
	
	private BundleMessageActor bluetoothMessageActor;
	private BundleMessageActor notificationMessageActor;
	private BundleMessageActor itemActor;

	private BundleMessageActor imageCachingActor;
	
	public BundleMessageHelper bmHelper;
	
	private ItemsSystem itemSys; 
	
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
		imageCachingActor = new BundleMessageActor(ImageCachingSystem.getInstance());
		imageCachingActor.register(this);
		
		btSys = new BluetoothSystem(this);
		notifySys = new NotificationSystem(this);
		itemSys = new ItemsSystem(this);
		
		bluetoothMessageActor = new BundleMessageActor(btSys);
		bluetoothMessageActor.register(this);
		
		notificationMessageActor = new BundleMessageActor(notifySys);
		notificationMessageActor.register(this);
		
		
		
		itemActor = new BundleMessageActor(itemSys);
		itemActor.register(this);
		
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
		itemActor.unregister(this);
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
