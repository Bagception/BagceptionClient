package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.util.Log;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion.CONTEXT;

public class ItemsInFragment extends OverviewTabFragment{

	private List<Item>  needless;

	@Override
	protected List<Item> getCorrespondingItemList(ContainerStateUpdate update) {
		needless = update.getNeedlessItems();
		List<Item> ret = new ArrayList<Item>(update.getItemList());
		

		//aus needless items context items entfernen
		for(ContextSuggestion sug:fragment.suggestionToRemove){
			if (!ret.contains(sug.getItemToReplace())){
				needless.remove(sug.getItemToReplace());
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
				adapter.putColorCodeItems(Color.CYAN, item);
			}
			Log.d("CONTEXT", item.getName()+":");
			final ContextSuggestion sug = ContextSuggestion.getReplaceSuggestions(fragment.suggestionToAdd, item);
			if (sug!=null){
				//
				List<CONTEXT> ctx =new ArrayList<CONTEXT>();
				ctx.add(sug.getReason());
				adapter.putContextItem(ctx, item);
				Log.d("CONTEXT",  " "+ctx.get(0).name());
			}
			
//			for(ContextSuggestion sug:fragment.suggestionToAdd){
//				ArrayList<CONTEXT> ctx = new ArrayList<ContextSuggestion.CONTEXT>();
//				ctx.add(sug.getReason());
//				for(Item replaceItem:sug.getReplaceSuggestions()){
//					if (replaceItem.equals(item)){
//						adapter.putContextItem(ctx, item);
//					}
//				}
//				
//			}
		}
		
		for(ContextSuggestion sug:fragment.suggestionToRemove){
			adapter.putColorCodeItems(Color.GRAY,sug.getItemToReplace());
			ArrayList<CONTEXT> ctx = new ArrayList<ContextSuggestion.CONTEXT>();
			ctx.add(sug.getReason());
			Log.d("CONTEXT","Context icon (in) for "+sug.getItemToReplace().getName()+": "+sug.getReason().name());
			adapter.putContextItem(ctx, sug.getItemToReplace());
		}
	}

}
