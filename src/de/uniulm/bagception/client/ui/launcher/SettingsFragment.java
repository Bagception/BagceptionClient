package de.uniulm.bagception.client.ui.launcher;

import org.json.simple.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.protocol.bundle.constants.Command;
import de.uniulm.bagception.protocol.bundle.constants.StatusCode;

public class SettingsFragment extends Fragment implements BundleMessageReactor{

	
	//ui
	private ImageView  service_button;
	private TextView service_status_text;
	private ImageView  bt_button;
	private TextView bt_status;
	private ImageView battery;
	private TextView batteryPercentage;
	private TextView bt_connected_with;
	
	private boolean isConnected = false;
	
	//communication
	private BundleMessageActor bmActor;
	private BundleMessageHelper bmHelper;
	
	public static Fragment newInstance(Context context) {
		SettingsFragment f = new SettingsFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_settings_layout, null);
		
		
		service_button = (ImageView) root.findViewById(R.id.service_button);
		service_status_text = (TextView)root.findViewById(R.id.service_status_text);
		bt_button = (ImageView) root.findViewById(R.id.bt_button);
		bt_status = (TextView) root.findViewById(R.id.bt_status_text);
		batteryPercentage = (TextView) root.findViewById(R.id.batteryPercentage);
		battery = (ImageView) root.findViewById(R.id.battery_status);
		bt_connected_with = (TextView) root.findViewById(R.id.bt_connected_with);
		
		
		bt_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isConnected){
					bmHelper.sendCommandBundle(Command.DISCONNECT.toBundle());	
				}else{
					bmHelper.sendCommandBundle(Command.TRIGGER_SCAN_DEVICES.toBundle());
				}
			}
				
			});
		
		
		service_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//TODO 
			}
		});
		
		return root;
	}

	
	
	
	@Override
	public void onStart() {
		bmActor = new BundleMessageActor(this);
		bmActor.register(getActivity());
		bmHelper = new BundleMessageHelper(getActivity());
		super.onStart();
	}
	
	@Override
	public void onResume() {
		bmHelper.sendCommandBundle(Command.RESEND_STATUS.toBundle());
		super.onResume();
	}
	
	@Override
	public void onStop() {
		bmActor.unregister(getActivity());
		super.onStop();
	}
	
	@Override
	public void onBundleMessageRecv(Bundle b) {
		BUNDLE_MESSAGE bmsg = BundleMessage.getInstance().getBundleMessageType(b);
		JSONObject obj = BundleMessage.getInstance().extractObject(b);
		switch (bmsg){
		case ADMINISTRATION_COMMAND:
			break;
		case CONTAINER_STATUS:
			break;
		case CONTAINER_STATUS_UPDATE:
			ContainerStateUpdate csu = ContainerStateUpdate.fromJSON(obj);
			int battery = csu.getBatteryState();
			batteryPercentage.setText(battery+"");
			break;
		case CONTAINER_STATUS_UPDATE_REQUEST:
			break;
		case IMAGE_REPLY:
			break;
		case IMAGE_REQUEST:
			break;
		case ITEM_FOUND:
			break;
		case ITEM_NOT_FOUND:
			break;
		case NOT_A_BUNDLE_MESSAGE:
			break;
		default:
			break;
		
		}
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
			bt_connected_with.setText(devName);
			bt_connected_with.setTextColor(Color.BLACK);
			
			bt_status.setText("connected");
			bt_connected_with.setTextColor(Color.GREEN);
			
			isConnected = true;
			
			bt_button.setImageResource(R.drawable.power_onstate);
			
			
			break;
			
		case DISCONNECTED:
			bt_connected_with.setText("nothing");
			bt_connected_with.setTextColor(Color.RED);
			
			bt_status.setText("disconnected");
			bt_connected_with.setTextColor(Color.RED);
			bt_button.setImageResource(R.drawable.power_offstate);
			isConnected = false;
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

}

