package com.quakewatch.ekos.quakewatchaustria.Tablayout_Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.quakewatch.ekos.quakewatchaustria.Custom_Adapter_Listener.CustomArrayAdapter;
import com.quakewatch.ekos.quakewatchaustria.Interfaces.onSpinnerClick;
import com.quakewatch.ekos.quakewatchaustria.Json.JsonParser;
import com.quakewatch.ekos.quakewatchaustria.MainActivity;
import com.quakewatch.ekos.quakewatchaustria.R;
import com.quakewatch.ekos.quakewatchaustria.SubACtivities.SubActivity_DetailAnsicht;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by pkogler on 22.10.2015.
 */
public class Fragment_EUROPA extends Fragment implements onSpinnerClick {
    protected static final int SUB_ACTIVITY_REQUEST_CODE = 100;
    ListView listView;
    View v;
    String magStaerke;
    Context context;
    private float mActionBarHeight;
    private ActionBar mActionBar;
    private boolean jetzt = true;
    private ArrayList<Erdbeben> values = new ArrayList<>();
    private double minMag = 0;
    private String[] spinnerValues = {"0.0+", "", "10", "nach Datum"};
    private boolean filter = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.list_layout_eu, container, false);
        this.createContent();
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frag, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Toast.makeText(getContext(), "hi", Toast.LENGTH_LONG).show();
                new AsyncTaskParseJson().execute();
                return false;
            case R.id.filter:
                FilterFragment dFragment = new FilterFragment(this, this.spinnerValues);
                // Show DialogFragmen
                dFragment.show(getFragmentManager(), "Dialog Fragment");
                filter = true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void createContent() {
        context = this.getContext();
        listView = null;
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        this.magStaerke = SP.getString("magType", "1").charAt(0) + "";
        Log.d("Magni", Integer.parseInt(magStaerke) + "");

        //return v;
        listView = (ListView) v.findViewById(R.id.listEu);
        listView.setEmptyView(v.findViewById(R.id.empty));
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(getContext());
        new AsyncTaskParseJson().execute();
    }

    @Override
    public void saveSpinnerData(String[] values) {
        this.spinnerValues = values;
        new AsyncTaskParseJson().execute();
    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {
        final String TAG = "AsyncTaskParseJson.java";
        ProgressDialog mDialog;
        // contacts JSONArray
        JSONArray dataJsonArr = null;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(context);
            mDialog.setMessage("Beben werden geladen...");
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                values = new ArrayList<>();
                // instantiate our json parser
                JsonParser jParser = new JsonParser();

                // get the array of users
                JSONObject json = JsonParser.readJsonFromUrl("http://geoweb.zamg.ac.at/fdsnws/app/1/query?location=Europa&limit=" + spinnerValues[2] + "&orderby=time");
                minMag = Double.parseDouble(spinnerValues[0].substring(0, 2));
                dataJsonArr = json.getJSONArray("features");

                // loop through all users
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);
                    JSONObject b = c.getJSONObject("properties");

                    //Jason Parsing Methode
                    //Server umstellungen dynamisch

                    // Storing each json item in variable
                    Double mag = Double.parseDouble(b.getString("mag"));
                    String flynn_region = b.getString("region");
                    String time = b.getString("time");
                    Double lat = b.getDouble("lat");
                    Double lon = b.getDouble("lon");
                    double depth = Double.parseDouble(b.getString("depth"));
                    //String username = c.getString("magtype");
                    int id = c.getInt("id");
                    JSONArray places = b.getJSONArray("places");

                    Log.d("newjson", "err" + mag);
                    // show the values in our logcat
                    //Log.e("MyJsonAt", "|" + flynn_region + "|");
                    if ((mag >= minMag)) {
                        Log.e("testla", "JA");
                        values.add(new Erdbeben(mag, flynn_region, time, depth, lat, lon, places, id));
                    }//} else
                    //Log.e("testla", "NEIN");

                }
                //JSONObject ob = json.getJSONObject("properties");
                //values.add(0,ob.getString("magType"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (spinnerValues[3].equals("nach Magnitude")) Collections.sort(values);
            return null;
        }


        @Override
        protected void onPostExecute(String strFromDoInBg) {
            mDialog.dismiss();
            if (filter) {
                filter = false;
                Toast.makeText(getContext(), values.size() + " Ergebnisse gefunden", Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> adapter = new CustomArrayAdapter(getContext(), values);
            listView.setAdapter(adapter);
            listView.deferNotifyDataSetChanged();
            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Erdbeben temp = (Erdbeben) parent.getItemAtPosition(position);
                            Toast.makeText(getContext(), temp.getMag() + "", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getContext(), SubActivity_DetailAnsicht.class);
                            i.putExtra("bebenData", temp);
                            i.putExtra("isAt", false);
                            startActivityForResult(i, SUB_ACTIVITY_REQUEST_CODE);
                        }
                    }
            );

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getContext(), "Beben auf map anzeigen", Toast.LENGTH_LONG).show();
                    Erdbeben temp = (Erdbeben) parent.getItemAtPosition(position);
                    ((MainActivity) getActivity()).setPager(3, temp);
                    return true;
                }
            });


            final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                    new int[]{android.R.attr.actionBarSize});
            mActionBarHeight = styledAttributes.getDimension(0, 0);
            mActionBar = ((MainActivity) getActivity()).getSupportActionBar();
        }
    }
}

