package in.ac.iiitkota.cairn.csr.android.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.adapter.CommentsAdapter;
import in.ac.iiitkota.cairn.csr.android.adapter.ShareWithAdapter;
import in.ac.iiitkota.cairn.csr.android.interfaces.CheckboxCheckChangeListener;
import in.ac.iiitkota.cairn.csr.android.interfaces.RCVItemClickListener;
import in.ac.iiitkota.cairn.csr.android.model.Comment;
import in.ac.iiitkota.cairn.csr.android.model.Post;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.MyActivityFragment;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.NewsFeedFragment;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;

import static in.ac.iiitkota.cairn.csr.android.ui.fragments.NewsFeedFragment.prepareTeamList;
import static in.ac.iiitkota.cairn.csr.android.ui.fragments.NewsFeedFragment.teams;

public class CommentDetailsActivity extends AppCompatActivity implements RCVItemClickListener,CheckboxCheckChangeListener {

    private RecyclerView recyclerView;
    private CommentsAdapter adapter;

    private Post post;
    private List<Comment> listComments;
    private EditText etComment;
    private ImageView iconPublishComment;
    ProgressBar progressBar;
    private RecyclerView recyclerViewShareTeams;
    ShareWithAdapter share_with_adapter;
    boolean hasPublic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_details);

        listComments = new ArrayList<>();
        hasPublic= UserData.getInstance(getApplicationContext()).getUser().getAccount_level()>0;
        int pos = getIntent().getExtras().getInt("position");
        if(getIntent().getExtras().getBoolean("my_activity",false)){
            post = MyActivityFragment.listMyActivityFeed.get(pos);
        }
        else{
            post = NewsFeedFragment.listNewsFeed.get(pos);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        etComment = (EditText) findViewById(R.id.comment_edit_text);

        iconPublishComment = (ImageView) findViewById(R.id.comment_publish_icon);

        recyclerView = (RecyclerView) findViewById(R.id.comment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentsAdapter(this, post, listComments);
        recyclerView.setAdapter(adapter);
        adapter.setRcvItemClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new GetComments().execute();
        iconPublishComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etComment.getText().toString().equals(""))
                    new AddComment().execute();
            }
        });

    }


    class GetComments extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            iconPublishComment.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            iconPublishComment.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            try {
                JSONArray jsonArray = new JSONArray(s);
                listComments.clear();

                for (int i = 0; i < jsonArray.length(); i++) {

                    Comment comment = new Comment(jsonArray.getJSONObject(i));
                    listComments.add(comment);
                    adapter.notifyItemInserted(i);
                }

                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(listComments.size());


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error loading page!Are you offline?", Toast.LENGTH_LONG).show();
                finish();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result = Server.performServerCall(getResources().getString(R.string.retrieve_comments) + post.getPost_id(), "GET");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        switch (view.getId()) {
            case R.id.newsfeed_image:
                //
                break;
            case R.id.newsfeed_like_icon:
                //
                break;
            case R.id.newsfeed_comment_icon:
                //
                break;
            case R.id.news_feed_share_icon_holder:
               popupShareDialog(post.getPost_id());

                break;
            case R.id.comment_holder:
                if(listComments.get(position-1).getAuthor().getUser_id()==UserData.getInstance(getApplicationContext()).getUser_id()){
                    popupCommentOverflowMenu(position-1,view);
                }


                break;
            case R.id.newsfeed_overflow_menu:
                popupOverflowMenu(post,view);
                break;

        }

    }
    private void popupOverflowMenu(final Post post, View clickedView) {

        //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
        PopupMenu popupMenu = new PopupMenu(this, clickedView);

        if(post.getAuthor().getUser_id()==UserData.getInstance(this).getUser().getUser_id()){
            popupMenu.getMenu().add(Menu.NONE, 1,1, "Edit");
            popupMenu.getMenu().add(Menu.NONE, 2,2,"Delete");
        }

        popupMenu.getMenu().add(Menu.NONE, 3,3,"Share on Social Media");

        popupMenu.show();


//        popupMenu.getMenuInflater().inflate(R.menu.overflow_menu_newsfeed, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

//                Toast.makeText(getActivity(), "Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                switch (item.getItemId()){
                    case 1://edit
                        Intent intent=new Intent(getApplicationContext(), NewMilestoneActivity.class);
                        intent.putExtra("edit",true);
                        intent.putExtra("edit_title",post.getText());
                        intent.putExtra("edit_lives",post.getSmiles());
                        if(post.getImages()!=null &&post.getImages().size()>0)
                            intent.putExtra("edit_image",post.getImages().get(0).getFilename());
                        intent.putExtra("edit_post_id",post.getPost_id());
                        startActivity(intent);
                        break;
                    case 2:
                        new DeleteMilestone(post.getPost_id()).execute();
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
                        if(post.getImages()!=null&&post.getImages().size()>0)
                            try{
                                ImageView    image    = (ImageView) recyclerView.findViewHolderForLayoutPosition(0).itemView.findViewById(R.id.newsfeed_image);
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

    private void popupCommentOverflowMenu(final int position, View clickedView) {

        PopupMenu popupMenu = new PopupMenu(this, clickedView);
        //popupMenu.getMenu().add(Menu.NONE, 3, 3, "Delete Comment");
        popupMenu.getMenuInflater().inflate(R.menu.overflow_menu_comment_press, popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.action_delete_comment) {
                    showAlertDialog(position);
                }

                return true;
            }
        });



        popupMenu.show();

}

    private void showAlertDialog(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Delete);
        builder.setMessage(R.string.sure);

        builder.setPositiveButton(R.string.Delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new DeleteComment(listComments.get(position).getComment_id(),post.getPost_id(),position).execute();
                new GetComments().execute();

            }
        });

        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    class AddComment extends AsyncTask<String, String, String> {
        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.put("post_id",String.valueOf(post.getPost_id()));
            params.put("comment_text",etComment.getText().toString());
            params.put("comment_author",String.valueOf(UserData.getInstance(getApplicationContext()).getUser_id()));
            iconPublishComment.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            etComment.setText("");
            Toast.makeText(getApplicationContext(), R.string.comment_add, Toast.LENGTH_SHORT).show();
            new GetComments().execute();
            iconPublishComment.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Server.performServerCall(getResources().getString(R.string.add_comment), params, "POST");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    class DeleteComment extends AsyncTask<String, String, String> {
        HashMap<String, String> params = new HashMap<>();
        Long comment_id,post_id;
        boolean success=false;
        boolean connected=true;
        int position;
        public DeleteComment(Long comment_id,Long post_id,int position){
            this.comment_id=comment_id;
            this.post_id=post_id;
            this.position=position;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.put("post_id",String.valueOf(post_id));
            params.put("comment_id",String.valueOf(comment_id));

            iconPublishComment.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            etComment.setText("");
            if(connected){
                if(success){
                    listComments.remove(position);
                    adapter.notifyItemRemoved(position+1);
                    Toast.makeText(getApplicationContext(), R.string.comment_delete, Toast.LENGTH_SHORT).show();

                }
                else Toast.makeText(getApplicationContext(), R.string.error_deleting_comment, Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getApplicationContext(), R.string.offline, Toast.LENGTH_SHORT).show();


            iconPublishComment.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Server.performServerCall(getResources().getString(R.string.delete_comment), params, "POST");
                success=true;
            } catch (IOException e) {
                e.printStackTrace();
                connected=false;
            }
            return null;
        }
    }
    private void popupShareDialog(final Long post_id) {

        final Dialog dialog = new Dialog(this,R.style.AppTheme_CustomDialog);
        dialog.setTitle("Share with ... ");
        dialog.setContentView(R.layout.dialog_share);


        recyclerViewShareTeams = (RecyclerView) dialog.findViewById(R.id.recycler_view_share_with);
        recyclerViewShareTeams.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final boolean hasPublic=UserData.getInstance(getApplicationContext()).getUser().getAccount_level()>0;
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

                    CheckBox checkbox=(CheckBox)  recyclerViewShareTeams.getLayoutManager().getChildAt(i).findViewById(R.id.checkbox_team);
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

                new SharePost(post_id,UserData.getInstance(getApplicationContext()).getUser_id(),share_teams).execute();
                dialog.dismiss();
            }
        });

        dialog.show();

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
            if(success)Toast.makeText(getApplicationContext(),R.string.post_share_successful,Toast.LENGTH_LONG).show();
            else Toast.makeText(getApplicationContext(),R.string.post_share_unsuccessful,Toast.LENGTH_LONG).show();


        }

        @Override
        protected String doInBackground(String... strings) {
            String url= getApplicationContext().getResources().getString(R.string.share_post);
            String response="";
            try {

                    response= Server.performServerCall(url,params,"POST");

                success=true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
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

    class DeleteMilestone extends AsyncTask<String, String, String> {
        boolean success = false;
        Long post_id;

        HashMap<String,String>params=new HashMap<>();

        DeleteMilestone(Long post_id) {
            this.post_id=post_id;

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
                Toast.makeText(getApplicationContext(),R.string.milestone_delete_success, Toast.LENGTH_LONG).show();
               finish();


            } else {
                Toast.makeText(getApplicationContext(), R.string.milestone_delete_unsuccess, Toast.LENGTH_LONG).show();
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


            startActivity(Intent.createChooser(share, getResources().getString(R.string.share_image)));
        }

        else{
            share.setType("text/plain");

            share.putExtra(Intent.EXTRA_TEXT, text);


            startActivity(Intent.createChooser(share, getResources().getString(R.string.share_post)));

        }

    }

}
