package de.uniulm.bagception.client.items;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.caching.ImageCachingSystem;

public class AutoUpdateableItemView extends ImageView implements Observer{

	private Item item;
	
	public AutoUpdateableItemView(Context c, AttributeSet set) {
		super(c,set);
	}
	
	public AutoUpdateableItemView(Context context, Item i) {
		super(context);
		setItem(i);
		
	}
	
	
	public void setItem(Item i){
		if (item != null) {
			i.deleteObserver(this);
		}
		this.item = i;
		
		i.addObserver(this);
		
		if (item.getImage() == null){
			Bitmap bmp = ImageCachingSystem.getInstance().getImage(i);
			if (bmp == null){
				//put pending image here
				BitmapFactory.decodeResource(getResources(), R.drawable.service_icon);
			}else{
				setImageBitmap(bmp);
			}
		}else{
			setImageBitmap(item.getImage());
		}
		
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
		Bitmap b = item.getImage();
		this.setImageBitmap(b);
		
		invalidate();
		
	}

	

}
