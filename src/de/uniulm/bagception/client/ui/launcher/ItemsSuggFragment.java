package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContainerStateUpdate;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;

public class ItemsSuggFragment extends OverviewTabFragment implements BundleMessageReactor, OnChildClickListener{

	private OverviewFragment fragment;
	private ListView itemsRedundantView;
	private ItemListArrayAdapter arrayAdapter;
	private BundleMessageActor bmActor;

	public void setParentFragment(OverviewFragment fragment) {
		this.fragment = fragment;
	}
	
	@Override
	public synchronized void updateView(ContainerStateUpdate update) {
		// TODO Auto-generated method stub
		super.updateView(update);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_items_sugg, null);
		
		ExpandableListView expandbleLis = (ExpandableListView) root.findViewById(R.id.itemSugg);
		expandbleLis.setDividerHeight(2);
		expandbleLis.setGroupIndicator(null);
		expandbleLis.setClickable(true);
		
		setGroupData();
		setChildGroupData();	
		
		SuggestionListAdapter suggAdapter = new SuggestionListAdapter(groupItem, childItem);
		suggAdapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
		expandbleLis.setAdapter(suggAdapter);
		
		return root;

	}
	
	ArrayList<String> groupItem = new ArrayList<String>();
	ArrayList<Object> childItem = new ArrayList<Object>();
	
	
	public void setGroupData(){
		groupItem.add("Kurze Hose");
		groupItem.add("T-Shirt");
	}
	
	public void setChildGroupData(){
		
		// Data for "Kurze Hose"
		ArrayList<String> child = new ArrayList<String>();
		child.add("Lange Jogginghose");
		child.add("Lange Jeans");
		childItem.add(child);
		
		
		// Data for "T-Shirt"
		child = new ArrayList<String>();
		child.add("Strickpulli");
		child.add("Pullover");
		childItem.add(child);
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		bmActor = new BundleMessageActor(this);
		bmActor.register(getActivity());
	}

	@Override
	public void onDetach() {
		bmActor.unregister(getActivity());
		super.onDetach();
	}

	@Override
	public void onBundleMessageRecv(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBundleMessageSend(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponseMessage(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponseAnswerMessage(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusMessage(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCommandMessage(Bundle b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List<Item> getCorrespondingItemList(ContainerStateUpdate update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
			int childPosition, long id) {
		
		Toast.makeText(getActivity().getApplicationContext(), "Clicked On Child", Toast.LENGTH_SHORT).show();
		return true;
	}


	
	
}
