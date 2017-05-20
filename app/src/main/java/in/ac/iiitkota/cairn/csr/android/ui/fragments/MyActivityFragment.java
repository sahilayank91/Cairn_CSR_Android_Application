package in.ac.iiitkota.cairn.csr.android.ui.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.adapter.AllLikesAdapter;
import in.ac.iiitkota.cairn.csr.android.adapter.MyActivityAdapter;
import in.ac.iiitkota.cairn.csr.android.adapter.NewsFeedAdapter;
import in.ac.iiitkota.cairn.csr.android.adapter.ShareWithAdapter;
import in.ac.iiitkota.cairn.csr.android.interfaces.CheckboxCheckChangeListener;
import in.ac.iiitkota.cairn.csr.android.interfaces.RCVItemClickListener;
import in.ac.iiitkota.cairn.csr.android.model.Post;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.ui.CommentDetailsActivity;
import in.ac.iiitkota.cairn.csr.android.ui.NewMilestoneActivity;
import in.ac.iiitkota.cairn.csr.android.ui.NewsFeedDetailImage;
import in.ac.iiitkota.cairn.csr.android.ui.OtherUserProfileActivity;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static in.ac.iiitkota.cairn.csr.android.ui.fragments.NewsFeedFragment.prepareTeamList;
import static in.ac.iiitkota.cairn.csr.android.ui.fragments.NewsFeedFragment.teams;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyActivityFragment extends Fragment implements RCVItemClickListener ,CheckboxCheckChangeListener{

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private MyActivityAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewShareTeams;
    ShareWithAdapter share_with_adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    boolean hasPublic;

    public static ArrayList<Post> listMyActivityFeed = new ArrayList<>();
    View view;


    public MyActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (view == null) view = inflater.inflate(R.layout.fragment_my_activity, container, false);
        else return view;

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_activity_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        hasPublic= UserData.getInstance(getContext()).getUser().getAccount_level()>0;
        adapter = new MyActivityAdapter(getActivity(), listMyActivityFeed);
        recyclerView.setAdapter(adapter);
        adapter.setRcvItemClickListener(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // Refresh items
                     prepareNewsFeedItems();
            }
        });
        prepareNewsFeedItems();

        fab = (FloatingActionButton) view.findViewById(R.id.my_activity_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewMilestoneActivity.class));
            }
        });
        return view;


    }

    private void prepareNewsFeedItems() {
        new GetNewsfeed().execute();

    }


    class GetNewsfeed extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(true);

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                if(isAdded())
                result = Server.performServerCall(getResources().getString(R.string.retrieve_author_feed) + UserData.getInstance(getContext()).getUser_id(), "GET");
                else cancel(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            listMyActivityFeed.clear();


            try {

                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject post = jsonArray.getJSONObject(i);
                    Post current = new Post(post);
                    listMyActivityFeed.add(current);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();

            progressBar.setVisibility(GONE);
            adapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);

        }


    }

    @Override
    public void onItemClick(View clickedView, int position) {
        switch (clickedView.getId()) {
            case R.id.newsfeed_image:
                startActivityWithTransition(clickedView, 0, position, "image_transition");
                break;
            case R.id.newsfeed_like_icon:
                break;
            case R.id.news_feed_comment_icon_holder:
                startActivityWithTransition(clickedView, 1, position, "comment_icon_transition");
                break;
            case R.id.news_feed_share_icon_holder:

                popupShareDialog(listMyActivityFeed.get(position).getPost_id());

                break;
            case R.id.newsfeed_profile_image:
                startActivityWithTransition(clickedView, 2, position, "profile_image_transition");
                break;
            case R.id.newsfeed_total_likes:
                popupLikesDialog(listMyActivityFeed.get(position));
                break;
            case R.id.newsfeed_overflow_menu:
                popupOverflowMenu(position,clickedView);
                break;
        }
    }

    private void popupShareDialog(final Long post_id) {

        final Dialog dialog = new Dialog(getContext(),R.style.AppTheme_CustomDialog);
        dialog.setTitle(R.string.share_with);
        dialog.setContentView(R.layout.dialog_share);


        recyclerViewShareTeams = (RecyclerView) dialog.findViewById(R.id.recycler_view_share_with);
        recyclerViewShareTeams.setLayoutManager(new LinearLayoutManager(getActivity()));

        final boolean hasPublic=UserData.getInstance(getContext()).getUser().getAccount_level()>0;
        share_with_adapter = new ShareWithAdapter(prepareTeamList(),hasPublic);
        recyclerViewShareTeams.setAdapter(share_with_adapter);

        share_with_adapter.setCheckboxCheckChangeListener(this);

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        final Button share = (Button) dialog.findViewById(R.id.share);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            ArrayList<Long>share_teams=new ArrayList<>();
            @Override
            public void onClick(View v) {


                for(int i=0;i<recyclerViewShareTeams.getLayoutManager().getChildCount();i++){

                    CheckBox  checkbox=(CheckBox)  recyclerViewShareTeams.getLayoutManager().getChildAt(i).findViewById(R.id.checkbox_team);
                    if(checkbox.isChecked()){
                        if(hasPublic){
                            if(i==0){
                                share_teams.add(0l);
                            }
                            else
                                share_teams.add(teams.get(i-1).getTeam_id());
                        }
                        else{
                            share_teams.add(teams.get(i).getTeam_id());
                        }


                    }
                }

                new SharePost(post_id,UserData.getInstance(getContext()).getUser_id(),share_teams).execute();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void popupOverflowMenu(final int position, View clickedView) {
        final Post post= listMyActivityFeed.get(position);
        //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
        PopupMenu popupMenu = new PopupMenu(getActivity(), clickedView);

        if(post.getAuthor().getUser_id()==UserData.getInstance(getContext()).getUser().getUser_id()){
            popupMenu.getMenu().add(Menu.NONE, 1,1, R.string.Edit);
            popupMenu.getMenu().add(Menu.NONE, 2,2,R.string.Delete);
        }

        popupMenu.getMenu().add(Menu.NONE, 3,3,R.string.share_social);

        popupMenu.show();


//        popupMenu.getMenuInflater().inflate(R.menu.overflow_menu_newsfeed, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

//                Toast.makeText(getActivity(), "Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                switch (item.getItemId()){
                    case 1://edit
                        Intent intent=new Intent(getContext(), NewMilestoneActivity.class);
                        intent.putExtra("edit",true);
                        intent.putExtra("edit_title",post.getText());
                        intent.putExtra("edit_lives",post.getSmiles());
                        if(post.getImages()!=null &&post.getImages().size()>0)
                            intent.putExtra("edit_image",post.getImages().get(0).getFilename());
                        intent.putExtra("edit_post_id",post.getPost_id());
                        startActivity(intent);
                        break;
                    case 2:
                        new DeleteMilestone(post.getPost_id(),position).execute();
                        break;
                    case 3:
                        String text="";
                        if (post.getSmiles() > 1)
                            text="made " + post.getSmiles() + " people smile";
                        else if (post.getSmiles() == 1)
                            text=getResources().getString(R.string.made_smile);
                        else
                            text="";
                        Drawable drawable=null;
                        if(listMyActivityFeed.get(position).getImages()!=null&& listMyActivityFeed.get(position).getImages().size()>0)
                            try{
                                ImageView image    = (ImageView) recyclerView.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.newsfeed_image);
                                drawable=image.getDrawable();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                        share(drawable,post.getText()+" - "+post.getAuthor().getName()+" "+text);
                        break;
                }

                return true;
            }
        });

        popupMenu.show();

    }

    private void popupLikesDialog(Post post) {

        Dialog dialog = new Dialog(getActivity());
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_likes);
        dialog.setTitle(R.string.people_liked);

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view_likes_dialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        AllLikesAdapter allLikesAdapter = new AllLikesAdapter(getActivity(), post.getLikers());
        recyclerView.setAdapter(allLikesAdapter);

        dialog.show();
    }




    private void startActivityWithTransition(View clickedView, int flag, int position, String transitionName) {

        Intent intent = null;

        switch (flag) {
            case 0:
                intent = new Intent(getActivity(), NewsFeedDetailImage.class);
                intent.putExtra("image", listMyActivityFeed.get(position).getImages().get(0).getFilename());
                intent.putExtra("caption", listMyActivityFeed.get(position).getText());
                break;
            case 1:
                intent = new Intent(getActivity(), CommentDetailsActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("my_activity",true);
                clickedView = clickedView.findViewById(R.id.newsfeed_comment_icon);
                break;
            case 2:
                intent = new Intent(getActivity(), OtherUserProfileActivity.class);
                intent.putExtra("user_id", listMyActivityFeed.get(position).getAuthor().getUser_id());
                break;

        }


        Pair<View, String> imagePair = Pair.create(clickedView, transitionName);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), imagePair);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
    class SharePost extends AsyncTask<String,String,String>{
        boolean success=false;
        Long post_id,user_id;
        String team_ids="";
        HashMap<String,String> params=new HashMap<>();
        public SharePost(Long post_id,Long user_id,ArrayList<Long>team_ids){
            this.post_id=post_id;
            this.user_id=user_id;
            for(int i=0;i<team_ids.size();i++){
                this.team_ids+=team_ids.get(i);
                if(i<team_ids.size()-1)
                    this.team_ids+=",";
            }
            params.put("user_id",String.valueOf(this.user_id));
            params.put("post_id",String.valueOf(this.post_id));
            params.put("teams",this.team_ids);

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(success)Toast.makeText(getContext(),R.string.post_share_successful,Toast.LENGTH_LONG).show();
            else Toast.makeText(getContext(),R.string.post_share_unsuccessful,Toast.LENGTH_LONG).show();


        }

        @Override
        protected String doInBackground(String... strings) {
            String url= getContext().getResources().getString(R.string.share_post);
            String response="";
            try {
                if(isAdded())
                    response= Server.performServerCall(url,params,"POST");
                else cancel(true);
                success=true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }
    }
    class DeleteMilestone extends AsyncTask<String, String, String> {
        boolean success = false;
        Long post_id;
        int position;
        HashMap<String,String>params=new HashMap<>();

        DeleteMilestone(Long post_id,int position) {
            this.post_id=post_id;
            this.position=position;
            params.put("post_id",String.valueOf(post_id));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (success) {
                Toast.makeText(getContext(), R.string.milestone_delete_success, Toast.LENGTH_LONG).show();
                listMyActivityFeed.remove(position);
                adapter.notifyItemRemoved(position+1);


            } else {
                Toast.makeText(getContext(), R.string.milestone_delete_unsuccess, Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String response = null;
            try {
                // new MultipartUtility().performMultipartServerCall(getResources().getString(R.string.add_post),form_params,file_params);
                Server.performServerCall(getResources().getString(R.string.delete_post), params, "POST");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    void share(Drawable drawable,String text) {


        Intent share = new Intent(Intent.ACTION_SEND);
        if(drawable!=null){
            Bitmap icon = ((BitmapDrawable)drawable).getBitmap();
            share.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "temporary_file.jpg");
            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            share.putExtra(Intent.EXTRA_STREAM,
                    Uri.parse("file:///sdcard/temporary_file.jpg"));
            share.putExtra(Intent.EXTRA_TEXT, text);


            startActivity(Intent.createChooser(share, "Share Image"));
        }

        else{
            share.setType("text/plain");

            share.putExtra(Intent.EXTRA_TEXT, text);


            startActivity(Intent.createChooser(share, "Share Post"));

        }

    }
    @Override
    public void onTeamCheckChange(CompoundButton compoundButton, boolean isChecked, int position) {
        if(hasPublic){
            if (position == 0 ) {
                //public
                if (isChecked) {
                    changeVisibilityOfRemainingItems(compoundButton, false);
                } else {
                    changeVisibilityOfRemainingItems(compoundButton, true);
                }

            } else {
                if (isAnyOneTeamChecked()) {
                    recyclerViewShareTeams.getLayoutManager().getChildAt(0).findViewById(R.id.checkbox_team).setEnabled(false);
                } else {
                    recyclerViewShareTeams.getLayoutManager().getChildAt(0).findViewById(R.id.checkbox_team).setEnabled(true);
                }
            }
        }


    }

    private boolean isAnyOneTeamChecked() {

        for (int i=1;i<=teams.size();i++) {
            CheckBox checkBoxTeam = (CheckBox) recyclerViewShareTeams.getLayoutManager().getChildAt(i).findViewById(R.id.checkbox_team);
            boolean isAnyTeamChecked = checkBoxTeam.isChecked();

            if (isAnyTeamChecked) {
                return isAnyTeamChecked;
            }

        }

        return false;
    }

    private void changeVisibilityOfRemainingItems(CompoundButton compoundButton, boolean shouldEnable) {

        for (int i=1;i<=teams.size();/*total teams*/i++) {
            View team = recyclerViewShareTeams.getLayoutManager().getChildAt(i);
            team.findViewById(R.id.checkbox_team).setEnabled(shouldEnable);
        }

    }

}
