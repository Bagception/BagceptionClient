package de.uniulm.bagception.client.debugactivities;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;

public class Debug extends Activity implements BundleMessageReactor{

	BundleMessageActor bmA;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bmA = new BundleMessageActor(this);
		setContentView(R.layout.activity_debug);
		
		
	}
	@Override
	public void onBundleMessageRecv(Bundle b) {
		switch (BundleMessage.getInstance().getBundleMessageType(b)){
			case CONTAINER_STATUS_UPDATE:
				JSONObject o = BundleMessage.getInstance().extractObject(b);
				ContainerStateUpdate update = ContainerStateUpdate.fromJSON(o);
				onUpdate(update);
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
		
	}
	@Override
	public void onCommandMessage(Bundle b) {
		
	}
	@Override
	public void onError(Exception e) {
		
	}

	private void onUpdate(ContainerStateUpdate update){
		ListView v = (ListView)findViewById(R.id.debugList);
		ArrayAdapter<String> debugListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		debugListAdapter.add("Activity:");
		debugListAdapter.add(update.getActivity().getName());
		debugListAdapter.add(" "+"Location:");
		debugListAdapter.add("  "+update.getActivity().getLocation().getName());
		debugListAdapter.add("  "+update.getActivity().getLocation().getLat()+"");
		debugListAdapter.add("  "+update.getActivity().getLocation().getLng()+"");
		debugListAdapter.add("  "+update.getActivity().getLocation().getRadius()+"");
		debugListAdapter.add("  "+update.getActivity().getLocation().getMac());
		debugListAdapter.add(" "+"items");
		for (Item i : update.getActivity().getItemsForActivity()){
			debugListAdapter.add("  "+i.getName());
			debugListAdapter.add("   "+i.getId());
			debugListAdapter.add("   "+i.getImageHash());
			debugListAdapter.add("   "+i.getCategory());
		}
		debugListAdapter.add(" ");
		debugListAdapter.add(" "+"itemsInBag");
		for(Item i:update.getItemList()){
			debugListAdapter.add("  "+i.getName());
			debugListAdapter.add("   "+i.getId());
			debugListAdapter.add("   "+i.getImageHash());
			debugListAdapter.add("   "+i.getCategory());
		}
		
		v.setAdapter(debugListAdapter);
	}
	
	@Override
	protected void onResume() {
		bmA.register(this);
		super.onResume();
		BundleMessageHelper h = new BundleMessageHelper(this);
		
		h.sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.CONTAINER_STATUS_UPDATE_REQUEST, ""));
		
	}
	
	@Override
	protected void onPause() {
		bmA.unregister(this);
		super.onPause();
	}
	

	
}
