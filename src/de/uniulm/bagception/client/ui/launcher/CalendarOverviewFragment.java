package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;

import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.CalendarEvent;
import de.uniulm.bagception.client.R;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CalendarOverviewFragment extends ListFragment implements BundleMessageReactor{

	private BundleMessageActor actor;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> calendarEventStrings;
	private ArrayList<CalendarEvent> calendarEvents;
	ArrayList<String> text = new ArrayList<String>();

	
	
	public static Fragment newInstance(Context context) {
		CalendarOverviewFragment c = new CalendarOverviewFragment();
		return c;
	}	
	
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		log("RESUUUUUUME");
		updateCalendarEventList();
	}




	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.actionbar_button, menu);
		MenuItem item = menu.findItem(R.id.menu_item_add);
		log("optionsMenü pressed");
		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(getActivity(), MainGUI.class);
				intent.putExtra("FRAGMENT", "de.uniulm.bagception.client.ui.launcher.CalendarFragment");
				startActivity(intent);
				return false;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}




	@Override
	public void onStart() {

		super.onStart();
		actor.register(getActivity());
	}
	
	@Override
	public void onStop() {

		super.onStop();
		actor.unregister(getActivity());
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_calendar_overview, null);
		this.actor = new BundleMessageActor(this);
		this.calendarEventStrings = new ArrayList<String>();
		this.calendarEvents = new ArrayList<CalendarEvent>();
		this.adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, calendarEventStrings);  
		setHasOptionsMenu(true);
		setListAdapter(adapter); 
		updateCalendarEventList();
		return root;

	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		CalendarEvent ce = calendarEvents.get(position);
		log(ce.getName());
		log(ce.getDescription());
		log(""+ce.getStartDate());
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	
	public void updateCalendarEventList(){
		calendarEventStrings.clear();
		calendarEvents.clear();
		
		new BundleMessageHelper(getActivity())
		.sendMessageSendBundle(BundleMessage.getInstance()
				.createBundle(
						BUNDLE_MESSAGE.CALENDAR_EVENT_REQUEST,
						null));
	}
	
	
	public void log(String s){
		Log.d("CalendarOverviewFragment", s);
	}

	@Override
	public void onBundleMessageRecv(Bundle b) {
		switch (BundleMessage.getInstance().getBundleMessageType(b)) {
		
		case CALENDAR_EVENT_REPLY: {
			CalendarEvent event = CalendarEvent.fromJSON(BundleMessage.getInstance().extractObject(b));
			calendarEvents.add(event);
			calendarEventStrings.add(event.getName());
			adapter.notifyDataSetChanged();
			break;
		}
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


}
