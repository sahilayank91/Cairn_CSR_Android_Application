package in.ac.iiitkota.cairn.csr.android.ui.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.adapter.FeedbackAdapter;
import in.ac.iiitkota.cairn.csr.android.interfaces.RCVItemClickListener;
import in.ac.iiitkota.cairn.csr.android.model.Feedback;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;
import com.hsalf.smilerating.SmileRating;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment implements RCVItemClickListener {

    private RecyclerView recyclerView;
    private FeedbackAdapter feedbackAdapter;

    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    View view;
    private List<Feedback> feedbackList=new ArrayList<>();

    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null)
        view=inflater.inflate(R.layout.fragment_feedback, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.feedback_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedbackAdapter = new FeedbackAdapter(getActivity(), feedbackList);
        recyclerView.setAdapter(feedbackAdapter);

        feedbackAdapter.setRcvItemClickListener(this);


        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // Refresh items
                mSwipeRefreshLayout.setRefreshing(true);
                prepareFeedbackItems();
            }
        });
        prepareFeedbackItems();
    }

    private void prepareFeedbackItems() {

        new GetFeedbackList().execute();

    }


    @Override
    public void onItemClick(View view, int position) {

        switch (view.getId()) {
            case R.id.btn_submit_feedback:
                //handle feedback submission

                SmileRating reaction = (SmileRating) view.getRootView().findViewById(R.id.smile_rating);
                EditText comment=(EditText)view.getRootView().findViewById(R.id.et_feedback);

                new UpdateFeedback(position,reaction.getSelectedSmile(),comment.getText().toString()).execute();

                break;
            case R.id.btn_see_activities:
                //startActivity(new Intent(getActivity(), OtherUserProfileActivity.summy_headcount));
                break;
        }

    }
    class GetFeedbackList extends AsyncTask<String,String,String>{
        boolean success=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(success){
                try {
                    ;
                    JSONArray jsonArray=new JSONArray(s);
                    feedbackList.clear();
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        Feedback feedback=new Feedback(jsonArray.getJSONObject(i));
                        feedbackList.add(feedback);
                    }
                    feedbackAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressBar.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            String response="";
            String url=getResources().getString(R.string.get_feedback)+ UserData.getInstance(getContext()).getUser_id();
            try {
                if(isAdded())
                response= Server.performServerCall(url,"GET");
                else  return null;
                success=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }
    class UpdateFeedback extends AsyncTask<String,String,String>{
        boolean success=false;

        int position;
        HashMap<String,String>params=new HashMap<>();

        public UpdateFeedback(int position,int reaction,String comment){
            this.position=position;

            params.put("feedback_id",String.valueOf(feedbackList.get(position).getFeedback_id()));
            params.put("reaction", String.valueOf(reaction));
            params.put("comment",comment);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(success){

                feedbackList.remove(position);
                feedbackAdapter.notifyItemRemoved(position);

            }


        }

        @Override
        protected String doInBackground(String... strings) {
            String response="";
            String url=getResources().getString(R.string.update_feedback);
            try {
                if(isAdded())
                    response=Server.performServerCall(url,params,"POST");
                else  return null;
                success=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }

}
