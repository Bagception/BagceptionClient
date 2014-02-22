package de.uniulm.bagception.client.ui.launcher;

import java.text.SimpleDateFormat;
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
	private Button viewActivitiesBtn;
	private Button showTimePickerBtn;
	private Button showDatePickerBtn;
	private Button showEndTimePickerBtn;
	private Button showEndDatePickerBtn;
	private TextView selectedActivityTextView;
	private EditText eventNameEditText;
	private TextView timeTextView;
	private TextView dateTextView;
	private TextView endDateTextView;
	private TextView endTimeTextView;
	private ArrayAdapter<String> calendarEventsAdapter;
	private ArrayAdapter<String> activityNamesAdapter;
	private ArrayList<String> calendarEvents;
	private ArrayList<String> activityNames;
	private ArrayList<Activity> activityList;
	private Calendar cal;
	private long startTime;
	private long endTime;
	private long startDate;
	private long endDate;
	private AlertDialog.Builder calendarNamesAlertDialog;
	private AlertDialog.Builder activitieNamesAlertDialog;
	

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
		cal = Calendar.getInstance();
		cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		actor = new BundleMessageActor(this);
		startTime = 0;
		endTime = 0;
		startDate = 0;
		endDate = 0;
		
		calendarEvents = new ArrayList<String>();
		activityNames = new ArrayList<String>();
		activityList = new ArrayList<Activity>();
		this.eventNameEditText = (EditText) root.findViewById(R.id.eventNameEditText);
		this.selectedActivityTextView = (TextView) root.findViewById(R.id.selectedActivityTextView);
		this.timeTextView = (TextView) root.findViewById(R.id.timeTextView);
		this.dateTextView = (TextView) root.findViewById(R.id.dateTextView);
		this.endDateTextView = (TextView) root.findViewById(R.id.endDateTextView);
		this.endTimeTextView = (TextView) root.findViewById(R.id.EndTimeTextView);
		this.sendBtn = (Button) root.findViewById(R.id.sendCalendarBtn);
		this.cancelBtn = (Button) root.findViewById(R.id.cancelCalendarBtn);
		this.viewActivitiesBtn = (Button) root.findViewById(R.id.viewActivitiesBtn);
		this.showTimePickerBtn = (Button) root.findViewById(R.id.showTimePickerBtn);
		this.showDatePickerBtn = (Button) root.findViewById(R.id.showDatePickerBtn);
		this.showEndTimePickerBtn = (Button) root.findViewById(R.id.showEndTimePickerBtn);
		this.showEndDatePickerBtn = (Button) root.findViewById(R.id.showEndDatePickerBtn);

		
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
						cal.setTimeInMillis(0);
						cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
						cal.set(Calendar.MINUTE, minute);
						startTime = cal.getTimeInMillis();
						timeTextView.setText(getTime(startTime));
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
								cal.setTimeInMillis(0);
								cal.set(Calendar.YEAR, year);
								cal.set(Calendar.MONTH, monthOfYear);
								cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
								startDate = cal.getTimeInMillis();
								dateTextView.setText(getDate(startDate));
							}
						}
						, rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MONTH), rightNow.get(Calendar.DAY_OF_MONTH));
				dialog.show();
				}
		
		});
		
		showEndDatePickerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar rightNow = Calendar.getInstance();

				DatePickerDialog dialog = new DatePickerDialog(getActivity(),
						new OnDateSetListener() {
					
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear,
									int dayOfMonth) {
								cal.setTimeInMillis(0);
								cal.set(Calendar.YEAR, year);
								cal.set(Calendar.MONTH, monthOfYear);
								cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
								endDate = cal.getTimeInMillis();
								endDateTextView.setText(getDate(endDate));
							}
						}
						, rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MONTH), rightNow.get(Calendar.DAY_OF_MONTH));
				dialog.show();
				}
		});
		
		showEndTimePickerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar rightNow = Calendar.getInstance();
				if(startTime>0){
					rightNow.setTimeInMillis(startTime + 1000 * 60 * 60);
				}
				TimePickerDialog dialog = new TimePickerDialog(getActivity(), new OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						cal.setTimeInMillis(0);
						cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
						cal.set(Calendar.MINUTE, minute);
						endTime = cal.getTimeInMillis();
						endTimeTextView.setText(getTime(endTime));
					}
				}, rightNow.get(Calendar.HOUR_OF_DAY), rightNow.get(Calendar.MINUTE), true);
				dialog.show();
			}
		});
		
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				long dateStart = startDate + startTime;
				long dateEnd = dateStart + 1000 * 60 * 60 * 2;
				CalendarEvent event = new CalendarEvent(eventNameEditText.getText().toString(),
														"", 
														selectedActivityTextView.getText().toString(),
														"-1",
														dateStart,
														dateEnd);
				new BundleMessageHelper(getActivity())
				.sendMessageSendBundle(BundleMessage.getInstance()
						.createBundle(
								BUNDLE_MESSAGE.CALENDAR_ADD_EVENT_REQUEST,
								event));
				getActivity().finish();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}

		});
		
		return root;
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	private void log(String s){
		Log.d("CalendarFragment", s);
	}


	@Override
	public void onBundleMessageRecv(Bundle b) {
		switch (BundleMessage.getInstance().getBundleMessageType(b)) {
//		case CALENDAR_NAME_REPLY: {
//			CalendarEvent calendarName = CalendarEvent.fromJSON(BundleMessage.getInstance().extractObject(b));
//			log("answer from server received. name: " + calendarName.getCalendarName());
//			calendarNames.add(calendarName.getCalendarName());
//			calendarNamesAdapter.notifyDataSetChanged();
//			break;
//		}
		
		case CALENDAR_EVENT_REPLY: {
			CalendarEvent event = CalendarEvent.fromJSON(BundleMessage.getInstance().extractObject(b));
			log("event answer received");
			log(event.getName() + " " + getDate(event.getStartDate()));
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
							activityNamesAdapter.notifyDataSetChanged();
							break;
						}
					}else{
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
	
	public String getDate(long milliSeconds) {
	    SimpleDateFormat formatter = new SimpleDateFormat(
	            "dd/MM/yyyy");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(milliSeconds);
	    return formatter.format(calendar.getTime());
	}
	
	public String getTime(long milliSeconds) {
	    SimpleDateFormat formatter = new SimpleDateFormat(
	            "hh:mm");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(milliSeconds);
	    return formatter.format(calendar.getTime());
	}

}


