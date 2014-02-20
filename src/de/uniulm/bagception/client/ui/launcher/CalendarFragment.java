package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;

import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.CalendarEvent;
import de.uniulm.bagception.bundlemessageprotocol.entities.WifiBTDevice;
import de.uniulm.bagception.client.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CalendarFragment extends Fragment implements BundleMessageReactor{

	private Button sendBtn;
	private Button cancelBtn;
	private Button viewCalendarBtn;
	private Button viewActivitiesBtn;
	private ArrayAdapter<String> calendarNamesAdapter;
	private ArrayAdapter<String> calendarEventsAdapter;
	private ArrayAdapter<String> activityNamesAdapter;
	private ArrayList<String> calendarNames;
	private ArrayList<String> calendarEvents;
	private ArrayList<String> activityNames;
	private AlertDialog.Builder calendarNamesAlertDialog;
	private AlertDialog.Builder activitieNamesAlertDialog;

	private BundleMessageActor actor;
	
	public static Fragment newInstance(Context context) {
		CreateNewItemFragment f = new CreateNewItemFragment();

		return f;
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
				R.layout.fragment_create_new_category, null);
		actor = new BundleMessageActor(this);
		
		calendarNames = new ArrayList<String>();
		calendarEvents = new ArrayList<String>();
		activityNames = new ArrayList<String>();
		
		sendBtn = (Button) root.findViewById(R.id.sendCalendarBtn);
		cancelBtn = (Button) root.findViewById(R.id.cancelCalendarBtn);
		viewCalendarBtn = (Button) root.findViewById(R.id.viewAllCalendarsBtn);
		viewActivitiesBtn = (Button) root.findViewById(R.id.viewAllActivitiesBtn);

		viewCalendarBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				calendarNames.clear();
				calendarNamesAlertDialog = new AlertDialog.Builder(getActivity());
				calendarNamesAlertDialog.setTitle("Kalendernamen");
				calendarNamesAdapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_selectable_list_item, calendarNames);

				new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_selectable_list_item, calendarNames);
				new BundleMessageHelper(getActivity())
				.sendMessageSendBundle(BundleMessage.getInstance()
						.createBundle(
								BUNDLE_MESSAGE.CALENDAR_NAME_REQUEST, null));
				calendarNamesAlertDialog.setAdapter(calendarNamesAdapter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,	int which) {
								log("clicked calendar: " + calendarNames.get(which));
							}
						});
				calendarNamesAlertDialog.create().show();
			}
		});
		
		viewActivitiesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activityNames.clear();
				activitieNamesAlertDialog = new AlertDialog.Builder(getActivity());
				activitieNamesAlertDialog.setTitle("Aktivitäten");
				activityNamesAdapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_selectable_list_item, activityNames);
				//TODO: erstelle bundlemessage type für get all activities
//				new BundleMessageHelper(getActivity())
//				.sendMessageSendBundle(BundleMessage.getInstance()
//						.createBundle(
//								BUNDLE_MESSAGE., null));
				calendarNamesAlertDialog.setAdapter(calendarNamesAdapter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,	int which) {
								log("clicked calendar: " + calendarNames.get(which));
							}
						});
				calendarNamesAlertDialog.create().show();
			}
		});
		
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return root;
	}

	@Override
	public void onPause() {
//		getFragmentManager().popBackStack();
		super.onPause();
	}
	
	private void log(String s){
		Log.d("CalendarFragment", s);
	}


	@Override
	public void onBundleMessageRecv(Bundle b) {
		log("bundle received");
		switch (BundleMessage.getInstance().getBundleMessageType(b)) {
		case CALENDAR_NAME_REPLY: {
			CalendarEvent calendarName = CalendarEvent.fromJSON(BundleMessage.getInstance().extractObject(b));
			log("answer from server received. name: " + calendarName.getCalendarName());
			calendarNames.add(calendarName.getCalendarName());
			calendarNamesAdapter.notifyDataSetChanged();
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


