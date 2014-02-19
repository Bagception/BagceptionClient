package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageActor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.actor.BundleMessageReactor;
import de.uniulm.bagception.bluetoothclientmessengercommunication.service.BundleMessageHelper;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage;
import de.uniulm.bagception.bundlemessageprotocol.BundleMessage.BUNDLE_MESSAGE;
import de.uniulm.bagception.bundlemessageprotocol.entities.Category;
import de.uniulm.bagception.bundlemessageprotocol.entities.Item;
import de.uniulm.bagception.bundlemessageprotocol.entities.ItemAttribute;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommandProcessor;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.CategoryCommand;
import de.uniulm.bagception.bundlemessageprotocol.entities.administration.ItemCommand;
import de.uniulm.bagception.client.R;
import de.uniulm.bagception.client.caching.ImageCachingSystem;

public class EditItemFragment extends Fragment implements BundleMessageReactor {

	Category categoryForActivity;
	EditText editName;
	Button send;
	Button cancel;
	Button addCategory;
	TextView viewCategory;
	ToggleButton warm;
	ToggleButton cold;
	ToggleButton sunny;
	ToggleButton rainy;
	ToggleButton light;
	ToggleButton dark;
	Spinner spinner;
	ImageView iv;
	CheckBox always;
	CheckBox independet;
	String warmOn;
	String coldOn;
	String rainyOn;
	String sunnyOn;
	String lightOn;
	String darkOn;
	String temperature;
	String weather;
	String lightness;
	boolean alwaysChecked;
	boolean independetChecked;
	BundleMessageActor bmActor;

	private String tagId;
	private ArrayList<String> tagIDs = new ArrayList<String>();

	static Fragment newInstance(Context context) {
		CreateNewItemFragment f = new CreateNewItemFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fragment_create_new_item, null);
		editName = (EditText) root.findViewById(R.id.editName);
		send = (Button) root.findViewById(R.id.sendItem);
		cancel = (Button) root.findViewById(R.id.cancelItem);
		addCategory = (Button) root.findViewById(R.id.addCategory);
		iv = (ImageView) root.findViewById(R.id.itemIcon);
		warm = (ToggleButton) root.findViewById(R.id.warmButton);
		cold = (ToggleButton) root.findViewById(R.id.coldButton);
		sunny = (ToggleButton) root.findViewById(R.id.sunButton);
		rainy = (ToggleButton) root.findViewById(R.id.rainButton);
		light = (ToggleButton) root.findViewById(R.id.lightButton);
		dark = (ToggleButton) root.findViewById(R.id.darkButton);
		always = (CheckBox) root.findViewById(R.id.always);
		independet = (CheckBox) root.findViewById(R.id.independent);
		viewCategory = (TextView) root.findViewById(R.id.viewCategory);

		/**
		 * Get item
		 */
		Item item = null;
		String i = getArguments().getString("ENTITYSTRING");
		// String i = intent.getStringExtra("ITEMSTRING");
		org.json.simple.JSONObject obj = new org.json.simple.JSONObject();
		JSONParser p = new JSONParser();
		try {
			obj = (org.json.simple.JSONObject) p.parse(i);
			item = Item.fromJSON(obj);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		final Item oldItem = item;

		bmActor = new BundleMessageActor(this);
		addCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new BundleMessageHelper(getActivity())
						.sendMessageSendBundle(BundleMessage.getInstance()
								.createBundle(
										BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
										CategoryCommand.list()));
			}
		});

		/**
		 * Fill GUI with the given values
		 */

		editName.setText(item.getName());

		if (item.getCategory() != null) {
			viewCategory.setText(item.getCategory().getName());
		}

		// TODO
		// get image
		Bitmap bmp=ImageCachingSystem.getInstance().getImage(item);
		iv.setImageBitmap(bmp);
		if (item.getAttribute() != null) {
			ItemAttribute iA = item.getAttribute();

			String weather = iA.getWeather();
			String temperature = iA.getTemperature();
			String lightness = iA.getLightness();

			if (temperature.equals("cold")) {
				cold.setChecked(true);
			} else if (temperature.equals("warm")) {
				warm.setChecked(true);
			}

			if (weather.equals("sunny")) {
				sunny.setChecked(true);
			} else if (weather.equals("rainy")) {
				rainy.setChecked(true);
			}

			if (lightness.equals("light")) {
				light.setChecked(true);
			} else if (lightness.equals("cold")) {
				dark.setChecked(true);
			}

			if (item.getContextItem() == true) {
				independet.setChecked(true);
			}

			if (item.getIndependentItem() == true) {
				always.setChecked(true);
			}
		}

		if (getArguments() == null) {
			Toast.makeText(getActivity(), "Arguments null", Toast.LENGTH_SHORT);
		} else {
			int id = getArguments().getInt("ID", 0);

			if (id > 0) {
				// edit item
				Toast.makeText(getActivity(), "load item with id " + id,
						Toast.LENGTH_SHORT).show();
			} else {
				// new item
				Toast.makeText(getActivity(), "new item", Toast.LENGTH_SHORT)
						.show();
				tagId = getArguments().getString("TAGID");

			}

			Toast.makeText(getActivity(), "load item with id " + id,
					Toast.LENGTH_SHORT).show();
		}

		warm.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					warmOn = "warm";
				} else {
					warmOn = null;
				}
			}
		});

		cold.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					coldOn = "cold";
				} else {
					coldOn = null;
				}
			}
		});

		rainy.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					rainyOn = "rainy";
				} else {
					rainyOn = null;
				}
			}
		});

		sunny.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					sunnyOn = "sunny";
				} else {
					sunnyOn = null;
				}
			}
		});

		light.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					lightOn = "light";
				} else {
					lightOn = null;
				}
			}
		});

		dark.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					darkOn = "cold";
				} else {
					darkOn = null;
				}
			}
		});

		always.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					alwaysChecked = true;
				} else {
					alwaysChecked = false;
				}
			}
		});

		independet.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					independetChecked = true;
				} else {
					independetChecked = false;
				}
			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Item newItem;

				if (tagId != null) {
					tagIDs.add(tagId);
				} else {
					tagIDs = null;
				}

				if (warmOn == null && coldOn == null) {
					temperature = null;
				} else if (warmOn != null && coldOn == null) {
					temperature = warmOn;
				} else if (warmOn == null && coldOn != null) {
					temperature = coldOn;
				} else if (warmOn != null && coldOn != null) {
					temperature = "Both selected";
				}

				if (rainyOn == null && sunnyOn == null) {
					weather = null;
				} else if (rainyOn != null && sunnyOn == null) {
					weather = rainyOn;
				} else if (rainyOn == null && sunnyOn != null) {
					weather = sunnyOn;
				} else if (rainyOn != null && sunnyOn != null) {
					weather = "Both selected";
				}

				if (lightOn == null && darkOn == null) {
					lightness = null;
				} else if (lightOn != null && darkOn == null) {
					lightness = lightOn;
				} else if (lightOn == null && darkOn != null) {
					lightness = darkOn;
				} else if (lightOn != null && darkOn != null) {
					lightness = "Both selected";
				}

				ItemAttribute attributes = new ItemAttribute(temperature,
						weather, lightness);
				newItem = new Item(-1, editName.getText().toString(),
						categoryForActivity, independetChecked, alwaysChecked,
						attributes, tagIDs);

				if (((MainGUI) getActivity()).currentPicturetaken != null) {
					newItem.setImage(((MainGUI) getActivity()).currentPicturetaken);
				}

				Log.d("TEST", newItem.toString());
				BundleMessageHelper helper = new BundleMessageHelper(
						getActivity());
				helper.sendMessageSendBundle(BundleMessage.getInstance()
						.createBundle(BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
								ItemCommand.edit(oldItem, newItem)));
				Log.d("TEST", newItem.toString());
				Intent intent = new Intent(getActivity(), MainGUI.class);
				startActivity(intent);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editName.setText("");
				iv.setImageResource(R.drawable.ic_launcher);

			}
		});

		return root;
	}

	@Override
	public void onBundleMessageRecv(Bundle b) {
		switch (BundleMessage.getInstance().getBundleMessageType(b)) {
		case ADMINISTRATION_COMMAND: {
			AdministrationCommandProcessor p = new AdministrationCommandProcessor() {

				@Override
				public void onCategoryList(
						de.uniulm.bagception.bundlemessageprotocol.entities.administration.AdministrationCommand<de.uniulm.bagception.bundlemessageprotocol.entities.Category> i) {
					final Category[] categories = i.getPayloadObjects();
					final String[] categoryStrings = new String[categories.length];
					for (int iter = 0; iter < categoryStrings.length; iter++) {
						categoryStrings[iter] = categories[iter].getName();
					}

					AlertDialog.Builder categoryAlert = new AlertDialog.Builder(
							getActivity());
					categoryAlert.setTitle("Items zur Activity hinzufügen");

					categoryAlert.setItems(categoryStrings,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									categoryForActivity = new Category(
											categoryStrings[which]);
									viewCategory
											.setText(categoryStrings[which]);
								}
							});
					categoryAlert.create().show();

				}

			};
			AdministrationCommand.fromJSONObject(
					BundleMessage.getInstance().extractObject(b)).accept(p);

		}

		default:
			break;

		}

	}

	@Override
	public void onResume() {
		bmActor.register(getActivity());
		super.onResume();
	}

	@Override
	public void onPause() {
		bmActor.unregister(getActivity());
		super.onPause();
	}

	@Override
	public void onBundleMessageSend(Bundle b) {

	}

	@Override
	public void onResponseMessage(Bundle b) {

	}

	@Override
	public void onResponseAnswerMessage(Bundle b) {

	}

	@Override
	public void onStatusMessage(Bundle b) {

	}

	@Override
	public void onCommandMessage(Bundle b) {

	}

	@Override
	public void onError(Exception e) {

	}

}
