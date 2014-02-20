package de.uniulm.bagception.client.ui.launcher;

import java.util.List;

import android.graphics.Color;

import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;

public class ItemsInFragment extends OverviewTabFragment{

	private List<Item>  needless;
	
	
	@Override
	protected List<Item> getCorrespondingItemList(ContainerStateUpdate update) {
		needless = update.getNeedlessItems();
		List<Item> ret = update.getItemList();
		for(ContextSuggestion sug:suggestionToRemove){
			if (!ret.contains(sug.getItemToReplace())){
				ret.add(sug.getItemToReplace());
			}
		}
		return ret;
	}
	
	@Override
	protected synchronized void onUpdateView(ItemListArrayAdapter adapter) {
		adapter.clearColorCodeItems();
		adapter.clearContextInfo();
		for(int i=0;i<adapter.getCount();i++){
			Item item = adapter.getItem(i);
			
			if (needless.contains(item)){
				adapter.putColorCodeItems(Color.BLUE, item);
			}
		}
		
		for(ContextSuggestion sug:suggestionToRemove){
			adapter.putColorCodeItems(Color.GRAY,sug.getItemToReplace());
		}
	}

}
