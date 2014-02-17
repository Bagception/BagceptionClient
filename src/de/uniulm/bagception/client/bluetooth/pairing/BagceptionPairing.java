package de.uniulm.bagception.client.bluetooth.pairing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import de.philipphock.android.lib.BroadcastActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.util.BagceptionBluetoothUtil;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class BagceptionPairing{

	private final BagceptionPairingCallbacks callbacks;
	private final List<BluetoothDevice> foundBTDevices =  Collections.synchronizedList(new  ArrayList<BluetoothDevice>());
	private final List<BluetoothDevice> foundBagceptionDevices = Collections.synchronizedList(new  ArrayList<BluetoothDevice>());
	public final BluetoothAdapter bluetoothAdapter;
	private final ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();
	private int sdpCount=0;
	private final BluetoothServiceActor actor;
	
	public BagceptionPairing(BagceptionPairingCallbacks callbacks) {
		this.callbacks = callbacks;
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		discoveredDevices.addAll(bluetoothAdapter.getBondedDevices());
		actor = new BluetoothServiceActor(btServicereactor);
	}
	
	public void register(Context c){
		actor.register(c);
	}
	public void unregister(Context c){
		actor.unregister(c);
	}
	
	public void startScan(){
		bluetoothAdapter.startDiscovery();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//device discovery finished
				bluetoothAdapter.cancelDiscovery();
				
				//start sdp
				for (BluetoothDevice d : foundBTDevices) {
					if (d.fetchUuidsWithSdp()) {
						sdpCount++;
					}
				}
				
				Handler h2 = new Handler();
				h2.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						//SDP finished
						for (BluetoothDevice d: foundBTDevices){
							if (BagceptionBluetoothUtil.isBagceptionServer(d)){
								foundBagceptionDevices.add(d);
							}
						}
						callbacks.onScanFinished(foundBagceptionDevices);
						
					}
				}, 15000);
			}
		}, 15000);
	}
//	public void cancel(){
//		
//	}
	//callbacks.onScanFinished(foundBagceptionDevices);
	
	private final BluetoothServiceReactor btServicereactor = new BluetoothServiceReactor() {
		
		@Override
		public void onServicesDiscovered(BluetoothDevice device) {
			
		}
		
		@Override
		public void onServiceDiscoveryStarted() {
			
		}
		
		@Override
		public void onDeviceFound(BluetoothDevice device) {
			if (!discoveredDevices.contains(device)){
				foundBTDevices.add(device);	
			}
		}
		
		@Override
		public void onDeviceDiscoveryFinished(BluetoothDevice[] devices,
				ConcurrentHashMap<String, BluetoothDevice> devicesAsMap) {
			
		}
	};
	
	public interface BagceptionPairingCallbacks{
		public void onDeviceFound(BluetoothDevice device);
		public void onScanFinished(List<BluetoothDevice> devices);
		public void onScanStart();
	}


	
	

}
