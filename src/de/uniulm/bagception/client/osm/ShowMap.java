package de.uniulm.bagception.client.osm;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import de.uniulm.bagception.client.R;

public class ShowMap extends Activity implements LocationListener {

	private LocationManager locManager;
	private LocationListener locListener;
	private String latitude;
	private String longitude;
	private HandleMapEvents mapView;
	private TextView meterIndicator;

	private final int INIT_RADIUS = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_map);

		mapView = (HandleMapEvents) findViewById(R.id.map);
		mapView.setClickable(true);
		mapView.setTileSource(TileSourceFactory.CYCLEMAP);
		mapView.setBuiltInZoomControls(true);
		meterIndicator = (TextView) findViewById(R.id.meter);
		final SeekBar meterSlider = (SeekBar) findViewById(R.id.meterSlider);
		meterSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				meterIndicator.setText("" + progress);
				mapView.setRadius(progress);
			}
		});

		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		meterSlider.setProgress(INIT_RADIUS);

		mapView.setMapListener(new MapListener() {

			@Override
			public boolean onZoom(ZoomEvent arg0) {
				Rect r = mapView.getProjection().getScreenRect();
				IGeoPoint nullP = mapView.getProjection().fromPixels(0, 0);
				IGeoPoint endP = mapView.getProjection().fromPixels(r.width(),
						r.height());
				float[] result = new float[3];
				Location.distanceBetween(nullP.getLatitude(),
						nullP.getLongitude(), endP.getLatitude(),
						endP.getLongitude(), result);

				if (result[0] == 0.0f)
					return false;
				meterSlider.setMax((int) (result[0] / 2));
				return false;
			}

			@Override
			public boolean onScroll(ScrollEvent arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		mapView.getController().setZoom(18);

		locListener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				location.getLatitude();
				location.getLongitude();

				latitude = String.valueOf(location.getLatitude());
				longitude = String.valueOf(location.getLongitude());
				
				double newLat = Double.parseDouble(latitude);
				double newLon = Double.parseDouble(longitude);
				GeoPoint here = new GeoPoint(newLat, newLon);
				mapView.getController().setCenter(here);
				mapView.placeMarker(here);
				locManager.removeUpdates(this);

			}
		};

		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				4000, 0, locListener);
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				4000, 0, locListener);

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Rect r = mapView.getProjection().getScreenRect();
				IGeoPoint nullP = mapView.getProjection().fromPixels(0, 0);
				IGeoPoint endP = mapView.getProjection().fromPixels(r.width(),
						r.height());
				float[] result = new float[3];
				Location.distanceBetween(nullP.getLatitude(),
						nullP.getLongitude(), endP.getLatitude(),
						endP.getLongitude(), result);

				if (result[0] != 0.0f)
					meterSlider.setMax((int) (result[0] / 2));
				
			}
		}, 1000);
				
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void onOkBtnClicked(View v) {
		// Todo finish activity with result;

		// Toast.makeText(this,
		// mapView.getGeoPoint().toString() + " " + mapView.getRadius(),
		// Toast.LENGTH_SHORT).show();

		Intent payload = new Intent();
		payload.putExtra("LAT", mapView.getGeoPoint().getLatitude());
		payload.putExtra("LNG", mapView.getGeoPoint().getLongitude());
		payload.putExtra("RAD", mapView.getRadius());

		setResult(Activity.RESULT_OK, payload);

		Log.d("TEST", "lat: " +  mapView.getGeoPoint().getLatitude());
		Log.d("TEST", "lng: " + mapView.getGeoPoint().getLongitude());
		finish();

	}

}
