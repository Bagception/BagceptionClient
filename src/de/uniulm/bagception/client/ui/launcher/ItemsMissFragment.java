package de.uniulm.bagception.client.ui.launcher;

import java.util.List;

import de.uniulm.bagception.bundlemessageprotocol.entities.RichItem;

public class ItemsMissFragment extends OverviewTabFragment{

	@Override
	protected List<RichItem> getCorrespondingItemList(ContextItems contextItems) {
		return contextItems.getItemsMiss();
	}
	
}