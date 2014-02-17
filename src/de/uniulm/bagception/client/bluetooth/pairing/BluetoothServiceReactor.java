package de.uniulm.bagception.client.bluetooth.pairing;

import java.util.concurrent.ConcurrentHashMap;

import android.bluetooth.BluetoothDevice;
import de.philipphock.android.lib.Reactor;

public interface BluetoothServiceReactor extends Reactor{
	public void onDeviceFound(BluetoothDevice device);
	public void onDeviceDiscoveryFinished(BluetoothDevice[] devices,ConcurrentHashMap<String,BluetoothDevice> devicesAsMap);
	public void onServicesDiscovered(BluetoothDevice device);
	public void onServiceDiscoveryStarted();

}