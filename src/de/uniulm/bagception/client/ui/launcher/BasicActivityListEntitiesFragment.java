package de.uniulm.bagception.client.ui.launcher;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand.Operation;
import de.uniulm.bagception.client.R;

public abstract class BasicActivityListEntitiesFragment<E> extends Fragment
		implements BundleMessageReactor {

	private ListView listView;
	protected ArrayAdapter<E> listAdapter;

	ListView listViewItems;
	ArrayAdapter<E> listAdapterItems;

	private BundleMessageActor actor;
	private BundleMessageHelper helper;

	protected abstract String getEditFragmentName();

	protected abstract String getCreateNewFragmentName();

	protected abstract long getId(E e);

	/**
	 * 
	 * @return the ArrayAdapter for the corresponding entity
	 */
	protected abstract ArrayAdapter<E> getEntityAdapter();

	private void sendDeleteCommand(AdministrationCommand<E> cmd) {
		helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(
				BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, cmd));
	}

	/**
	 * Returns the AdministrationCommand to delete the entity from the list
	 * 
	 * @param pos
	 *            the position of the entity in the list
	 * @return the AdministrationCommand to delete
	 */
	protected abstract AdministrationCommand<E> getToDeleteEntity(int pos);

	protected abstract AdministrationCommand<E> getAdminCommandRequest();

	protected abstract String getFragmentName();

	protected abstract long itemSelected(E e);

	protected abstract void onItemClicked(E elem);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actor = new BundleMessageActor(this);
		helper = new BundleMessageHelper(getActivity());
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_all_items, null);
		listView = (ListView) root.findViewById(R.id.listViewAllItems);
		listAdapter = getEntityAdapter();
		listView.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				dialog(arg2);
				return true;
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				final int id = arg2;
				AlertDialog.Builder startActivity = new AlertDialog.Builder(
						getActivity());
				startActivity.setTitle("Wollen Sie die Aktivität starten?");

				for (int i = 0; i < listAdapter.getCount(); i++) {
					Log.d("TEST", listAdapter.getItem(arg2).toString());
				}

				Activity activity = (Activity) listAdapter.getItem(arg2);
				String[] data = new String[activity.getItemsForActivity()
						.size()];

				int iter = 0;
				for (Item i : activity.getItemsForActivity()) {
					data[iter++] = i.getName();
				}

				startActivity.setItems(data,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				startActivity.setNeutralButton("Bearbeiten",
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								Intent intent = new Intent(getActivity(),
										MainGUI.class);
								intent.putExtra("FRAGMENT",
										getEditFragmentName());

								long itemID = getId(listAdapter.getItem(id));
								String serializedString = listAdapter.getItem(
										id).toString();
								intent.putExtra("ID", itemID);
								intent.putExtra("ENTITYSTRING",
										serializedString);

								startActivity(intent);
							}
						});

				startActivity.setPositiveButton("Starten",
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								onItemClicked(listAdapter.getItem(id));
								Intent intent = new Intent(getActivity(),
										MainGUI.class);
								startActivity(intent);
							}
						});

				startActivity.setNegativeButton("Abbrechen",
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});

				startActivity.create().show();
			}
		});

		return root;
	}

	public void dialog(final int pos) {
		AlertDialog.Builder dialogAlert = new AlertDialog.Builder(getActivity());
		dialogAlert.setTitle("Eintrag löschen?");
		dialogAlert.setNegativeButton("Abbrechen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		});

		dialogAlert.setPositiveButton("Löschen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendDeleteCommand(getToDeleteEntity(pos));
				listAdapter.notifyDataSetChanged();
			}
		});

		dialogAlert.create().show();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.actionbar_button, menu);

		MenuItem item = menu.findItem(R.id.menu_item_add);
		Log.w("TEST", "TEST");
		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(getActivity(), MainGUI.class);
				intent.putExtra("FRAGMENT", getCreateNewFragmentName());

				startActivity(intent);
				return false;
			}
		});
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onResume() {
		super.onResume();

		actor.register(getActivity()); // necessary?
		AdministrationCommand<?> cmd = getAdminCommandRequest();
		helper.sendMessageSendBundle(BundleMessage.getInstance().createBundle(
				BUNDLE_MESSAGE.ADMINISTRATION_COMMAND, cmd));
		listAdapter.notifyDataSetChanged();

	}

	@Override
	public void onPause() {
		super.onPause();
		actor.unregister(getActivity()); // necessary?
	}

	// BundleMessageReactor

	@SuppressWarnings("unchecked")
	@Override
	public void onBundleMessageRecv(Bundle b) {
		BUNDLE_MESSAGE msg = BundleMessage.getInstance()
				.getBundleMessageType(b);
		switch (msg) {
		case ADMINISTRATION_COMMAND:
			AdministrationCommand<?> a_cmd = AdministrationCommand
					.fromJSONObject(BundleMessage.getInstance()
							.extractObject(b));
			onAdminCommand(a_cmd);
			Operation op = a_cmd.getOperation();
			switch (op) {
			case DEL: {
				if (a_cmd.isSuccessful()) {
					Object o = a_cmd.getPayloadObjects()[0];
					listAdapter.remove((E) o);
				} else {
					Toast.makeText(getActivity(),
							"Löschvorgang fehlgeschlagen", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			}
			}

		default:
			break;
		}
	}

	public abstract void onAdminCommand(AdministrationCommand<?> a_cmd);

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
