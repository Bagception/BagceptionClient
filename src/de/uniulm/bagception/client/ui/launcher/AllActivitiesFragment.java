package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.uniulm.bagception.bundlemessageprotocol.entities.Activity;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ActivityCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ItemCommand;
import de.uniulm.bagception.client.R;

public class AllActivitiesFragment extends BasicListEntitiesFragment<Activity>{

	public long activity_id = -1;
	

	
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
				Log.w("TEST", "Activity: " + activity);

				if (activity != null) {
					TextView itemView = (TextView) view.findViewById(android.R.id.text1);

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
	protected String getFragmentName() {
		// TODO Auto-generated method stub
		return "de.uniulm.bagception.client.ui.launcher.CreateNewActivityFragment";
	}


	@Override
	protected long itemSelected(Activity e) {
		// TODO Auto-generated method stub
		return e.getId();
	}

}
