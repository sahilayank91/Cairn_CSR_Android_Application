package in.ac.iiitkota.cairn.csr.android.ui.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import in.ac.iiitkota.cairn.csr.android.adapter.MessageThreadAdapter;
import in.ac.iiitkota.cairn.csr.android.interfaces.RCVItemClickListener;
import in.ac.iiitkota.cairn.csr.android.model.Team;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.ui.MessageActivity;
import in.ac.iiitkota.cairn.csr.android.ui.TeamProfileActivity;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import in.ac.iiitkota.cairn.csr.android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesThreadFragment extends Fragment implements RCVItemClickListener {

    private RecyclerView recyclerView;
    private MessageThreadAdapter adapter;
    public static ArrayList<Team>teams=new ArrayList<>();
    View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;

    public MessagesThreadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        if(view==null)view=inflater.inflate(R.layout.fragment_messages_thread, container, false);
        else return view;

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        recyclerView = (RecyclerView) view.findViewById(R.id.messages_thread_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MessageThreadAdapter(getActivity(), teams);
        recyclerView.setAdapter(adapter);
        adapter.setRcvItemClickListener(this);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // Refresh items
                mSwipeRefreshLayout.setRefreshing(true);
                prepareMessages();
            }
        });
        prepareMessages();
        return view;
    }



    private void prepareMessages() {

        new GetTeams().execute();

    }


    @Override
    public void onItemClick(View view, int position) {

        if (view.getId() == R.id.thread_image) {

            Intent intent = new Intent(getActivity(), TeamProfileActivity.class);
            intent.putExtra("position", position);

            Pair<View, String> imagePair = Pair.create(view, "team_image_transition");
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), imagePair);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }

        } else {

            Intent intent = new Intent(getActivity(), MessageActivity.class);

            intent.putExtra("position", position);
            intent.putExtra("threadTitle", teams.get(position).getName());
            startActivity(intent);
        }
    }

    class GetTeams extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            mSwipeRefreshLayout.setRefreshing(false);

            try {
                JSONArray teams_array=new JSONArray(response);
                teams.clear();
                for (int i = 0; i <teams_array.length() ; i++) {
                    Team team=new Team(teams_array.getJSONObject(i));
                    teams.add(team);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();


        }

        @Override
        protected String doInBackground(String... strings) {
            String url= getContext().getResources().getString(R.string.retrieve_teams)+ UserData.getInstance(getContext()).getUser_id();
         String response="";
            try {
                if(isAdded())
          response=     Server.performServerCall(url,"GET");
                else cancel(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }
    }
}
