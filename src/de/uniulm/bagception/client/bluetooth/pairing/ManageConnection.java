package de.uniulm.bagception.client.bluetooth.pairing;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

public class ManageConnection extends AsyncTask<BluetoothDevice, Integer, Void> {

	private final String TAG = getClass().getName();
	private BluetoothSocket bluetoothSocket;
	private BluetoothDevice device;
	public BluetoothAdapter bluetoothAdapter;
	public static final String BT_UUID = "1bcc9340-2c29-11e3-8224-0800200c9a66";
	public String new_BLUETOOTH_DEVICE;

	public AddNewBagStartActivity context;

	public ManageConnection(AddNewBagStartActivity c) {
		context = c;
	}

	@Override
	protected Void doInBackground(BluetoothDevice... devices) {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		new_BLUETOOTH_DEVICE = devices[0].getName();
		this.device = devices[0];
		bluetoothAdapter.cancelDiscovery();

		try {
			bluetoothSocket = devices[0].createRfcommSocketToServiceRecord(UUID
					.fromString(BT_UUID));
		} catch (IOException e) {

			e.printStackTrace();
			cancel(true);
			return null;
		}

		try {
			// Connect the device through the socket
			bluetoothSocket.connect();
			
			Set<BluetoothDevice> pairedDevices = bluetoothAdapter
					.getBondedDevices();
			// If there are paired devices
			if (pairedDevices.size() > 0) {
				// Loop through paired devices
				for (BluetoothDevice device : pairedDevices) {
					// Add the name and address to an array adapter to show in a
					// ListView
				}
			} 
		} catch (IOException connectException) {

			
			try {
				bluetoothSocket.close();
			} catch (IOException closeException) {
			}
			cancel(true);
			return null;
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		context.devicesNotPaired(device);
	}

	public void sendString(String string) {
		try {
			bluetoothSocket.getOutputStream().write(string.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// Handle disconnection
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		try {
			bluetoothSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		context.updatePairedDevices();
		context.devicesSuccessfullyPaired(device);
	}

}
