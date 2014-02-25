package de.uniulm.bagception.client.ui.launcher;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import de.uniulm.bagception.bundlemessageprotocol.entities.ContextSuggestion.CONTEXT;
import de.uniulm.bagception.client.R;

public class Bitmaps {
	
	private static final Bitmaps instances = new Bitmaps();
	private volatile boolean isInit = false;
	
	private static final HashMap<CONTEXT,Bitmap> contextImageMap = new HashMap<CONTEXT, Bitmap>();

	private synchronized void init(Context context){
		contextImageMap.put(CONTEXT.BRIGHT, BitmapFactory.decodeResource(context.getResources(), R.drawable.daywarm));
		contextImageMap.put(CONTEXT.COLD, BitmapFactory.decodeResource(context.getResources(), R.drawable.cold));
		contextImageMap.put(CONTEXT.DARK, BitmapFactory.decodeResource(context.getResources(), R.drawable.night));
		contextImageMap.put(CONTEXT.RAIN, BitmapFactory.decodeResource(context.getResources(), R.drawable.rain));
		contextImageMap.put(CONTEXT.SUNNY, BitmapFactory.decodeResource(context.getResources(), R.drawable.dry));
		contextImageMap.put(CONTEXT.WARM, BitmapFactory.decodeResource(context.getResources(), R.drawable.hot));
		isInit = true;
	}
	
	private Bitmaps(){}

	
	
	public static synchronized Bitmaps getInstance(Context context){
		if (!instances.isInit){
			instances.init(context);
		}
		return instances;
	}
	
	public Bitmap getContextIcon(CONTEXT c){
		return contextImageMap.get(c);
	}
}
