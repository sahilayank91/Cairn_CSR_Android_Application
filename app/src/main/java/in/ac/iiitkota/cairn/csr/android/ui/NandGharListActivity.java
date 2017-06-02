package in.ac.iiitkota.cairn.csr.android.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.adapter.NandgramListAdapter;
import in.ac.iiitkota.cairn.csr.android.interfaces.RCVItemClickListener;
import in.ac.iiitkota.cairn.csr.android.model.Nandgram;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class NandGharListActivity extends AppCompatActivity implements RCVItemClickListener {

    private RecyclerView recyclerView;
    private NandgramListAdapter adapter;
    private ArrayList<Nandgram> listNandgrams=new ArrayList<Nandgram>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nandghar_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.nandghar_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_nandgram_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NandgramListAdapter(listNandgrams);
        recyclerView.setAdapter(adapter);
        adapter.setRcvItemClickListener(this);
        new GetNandgrams().execute();

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
                i.putExtra("nandgram_id",listNandgrams.get(position).getNandgram_id());
                startActivity(i);
                break;
            case R.id.btn_add_attendance:
                Intent intent=new Intent(this, AddAttendance.class);
                intent.putExtra("nandgram_id",listNandgrams.get(position).getNandgram_id());
                intent.putExtra("longitude",listNandgrams.get(position).getLongitude());
                intent.putExtra("latitude",listNandgrams.get(position).getLatitude());
                intent.putExtra("nandgram_name",listNandgrams.get(position).getLongitude());
                startActivity(intent);

                break;
        }

    }
    class GetNandgrams extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            try {
                JSONArray nandgrams=new JSONArray(response);
                listNandgrams.clear();
                for (int i = 0; i <nandgrams.length() ; i++) {
                    listNandgrams.add(new Nandgram(nandgrams.getJSONObject(i)));
                }
                adapter.notifyDataSetChanged();
                //recyclerView.scrollToPosition(layout_manager.getHeight()+50);
                recyclerView.scrollToPosition(listNandgrams.size() -1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String url=getApplicationContext().getResources().getString(R.string.retrieve_nandgrams);
            String response="";
            try {

                response= Server.performServerCall(url,"GET");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }

}
