package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ActivityCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;

public class AllActivitiesFragment extends
		BasicActivityListEntitiesFragment<Activity> {

	public long activity_id = -1;

	public long getActivityId() {
		return this.activity_id;
	}

	public static Fragment newInstance(Context context) {
		AllActivitiesFragment f = new AllActivitiesFragment();

		return f;
	}
	
	

	@Override
	protected ArrayAdapter<Activity> getEntityAdapter() {
		return new ArrayAdapter<Activity>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = convertView;
				if (view == null) {
					LayoutInflater inflater = (LayoutInflater) getContext()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate(
							android.R.layout.simple_list_item_1, null);
				}
				Activity activity = getItem(position);

				if (activity != null) {
					TextView itemView = (TextView) view
							.findViewById(android.R.id.text1);

					if (itemView != null) {
						itemView.setText(activity.getName());

					}

					activity_id = activity.getId();
				}

				return view;
			}
		};
	}

	@Override
	protected AdministrationCommand<Activity> getAdminCommandRequest() {
		return ActivityCommand.list();
	}

	@Override
	public void onAdminCommand(AdministrationCommand<?> a_cmd) {
		AdministrationCommandProcessor adminCommandProcessor = new AdministrationCommandProcessor() {
			@Override
			public void onActivityList(AdministrationCommand<Activity> a) {
				// item list
				Activity[] theActivitiesWeWantToDisplay = a.getPayloadObjects();

				listAdapter.clear();
				listAdapter.addAll(theActivitiesWeWantToDisplay);
			}
		};
		a_cmd.accept(adminCommandProcessor);
	}

	@Override
	protected AdministrationCommand<Activity> getToDeleteEntity(int pos) {
		return ActivityCommand.remove(listAdapter.getItem(pos));
	}

	@Override
	protected long itemSelected(Activity e) {
		return e.getId();
	}

	@Override
	protected void onItemClicked(Activity elem) {
		// new
		// BundleMessageHelper(getActivity()).sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,ActivityCommand.start(elem)));
		// new
		// BundleMessageHelper(getActivity()).sendMessageSendBundle(BundleMessage.getInstance().createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
		// ActivityCommand.stop(elem)));
	}

	@Override
	protected String getEditFragmentName() {
		return "de.uniulm.bagception.client.ui.launcher.EditActivityFragment";
	}

	@Override
	protected String getCreateNewFragmentName() {
		return "de.uniulm.bagception.client.ui.launcher.CreateNewActivityFragment";
	}

	@Override
	protected String getFragmentName() {
		return "de.uniulm.bagception.client.ui.launcher.AllActivitiesFragment";
	}

	@Override
	protected long getId(Activity e) {
		return e.getId();
	}

}
