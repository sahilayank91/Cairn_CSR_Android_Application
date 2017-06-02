package in.ac.iiitkota.cairn.csr.android.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.adapter.FontusLocationsAdapter;
import in.ac.iiitkota.cairn.csr.android.adapter.NandgramListAdapter;
import in.ac.iiitkota.cairn.csr.android.interfaces.RCVItemClickListener;
import in.ac.iiitkota.cairn.csr.android.model.Fontus;
import in.ac.iiitkota.cairn.csr.android.model.Nandgram;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;

public class DepartmentLocationList extends AppCompatActivity implements RCVItemClickListener {

    private RecyclerView recyclerView;
    private FontusLocationsAdapter adapter;
    private ArrayList<Fontus> listFontus=new ArrayList<Fontus>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_location_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Fontus Locations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_nandgram_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FontusLocationsAdapter(listFontus);
        recyclerView.setAdapter(adapter);
        adapter.setRcvItemClickListener(this);
        updateList();
    }

    public void updateList(){
        int i;
         String[] area_name;
         String[] area_launch_date;
        area_name = getResources().getStringArray(R.array.fontus_area);
        area_launch_date = getResources().getStringArray(R.array.fontus_launch_dates);
        listFontus.clear();
        for(i=0;i<area_name.length;i++){
            Fontus fontus = new Fontus();
            fontus.setArea_name(area_name[i]);
            fontus.setLaunch_date(area_launch_date[i]);
            listFontus.add(fontus);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {

        switch (view.getId()) {
            case R.id.data_holder:
                Intent i = new Intent(this,NandGharStatsActivity.class);
                startActivity(i);
                break;
        }

    }


}
