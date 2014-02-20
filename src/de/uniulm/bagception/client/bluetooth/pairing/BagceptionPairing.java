package de.uniulm.bagception.client.bluetooth.pairing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class BagceptionPairing{
	private boolean DEBUG = false;

	private final BagceptionPairingCallbacks callbacks;
	private final List<BluetoothDevice> foundBTDevices =  Collections.synchronizedList(new  ArrayList<BluetoothDevice>());
	public final BluetoothAdapter bluetoothAdapter;
	private final ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();
	private final BluetoothServiceActor actor;
	private volatile boolean ddFinished = false;
	public BagceptionPairing(BagceptionPairingCallbacks callbacks) {
		this.callbacks = callbacks;
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		startOver();
		
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
		startOver();
		bluetoothAdapter.startDiscovery();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//device discovery finished
				deviceDiscoveryFinished();
			}
		}, 10000);
	}
	public void cancel(){
		bluetoothAdapter.cancelDiscovery();
		callbacks.onScanFinished(foundBTDevices);
		startOver();
	}
	
	private void startOver(){
		discoveredDevices.clear();
		foundBTDevices.clear();
		if (actor!=null)
			actor.clear();
		discoveredDevices.addAll(bluetoothAdapter.getBondedDevices());
		ddFinished = false;
	}
	

	private synchronized void deviceDiscoveryFinished(){
		if (ddFinished)return;
		
		ddFinished=true;
		if (DEBUG)
			Log.d("bt", "device discovery finished");

		bluetoothAdapter.cancelDiscovery();
		
		//start sdp
		for (BluetoothDevice d : foundBTDevices) {
			if (DEBUG)
				Log.d("bt", "start sdp for: "+d.getName());
			if (d.fetchUuidsWithSdp()) {
				if (DEBUG)
					Log.d("bt", "sdp started for: "+d.getName());

			}
				
		}
		callbacks.onScanFinished(foundBTDevices);
		
	}
	
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
				if (device.getName() != null){
					if (DEBUG)
						Log.d("bt", "is not bound: "+device.getName());
					foundBTDevices.add(device);	
					callbacks.onDeviceFound(device);
				}
			}else{
				if (DEBUG)
					Log.d("bt", "is bound: "+device.getName());
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
