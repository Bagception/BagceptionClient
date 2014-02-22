package de.uniulm.bagception.client.ui.launcher;

import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.client.bluetooth.pairing.BagceptionPairing;
import de.uniulm.bagception.client.bluetooth.pairing.BagceptionPairing.BagceptionPairingCallbacks;
import de.uniulm.bagception.client.bluetooth.pairing.BluetoothDeviceArrayAdapter;
import de.uniulm.bagception.client.bluetooth.pairing.ManageConnection;
import de.uniulm.bagception.protocol.bundle.constants.ResponseAnswer;

public class NewBagFragment extends ListFragment {

	private BluetoothDeviceArrayAdapter mAdapter;
	private BagceptionPairing pairingHelper;
	private ProgressDialog dialog;

	public static Fragment newInstance(Context context) {
		NewBagFragment f = new NewBagFragment();
		return f;
	}

	private void abortScan() {
		if (dialog != null)
			dialog.dismiss();
		pairingHelper.cancel();

	}

	private void startScan() {
		dialog(DIALOG_SEARCH);
		pairingHelper.startScan();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mAdapter = new BluetoothDeviceArrayAdapter(getActivity());
		pairingHelper = new BagceptionPairing(callback);
		super.onActivityCreated(savedInstanceState);
		setListAdapter(mAdapter);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Nach Geräten suchen?")
				.setCancelable(false)
				.setPositiveButton("suchen",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								startScan();

							}
						})
				.setNegativeButton("abbrechen",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
 
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void pairingStatus(BluetoothDevice d, boolean success) {
		dialog(DIALOG_NONE);
		if (getActivity() == null)
			return;
		if (success) {
			Bundle b = ResponseAnswer.Ask_For_Specific_Device.toBundle();
			b.putParcelable(ResponseAnswer.EXTRA_KEYS.PAYLOAD, d);
			new BundleMessageHelper(getActivity()).sendResponseAnswerBundle(b);
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
									dialog(DIALOG_SEARCH);
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

		
		dialog(DIALOG_PAIR);
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
		dialog(DIALOG_NONE);
		try {
			if (pairingHelper != null)
				pairingHelper.unregister(getActivity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private final BagceptionPairingCallbacks callback = new BagceptionPairingCallbacks() {

		@Override
		public void onScanStart() {
		}

		@Override
		public void onScanFinished(List<BluetoothDevice> devices) {
			dialog(DIALOG_NONE);
			if (devices.size() == 0) {
				if (getActivity() != null) {
					Toast.makeText(getActivity(), "Keine Geräte gefunden",
							Toast.LENGTH_SHORT).show();
				}

			}
		}

		@Override
		public void onDeviceFound(BluetoothDevice device) {
			mAdapter.add(device);
		}
	};

	private final int DIALOG_SEARCH = 0;
	private final int DIALOG_PAIR = 1;
	private final int DIALOG_NONE = 2;

	private void dialog(int dialog) {
		if (this.dialog != null) {
			this.dialog.dismiss();
		}

		switch (dialog) {
		case DIALOG_SEARCH:
			this.dialog = ProgressDialog
					.show(getActivity(),
							"suche..",
							"Suche nach Geräten... bitte warten");//\n\nDer Bagception Server muss auf dem fremden Gerät eingeschaltet sein");
			this.dialog.setCanceledOnTouchOutside(true);
			this.dialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					abortScan();
				}
			});
			break;
		case DIALOG_PAIR:
			this.dialog = ProgressDialog.show(getActivity(), "Pairing..",
					"Pairing wird durchgeführt");
			this.dialog.setCanceledOnTouchOutside(false);
			break;
		}
	}
}
