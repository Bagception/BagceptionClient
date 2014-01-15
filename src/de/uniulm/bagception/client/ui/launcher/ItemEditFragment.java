package de.uniulm.bagception.client.ui.launcher;

import de.uniulm.bagception.client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ItemEditFragment extends Fragment {

	// This is use as an ID for ItemEditFragment
	public static final String DEFAULT_EDIT_FRAGMENT_TAG = "editFragmentTag";
	
	// Constants
//	private TextView itemName;
//	private TextView itemTagId;
//	private TextView textTagId;
//	private TextView itemVisibility;
//	private TextView itemPhoto;
	
	private EditText editName;
	private TextView textTagId;
	private RadioButton itemVisibility_public;
	private RadioButton itemVisibility_private;
	private ImageView editPhoto;
	private Button cancelButton;
	private Button saveButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// TODO 
		/** Get arguments from database
		 *  Bundle arguments = getArguments();
		 *  if (arguments != null) {
		 *  	editName = arguments.getString(BagceptionProvider.ITEM_NAME);
		 *  }
		 */
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.item_edit, container, false);
		
		editName = (EditText) rootView.findViewById(R.id.editName);
//		editName.setText(Item.Name);
		
		textTagId = (TextView) rootView.findViewById(R.id.textTagId);
//		textTagId.setText(Item.TagId);
		
		itemVisibility_private = (RadioButton) rootView.findViewById(R.id.itemVisibility_private);
		itemVisibility_public = (RadioButton) rootView.findViewById(R.id.itemVisibility_public);
//		if(Item.itemVisibility_public != 1) {
//			itemVisibility_private.setChecked(true);
//			itemVisibility_public.setChecked(false);
//		} else {
//			itemVisibility_private.setChecked(false);
//			itemVisibility_public.setChecked(true);
//		}
		
		// TODO 
		// get Photo and insert it into: 
		// (ImageView) rootView.findViewById(R.id.editPhoto);
		
		
		
		return rootView;
	}
}
