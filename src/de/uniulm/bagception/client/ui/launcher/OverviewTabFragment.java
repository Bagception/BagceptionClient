package de.uniulm.bagception.client.ui.launcher;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;

public abstract class OverviewTabFragment extends Fragment {
	protected ListView itemsStatusView;
	protected OverviewFragment fragment;

	public OverviewTabFragment() {
		Log.d("COLOR","overviewFragment created");
	}
	


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
