package de.uniulm.bagception.client.ui.launcher;

import java.util.Observable;
import java.util.Observer;

import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.caching.ImageCachingSystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

public class AutoUpdatableCheckedTextView extends CheckedTextView implements Observer{
	
	private Item item;
	private Bitmap contextIcon;
	public AutoUpdatableCheckedTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	public void setCompoundDrawablesWithIntrinsicBounds(Bitmap contextIcon) {
		this.contextIcon = contextIcon;
		Drawable contextDrawable = new BitmapDrawable(getContext().getResources(),contextIcon);
		Bitmap itemImage = item.getImage();
		Drawable itemDrawable = null;
		
		if (item.getImage() == null){
			Bitmap bmp = ImageCachingSystem.getInstance().getImage(item);
			if (bmp == null){
				BitmapFactory.decodeResource(getResources(), R.drawable.service_icon);
			}else{
				itemDrawable = new BitmapDrawable(getContext().getResources(),itemImage);
			}
		}else{
			itemDrawable = new BitmapDrawable(getContext().getResources(),item.getImage());
		}
		
		super.setCompoundDrawablesWithIntrinsicBounds(itemDrawable, null, contextDrawable, null);
	}
	

	
	public void setItem(Item i){
		if (item != null) {
			i.deleteObserver(this);
		}
		this.item = i;
		
		i.addObserver(this);
		
		invalidate();
	}
	
	public Item getItem(){
		return item;
	}

	
	@Override
	protected void onDetachedFromWindow() {
		if (item != null){
			item.deleteObserver(this);
		}
		super.onDetachedFromWindow();
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (item == null) return;
		if (contextIcon!=null){
			setCompoundDrawablesWithIntrinsicBounds(contextIcon);	
		}
		
		invalidate();
		
	}

	
	

}
