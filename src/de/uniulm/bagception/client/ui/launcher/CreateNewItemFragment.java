package de.uniulm.bagception.client.ui.launcher;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class CreateNewItemFragment extends Fragment implements
		BundleMessageReactor {

	private boolean acceptList = false;
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
	CheckBox independent;
	String warmOn;
	String coldOn;
	String rainyOn;
	String sunnyOn;
	String lightOn;
	String darkOn;
	String temperature;
	String weather;
	String lightness;
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
		getActivity().getActionBar().setTitle("Item hinzufügen");
		getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0099CC")));
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
		independent = (CheckBox) root.findViewById(R.id.independent);
		viewCategory = (TextView) root.findViewById(R.id.viewCategory);

		bmActor = new BundleMessageActor(this);
		addCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				acceptList = true;
				new BundleMessageHelper(getActivity())
						.sendMessageSendBundle(BundleMessage.getInstance()
								.createBundle(
										BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
										CategoryCommand.list()));
			}
		});

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
					darkOn = "dark";
				} else {
					darkOn = null;
				}
			}
		});


		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Item item;

				Log.w("TEST", "TAGID: " + tagId);
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
				
				item = new Item(-1, editName.getText().toString(),
						categoryForActivity, independent.isChecked(), always.isChecked() ,
 						attributes, tagIDs);

				if (((MainGUI) getActivity()).currentPicturetaken != null) {
					item.setImage(((MainGUI) getActivity()).currentPicturetaken);
				}

				Log.d("TEST", item.toString());
				BundleMessageHelper helper = new BundleMessageHelper(
						getActivity());

				if ("".equals(editName.getText().toString().trim())) {
					AlertDialog.Builder dialogAlert = new AlertDialog.Builder(
							getActivity());
					dialogAlert.setTitle("Bitte alle Felder ausfüllen");
					dialogAlert.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					dialogAlert.create().show();
				} else {

					helper.sendMessageSendBundle(BundleMessage.getInstance()
							.createBundle(
									BUNDLE_MESSAGE.ADMINISTRATION_COMMAND,
									ItemCommand.add(item)));
					Log.d("TEST", item.toString());
					
					getActivity().finish();
//					Intent intent = new Intent(getActivity(), MainGUI.class);
//					startActivity(intent);
				}
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editName.setText("");
				iv.setImageResource(R.drawable.ic_launcher);
				
				getActivity().finish();
//				Intent intent = new Intent(getActivity(), MainGUI.class);
//				startActivity(intent);
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
					acceptList = false;
					final Category[] categories = i.getPayloadObjects();
					final String[] categoryStrings = new String[categories.length];
					final long[] categoryIDs = new long[categories.length];
					for (int iter = 0; iter < categoryStrings.length; iter++) {
						categoryIDs[iter] = categories[iter].getId();
						categoryStrings[iter] = categories[iter].getName();
					}

					if (categoryStrings.length == 0) {
						AlertDialog.Builder noCategoriesAvailableAlert = new AlertDialog.Builder(getActivity());
						noCategoriesAvailableAlert.setTitle("Es existiert keine Kategorie");
						
						noCategoriesAvailableAlert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
							}
						});
						noCategoriesAvailableAlert.create().show();
					} else {

						AlertDialog.Builder categoryAlert = new AlertDialog.Builder(
								getActivity());
						categoryAlert
								.setTitle("Kategorie hinzufügen");

						categoryAlert.setItems(categoryStrings,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										categoryForActivity = new Category(
												categoryIDs[which],
												categoryStrings[which]);
										viewCategory
												.setText(categoryStrings[which]);
									}
								});
						categoryAlert.create().show();
					}
				}

			};
			if (!acceptList) {
				return;
			}
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
