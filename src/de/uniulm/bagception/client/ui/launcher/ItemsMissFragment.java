package de.uniulm.bagception.client.ui.launcher;

import java.util.List;

import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;

public class ItemsMissFragment extends OverviewTabFragment{

	@Override
	protected List<Item> getCorrespondingItemList(ContainerStateUpdate update) {
		return update.getMissingItems();
	}


}
