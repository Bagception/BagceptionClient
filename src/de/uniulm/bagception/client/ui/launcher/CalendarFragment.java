package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.CalendarEvent;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ActivityCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.client.R;

public class CalendarFragment extends Fragment implements BundleMessageReactor{

	private Button sendBtn;
	private Button cancelBtn;
	private Button viewCalendarBtn;
	private Button viewActivitiesBtn;
	private Button showTimePickerBtn;
	private Button showDatePickerBtn;
	private TextView selectedActivityTextView;
	private TextView selectedCalendarTextView;
	private EditText eventNameEditText;
	private TextView timeTextView;
	private TextView dateTextView;
	private ArrayAdapter<String> calendarNamesAdapter;
	private ArrayAdapter<String> calendarEventsAdapter;
	private ArrayAdapter<String> activityNamesAdapter;
	private ArrayList<String> calendarNames;
	private ArrayList<String> calendarEvents;
	private ArrayList<String> activityNames;
	private ArrayList<Activity> activityList;
	private AlertDialog.Builder calendarNamesAlertDialog;
	private AlertDialog.Builder activitieNamesAlertDialog;
	
	private Calendar chosenCalendarTime;

	private BundleMessageActor actor;
	
	public static Fragment newInstance(Context context) {
		CalendarFragment c = new CalendarFragment();
		return c;
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
				R.layout.fragment_calendar, null);
		chosenCalendarTime = Calendar.getInstance();
		actor = new BundleMessageActor(this);
		
		calendarNames = new ArrayList<String>();
		calendarEvents = new ArrayList<String>();
		activityNames = new ArrayList<String>();
		activityList = new ArrayList<Activity>();
		this.eventNameEditText = (EditText) root.findViewById(R.id.eventNameEditText);
		this.selectedActivityTextView = (TextView) root.findViewById(R.id.selectedActivityTextView);
		this.selectedCalendarTextView = (TextView) root.findViewById(R.id.selectedCalendarTextView);
		this.timeTextView = (TextView) root.findViewById(R.id.timeTextView);
		this.dateTextView = (TextView) root.findViewById(R.id.dateTextView);
		sendBtn = (Button) root.findViewById(R.id.sendCalendarBtn);
		cancelBtn = (Button) root.findViewById(R.id.cancelCalendarBtn);
		viewCalendarBtn = (Button) root.findViewById(R.id.viewCalendarBtn);
		viewActivitiesBtn = (Button) root.findViewById(R.id.viewActivitiesBtn);
		showTimePickerBtn = (Button) root.findViewById(R.id.showTimePickerBtn);
		this.showDatePickerBtn = (Button) root.findViewById(R.id.showDatePickerBtn);

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
								selectedCalendarTextView.setText(calendarNames.get(which));
							}
						});
				calendarNamesAlertDialog.create().show();
			}
		});
		
		viewActivitiesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activityNames.clear();
				activityList.clear();
				activitieNamesAlertDialog = new AlertDialog.Builder(getActivity());
				activitieNamesAlertDialog.setTitle("Aktivitäten");
				activityNamesAdapter = new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_selectable_list_item, activityNames);
				new BundleMessageHelper(getActivity())
				.sendMessageSendBundle(BundleMessage.getInstance()
						.createBundle(
								BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, ActivityCommand.list()));
				activitieNamesAlertDialog.setAdapter(activityNamesAdapter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,	int which) {
								selectedActivityTextView.setText(activityNames.get(which));
							}
						});
				activitieNamesAlertDialog.create().show();
			}
		});
		
		showTimePickerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar rightNow = Calendar.getInstance();
				TimePickerDialog dialog = new TimePickerDialog(getActivity(), new OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						timeTextView.setText(hourOfDay + ":" + minute + "Uhr");
						chosenCalendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
						chosenCalendarTime.set(Calendar.MINUTE, minute);
					}
				}, rightNow.get(Calendar.HOUR_OF_DAY), rightNow.get(Calendar.MINUTE), true);
				dialog.show();
			}
		});
		
		showDatePickerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar rightNow = Calendar.getInstance();
				DatePickerDialog dialog = new DatePickerDialog(getActivity(),
						new OnDateSetListener() {
					
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear,
									int dayOfMonth) {
								chosenCalendarTime.set(Calendar.YEAR, year);
								chosenCalendarTime.set(Calendar.MONTH, monthOfYear);
								dateTextView.setText(dayOfMonth + "/" + chosenCalendarTime.get(Calendar.MONTH) + "/" + year);
							}
						}
						, rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MONTH), rightNow.get(Calendar.DAY_OF_MONTH));
				dialog.show();
				}
		
		});
		
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				log("YEAR:" + chosenCalendarTime.get(Calendar.YEAR));
				log("MONTH:" + chosenCalendarTime.get(Calendar.MONTH));
				log("DAY:" + chosenCalendarTime.get(Calendar.DAY_OF_MONTH));
				log("HOUR:" + chosenCalendarTime.get(Calendar.HOUR_OF_DAY));
				log("MIN:" + chosenCalendarTime.get(Calendar.MINUTE));
				log("2HLATER: " + (chosenCalendarTime.get(Calendar.HOUR_OF_DAY)+2));
				Calendar twoHoursLaterEvent = chosenCalendarTime;
				int hours = chosenCalendarTime.get(Calendar.HOUR_OF_DAY);
				hours += 2;
				twoHoursLaterEvent.set(Calendar.HOUR_OF_DAY, hours);
				CalendarEvent event = new CalendarEvent(eventNameEditText.getText().toString(),
														selectedCalendarTextView.getText().toString(), 
														selectedActivityTextView.getText().toString(),
														"-1",
														chosenCalendarTime.getTimeInMillis(),
														twoHoursLaterEvent.getTimeInMillis());
				
				new BundleMessageHelper(getActivity())
				.sendMessageSendBundle(BundleMessage.getInstance()
						.createBundle(
								BUNDLE_MESSAGE.CALENDAR_ADD_EVENT_REQUEST,
								event));
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
		case ADMINISTRATION_COMMAND: {
			log("admin command received...");
			AdministrationCommandProcessor p = new AdministrationCommandProcessor(){
				public void onActivityList(de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand<de.uniulm.bagception.bundlemessageprotocol.entities.Activity> i) {
					if (i.isSuccessful()){
						Activity[] temp = i.getPayloadObjects();
						for(int j=0; j<temp.length; j++){
							activityNames.add(temp[j].getName());
							activityList.add(temp[j]);
							log(j + " " + temp[j]);
							activityNamesAdapter.notifyDataSetChanged();
							break;
						}
					}else{
						log("error");
					}
				};
			};
			AdministrationCommand.fromJSONObject(BundleMessage.getInstance().extractObject(b)).accept(p);
			
		}
		default:
			break;
		}
	}

	@Override
	public void onBundleMessageSend(Bundle b) {	}
	@Override
	public void onResponseMessage(Bundle b) {	}
	@Override
	public void onResponseAnswerMessage(Bundle b) {	}
	@Override
	public void onStatusMessage(Bundle b) {	}
	@Override
	public void onCommandMessage(Bundle b) {	}
	@Override
	public void onError(Exception e) {	}
	
//	
//	
//	public static class TimePickerFragment extends DialogFragment implements
//			TimePickerDialog.OnTimeSetListener {
//
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//			// Use the current time as the default values for the picker
//			final Calendar c = Calendar.getInstance();
//			int hour = c.get(Calendar.HOUR_OF_DAY);
//			int minute = c.get(Calendar.MINUTE);
//
//			// Create a new instance of TimePickerDialog and return it
//			return new TimePickerDialog(getActivity(), this, hour, minute,
//					DateFormat.is24HourFormat(getActivity()));
//		}
//
//		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//			log("hourOfDay: " + hourOfDay);
//			log("minute: " + minute);
//		}
//		
//		private void log(String s){
//			Log.d("TimePickerFragment", s);
//		}
//	}
//	
//	public static class DatePickerFragment extends DialogFragment implements
//			DatePickerDialog.OnDateSetListener {
//		
//		
//		@Override
//		public Dialog onCreateDialog(Bundle savedInstanceState) {
//			// Use the current date as the default date in the picker
//			final Calendar c = Calendar.getInstance();
//			int year = c.get(Calendar.YEAR);
//			int month = c.get(Calendar.MONTH);
//			int day = c.get(Calendar.DAY_OF_MONTH);
//
//			// Create a new instance of DatePickerDialog and return it
//			return new DatePickerDialog(getActivity(), this, year, month, day);
//		}
//
//		public void onDateSet(DatePicker view, int year, int month, int day) {
//			log("year: " + year);
//			log("month: " + month);
//			log("day: " + day);
//		}
//		
//		private void log(String s){
//			Log.d("DatePickerFragment", s);
//		}
//	}

}


