package de.uniulm.bagception.client;

import java.util.ArrayList;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.client.bluetooth.pairing.BluetoothDeviceArrayAdapter;
import de.uniulm.bagception.protocol.bundle.constants.Response;
import de.uniulm.bagception.protocol.bundle.constants.ResponseAnswer;

public class ConnectToDevice extends ListActivity {

	private BluetoothDeviceArrayAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		if (getIntent().getExtras() == null){
			Toast.makeText(this, "Es steht kein Bluetooth Gerät zur Auswahl", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		ArrayList<BluetoothDevice> devices = getIntent().getExtras().getParcelableArrayList(Response.EXTRA_KEYS.PAYLOAD);
		adapter = new BluetoothDeviceArrayAdapter(this);
		adapter.addAll(devices);
		
		setListAdapter(adapter);
		
		
		super.onCreate(savedInstanceState);

		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Bundle b = ResponseAnswer.Ask_For_Specific_Device.toBundle();
		b.putParcelable(ResponseAnswer.EXTRA_KEYS.PAYLOAD, adapter.getItem(position));
		new BundleMessageHelper(this).sendResponseAnswerBundle(b);
		finish();
	}


}
