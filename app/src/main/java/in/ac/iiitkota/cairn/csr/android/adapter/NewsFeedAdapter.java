package in.ac.iiitkota.cairn.csr.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.ac.iiitkota.cairn.csr.android.AppSingleton;
import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.interfaces.RCVItemClickListener;
import in.ac.iiitkota.cairn.csr.android.model.Post;
import in.ac.iiitkota.cairn.csr.android.model.User;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SS Verma on 14-03-2017.
 */

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder> {

    private Context context;
    private ArrayList<Post> listNewsFeed;
    private RCVItemClickListener rcvItemClickListener;

    public NewsFeedAdapter(Context context, ArrayList<Post> listNewsFeed) {
        this.context = context;
        this.listNewsFeed = listNewsFeed;
    }

    public void setRcvItemClickListener(RCVItemClickListener rcvItemClickListener) {
        this.rcvItemClickListener = rcvItemClickListener;
    }

    @Override
    public NewsFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_newsfeed, parent, false);
        return new NewsFeedViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final NewsFeedViewHolder holder, int position) {
        final Post current = listNewsFeed.get(position);
        Context image_context = holder.newsFeedImage.getContext();
        holder.authorName.setText(current.getAuthor().getName());

        holder.newsFeedDescription.setText(current.getText());

        holder.newsFeedDate.setText(DateUtils.getRelativeTimeSpanString(current.getTime(), new java.util.Date().getTime(), DateUtils.FORMAT_ABBREV_RELATIVE));
        if (current.getImages() != null && current.getImages().size() > 0) {
            holder.newsFeedImage.setVisibility(View.VISIBLE);
            String newsfeed_image_url = AppSingleton.getInstance().getApplicationContext().getResources().getString(R.string.images) + current.getImages().get(0).getFilename();
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder().header("Cache-Control", "max-age=" + (60 * 60 * 24 * 365)).build();
                }
            });

            okHttpClient.setCache(new Cache(context.getCacheDir(), Integer.MAX_VALUE));
            OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
            Picasso picasso = new Picasso.Builder(image_context).downloader(okHttpDownloader).build();


            picasso.load(newsfeed_image_url).into(holder.newsFeedImage);
//                    .into(new Target() {
//                        @Override
//                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//                            if (bitmap == null) {
//                                return;
//                            }
//
//                            holder.newsFeedImage.setImageBitmap(bitmap);
//
//                            Palette.from(bitmap)
//                                    .generate(new Palette.PaletteAsyncListener() {
//                                        @Override
//                                        public void onGenerated(Palette palette) {
//                                            Palette.Swatch swatch = palette.getDominantSwatch();
//                                            if (swatch == null) {
//                                                //Toast.makeText(context, "Null swatch :(", Toast.LENGTH_SHORT).show();
//                                                return;
//                                            }
//                                            holder.feedImageHolder.setBackgroundColor(swatch.getRgb());
//                                        }
//                                    });
//                        }
//
//                        @Override
//                        public void onBitmapFailed(Drawable errorDrawable) {
//
//                        }
//
//                        @Override
//                        public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                        }
//                    });

        }
        else holder.newsFeedImage.setVisibility(View.GONE);

        String profile_image_url = AppSingleton.getInstance().getApplicationContext().getResources().getString(R.string.profile_images) + current.getAuthor().getUser_id() + ".png";
        Picasso.with(context)
                .load(profile_image_url).placeholder(R.drawable.profile_image_place_holder)
                .into(holder.profileImage);
        if (current.getSmiles() > 1)
            holder.title.setText("made " + current.getSmiles() + " people smile");
        else if (current.getSmiles() == 1)
            holder.title.setText(R.string.made_smile);
        else
            holder.title.setText(R.string.post_updated);


        if (current.getComments_count() == 1)
            holder.total_comments.setText(R.string.one_comment);
        else if (current.getComments_count() > 1) {
            holder.total_comments.setText(current.getComments_count() + " comments");
        } else holder.total_comments.setText("");
        if (current.getLikes_count() == 1)
            holder.total_likes.setText(R.string.one_like);
        else if (current.getLikes_count() > 1) {
            holder.total_likes.setText(current.getLikes_count() + " likes");
        } else holder.total_likes.setText("");
        final String post_id = String.valueOf(current.getPost_id());

        boolean post_liked = false;
        for (User user : current.getLikers()) {
            if (user.getUser_id() == UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser_id())
                post_liked = true;
        }
        if (!post_liked) {
            holder.iconLike.setImageResource(R.drawable.ic_like_grey_24dp);
        }
        else {
            holder.iconLike.setImageResource(R.drawable.ic_like_black_24dp);
        }

        holder.likeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser_id()));
                params.put("post_id", post_id);
                boolean liked = false;
                for (User user : current.getLikers()) {
                    if (user.getUser_id() == UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser_id()) {
                        liked = true;

                    }

                }

                if (!liked) {

                    params.put("set_like", "1");
                    holder.total_likes.setText((current.getLikes_count()+1)+ " likes");
                    holder.iconLike.setImageResource(R.drawable.ic_like_black_24dp);
                    holder.likeLabel.setTextColor(Color.BLACK);
                    current.getLikers().add(UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser());
                    current.setLikes_count(current.getLikes_count()+1);
                    Toast.makeText(AppSingleton.getInstance().getApplicationContext(), "You liked this post!", Toast.LENGTH_SHORT).show();
                } else {
                    params.put("set_like", "0");
                    holder.total_likes.setText((current.getLikes_count()-1) + " likes");
                    current.setLikes_count(current.getLikes_count()-1);
                    holder.iconLike.setImageResource(R.drawable.ic_like_grey_24dp);
                    holder.likeLabel.setTextColor(Color.GRAY);
                    current.getLikers().remove(UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser());
                }


                new SetLike().execute(params);

            }
        });

                if(current.getAuthor().getUser_id()==UserData.getInstance(context).getUser_id()){
                 //   holder.edit.setVisibility(View.VISIBLE);
                    holder.authorName.setText("You");
                }
                else{
                    //holder.edit.setVisibility(View.GONE);
                }


    }

    @Override
    public int getItemCount() {
        return listNewsFeed.size();
    }

    public class NewsFeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView authorName, title, newsFeedDescription, newsFeedDate;
        ImageView profileImage;
        ImageView newsFeedImage;
        LinearLayout likeHolder;
        LinearLayout commentHolder;
        LinearLayout shareHolder;
        TextView total_likes, total_comments;

        ImageView iconLike;
        ImageView iconComment;
        ImageView iconShare;

        FrameLayout feedImageHolder;
        TextView likeLabel;


        ImageView newsfeedOverflowMenu;

        public NewsFeedViewHolder(View itemView) {
            super(itemView);

            authorName = (TextView) itemView.findViewById(R.id.newsfeed_author_name);
            title = (TextView) itemView.findViewById(R.id.newsfeed_title);
            newsFeedDate = (TextView) itemView.findViewById(R.id.newsfeed_date);
            profileImage = (ImageView) itemView.findViewById(R.id.newsfeed_profile_image);
            newsFeedImage = (ImageView) itemView.findViewById(R.id.newsfeed_image);
            newsFeedDescription = (TextView) itemView.findViewById(R.id.newsfeed_description);
            likeHolder = (LinearLayout) itemView.findViewById(R.id.news_feed_like_icon_holder);

            iconLike = (ImageView) itemView.findViewById(R.id.newsfeed_like_icon);
            iconComment = (ImageView) itemView.findViewById(R.id.newsfeed_comment_icon);
            iconShare = (ImageView) itemView.findViewById(R.id.newsfeed_share_icon);

            feedImageHolder = (FrameLayout) itemView.findViewById(R.id.newsfeed_image_holder);
            likeLabel = (TextView) itemView.findViewById(R.id.newsfeed_like_label);

            commentHolder = (LinearLayout) itemView.findViewById(R.id.news_feed_comment_icon_holder);
            shareHolder = (LinearLayout) itemView.findViewById(R.id.news_feed_share_icon_holder);
            total_likes = (TextView) itemView.findViewById(R.id.newsfeed_total_likes);
            total_comments = (TextView) itemView.findViewById(R.id.newsfeed_total_comments);

            //edit=(ImageButton)itemView.findViewById(R.id.newsfeed_edit);
            newsFeedImage.setOnClickListener(this);

            total_likes = (TextView) itemView.findViewById(R.id.newsfeed_total_likes);
            total_comments = (TextView) itemView.findViewById(R.id.newsfeed_total_comments);
            newsfeedOverflowMenu = (ImageView) itemView.findViewById(R.id.newsfeed_overflow_menu);

            likeHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            commentHolder.setOnClickListener(this);
            shareHolder.setOnClickListener(this);
            profileImage.setOnClickListener(this);
            total_likes.setOnClickListener(this);
            total_comments.setOnClickListener(this);
            newsfeedOverflowMenu.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (rcvItemClickListener != null) {
                rcvItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    static class SetLike extends AsyncTask<HashMap<String, String>, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            try {
                Server.performServerCall(AppSingleton.getInstance().getResources().getString(R.string.set_like), params[0], "POST");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
