package de.uniulm.bagception.client;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.protocol.bundle.constants.Command;
import de.uniulm.bagception.protocol.bundle.constants.StatusCode;

public class ConnectivityFragment extends Fragment implements BundleMessageReactor{

	
	private BundleMessageActor bmActor;
	private TextView connectedString;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		connectedString = new TextView(getActivity());
		connectedString.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new BundleMessageHelper(getActivity()).sendCommandBundle(Command.DISCONNECT.toBundle());
			}
		});
		return connectedString;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		bmActor = new BundleMessageActor(this);
		bmActor.register(getActivity());
		new BundleMessageHelper(getActivity()).sendCommandBundle(Command.RESEND_STATUS.toBundle());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		bmActor.unregister(getActivity());
	}


	// \\ BundleMessageReactor //
	@Override
	public void onBundleMessageRecv(Bundle b) {
		
	}



	@Override
	public void onBundleMessageSend(Bundle b) {		
	}



	@Override
	public void onResponseMessage(Bundle b) {
	}



	@Override
	public void onResponseAnswerMessage(Bundle b) {
	}


	@Override
	public void onStatusMessage(Bundle b) {
		StatusCode status = StatusCode.getStatusCode(b);
		
		switch (status){
		case CONNECTED:
			String devName = b.getString(StatusCode.EXTRA_KEYS.CONNECTED_DEVICE_NAME);
			connectedString.setText("connected with "+devName);
			connectedString.setTextColor(Color.GREEN);
			break;
			
		case DISCONNECTED:
			connectedString.setText("disconnected");
			connectedString.setTextColor(Color.RED);
			break;
			
		default: break;
		
		}
	}



	@Override
	public void onCommandMessage(Bundle b) {		
	}



	@Override
	public void onError(Exception e) {		
	}
	
	// BundleMessageReactor \\
}

