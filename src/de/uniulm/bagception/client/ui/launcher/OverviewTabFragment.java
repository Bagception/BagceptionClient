package de.uniulm.bagception.client.ui.launcher;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.uniulm.bagception.bundlemessageprotocol.entities.RichItem;
import de.uniulm.bagception.client.R;

public abstract class OverviewTabFragment extends Fragment {
	protected ListView itemsStatusView;
	protected OverviewFragment fragment;

	

	public int getSize(){
		if (itemsStatusView == null) return 0;
		return itemsStatusView.getAdapter().getCount();
	}

	public void setParentFragment(OverviewFragment fragment) {
		this.fragment = fragment;
	}

	
	protected synchronized void onUpdateView(SuggestionListAdapter adapter){
		
	}
	
	public synchronized void update(){
		if (itemsStatusView != null){
			itemsStatusView.invalidate();
			RichItemArrayAdapter adapter = (RichItemArrayAdapter)itemsStatusView.getAdapter();
			List<RichItem> list = getCorrespondingItemList(fragment.contextItems);
			adapter.clear();
			if (list!=null){
				adapter.addAll(list);
			}
		}
			
	}

	/**
	 * 
	 * @param update
	 * @return all items to display in  the list
	 */
	protected abstract List<RichItem> getCorrespondingItemList(ContextItems contextItems);

	

	
	
	
	@Override
	public synchronized View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_items_in, null);
		itemsStatusView = (ListView) root.findViewById(R.id.itemsIn);
		itemsStatusView.setAdapter(new RichItemArrayAdapter(getActivity()));
		update();
		
		return root;
	}
	
	 
}
