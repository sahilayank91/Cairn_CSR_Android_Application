package in.ac.iiitkota.cairn.csr.android.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
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
import in.ac.iiitkota.cairn.csr.android.ui.fragments.SmilesFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.List;

import in.ac.iiitkota.cairn.csr.android.ui.fragments.MyActivityFragment;
import me.relex.circleindicator.CircleIndicator;

import static android.view.View.GONE;

/**
 * Created by SS Verma on 25-03-2017.
 */

public class MyActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SmilesFragment week_fragment, month_fragment, year_fragment;
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEMS = 1;

    private Activity activityContext;
    private List<Post> listPosts;
    private RCVItemClickListener rcvItemClickListener;

    public MyActivityAdapter(Activity context, List<Post> listPosts) {
        this.activityContext = context;
        this.listPosts = listPosts;
    }

    public void setRcvItemClickListener(RCVItemClickListener rcvItemClickListener) {
        this.rcvItemClickListener = rcvItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView;

        if (viewType == VIEW_TYPE_HEADER) {

            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_header_my_activity, parent, false);
            return new HeaderViewHolder(rootView);

        } else {

            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_newsfeed, parent, false);
            return new ItemViewHolder(rootView);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof HeaderViewHolder) {
            //header
            //handle graph
        } else {
            //items
            final ItemViewHolder holder = (ItemViewHolder) viewHolder;
            final Post current = MyActivityFragment.listMyActivityFeed.get(position - 1);

            holder.authorName.setText(current.getAuthor().getName());
            holder.newsFeedDescription.setText(current.getText());
            if(current.getSmiles()>0)holder.title.setText("touched "+current.getSmiles()+" lives");
            else holder.title.setText(R.string.post_updated);

            holder.newsFeedDate.setText(DateUtils.getRelativeTimeSpanString(current.getTime(), new java.util.Date().getTime(), DateUtils.FORMAT_ABBREV_RELATIVE));
            if (current.getImages() != null && current.getImages().size() > 0) {
                holder.newsFeedImage.setVisibility(View.VISIBLE);
                String newsfeed_image_url = AppSingleton.getInstance().getApplicationContext().getResources().getString(R.string.images) + current.getImages().get(0).getFilename();

                Picasso.with(activityContext)
                        .load(newsfeed_image_url)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                if (bitmap == null) {
                                    return;
                                }

                                holder.newsFeedImage.setImageBitmap(bitmap);

                                Palette.from(bitmap)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(Palette palette) {
                                                Palette.Swatch swatch = palette.getDominantSwatch();
                                                if (swatch == null) {
                                                    //Toast.makeText(context, "Null swatch :(", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                holder.feedImageHolder.setBackgroundColor(swatch.getRgb());
                                            }
                                        });
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
            }
            else{
                holder.newsFeedImage.setVisibility(GONE);
            }

            String profile_image_url = AppSingleton.getInstance().getApplicationContext().getResources().getString(R.string.profile_images) + current.getAuthor().getUser_id() + ".png";
            Picasso.with(activityContext)
                    .load(profile_image_url)
                    .into(holder.profileImage);

            holder.total_likes.setText(String.valueOf(current.getLikes_count()) + " likes");
            holder.total_comments.setText(String.valueOf(current.getComments_count()) + " comments");

            final String post_id = String.valueOf(current.getPost_id());

            boolean post_liked = false;
            for (User user : current.getLikers()) {
                if (user.getUser_id() == UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser_id())
                    post_liked = true;
            }
            if (!post_liked) {
                holder.iconLike.setImageResource(R.drawable.ic_like_grey_24dp);
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
                        holder.total_likes.setText(String.valueOf(Long.valueOf(holder.total_likes.getText().toString().substring(0, 1)) + 1) + " likes");
                        holder.iconLike.setImageResource(R.drawable.ic_like_black_24dp);
                        holder.likeLabel.setTextColor(Color.BLACK);
                        current.getLikers().add(UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser());
                    } else {
                        params.put("set_like", "0");
                        holder.total_likes.setText(String.valueOf(Long.valueOf(holder.total_likes.getText().toString().substring(0, 1)) - 1) + " likes");
                        holder.iconLike.setImageResource(R.drawable.ic_like_grey_24dp);
                        holder.likeLabel.setTextColor(Color.GRAY);
                        current.getLikers().remove(UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser());
                    }

                    Toast.makeText(AppSingleton.getInstance().getApplicationContext(), "You liked this post!", Toast.LENGTH_LONG).show();
                    new NewsFeedAdapter.SetLike().execute(params);

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return listPosts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }

        return VIEW_TYPE_ITEMS;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
            StatisticsSectionPagerAdapter sectionsPagerAdapter = new StatisticsSectionPagerAdapter(activityContext.getFragmentManager(), "user", UserData.getInstance(activityContext).getUser_id());
//
            ViewPager viewPager = (ViewPager) itemView.findViewById(R.id.viewpager_my_activity);
            CircleIndicator indicator = (CircleIndicator) itemView.findViewById(R.id.indicator);
            viewPager.setAdapter(sectionsPagerAdapter);
            indicator.setViewPager(viewPager);

        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView authorName, title, newsFeedDescription, newsFeedDate;
        ImageView profileImage;
        ImageView newsFeedImage;
        LinearLayout likeHolder;
        LinearLayout commentHolder;
        LinearLayout shareHolder;
        TextView total_likes, total_comments;

        FrameLayout feedImageHolder;
        TextView likeLabel;

        ImageView iconLike;
        ImageView iconComment;
        ImageView iconShare;

        ImageView overflowMenu;
        TextView newsfeedTotalLikes;

        public ItemViewHolder(View itemView) {
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

            overflowMenu = (ImageView) itemView.findViewById(R.id.newsfeed_overflow_menu);
            newsfeedTotalLikes = (TextView) itemView.findViewById(R.id.newsfeed_total_likes);

            newsFeedImage.setOnClickListener(this);
            commentHolder.setOnClickListener(this);
            shareHolder.setOnClickListener(this);
            profileImage.setOnClickListener(this);
            overflowMenu.setOnClickListener(this);
            newsfeedTotalLikes.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (rcvItemClickListener != null) {
                rcvItemClickListener.onItemClick(v, getAdapterPosition() -1);
            }
        }
    }


}
