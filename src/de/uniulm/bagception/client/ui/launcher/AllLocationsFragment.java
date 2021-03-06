package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.LocationCommand;

public class AllLocationsFragment extends BasicListEntitiesFragment<Location> {

	public static Fragment newInstance(Context context) {
		AllLocationsFragment f = new AllLocationsFragment();

		return f;
	}

	@Override
	protected ArrayAdapter<Location> getEntityAdapter() {
		return new ArrayAdapter<Location>(getActivity(),
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
				Location loc = getItem(position);

				if (loc != null) {
					TextView itemView = (TextView) view
							.findViewById(android.R.id.text1);

					if (itemView != null) {
						itemView.setText(loc.getName());
					}
				}
				return view;
			}
		};
	}

	@Override
	protected AdministrationCommand<Location> getAdminCommandRequest() {
		return LocationCommand.list();
	}

	@Override
	public void onAdminCommand(AdministrationCommand<?> a_cmd) {
		AdministrationCommandProcessor adminCommandProcessor = new AdministrationCommandProcessor() {
			@Override
			public void onLocationList(AdministrationCommand<Location> l) {
				// item list
				Location[] theLocationsWeWantToDisplay = l.getPayloadObjects();
				for (int loc = 0; loc < theLocationsWeWantToDisplay.length; loc++) {
					Log.w("TEST", "Location: "
							+ theLocationsWeWantToDisplay[loc]);
				}
				listAdapter.clear();
				listAdapter.addAll(theLocationsWeWantToDisplay);
			}
		};
		a_cmd.accept(adminCommandProcessor);
	}

	@Override
	protected AdministrationCommand<Location> getToDeleteEntity(int pos) {
		return LocationCommand.remove(listAdapter.getItem(pos));
	}

	@Override
	protected long getId(Location e) {
		return e.getId();
	}

	@Override
	protected String getEditFragmentName() {
		return "de.uniulm.bagception.client.ui.launcher.EditLocationFragment";
	}

	@Override
	protected String getCreateNewFragmentName() {
		return "de.uniulm.bagception.client.ui.launcher.CreateNewPlaceFragment";
	}

}
