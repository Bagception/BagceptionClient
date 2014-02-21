package de.uniulm.bagception.client.ui.launcher;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;

public class SuggestionListAdapter extends BaseExpandableListAdapter {

	private LayoutInflater inflater;
	private final Context context;
	
	private final List<ContextSuggestion> data;
	
	public SuggestionListAdapter(Context context,List<ContextSuggestion> contextSuggestions) {
		this.data = contextSuggestions;
		this.context = context;
		
	}

	public void setInflater(LayoutInflater mInflater) {
		this.inflater = mInflater;
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
		ContextSuggestion sug = data.get(groupPosition);
		Log.w("TEST", "ContextSuggestion (Client/SuggestionListAdapter): " + sug);
		
		List<Item> replaceSuggestions = sug.getReplaceSuggestions();
		//TODO render replaceSuggestions
		
//		TextView text = null;
//		if (convertView == null) {
//			convertView = minflater.inflate(R.layout.child_row, null);
//		}
//		
//		text = (TextView) convertView.findViewById(R.id.suggestionItem);
//		text.setText(tempChild.get(childPosition));
//		
//		convertView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//				Toast.makeText(activity, tempChild.get(childPosition), Toast.LENGTH_SHORT).show();
//			}
//		});
		
	  return convertView;
	 }


	@Override
	public int getChildrenCount(int groupPosition) {
	
		return data.get(groupPosition).getReplaceSuggestions().size();
	}

	@Override
	public Object getGroup(int arg0) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return data.size();
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
			convertView = inflater.inflate(R.layout.group_row, null);
		}
		
		Log.w("TEST", "List<ContextSuggestion> data (Client/SuggestionListAdapter): " + data);
		
		ContextSuggestion sug = data.get(groupPosition);
		Item toRender = sug.getItemToReplace();
		Bitmap contextIcon = Bitmaps.getInstance(context).getContextIcon(sug.getReason());
		
		
		((CheckedTextView) convertView).setText(data.get(groupPosition).getName());
		((CheckedTextView) convertView).setChecked(isExpanded);
		
		
		/**
		 * Set the images of the parent entries (the items that shall be replaced)
		 */
		AutoUpdatableCheckedTextView cv = (AutoUpdatableCheckedTextView) convertView.findViewById(R.id.replaceItem);
//		Drawable icon = (Drawable) convertView.getResources().getDrawable(R.drawable.ic_launcher);
//		Drawable icon2 = (Drawable) convertView.getResources().getDrawable(R.drawable.service_icon);
		cv.setItem(toRender);
		//cv.setCompoundDrawablesWithIntrinsicBounds(icon2, null, icon, null);
		cv.setCompoundDrawablesWithIntrinsicBounds(contextIcon);
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
