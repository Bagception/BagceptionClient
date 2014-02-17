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
import de.uniulm.bagception.bluetoothclientmessengercommunication.util.BagceptionBluetoothUtil;

public class BagceptionPairing{
	private boolean DEBUG = true;

	private final BagceptionPairingCallbacks callbacks;
	private final List<BluetoothDevice> foundBTDevices =  Collections.synchronizedList(new  ArrayList<BluetoothDevice>());
	private final List<BluetoothDevice> foundBagceptionDevices = Collections.synchronizedList(new  ArrayList<BluetoothDevice>());
	public final BluetoothAdapter bluetoothAdapter;
	private final ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();
	private int sdpCount=0;
	private final BluetoothServiceActor actor;
	private volatile boolean sdpFinished = false;
	private volatile boolean ddFinished = false;
	public BagceptionPairing(BagceptionPairingCallbacks callbacks) {
		this.callbacks = callbacks;
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		discoveredDevices.clear();
		foundBTDevices.clear();
		foundBagceptionDevices.clear();
		sdpFinished = false;
		ddFinished = false;
		
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
		discoveredDevices.clear();
		foundBTDevices.clear();
		actor.clear();
		foundBagceptionDevices.clear();
		discoveredDevices.addAll(bluetoothAdapter.getBondedDevices());
		sdpFinished = false;
		ddFinished = false;
		bluetoothAdapter.startDiscovery();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//device discovery finished
				deviceDiscoveryFinished();
			}
		}, 15000);
	}
//	public void cancel(){
//		
//	}
	//callbacks.onScanFinished(foundBagceptionDevices);
	private synchronized void sdpFinished(){
		if (sdpFinished)return;
		sdpFinished=true;
		//SDP finished
		if (DEBUG)
			Log.d("bt", "sdp finished");
		for (BluetoothDevice d: foundBTDevices){
			if (DEBUG)
				Log.d("bt", "device found: "+d.getName());

			if (BagceptionBluetoothUtil.isBagceptionServer(d)){
				if (DEBUG)
					Log.d("bt", "device is bacgeption: "+d.getName());

				foundBagceptionDevices.add(d);
			}
		}
		callbacks.onScanFinished(foundBagceptionDevices);
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

				sdpCount++;
			}
			
		}
		
		Handler h2 = new Handler();
		h2.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				sdpFinished();
				
			}
		}, 15000);
		
	}
	
	private final BluetoothServiceReactor btServicereactor = new BluetoothServiceReactor() {
		
		@Override
		public void onServicesDiscovered(BluetoothDevice device) {
//			sdpCount--;
//			if (sdpCount<=0){
//				sdpFinished();
//			}
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
