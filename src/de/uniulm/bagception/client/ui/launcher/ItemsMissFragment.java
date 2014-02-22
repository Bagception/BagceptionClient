package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.util.Log;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion.CONTEXT;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;

public class ItemsMissFragment extends OverviewTabFragment{
	@Override
	protected List<Item> getCorrespondingItemList(ContainerStateUpdate update) {
		List<Item> ret = new ArrayList<Item>(update.getMissingItems());
		
		for(ContextSuggestion sug:fragment.suggestionToAdd){
			for(Item i:sug.getReplaceSuggestions()){
				if (!ret.contains(i)){
					ret.add(i);
				}
			}
		}
		return ret;
	
	}
		

	
	@Override
	protected synchronized void onUpdateView(ItemListArrayAdapter adapter) {
		adapter.clearColorCodeItems();
		adapter.clearContextInfo();
		for(ContextSuggestion sug:fragment.suggestionToAdd){
			for(Item i:sug.getReplaceSuggestions()){
				adapter.putColorCodeItems(Color.RED, i);
				ArrayList<CONTEXT> ctx = new ArrayList<ContextSuggestion.CONTEXT>();
				Log.d("CONTEXT","Context icon (miss) for "+i.getName()+": "+sug.getReason().name());
				ctx.add(sug.getReason());
				adapter.putContextItem(ctx, i);
			}
			
		}
	}
}