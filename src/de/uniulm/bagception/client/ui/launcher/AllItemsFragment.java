package de.uniulm.bagception.client.ui.launcher;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ItemCommand;

public class AllItemsFragment extends BasicListEntitiesFragment<Item> {
	

	public static Fragment newInstance(Context context) {
		AllItemsFragment f = new AllItemsFragment();
		return f;
	}
	
	@Override
	protected ArrayAdapter<Item> getEntityAdapter() {	
		return new ItemListArrayAdapter(getActivity());
	}
	@Override
	public void onAdminCommand(AdministrationCommand<?> a_cmd) {
		AdministrationCommandProcessor adminCommandProcessor = new AdministrationCommandProcessor(){
			@Override
			public void onItemList(AdministrationCommand<Item> i) {
				//item list
				Item[] theItemsWeWantToDisplay = i.getPayloadObjects();
				
				for(int j = 0; j < theItemsWeWantToDisplay.length; j++){
					Log.w("TEST", "Item " + j + ": " + theItemsWeWantToDisplay[j]);
				}
					
				listAdapter.clear();
				listAdapter.addAll(theItemsWeWantToDisplay);
			}
		};
		a_cmd.accept(adminCommandProcessor);
		
	}

	@Override
	protected AdministrationCommand<Item> getAdminCommandRequest() {
		return ItemCommand.list();
	}

	@Override
	protected AdministrationCommand<Item> getToDeleteEntity(int pos) {
		
		return ItemCommand.remove(listAdapter.getItem(pos));
	}

	@Override
	protected String getFragmentName() {
		
//		return "de.uniulm.bagception.client.ui.launcher.CreateNewItemFragment";
		return "de.uniulm.bagception.client.ui.launcher.EditItemFragment";
	}

	@Override
	protected long getId(Item e) {
		
		return e.getId();
	}

	



}
