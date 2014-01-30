package de.uniulm.bagception.client.ui.launcher;

import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ItemCommand;
import de.uniulm.bagception.client.R;
import android.R.anim;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AllItemsFragment extends Fragment{
	
	ListView listView;
	ArrayAdapter<Item> ad;
	String bla;
	
	public static Fragment newInstance(Context context) {
		AllItemsFragment f = new AllItemsFragment();

		return f;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_all_items, null);
		listView = (ListView) root.findViewById(R.id.listViewAllItems);
		return root;
	}

}
