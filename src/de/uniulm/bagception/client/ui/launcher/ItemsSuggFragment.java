package de.uniulm.bagception.client.ui.launcher;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import de.uniulm.bagception.bundlemessageprotocol.entities.RichItem;
import de.uniulm.bagception.client.R;

public class ItemsSuggFragment extends OverviewTabFragment implements OnChildClickListener{

	
	@Override
	protected List<RichItem> getCorrespondingItemList(ContextItems contextItems) {
		// TODO Auto-generated method stub
		return null;
	}

	private SuggestionListAdapter suggAdapter;
	private ExpandableListView expandbleLis;

	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_items_sugg, null);
		
		expandbleLis = (ExpandableListView) root.findViewById(R.id.itemSugg);
		expandbleLis.setDividerHeight(2);
		expandbleLis.setGroupIndicator(null);
		expandbleLis.setClickable(true);
		
		if(suggAdapter == null){
			
			Log.w("TEST", "ContextSuggestion (Client/ItemSuggFragment): " + fragment.contextItems.getItemsReplace());
			suggAdapter = new SuggestionListAdapter(getActivity(),fragment.contextItems.getItemsReplace());
			suggAdapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		}
	
		if(expandbleLis.getExpandableListAdapter() == null){
			Log.w("TEST", "suggAdapter (Client/ItemSuggFragment): " + fragment.contextItems.getItemsReplace());
			expandbleLis.setAdapter(suggAdapter);
		}	
		
		return root;

	}
	


	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
			int childPosition, long id) {
		
		Toast.makeText(getActivity().getApplicationContext(), "Clicked On Child", Toast.LENGTH_SHORT).show();
		return true;
	}
}
