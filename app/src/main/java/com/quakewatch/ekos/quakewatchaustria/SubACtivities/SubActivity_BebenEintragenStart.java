package com.quakewatch.ekos.quakewatchaustria.SubACtivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.quakewatch.ekos.quakewatchaustria.R;
import com.quakewatch.ekos.quakewatchaustria.Tablayout_Fragments.Erdbeben;

import java.util.Calendar;

/**
 * Created by pkogler on 22.10.2015.
 * Usage: Defines :
 * the DatePicker
 * Timepicker
 */

public class SubActivity_BebenEintragenStart extends AppCompatActivity {
    public final static int SUCCESS_RETURN_CODE = 1;
    protected static final int SUB_ACTIVITY_REQUEST_CODE = 100;


    private TextView state;
    private Erdbeben bebenData;

    EditText zeit;
    EditText datum;
    EditText ort;
    EditText plz;

    TextInputLayout lZeit;
    TextInputLayout lDate;
    TextInputLayout lOrt;
    TextInputLayout lPlz;

    Button weiter;

    int hour_x, minute_x;
    int year_x, month_x, day_x;

    static final int DATE_ID = 0;
    static final int TIME_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bebenData = (Erdbeben) getIntent().getExtras().get("bebenData");
        setContentView(R.layout.subactivity_beben);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        showTimeDialog();
        showDialogOnClick();

        lZeit = (TextInputLayout) findViewById(R.id.LayoutZeit);
        lDate = (TextInputLayout) findViewById(R.id.LayoutDatum);
        lOrt = (TextInputLayout) findViewById(R.id.LayoutOrt);
        lPlz = (TextInputLayout) findViewById(R.id.LayoutPlz);

        plz = (EditText) findViewById(R.id.plz);
        ort = (EditText) findViewById(R.id.Ort);

        weiter = (Button) findViewById(R.id.next);
        weiter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                if (zeit.getText().toString().trim().equals("")) {
                    lZeit.setError("Zeit ist notwendig!");
                    lZeit.setHint("Bitte Zeit auswählen");

                } else if (datum.getText().toString().trim().equals("")){
                    lDate.setError("Datum ist notwendig!");
                    lDate.setHint("Bitte Datum auswählen");

                } else if (plz.getText().toString().trim().equals("")){
                    lPlz.setError("PLZ ist notwendig!");
                    lPlz.setHint("Bitte PLZ eingeben");

                } else if (ort.getText().toString().trim().equals("")){
                    lOrt.setError("Ort ist notwendig!");
                    lOrt.setHint("Bitte Ort auswählen");

                } else {
                    Intent i = new Intent(getBaseContext(), SubActivity_Phase2.class);
                    startActivity(i);
                }
            }
        });

        boolean state = (boolean) getIntent().getExtras().get("state");
        if (state) {
            minute_x = cal.get(Calendar.MINUTE);
            hour_x = cal.get(Calendar.HOUR);
            zeit.setText(hour_x + ":" + minute_x);
        } else {
            setUp();
        }
    }

    protected Dialog onCreateDialog(int id) {
        if (id == TIME_ID)
            return new TimePickerDialog(this, kTimePickerListner, hour_x, minute_x, true);
        if (id == DATE_ID)
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        return null;
    }

    /**
     * Methoden um die Zeit einstellen zu können
     */
    public void showTimeDialog() {
        zeit = (EditText) findViewById(R.id.zeit);
        zeit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                showDialog(TIME_ID);
            }
        });
    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListner =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hour_x = hourOfDay;
                    minute_x = minute;
                    zeit.setText(hour_x + ":" + minute_x);
                }
            };

    /**
     * Methoden um das Datum eingeben zu können
     */
    public void showDialogOnClick() {
        datum = (EditText) findViewById(R.id.datum);
        datum.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                showDialog(DATE_ID);
            }
        });

    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;
            datum.setText(year_x + "/" + month_x + "/" + day_x);
        }
    };


    private void setUp() {
        //state.setText("Nicht Jetzt\n"+bebenData.getRegion()+"\n"+bebenData.getMag());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent i = new Intent();
                setResult(SUCCESS_RETURN_CODE, i);
                //startActivityForResult(new Intent(getBaseContext(), MainActivity.class), 12);
                i.putExtra("position", "1");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
