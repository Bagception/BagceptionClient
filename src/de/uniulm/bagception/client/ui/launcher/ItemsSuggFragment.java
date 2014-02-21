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
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;

public class ItemsSuggFragment extends OverviewTabFragment implements OnChildClickListener{

	private SuggestionListAdapter suggAdapter;
	private ExpandableListView expandbleLis;

	
	@Override
	public synchronized void updateView(ContainerStateUpdate update) {
		
		super.updateView(update);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_items_sugg, null);
		
		expandbleLis = (ExpandableListView) root.findViewById(R.id.itemSugg);
		expandbleLis.setDividerHeight(2);
		expandbleLis.setGroupIndicator(null);
		expandbleLis.setClickable(true);
		
		if(suggAdapter == null){
			
			Log.w("TEST", "ContextSuggestion (Client/ItemSuggFragment): " + fragment.suggestionToReplace);
			suggAdapter = new SuggestionListAdapter(getActivity(),fragment.suggestionToReplace);
			suggAdapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		}
	
		if(expandbleLis.getExpandableListAdapter() == null){
			Log.w("TEST", "suggAdapter (Client/ItemSuggFragment): " + fragment.suggestionToReplace);
			expandbleLis.setAdapter(suggAdapter);
		}	
		
		return root;

	}
	
	@Override
	protected List<Item> getCorrespondingItemList(ContainerStateUpdate update) {
		//we do not need this, because the listview is different
		return null;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
			int childPosition, long id) {
		
		Toast.makeText(getActivity().getApplicationContext(), "Clicked On Child", Toast.LENGTH_SHORT).show();
		return true;
	}
}
