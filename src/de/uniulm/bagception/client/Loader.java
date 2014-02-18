package de.uniulm.bagception.client;

import android.content.Context;
import android.content.Intent;
import de.uniulm.bagception.client.service.BagceptionClientService;

public class Loader {
	public static void startService(Context c){
		Intent startServiceIntent = new Intent(c,BagceptionClientService.class);
		
		c.startService(startServiceIntent);	
	}
	
	public static void stopService(Context c){
		Intent startServiceIntent = new Intent(c,BagceptionClientService.class);
		
		c.stopService(startServiceIntent);	
	}
}
