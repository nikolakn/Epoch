package nk.code.epoch;

import java.util.ArrayList;

import nk.code.data.Event;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.android.colorpicker.ColorPickerDialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

//Activity for Event data input,edit and validation
public class AddEpochActivity extends AppCompatActivity implements
		DialogInterface.OnDismissListener {

	private EditText name;
	private EditText date;
	private EditText time;
	private EditText enddate;
	private EditText endtime;
	private Button colorb;
	ColorPickerDialog colorcalendar;
	private RadioGroup radiog1;
	private RadioGroup radiog2;
	private int boja;
	private Spinner s;
	private boolean spinerchange=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_epoch);
		spinerchange = false;
		name = (EditText) findViewById(R.id.editText12);
		date = (EditText) findViewById(R.id.editText22);
		time = (EditText) findViewById(R.id.editText32);

		enddate = (EditText) findViewById(R.id.editTextEndDate);
		endtime = (EditText) findViewById(R.id.editTextEndTime);

		colorb = (Button) findViewById(R.id.colorButton2);
		radiog1 = (RadioGroup) findViewById(R.id.radio_group12);
		radiog2 = (RadioGroup) findViewById(R.id.radio_group22);
		ArrayList<String> SourceArray = new ArrayList<String>();
		s = (Spinner) findViewById(R.id.Spinner012);
		s.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//if(!prvapromena)
				spinerchange = true;
				//Log.d("nk","promena");
				//prvapromena = false;
				return false;
			}
			
		});
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, SourceArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);
		adapter.add(Event.Visibility.ALWAYS.getFieldDescription());
		adapter.add(Event.Visibility.HEREANDMINUS.getFieldDescription());
		adapter.add(Event.Visibility.ONLYHERE.getFieldDescription());
		adapter.add(Event.Visibility.HEREANDPLUS.getFieldDescription());

		String argname = getIntent().getStringExtra("name");
		String argdate = getIntent().getStringExtra("date");
		String argtime = getIntent().getStringExtra("time");
		String argdateend = getIntent().getStringExtra("dateend");
		String argtimeend = getIntent().getStringExtra("timeend");
		int argcolor = getIntent().getIntExtra("color", Event.DEFEVENTCOLOR);
		int argsize = getIntent().getIntExtra("size", Event.DEFEVENTSIZE);
		int argstyle = getIntent().getIntExtra("style", Event.DEFEVENTSTYLE);

		int argvisibility = getIntent().getIntExtra("visibility",
				Event.Visibility.ALWAYS.ordinal());

		if (savedInstanceState != null) {
			// Restore value of members from saved state
			argname = savedInstanceState.getString("name");
			argdate = savedInstanceState.getString("date");
			argtime = savedInstanceState.getString("time");
			argdateend = savedInstanceState.getString("dateend");
			argtimeend = savedInstanceState.getString("timeend");
			argcolor = savedInstanceState.getInt("boja");
			argsize = savedInstanceState.getInt("size");
			argstyle = savedInstanceState.getInt("style");
			argvisibility = savedInstanceState.getInt("visibility");
		}

		s.setSelection(argvisibility);

		if (name != null)
			name.setText(argname);
		if (date != null)
			date.setText(argdate);
		if (time != null)
			time.setText(argtime);

		if (enddate != null)
			enddate.setText(argdateend);
		if (endtime != null)
			endtime.setText(argtimeend);
		colorb.setBackgroundColor(argcolor);
		boja = argcolor;
		switch (argsize) {
		case 0:
			radiog1.check(R.id.radio12);
			break;
		case 1:
			radiog1.check(R.id.radio22);
			break;
		case 2:
			radiog1.check(R.id.radio32);
			break;
		case 3:
			radiog1.check(R.id.radio42);
			break;
		case 4:
			radiog1.check(R.id.radio52);
			break;
		}
		switch (argstyle) {
		case 0:
			radiog2.check(R.id.sradio12);
			break;
		case 1:
			radiog2.check(R.id.sradio22);
			break;
		case 2:
			radiog2.check(R.id.sradio32);
			break;
		case 3:
			radiog2.check(R.id.sradio42);
			break;

		}

		String color_array[] = { "#ff8000", "#fcb314", "#067ab4", "#00ff00",
				"#f2ff00", "#19e3d9", "#52a74f", "#fedf83", "#9c2902",
				"#cc0000", "#800080", "#696969", "#95a484", "#00ffff" };
		int[] mColor = new int[color_array.length];
		for (int i = 0; i < color_array.length; i++) {
			mColor[i] = Color.parseColor(color_array[i]);
		}
		colorcalendar = ColorPickerDialog.newInstance(
				R.string.color_picker_default_title, mColor, 0, 5,
				ColorPickerDialog.SIZE_SMALL);

		// validate input for name
		name.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// name input validation
					if (name.getText().toString().length() == 0) {
						name.setError("Name is required!");
						return true;
					} else {
						name.setError(null);
					}
				}
				return false;
			}
		});

		// validate input for date
		date.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// date input validation
					try {
						String pattern = "dd.MM.yyyy";
						DateTime.parse(date.getText().toString(),
								DateTimeFormat.forPattern(pattern));
						date.setError(null);
					} catch (Exception e) {
						date.setError("Format day.month.year e.g.: 19.2.2015");
						return false;
					}
				}
				return false;
			}
		});

		// validate input for time
		time.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// date input validation
					// time input validation
					try {
						String pattern = "HH:mm";
						DateTime.parse(time.getText().toString(),
								DateTimeFormat.forPattern(pattern));
						time.setError(null);
					} catch (Exception e) {
						time.setError("Format hours:minute e.g.: 19:15");
						return false;
					}
				}
				return false;
			}
		});

		// validate input for date
		enddate.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// date input validation
					try {
						String pattern = "dd.MM.yyyy";
						DateTime.parse(date.getText().toString(),
								DateTimeFormat.forPattern(pattern));
						date.setError(null);
					} catch (Exception e) {
						date.setError("Format day.month.year e.g.: 19.2.2015");
						return false;
					}
				}
				return false;
			}
		});

		// validate input for time
		endtime.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// date input validation
					// time input validation
					try {
						String pattern = "HH:mm";
						DateTime.parse(time.getText().toString(),
								DateTimeFormat.forPattern(pattern));
						time.setError(null);
					} catch (Exception e) {
						time.setError("Format hours:minute e.g.: 19:15");
						return false;
					}
				}
				return false;
			}
		});
		// save button
		Button save = (Button) findViewById(R.id.saveButton2);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// name input validation
				if (name.getText().toString().length() == 0) {
					name.setError("Name is required!");
					return;
				}
				// date input validation
				try {
					String pattern = "dd.MM.yyyy";
					DateTime.parse(date.getText().toString(),
							DateTimeFormat.forPattern(pattern));
				} catch (Exception e) {
					date.setError("Format day.month.year e.g.: 19.2.2015");
					return;
				}

				// time input validation
				try {
					String pattern = "HH:mm";
					DateTime.parse(time.getText().toString(),
							DateTimeFormat.forPattern(pattern));
				} catch (Exception e) {
					time.setError("Format hours:minute e.g.: 19:15");
					return;
				}

				Intent returnIntent = new Intent();

				int radioButtonID = radiog1.getCheckedRadioButtonId();
				View radioButton = radiog1.findViewById(radioButtonID);
				int a1 = radiog1.indexOfChild(radioButton);

				int radioButtonID2 = radiog2.getCheckedRadioButtonId();
				View radioButton2 = radiog2.findViewById(radioButtonID2);
				int a2 = radiog2.indexOfChild(radioButton2);

				int vis = (int) s.getSelectedItemId();
				if (vis == android.widget.AdapterView.INVALID_ROW_ID)
					vis = 0;
				returnIntent.putExtra("name", name.getText().toString());
				returnIntent.putExtra("date", date.getText().toString());
				returnIntent.putExtra("time", time.getText().toString());
				returnIntent.putExtra("dateend", enddate.getText().toString());
				returnIntent.putExtra("timeend", endtime.getText().toString());
				returnIntent.putExtra("boja", boja);
				returnIntent.putExtra("size", a1);
				returnIntent.putExtra("style", a2);
				if(spinerchange){
					returnIntent.putExtra("visibility", vis);
					//Log.d("nk","gore");
				}
				else{
					//Log.d("nk","dole");
					returnIntent.putExtra("visibility", -1);
				}
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});

		colorb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Colours for colour picker dialog
				colorcalendar.show(getFragmentManager(), "cal");
			}
		});
	}

	// called when colour picker dialog is dismissed
	@Override
	public void onDismiss(final DialogInterface dialog) {

		int col = colorcalendar.getSelectedColor();
		colorb.setBackgroundColor(col);
		boja = col;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
		// Save the user's current game state
		int radioButtonID = radiog1.getCheckedRadioButtonId();
		View radioButton = radiog1.findViewById(radioButtonID);
		int a1 = radiog1.indexOfChild(radioButton);

		int radioButtonID2 = radiog2.getCheckedRadioButtonId();
		View radioButton2 = radiog2.findViewById(radioButtonID2);
		int a2 = radiog2.indexOfChild(radioButton2);

		int vis = (int) s.getSelectedItemId();
		if (vis == android.widget.AdapterView.INVALID_ROW_ID)
			vis = 0;

		savedInstanceState.putString("name", name.getText().toString());
		savedInstanceState.putString("date", date.getText().toString());
		savedInstanceState.putString("time", time.getText().toString());
		savedInstanceState.putString("dateend", enddate.getText().toString());
		savedInstanceState.putString("timeend", endtime.getText().toString());
		savedInstanceState.putInt("boja", boja);
		savedInstanceState.putFloat("size", a1);
		savedInstanceState.putFloat("style", a2);
		savedInstanceState.putFloat("visibility", vis);

	}

}
