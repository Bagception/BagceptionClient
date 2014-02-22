package de.uniulm.bagception.client.ui.launcher;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckedTextView;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.caching.ImageCachingSystem;

public class AutoUpdatableCheckedTextView extends CheckedTextView implements Observer{
	
	private Item item;
	private Bitmap contextIcon;
	private View parentView;
	public AutoUpdatableCheckedTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public AutoUpdatableCheckedTextView(Context arg0) {
		super(arg0);
	}
	
	public AutoUpdatableCheckedTextView(Context arg0,AttributeSet arg1) {
		super(arg0, arg1);
	}

	public void setParentView(View parent){
		this.parentView = parent;
	}
	
	public void setCompoundDrawablesWithIntrinsicBounds(Bitmap contextIcon) {
		this.contextIcon = contextIcon;
		Drawable contextDrawable = resize(new BitmapDrawable(getContext().getResources(),contextIcon),50);
		Drawable itemDrawable = null;
		
		if (item.getImage() == null){
			Log.d("IMG","image is null before cache");
			Bitmap bmp = ImageCachingSystem.getInstance().getImage(item);
			if (bmp == null){
				Log.d("IMG","not in cache");
				BitmapFactory.decodeResource(getResources(), R.drawable.service_icon);
			}else{
				Log.d("IMG","in cache");
				itemDrawable = resize(new BitmapDrawable(getContext().getResources(),bmp),100);
			}
		}else{
			Log.d("IMG","image is not null");
			itemDrawable = resize(new BitmapDrawable(getContext().getResources(),item.getImage()),100) ;
		}
		
		super.setCompoundDrawablesWithIntrinsicBounds(itemDrawable, null, contextDrawable, null);
		if (parentView!=null){
			Log.d("IMG","invalidate parent");
			
			parentView.invalidate();
		}
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
		Log.d("IMG","update image " + item.getName());
		invalidate();
		
	}

	private Drawable resize(Drawable image,int size) {
	    Bitmap b = ((BitmapDrawable)image).getBitmap();
	    if (b==null){
	    	Log.d("IMG","nothing to resize");
	    	return null;
	    }
	    Bitmap bitmapResized = Bitmap.createScaledBitmap(b, size, size, false);
	    return new BitmapDrawable(getResources(), bitmapResized);
	}

	

}
