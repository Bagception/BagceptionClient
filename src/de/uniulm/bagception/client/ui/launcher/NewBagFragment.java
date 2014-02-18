package de.uniulm.bagception.client.ui.launcher;

import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import de.uniulm.bagception.client.bluetooth.pairing.BagceptionPairing;
import de.uniulm.bagception.client.bluetooth.pairing.BagceptionPairing.BagceptionPairingCallbacks;
import de.uniulm.bagception.client.bluetooth.pairing.BluetoothDeviceArrayAdapter;
import de.uniulm.bagception.client.bluetooth.pairing.ManageConnection;

public class NewBagFragment extends ListFragment {

	private BluetoothDeviceArrayAdapter mAdapter;
	private BagceptionPairing pairingHelper;
	private ProgressDialog dialog;
	private ProgressDialog pairingDialog;

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
		if (dialog != null)
			dialog.dismiss();

		dialog = ProgressDialog
				.show(getActivity(),
						"suche..",
						"Suche nach Geräten... bitte warten\n\nDer Bagception Server muss auf dem fremden Gerät eingeschaltet sein");
		pairingHelper.startScan();

	}

	public void pairingStatus(BluetoothDevice d, boolean success) {
		pairingDialog.dismiss();
		mAdapter.clear();
		if (success) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Pairing erfolgreich")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Pairing fehlgeschlagen")
					.setCancelable(false)
					.setNegativeButton("erneut suchen",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									NewBagFragment.this.dialog = ProgressDialog
											.show(getActivity(),
													"suche..",
													"Suche nach Geräten... bitte warten\n\nDer Bagception Server muss auf dem fremden Gerät eingeschaltet sein");
									pairingHelper.startScan();
								}
							})
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});

			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (pairingDialog != null)
			pairingDialog.dismiss();
		pairingDialog = ProgressDialog.show(getActivity(), "Pairing..",
				"Pairing wird durchgeführt");
		BluetoothDevice d = mAdapter.getItem(position);
		ManageConnection mg = new ManageConnection(this);
		mg.execute(d);
	}

	@Override
	public void onStart() {
		// scan for devices
		super.onStart();
		pairingHelper.register(getActivity());
	}

	@Override
	public void onStop() {

		super.onStop();
		if (dialog != null)
			dialog.dismiss();
		if (pairingDialog != null)
			pairingDialog.dismiss();
		try {
			if (pairingHelper != null)
				pairingHelper.unregister(getActivity());
		} catch (Exception e) {
		}
	}

	@Override
	public void onPause() {
		getFragmentManager().popBackStack();
		super.onPause();
	}

	private final BagceptionPairingCallbacks callback = new BagceptionPairingCallbacks() {

		@Override
		public void onScanStart() {
		}

		@Override
		public void onScanFinished(List<BluetoothDevice> devices) {
			mAdapter.addAll(devices);
			dialog.dismiss();
			if (devices.size() == 0) {
				Toast.makeText(getActivity(), "Keine Geräte gefunden",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onDeviceFound(BluetoothDevice device) {
			// nop
		}
	};
}
