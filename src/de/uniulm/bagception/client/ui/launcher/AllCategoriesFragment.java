package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.Location;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.client.R;

public class AllCategoriesFragment extends BasicListEntitiesFragment<Category> {

	public static Fragment newInstance(Context context) {
		AllCategoriesFragment f = new AllCategoriesFragment();

		return f;
	}

	@Override
	protected ArrayAdapter<Category> getEntityAdapter() {
		return new ArrayAdapter<Category>(getActivity(),
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
				Category category = getItem(position);
				if (category != null) {
					TextView itemView = (TextView) view
							.findViewById(android.R.id.text1);

					if (itemView != null) {
						itemView.setText(category.getName());
					}
				}
				return view;
			}
		};
	}

	@Override
	protected AdministrationCommand<Category> getAdminCommandRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAdminCommand(AdministrationCommand<?> a_cmd) {
		// TODO Auto-generated method stub

	}

}
