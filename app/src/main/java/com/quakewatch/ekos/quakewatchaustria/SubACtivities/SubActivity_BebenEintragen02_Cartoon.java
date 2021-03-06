package com.quakewatch.ekos.quakewatchaustria.SubACtivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.quakewatch.ekos.quakewatchaustria.Custom_Adapter_Listener.ContactAdapter;
import com.quakewatch.ekos.quakewatchaustria.Custom_Adapter_Listener.ContactInfo;
import com.quakewatch.ekos.quakewatchaustria.R;

import java.util.ArrayList;

public class SubActivity_BebenEintragen02_Cartoon extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ActionBar actionBar;

    /**
     *das erstellen der Cardviwe und einfügen der Bilder
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_activity__news);
        actionBar = getSupportActionBar();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        ArrayList<ContactInfo> temp = new ArrayList<>();
        this.createContent(temp);
        mAdapter = new ContactAdapter(temp, getBaseContext());

        ContactAdapter.OnItemClickListener clickListener = new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getBaseContext(), "Pos: " + position, Toast.LENGTH_SHORT).show();
                Log.d("Card", "drin");
                //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.canada));
                Intent i = new Intent(getBaseContext(), SubActivity_BebenEintragen02_Detail.class);
                i.putExtra("position", position);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        };
        mAdapter.setOnItemClickListener(clickListener);

        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     *zu den verschiedenen bildern werden die eigenschaften
     * hinzugefuegt
     * @param temp
     */
    private void createContent(ArrayList<ContactInfo> temp) {
        temp.add(new ContactInfo(R.drawable.schwach, "Schwach verspürt"));
        temp.add(new ContactInfo(R.drawable.deutlich, "Deutlich verspürt"));
        temp.add(new ContactInfo(R.drawable.stark, "Stark verspürt"));
        temp.add(new ContactInfo(R.drawable.leichte, "Leichte Gebäudeschäden"));
        temp.add(new ContactInfo(R.drawable.gebaeudenschaden, "Bebäudeschäden"));
        temp.add(new ContactInfo(R.drawable.schwere, "Schwere Gebäudeschäden"));
    }

    /**
     *Liefert das Item welches beschreibt um welches Item es sich handelt
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
