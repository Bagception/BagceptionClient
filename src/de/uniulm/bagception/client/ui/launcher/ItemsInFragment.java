package de.uniulm.bagception.client.ui.launcher;

import java.util.List;

import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;

public class ItemsInFragment extends OverviewTabFragment{

	private List<Item>  needless;
	
	
	@Override
	protected List<Item> getCorrespondingItemList(ContainerStateUpdate update) {
		needless = update.getNeedlessItems();
		
		return update.getItemList();
	}
	
	@Override
	protected synchronized void onUpdateView(ItemListArrayAdapter adapter) {
		adapter.clearSpecialItems();
		for(int i=0;i<adapter.getCount();i++){
			Item item = adapter.getItem(i);
			
			if (needless.contains(item)){
				
				adapter.setSpecialItem(i);
			}
		}
		
		
	}

}
