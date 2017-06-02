package in.ac.iiitkota.cairn.csr.android.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import in.ac.iiitkota.cairn.csr.android.R;

import in.ac.iiitkota.cairn.csr.android.adapter.MessagesAdapter;
import in.ac.iiitkota.cairn.csr.android.model.Message;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.MessagesThreadFragment;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    private List<Message> listMessages;
    private Long team_id;
    private int position;
    //private ScrollView messageScrollView;
    private ImageView messagePublishIcon;
    TextView message;
    private LinearLayoutManager layout_manager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        position=getIntent().getIntExtra("position",-1);
        if(position==-1) finish();
        team_id= MessagesThreadFragment.teams.get(position).getTeam_id();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getExtras().getString("threadTitle"));

        listMessages = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.messages_thread_recycler_view);
        layout_manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout_manager);
        //messageScrollView=(ScrollView)findViewById(R.id.messages_scroll_view);
        messagePublishIcon=(ImageView)findViewById(R.id.message_publish_icon);
        message=(TextView)findViewById(R.id.message_edit_text);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        messagePublishIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!message.getText().toString().trim().equals(""))
                new SendMessages().execute();
            }
        });
        adapter = new MessagesAdapter(this, listMessages);
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // Refresh items
                           prepareMessages();
            }
        });
        prepareMessages();

    }

    private void prepareMessages() {

        new GetMessages().execute();

    }



    class GetMessages extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
            messagePublishIcon.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            mSwipeRefreshLayout.setRefreshing(false);
            messagePublishIcon.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            try {
                JSONArray messages=new JSONArray(response);
                listMessages.clear();
                for (int i = 0; i <messages.length() ; i++) {
                    listMessages.add(new Message(messages.getJSONObject(i)));
                }
                adapter.notifyDataSetChanged();
                //recyclerView.scrollToPosition(layout_manager.getHeight()+50);
                recyclerView.scrollToPosition(listMessages.size() -1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String url=getApplicationContext().getResources().getString(R.string.retrieve_messages)+team_id;
            String response="";
            try {

                response=Server.performServerCall(url,"GET");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }
    class SendMessages extends AsyncTask<String,String,String>{
        boolean success=false;

        HashMap<String,String>params=new HashMap<>();
        Message message_obj;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            messagePublishIcon.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

                params.put("message_text", message.getText().toString());
                params.put("team_id",String.valueOf(team_id));
                params.put("user_id",String.valueOf(UserData.getInstance(getApplicationContext()).getUser_id()));
                message_obj =new Message();
                message_obj.setTime(new Date());
                message_obj.setText(message.getText().toString());
                message_obj.setAuthor(UserData.getInstance(getApplicationContext()).getUser());

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            messagePublishIcon.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            if(success){
                try{

                    listMessages.add(message_obj);
                    message.setText("");
                    adapter.notifyItemInserted(listMessages.size());
                    recyclerView.scrollToPosition(listMessages.size() - 1);
                }
                catch (Exception e){
                    e.printStackTrace();
                }


//                message_obj.setFocusableInTouchMode(false);
//                message_obj.setFocusable(false);
//                message_obj.setFocusableInTouchMode(true);
//                message_obj.setFocusable(true);

            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String url=getApplicationContext().getResources().getString(R.string.send_message);
            String response="";
            try {
                response=Server.performServerCall(url,params,"POST");
                success=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
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

}
