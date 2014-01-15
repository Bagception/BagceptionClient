package de.uniulm.bagception.client;

import de.uniulm.bagception.client.service.BagceptionClientService;
import android.content.Context;
import android.content.Intent;

public class Loader {
	public static void startService(Context c){
		Intent startServiceIntent = new Intent(c,BagceptionClientService.class);
		
		c.startService(startServiceIntent);	
	}
}
