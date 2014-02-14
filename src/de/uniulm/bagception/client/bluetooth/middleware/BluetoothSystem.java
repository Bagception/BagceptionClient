package de.uniulm.bagception.client.bluetooth.middleware;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import de.philipphock.android.lib.logging.LOG;
import de.uniulm.bagception.bluetooth.BagceptionBTServiceInterface;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.util.BagceptionBluetoothUtil;
import de.uniulm.bagception.bluetoothclientmessengercommunication.util.CheckReachableCallback;
import de.uniulm.bagception.client.service.BagceptionClientService;
import de.uniulm.bagception.protocol.bundle.BundleProtocolCallback;
import de.uniulm.bagception.protocol.bundle.constants.Command;
import de.uniulm.bagception.protocol.bundle.constants.Response;
import de.uniulm.bagception.protocol.bundle.constants.StatusCode;

public class BluetoothSystem implements CheckReachableCallback,
		ResponseSystem.Interaction, BundleProtocolCallback,
		BTClient.ClientStatusCallback, BundleMessageReactor {

	private boolean isConnecting=false;
	private BTClient btclient;
	private boolean lastConnectionState_connected = false;

	// TODO later, init this with config values
	private ResponseMode responseMode = ResponseMode.MINIMAL;
	private ResponseSystem responseSystem;

	private final BluetoothAdapter btAdapter = BluetoothAdapter
			.getDefaultAdapter();

	// counter that indicates if there is a pending isReachable request to
	// determine when the test is done (due to async operatopn)
	private volatile int pendingDeviceFeedbacks = 0;
	private final ArrayList<BluetoothDevice> bagceptionDevicesInRange = new ArrayList<BluetoothDevice>();

	private BluetoothDevice tmp_bt_device_confirm;

	private final BagceptionClientService mainService;

	public BluetoothSystem(BagceptionClientService mainService) {
		this.mainService = mainService;
		responseSystem = new ResponseSystem(this);
	}

	public enum ResponseMode {

		/**
		 * the service does most things automatically, if he is not sure, the
		 * ResponseSystem is asked to handle that note that an actual responder
		 * can also be software, not only a user interaction
		 * 
		 * with this design, we can implement smart services that handles
		 * interactions implicitly
		 */
		MINIMAL,

		/**
		 * the service performs operations in background but halts on every
		 * decision, even if it is the only option(like connection to only one
		 * device) this mode is intended to be used with your primary smartphone
		 */
		MAXIMAL
	}

	public void sendResponseBundle(Bundle b) {
		mainService.bmHelper.sendResponseBundle(b);
	}

	// internal routines

	// states

	protected void handleNotConnectedState(boolean connectionChanged) {
		Bundle conn = StatusCode.DISCONNECTED.toBundle();
		conn.putBoolean(StatusCode.EXTRA_KEYS.CONNECTION_CHANGED,
				connectionChanged);
		mainService.bmHelper.sendStatusBundle(conn);
	}

	protected void handleConnectedState(boolean connectionChanged) {
		Bundle conn = StatusCode.CONNECTED.toBundle();
		conn.putString(StatusCode.EXTRA_KEYS.CONNECTED_DEVICE_NAME,
				btclient.getRemoteDeviceName());
		conn.putBoolean(StatusCode.EXTRA_KEYS.CONNECTION_CHANGED,
				connectionChanged);
		mainService.bmHelper.sendStatusBundle(conn);

	}

	// connect

	protected void connectToAvailableContainer(BluetoothDevice device) {

		try {
			btclient = new BTClient(device,
					BagceptionBTServiceInterface.BT_UUID, this, this);
			btclient.startListeningForIncomingBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// data transmission

	/*
	 * ############################################### ###############
	 * reachability check ##############
	 * #################################################
	 */

	protected void getPairedBagceptionDevicesInRangeAsyncDone() {

		// //\\\\\\\\\ DEBUG CODE ///////////
		// if (!DEBUG_SWITCH){
		// DEBUG_SWITCH=true;
		// }else{
		// BluetoothDevice d = bagceptionDevicesInRange.get(0);
		// bagceptionDevicesInRange.add(d);
		// }
		// //////////// DEBUG CODE \\\\\\\\\\

		if (bagceptionDevicesInRange.size() == 0) {
			// nothing in range.. pause? //TODO
		} else if (bagceptionDevicesInRange.size() == 1) {
			// here we have only one device in range

			final BluetoothDevice device = bagceptionDevicesInRange.get(0);

			switch (responseMode) {

			case MAXIMAL:
				tmp_bt_device_confirm = device;
				responseSystem
						.makeResponse_confirmEstablishingConnection(device);

				break;

			case MINIMAL:
				// connect to container
				connectToAvailableContainer(device);
				break;

			}

		} else {
			LOG.out(this, "multiple devices found");
			responseSystem
					.makeResponse_askForSpecificDevice(bagceptionDevicesInRange);
		}
	}

	protected synchronized void getPairedBagceptionDevicesInRangeAsync() {
		LOG.out(this, "begin scanning..");
		bagceptionDevicesInRange.clear();
		Set<BluetoothDevice> bonded = btAdapter.getBondedDevices();
		final UUID serviceUUID = UUID
				.fromString(BagceptionBTServiceInterface.BT_UUID);
		for (BluetoothDevice device : bonded) {
			LOG.out(this, "bond device: " + device.getName());
			if (BagceptionBluetoothUtil.isBagceptionServer(device)) {
				LOG.out(this, "bagception device: " + device.getName());
				pendingDeviceFeedbacks++;
				BagceptionBluetoothUtil.checkReachable(device, serviceUUID,
						this);
			}else{
				LOG.out(this, "no bagception device: " + device.getName());
			}
		}

	}

	/*
	 * Begin CheckReachableCallback
	 */
	@Override
	public void isReachable(BluetoothDevice device, boolean reachable) {
		pendingDeviceFeedbacks--;
		LOG.out(this, "DEVICE: " + device.getName() + " reachable: "
				+ reachable);
		if (reachable) {
			bagceptionDevicesInRange.add(device);
		}
		if (pendingDeviceFeedbacks <= 0) {
			// all bond devices checked for reachability
			getPairedBagceptionDevicesInRangeAsyncDone();

		}

	}

	/*
	 * **********************************
	 * End CheckReachableCallback**********************************
	 */

	/*
	 * **********************************
	 * Begin ResponseSystem.Interaction**********************************
	 */
	@Override
	public void interactionFor_askForSpecificDevice(BluetoothDevice d) {
		connectToAvailableContainer(d);
	}

	@Override
	public void interactionFor_confirmEstablishingConnection(boolean connect) {
		if (connect) {
			connectToAvailableContainer(tmp_bt_device_confirm);
		} else {
			// TODO todo?
		}

	}

	/*
	 * **********************************
	 * End ResponseSystem.Interaction**********************************
	 */

	/*
	 * **********************************
	 * Begin BundleProtocolCallback**********************************
	 */
	@Override
	public void onBundleRecv(Bundle bundle) {
		mainService.bmHelper.sendMessageRecvBundle(bundle);

	}

	/*
	 * **********************************
	 * End BundleProtocolCallback**********************************
	 */

	/*
	 * **********************************
	 * Begin BTClient.ClientStatusCallback**********************************
	 */
	@Override
	public void onConnect() {
		handleConnectedState(lastConnectionState_connected != true);
		responseSystem.makeResponse_bluetoothConnection(true,
				lastConnectionState_connected != true);
		lastConnectionState_connected = true;
		isConnecting=false;
	}

	@Override
	public void onDisconnect() {
		handleNotConnectedState(lastConnectionState_connected != false);
		responseSystem.makeResponse_bluetoothConnection(false,
				lastConnectionState_connected != false);
		lastConnectionState_connected = false;
		isConnecting=false;
	}

	@Override
	public void onConnecting() {
		isConnecting=true;
		mainService.bmHelper.sendStatusBundle(StatusCode.CONNECTING.toBundle());
	}
	
	/*
	 * **********************************
	 * End BTClient.ClientStatusCallback**********************************
	 */

	/*
	 * **********************************
	 * Begin BundleMessageReactor**********************************
	 */
	@Override
	public void onResponseMessage(Bundle b) {
		// noting to do here
	}

	@Override
	public void onStatusMessage(Bundle b) {

	}

	@Override
	public void onResponseAnswerMessage(Bundle b) {
		responseSystem.handleInteraction(b);
	}

	@Override
	public void onCommandMessage(Bundle b) {
		Command command = Command.getCommand(b);
		switch (command) {
		case TRIGGER_SCAN_DEVICES:
			mainService.bmHelper.sendStatusBundle(StatusCode.CONNECTING.toBundle());
			mainService.bmHelper.sendResponseBundle(Response.CLEAR_RESPONSES.toBundle());

			getPairedBagceptionDevicesInRangeAsync();
			break;
		case PING:
			mainService.bmHelper.sendCommandBundle(Command
					.getCommandBundle(Command.PONG));
			break;
		case PONG:
			// nothing to do here, Pong is only on client side
		case POLL_ALL_RESPONSES:
			responseSystem.resendAll();
			break;

		case RESEND_STATUS:
			if (btclient == null) {
				onDisconnect();
				return;
			}
			if (btclient.isConnected()) {
				onConnect();
			} else {
				if (isConnecting){
					onConnecting();
				}else{
					onDisconnect();	
				}
				
			}
			break;
		case DISCONNECT:
			try {
				if (btclient != null){
					btclient.cancel();	
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}

	}

	

	// data recv from remote endpont via broadcast
	@Override
	public void onBundleMessageRecv(Bundle b) {
		// noting todo here, this is not delivered by broadcasts

	}

	// data received from local endpoint, that is data to be send
	@Override
	public void onBundleMessageSend(Bundle b) {
		if (btclient == null) {
			onCannotSendDueToNotConnected();
		} else {
			btclient.send(b);
		}
	}

	@Override
	public void onError(Exception e) {
		if (e.getMessage().equals("not connected")){
			onCannotSendDueToNotConnected();	
		}
	}
	
	
	
	private void onCannotSendDueToNotConnected(){
		mainService.bmHelper.sendStatusBundle(StatusCode.UNABLE_TO_SEND_DATA.toBundle());
	}

	

	/*
	 * **********************************
	 * End BundleMessageReactor**********************************
	 */

}
