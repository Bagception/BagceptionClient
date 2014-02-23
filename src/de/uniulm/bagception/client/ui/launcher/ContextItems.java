package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.RichItem;

public class ContextItems {

	private final Set<RichItem> itemsIn = new HashSet<RichItem>();
	private final Set<RichItem> itemsMiss = new HashSet<RichItem>();
	//private final Set<RichItem> itemsReplace = new HashSet<RichItem>();
	private final List<ContextSuggestion> itemsReplace = new ArrayList<ContextSuggestion>();
	
	public ContextItems() {
		
	}
	
	private void clearAll(){
		itemsIn.clear();
		itemsMiss.clear();
		itemsReplace.clear();
	}
	
	public synchronized void update(ContainerStateUpdate stateUpdate){
		clearAll();
		
		List<ContextSuggestion> suggestionToRemove = new ArrayList<ContextSuggestion>();
		List<ContextSuggestion> suggestionToAdd = new ArrayList<ContextSuggestion>();
		
		//itemsIn: 
		//needless, context
		
		
		
		
		if (stateUpdate.getContextSuggestions() != null) {
			for (ContextSuggestion sug : stateUpdate.getContextSuggestions()) {
				if (sug.getItemToReplace()==null){
					//no item to replace/remove => nothing to remove, only  to add
					suggestionToAdd.add(sug);
					
					for(Item i:sug.getReplaceSuggestions()){
						Log.d("CONTEXT","toAdd: "+i.getName());	
					}
				}else{
					//there is an item to replace/remove (I)
					if (sug.getReplaceSuggestions()!=null && sug.getReplaceSuggestions().size()>0){
						//there are suggestions + I => replace 
						itemsReplace.add(sug);
						Log.d("CONTEXT","toReplace: "+sug.getItemToReplace().getName());
						for(Item i:sug.getReplaceSuggestions()){
							Log.d("CONTEXT","toReplaceWith: "+i.getName());
						}
					}else{
						//I + no suggestions => remove item
						suggestionToRemove.add(sug);
						Log.d("CONTEXT","toRemove: "+sug.getName());
					}
				}
			}
		}else{
				Log.d("CONTEXT","suggestions are null");	
		}
		
		//calculate items in: needless must be marked, needless are only needless when they are not context items
		for(Item item:stateUpdate.getItemList()){
//			ContextSuggestion sug = ContextSuggestion.getReplaceSuggestions(stateUpdate.getContextSuggestions(), item);
			ContextSuggestion sug = ContextSuggestion.getItemsToReplace(stateUpdate.getContextSuggestions(), item);	//item can be replaced or is needless 
			boolean needless = (stateUpdate.getNeedlessItems().contains(item) || sug != null);
			

			
			Log.d("CTX"," in name: "+item.getName());
			Log.d("CTX"," sug: "+sug);
			if (sug != null){
				Log.d("CTX"," reason: "+sug.getReason());
				Log.d("CTX"," replace: "+sug.getReplaceSuggestions());	
			}
			if (sug !=null){
				Log.d("CTX",sug.toString());	
			}else{
				Log.d("CTX","sug is null");
			}
			
			itemsIn.add(new RichItem(item,sug,needless));
			
			
			
		}
		Log.d("CTX","items miss:");
		for(Item item:stateUpdate.getMissingItems()){
			ContextSuggestion sug = ContextSuggestion.getItemsToReplace(stateUpdate.getContextSuggestions(), item);
			
			Log.d("CTX"," miss: name: "+item.getName());
			if (sug!=null){
				Log.d("CTX"," reason: "+sug.getReason());
				Log.d("CTX"," replace: "+sug.getReplaceSuggestions());
					
			}
			ContextSuggestion sugReplace = ContextSuggestion.getReplaceSuggestions(itemsReplace, item);
			if (sugReplace != null){
				for(Item sugitem:sugReplace.getReplaceSuggestions()){
					itemsMiss.add(new RichItem(sugitem,sug,false));
				}	
			}
			ContextSuggestion sugToReplace = ContextSuggestion.getItemsToReplace(itemsReplace, item);
			Log.d("CTX","for item: "+item.getName()+" replace suggestion is " + sugToReplace);
			if (sugToReplace == null){
				itemsMiss.add(new RichItem(item,sug,false));
			}
//			Log.d("CTX2",item.getName()+": "+sugReplace.getItemToReplace().getName());
//			if ( !(sugReplace != null && sugReplace.getItemToReplace() != null)){
//				itemsMiss.add(new RichItem(item,sug,false));
//			}
			
		}
		
		debug(stateUpdate);
	}
	
	

	
	public synchronized List<RichItem> getItemsIn(){
		return new ArrayList<RichItem>(itemsIn);
	}
	
	public synchronized List<RichItem> getItemsMiss(){
		return new ArrayList<RichItem>(itemsMiss);
	}
	
	public synchronized List<ContextSuggestion> getItemsReplace(){
		return itemsReplace;
	}
	
	private void debug(ContainerStateUpdate stateUpdate){
		Log.d("DEBUGCONTEXT","Debug context:");
		if (stateUpdate.getContextSuggestions() == null){
			Log.d("DEBUGCONTEXT","context is null");
		}else{
			for(ContextSuggestion sss:stateUpdate.getContextSuggestions()){
				if (sss.getItemToReplace()!=null){
					Log.d("DEBUGCONTEXT","suggestion replace: " +sss.getItemToReplace().getName());
				}else{
					Log.d("DEBUGCONTEXT","suggestion replace: no");	
				}
				Log.d("DEBUGCONTEXT"," reason: "+sss.getReason().name());	
				for (Item i:sss.getReplaceSuggestions()){
					if (i == null){
						Log.d(" DEBUGCONTEXT","replace with nothing:");
					}else{
						Log.d(" DEBUGCONTEXT","replace with:"+i.getName());	
					}
						
				}
			}
			
		}		
	}
	
	
	
}



