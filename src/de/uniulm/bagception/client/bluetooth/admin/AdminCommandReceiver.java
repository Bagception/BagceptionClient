package de.uniulm.bagception.client.bluetooth.admin;

import org.json.simple.JSONObject;

import android.os.Bundle;
import android.util.Log;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;

public class AdminCommandReceiver extends AdministrationCommandProcessor implements BundleMessageReactor {

	@Override
	public void onBundleMessageRecv(Bundle b) {
		BUNDLE_MESSAGE msg = BundleMessage.getInstance().getBundleMessageType(b);
		switch (msg){
		case ADMINISTRATION_COMMAND:
			JSONObject json = BundleMessage.getInstance().extractObject(b);
			AdministrationCommand<?> a_cmd = AdministrationCommand.fromJSONObject(json);
			a_cmd.accept(this);
		break;
		default: break;
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
		
	}

	@Override
	public void onCommandMessage(Bundle b) {
		
	}

	@Override
	public void onError(Exception e) {
		
	}

	
	
}
