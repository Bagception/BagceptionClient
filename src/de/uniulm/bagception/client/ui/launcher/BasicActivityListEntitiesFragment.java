package de.uniulm.bagception.client.ui.launcher;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.client.R;

public abstract class BasicActivityListEntitiesFragment<E> extends Fragment
		implements BundleMessageReactor {

	private ListView listView;
	protected ArrayAdapter<E> listAdapter;

	ListView listViewItems;
	ArrayAdapter<E> listAdapterItems;

	private BundleMessageActor actor;
	private BundleMessageHelper helper;

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

				String[] test = { "1", "2" };
				Activity activity = (Activity)listAdapter.getItem(arg2);
				String[] data = new String[activity.getItemsForActivity().size()];
				
				int iter=0;
				for (Item i:activity.getItemsForActivity()){
					data[iter++] = i.getName();
				}

				startActivity.setItems(data,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});

				startActivity.setNeutralButton("Bearbeiten",
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

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

		// listView.setOnItemLongClickListener(new
		// AdapterView.OnItemLongClickListener() {
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// dialog(arg2);
		// return false;
		// }
		// });

		return root;
	}

	public void dialog(final int pos) {
		AlertDialog.Builder dialogAlert = new AlertDialog.Builder(getActivity());
		dialogAlert.setTitle("Eintrag löschen?");
		dialogAlert.setNegativeButton("Abbrechen", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
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
			break;

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
