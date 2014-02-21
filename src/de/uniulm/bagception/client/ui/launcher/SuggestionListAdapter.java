package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;

import de.uniulm.bagception.client.R;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unchecked")
public class SuggestionListAdapter extends BaseExpandableListAdapter {

	
	public ArrayList<String> groupItem, tempChild;
	public ArrayList<Object> Childtem = new ArrayList<Object>();
	public LayoutInflater minflater;
	public Activity activity;
	
	
	public SuggestionListAdapter(ArrayList<String> grList, ArrayList<Object> childItem) {
		groupItem = grList;
		this.Childtem = childItem;
	}

	public void setInflater(LayoutInflater mInflater, Activity act) {
		this.minflater = mInflater;
		activity = act;
	}
	 
	@Override
	public Object getChild(int arg0, int arg1) {
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return 0;
	}

	
	/**
	 * Create the view of the child entries (the items that shall be put in the bag)
	 */
	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		tempChild = (ArrayList<String>) Childtem.get(groupPosition);
		TextView text = null;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.child_row, null);
		}
		
		text = (TextView) convertView.findViewById(R.id.suggestionItem);
		text.setText(tempChild.get(childPosition));
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Toast.makeText(activity, tempChild.get(childPosition), Toast.LENGTH_SHORT).show();
			}
		});
		
	  return convertView;
	 }


	@Override
	public int getChildrenCount(int groupPosition) {
	
		Log.w("TEST", "Childtem: " + Childtem);
		return ((ArrayList<String>) Childtem.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int arg0) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return groupItem.size();
	}
	
	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}
	
	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int arg0) {
		return 0;
	}

	/**
	 * Create the parent view (the items that shall be replaced)
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		if(convertView == null){
			convertView = minflater.inflate(R.layout.group_row, null);
		}
		
		((CheckedTextView) convertView).setText(groupItem.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);
		
		
		/**
		 * Set the images of the parent entries (the items that shall be replaced)
		 */
		CheckedTextView cv = (CheckedTextView) convertView.findViewById(R.id.replaceItem);
		Drawable icon = (Drawable) convertView.getResources().getDrawable(R.drawable.ic_launcher);
		Drawable icon2 = (Drawable) convertView.getResources().getDrawable(R.drawable.service_icon);
		
		cv.setCompoundDrawablesWithIntrinsicBounds(icon2, null, icon, null);
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return false;
	}

}
