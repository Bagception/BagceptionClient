package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ActivityCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ItemCommand;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.protocol.bundle.constants.Command;
import de.uniulm.bagception.protocol.bundle.constants.StatusCode;

public class OverviewFragment extends Fragment implements BundleMessageReactor {

	private boolean acceptList = false;

	public static Context appContext;
	private BundleMessageActor bmActor;
	private volatile ContainerStateUpdate statusUpdate;
	
	volatile List<ContextSuggestion> contextSuggestions;
	
	private ItemsInFragment itemsInFragment;
	private ItemsMissFragment itemsMissFragment;
	private ItemsSuggFragment itemsSuggFragment;

	private BundleMessageHelper bmHelper;
	private TextView currentActivityView;

	private ActionBar.Tab itemsInTab;
	private ActionBar.Tab itemsMissTab;
	// ActionBar.Tab itemsNeedlessTab;
	ActionBar.Tab itemsSuggTab;

	private Item unknownItem;
	Button changeActivity;
	Button endActivity;

	private Activity ac = null;

	static Fragment newInstance(Context context) {
		OverviewFragment f = new OverviewFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getActivity().getActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_TABS);
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_overview, null);
		getActivity().setTitle("Übersicht");

		currentActivityView = (TextView) root.findViewById(R.id.test);
		changeActivity = (Button) root.findViewById(R.id.changeActivity);
		endActivity = (Button) root.findViewById(R.id.endActivity);

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#0099CC")));
		actionBar.removeAllTabs();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// initiating both tabs and set text to it.
		itemsInTab = actionBar.newTab().setText("Enthalten (0)");
		itemsMissTab = actionBar.newTab().setText("Fehlend (0)");
		itemsSuggTab = actionBar.newTab().setText("Vorschlag");

		itemsInFragment = new ItemsInFragment();
		itemsInFragment.setParentFragment(this);
		itemsMissFragment = new ItemsMissFragment();
		itemsMissFragment.setParentFragment(this);
		// itemsNeedlessFragment = new ItemsNeedlessFragment();
		// itemsNeedlessFragment.setParentFragment(this);
		itemsSuggFragment = new ItemsSuggFragment();
		itemsSuggFragment.setParentFragment(this);

		itemsInTab.setTabListener(new ItemTabListener(itemsInFragment));
		itemsMissTab.setTabListener(new ItemTabListener(itemsMissFragment));
		// itemsNeedlessTab.setTabListener(new
		// ItemTabListener(itemsNeedlessFragment));
		itemsSuggTab.setTabListener(new ItemTabListener(itemsSuggFragment));

		actionBar.addTab(itemsInTab);
		actionBar.addTab(itemsMissTab);
		// actionBar.addTab(itemsNeedlessTab);
		actionBar.addTab(itemsSuggTab);
		actionBar.getTabAt(0).select();

		changeActivity.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				acceptList = true;
				new BundleMessageHelper(getActivity())
						.sendMessageSendBundle(BundleMessage.getInstance()
								.createBundle(
										BUNDLE_MESSAGE.ACTIVITY_PRIORITY_LIST,
										ActivityCommand.list()));
			}
		});

		endActivity.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder categoryAlert = new AlertDialog.Builder(
						getActivity());
				categoryAlert.setTitle("Aktivität beenden?");

				categoryAlert.setNegativeButton("Abbrechen",
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});

				categoryAlert.setPositiveButton("Beenden",
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (ac != null) {
									new BundleMessageHelper(getActivity())
											.sendMessageSendBundle(BundleMessage
													.getInstance()
													.createBundle(
															BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
															ActivityCommand
																	.stop(ac)));
								}
							}
						});

				categoryAlert.create().show();
			}
		});

		return root;
	}

	List<Item> itemsMust;
	List<Item> itemsIn;
	List<Item> needlessItems;
	List<Item> missingItems;
	List<Item> copiedMustItems;
	

	
	

	public void onBundleMessageRecv(Bundle b) {
		BUNDLE_MESSAGE msg = BundleMessage.getInstance()
				.getBundleMessageType(b);
		switch (msg) {

		case CONTAINER_STATUS_UPDATE:
			itemsInFragment.setParentFragment(this);
			itemsMissFragment.setParentFragment(this);
			// itemsNeedlessFragment.setParentFragment(this);
			
			
			statusUpdate = ContainerStateUpdate.fromJSON(BundleMessage
					.getInstance().extractObject(b));
			String currentActivity = statusUpdate.getActivity().getName();
			ac = statusUpdate.getActivity();
			currentActivityView.setText("Aktuelle Aktivität: "
					+ currentActivity);

			itemsIn = statusUpdate.getItemList();
			itemsMust = statusUpdate.getActivity().getItemsForActivity();
			if (statusUpdate.getNeedlessItems() == null) {
				// Toast.makeText(getActivity(), "Keine needless Items",
				// Toast.LENGTH_SHORT);
			} else {
				needlessItems = statusUpdate.getNeedlessItems();
			}
			missingItems = statusUpdate.getMissingItems();

			if (itemsMust == null) {
				copiedMustItems = new ArrayList<Item>();
			} else {
				copiedMustItems = new ArrayList<Item>(itemsMust);
			}
			
			
			
			//SUGGESTIONS
			contextSuggestions = statusUpdate.getContextSuggestions();
			
			//TODO
			itemsSuggFragment.updateView(statusUpdate);
			
			
			itemsInTab.setText(String.format("Enthalten" + " (%d)",
					itemsIn.size()));
			// itemsNeedlessTab.setText(String.format("Überflüssig (%d)",needlessItems.size()));
			itemsMissTab.setText(String.format("Fehlend (%d)",
					missingItems.size()));

			itemsInFragment.updateView(statusUpdate);
			itemsMissFragment.updateView(statusUpdate);
			// itemsNeedlessFragment.updateView(statusUpdate);
			// debugMessage(statusUpdate);

			StringBuilder sb = new StringBuilder();

			sb.append("Update: \n");
			sb.append("Items in Bag:");
			sb.append("\n");

			sb.append("\n");
			sb.append("Activity: ");
			sb.append(statusUpdate.getActivity().getName());
			sb.append("\n");
			sb.append("Items for activity:");
			for(Item i:statusUpdate.getActivity().getItemsForActivity()){
				sb.append(i.getName() + ", ");
			}
			sb.append("\n");

			sb.append("\n");
			 Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG)
			 .show();
			break;

		case ITEM_NOT_FOUND:
			Item unknownItem = Item.fromJSON(BundleMessage.getInstance()
					.extractObject(b));
			// Toast.makeText(getActivity(), "Found Item ID: " +
			// unknownItem.getIds().get(0), Toast.LENGTH_LONG).show();

			dialog(unknownItem);
			break;

		case ACTIVITY_PRIORITY_LIST: {
			onActivityPriorityList(b);
		}

		case ADMINISTRATION_COMMAND: {
			onAdminCommand(b);
		}
		default:
			break;
		}
	}

	public void dialog(final Item item) {
		AlertDialog.Builder dialogAlert = new AlertDialog.Builder(getActivity());
		dialogAlert.setTitle("Unbekannter Tag gefunden");
		dialogAlert.setNegativeButton("Neues Item", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(getActivity(), MainGUI.class);
				intent.putExtra("FRAGMENT",
						"de.uniulm.bagception.client.ui.launcher.CreateNewItemFragment");
				intent.putExtra("ID", -1);
				intent.putExtra("TAGID", item.getIds().get(0));

				startActivity(intent);
			}
		});

		dialogAlert.setPositiveButton("Zu Item hinzu", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				acceptList = true;
				unknownItem = item;
				new BundleMessageHelper(getActivity())
						.sendMessageSendBundle(BundleMessage.getInstance()
								.createBundle(
										BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
										ItemCommand.list()));

			}
		});

		dialogAlert.create().show();
	}

	// private void debugMessage(ContainerStateUpdate update) {
	// StringBuilder sb = new StringBuilder();
	//
	// sb.append("Update: \n");
	// sb.append("Items in Bag:");
	// sb.append("\n");
	//
	// sb.append("\n");
	// sb.append("Activity: ");
	// sb.append(statusUpdate.getActivity().getName());
	// sb.append("\n");
	// sb.append("Items for activity:");
	// sb.append("\n");
	//
	// sb.append("\n");
	// Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG).show();
	// }

	public synchronized ContainerStateUpdate getItemUpdate() {
		return statusUpdate;
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
		switch (status) {
		case CONNECTED:
			bmHelper.sendMessageSendBundle(BundleMessage.getInstance()
					.createBundle(
							BUNDLE_MESSAGE.CONTAINER_STATUS_UPDATE_REQUEST, ""));
			// Toast.makeText(getActivity(), "update status request",
			// Toast.LENGTH_SHORT).show();
			break;
		case DISCONNECTED:
			break;
		case ERROR:
			break;
		case SCAN_DEVICES_DONE:
			break;
		default:
			break;
		}
	}

	@Override
	public void onCommandMessage(Bundle b) {

	}

	@Override
	public void onError(Exception e) {

	}

	@Override
	public void onStart() {
		super.onStart();
		bmActor = new BundleMessageActor(this);
		bmActor.register(getActivity());
		bmHelper = new BundleMessageHelper(getActivity());
		bmHelper.sendCommandBundle(Command.RESEND_STATUS.toBundle());

	}

	@Override
	public void onStop() {
		super.onStop();
		bmActor.unregister(getActivity());

	}

	private void onActivityPriorityList(Bundle b) {

	}

	// add tagid to item
	private void onAdminCommand(Bundle b) {

		AdministrationCommandProcessor p = new AdministrationCommandProcessor() {
			@Override
			public void onItemList(AdministrationCommand<Item> i) {
				if (!acceptList) {
					return;
				}
				acceptList = false;
				final Item[] items = i.getPayloadObjects();
				String[] itemStrings = new String[items.length];

				for (int iter = 0; iter < itemStrings.length; iter++) {
					itemStrings[iter] = items[iter].getName();
				}

				AlertDialog.Builder itemAlert = new AlertDialog.Builder(
						getActivity());
				itemAlert.setTitle("Tag zu Item hinzufügen");
				itemAlert.setSingleChoiceItems(itemStrings, -1,
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Item selected = items[which];
								if (unknownItem != null) {
									selected.getIds().add(
											unknownItem.getIds().get(0));
									bmHelper.sendMessageSendBundle(BundleMessage
											.getInstance()
											.createBundle(
													BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
													ItemCommand.edit(selected,
															selected)));
								} else {
									Toast.makeText(getActivity(),
											"ERROR: selected item is null",
											Toast.LENGTH_SHORT).show();
								}
								dialog.dismiss();
							}
						});
				// itemAlert.setPositiveButton("ok",new OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// // TODO Auto-generated method stub
				//
				// }
				// });
				itemAlert.setNegativeButton("abbrechen", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				itemAlert.create().show();
			}
		};

		JSONObject o = BundleMessage.getInstance().extractObject(b);
		AdministrationCommand<?> adminCmd = AdministrationCommand
				.fromJSONObject(o);
		if (adminCmd != null) {
			adminCmd.accept(p);
		} else {
			Log.d("FIXME", "admin command is null (int Overview fragment)"); // XXX
		}

	}

}

//

