package de.uniulm.bagception.client.ui.launcher;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import de.uniulm.bagception.client.bluetooth.pairing.BagceptionPairing;
import de.uniulm.bagception.client.bluetooth.pairing.BagceptionPairing.BagceptionPairingCallbacks;
import de.uniulm.bagception.client.bluetooth.pairing.BluetoothDeviceArrayAdapter;


public class NewBagFragment extends ListFragment{

	
	private BluetoothDeviceArrayAdapter mAdapter;
	private BagceptionPairing pairingHelper;
	private ProgressDialog dialog;
	
	public static Fragment newInstance(Context context) {
		NewBagFragment f = new NewBagFragment();

		return f;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mAdapter = new BluetoothDeviceArrayAdapter(getActivity());
		pairingHelper = new BagceptionPairing(callback);
		super.onActivityCreated(savedInstanceState);
	    setListAdapter(mAdapter);	
	    dialog = ProgressDialog.show(getActivity(), "suche..",
				"Suche nach Geräten... bitte warten"); 
	    
	}

	
	
	
	  

	
	@Override
	public void onStart() {
		//scan for devices
		super.onStart();
		pairingHelper.register(getActivity());
		pairingHelper.startScan();
		
		
	}
	
	@Override
	public void onStop() {
	
		super.onStop();
		dialog.dismiss();
		pairingHelper.unregister(getActivity());
	}
	
	public void onScanFinished(ArrayList<BluetoothDevice> foundDevices){
		
	}

	
	private final BagceptionPairingCallbacks callback = new BagceptionPairingCallbacks() {
		
		@Override
		public void onScanStart() {
			dialog.show();	
		}
		
		@Override
		public void onScanFinished(List<BluetoothDevice> devices) {
			mAdapter.addAll(devices);
			dialog.dismiss();
		}
		
		@Override
		public void onDeviceFound(BluetoothDevice device) {
			//nop
		}
	};
}
