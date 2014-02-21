package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;

public abstract class OverviewTabFragment extends Fragment {
	protected ListView itemsStatusView;
	private OverviewFragment fragment;

	public OverviewTabFragment() {
		Log.d("COLOR","overviewFragment created");
	}
	
	protected List<ContextSuggestion> suggestionToReplace = new ArrayList<ContextSuggestion>();
	protected List<ContextSuggestion> suggestionToRemove = new ArrayList<ContextSuggestion>();
	protected List<ContextSuggestion> suggestionToAdd = new ArrayList<ContextSuggestion>();

	public void setParentFragment(OverviewFragment fragment) {
		this.fragment = fragment;
	}
	public synchronized void updateView(ContainerStateUpdate update) {
		//update view called when no ui is present, normally we fix this by requesting the ContainerStatusUpdate after the ui is fully loaded in onViewCreated, but this does not work
		if (itemsStatusView == null){
			return;
		}
		if (update == null) return; 
		
		ItemListArrayAdapter adapter = (ItemListArrayAdapter)itemsStatusView.getAdapter();
		calcCorrespondingContextItems();
		List<Item> list = getCorrespondingItemList(update);
		adapter.clear();
		adapter.addAll(list);
		
		onUpdateView((ItemListArrayAdapter)itemsStatusView.getAdapter());
//		itemsStatusView.invalidate();

		
	}
	protected synchronized void  onUpdateView(ItemListArrayAdapter adapter){
		
	}
	
	protected synchronized void onUpdateView(SuggestionListAdapter adapter){
		
	}
	
	private synchronized void calcCorrespondingContextItems(){
		
		Log.w("TEST", "calcCorrespondingContextItems, suggestionToReplace: " + suggestionToReplace);
		Log.w("TEST", "ContextSuggestions: " + fragment.contextSuggestions);
		suggestionToReplace.clear();
		suggestionToRemove.clear();
		suggestionToReplace.clear();
		if (fragment.contextSuggestions != null) {
			for (ContextSuggestion sug : fragment.contextSuggestions) {
				if (sug.getItemToReplace()==null){
					//no item to replace/remove => nothing to remove, only  to add
					suggestionToAdd.add(sug);
				}else{
					//there is an item to replace/remove (I)
					if (sug.getReplaceSuggestions()!=null && sug.getReplaceSuggestions().size()>0){
						//there are suggestions + I => replace 
						suggestionToReplace.add(sug);
					}else{
						//I + no suggestions => remove item
						suggestionToRemove.add(sug);
					}
				}
			}
		}
	}
	/**
	 * 
	 * @param update
	 * @return all items to display in  the list
	 */
	protected abstract List<Item> getCorrespondingItemList(ContainerStateUpdate update);
	
	@Override
	public void onResume() {
		super.onResume();
		if (fragment == null) {
			return;
		}
		ContainerStateUpdate update = fragment.getItemUpdate();
		if (update != null) {
			updateView(update);
		}
	}
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_items_in, null);
		Log.d("COLOR","onCreateView called");
		itemsStatusView = (ListView) root.findViewById(R.id.itemsIn);
		
		itemsStatusView.setAdapter(new ItemListArrayAdapter(getActivity()));
		return root;
	}
	
	 
}
