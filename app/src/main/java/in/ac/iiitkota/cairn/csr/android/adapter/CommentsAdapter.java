package in.ac.iiitkota.cairn.csr.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.ac.iiitkota.cairn.csr.android.AppSingleton;
import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.interfaces.RCVItemClickListener;
import in.ac.iiitkota.cairn.csr.android.model.Comment;
import in.ac.iiitkota.cairn.csr.android.model.Post;
import in.ac.iiitkota.cairn.csr.android.model.User;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.ui.NewsFeedDetailImage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by SS Verma on 17-03-2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_HEADER = 0;
    private final int VIEW_TYPE_COMMENTS = 1;

    private Context context;
    private List<Comment> listComments;
    private RCVItemClickListener rcvItemClickListener;
    private Post post;

    public CommentsAdapter(Context context, Post post, List<Comment> listComments) {
        this.context = context;
        this.listComments = listComments;
        this.post = post;
    }

    public void setRcvItemClickListener(RCVItemClickListener rcvItemClickListener) {
        this.rcvItemClickListener = rcvItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;

        if (viewType == 0) {
            //header
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_newsfeed, parent, false);
            holder = new HeaderViewHolder(rootView);
        } else {
            //comments
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_comment, parent, false);
            holder = new CommentViewHolder(rootView);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {

            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.authorName.setText(post.getAuthor().getName());
            if(post.getSmiles()<1)headerViewHolder.title.setText(R.string.post_updated);
           else  headerViewHolder.title.setText("touched "+post.getSmiles()+" lives!");
            headerViewHolder.newsFeedDescription.setText(post.getText());
            headerViewHolder.newsFeedDate.setText(DateUtils.getRelativeTimeSpanString(post.getTime(),new java.util.Date().getTime(),DateUtils.FORMAT_ABBREV_RELATIVE));
            headerViewHolder.comment_view_holder.setVisibility(View.INVISIBLE);
            String profile_image_url = context.getResources().getString(R.string.profile_images) + post.getAuthor().getUser_id() + ".png";
            Picasso.with(context)
                    .load(profile_image_url).placeholder(R.drawable.profile_image_place_holder)
                    .into(headerViewHolder.profileImage);

            if(post.getComments_count()==1)
                headerViewHolder.total_comments.setText(R.string.one_comment);
            else if(post.getComments_count()>1){
                headerViewHolder.total_comments.setText(post.getComments_count()+" comments");
            }
            else headerViewHolder.total_comments.setText("");

            if(post.getLikes_count()==1)
                headerViewHolder.total_likes.setText(R.string.one_like);
            else if(post.getLikes_count()>1){
                headerViewHolder.total_likes.setText(post.getLikes_count()+" likes");
            }
            else headerViewHolder.total_likes.setText("");

            if(post.getImages()!=null && post.getImages().size()>0){
                String newsfeed_image_url=AppSingleton.getInstance().getApplicationContext().getResources().getString(R.string.images)+post.getImages().get(0).getFilename();
                Picasso.with(context)
                        .load(newsfeed_image_url)

                        .into(headerViewHolder.newsFeedImage);
                headerViewHolder.newsFeedImage.setVisibility(View.VISIBLE);
            }
            else{
                headerViewHolder.newsFeedImage.setVisibility(View.GONE);
            }
            //handle image
            ((HeaderViewHolder) holder).newsFeedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewsFeedDetailImage.class);
                    intent.putExtra("image",post.getImages().get(0).getFilename());
                    intent.putExtra("caption",post.getImages().get(0).getCaption());
                }
            });

            //like button
             boolean post_liked = false;
            for (User user : post.getLikers()) {
                if (user.getUser_id() == UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser_id())
                    post_liked = true;
            }
            if (!post_liked) {
                ((HeaderViewHolder) holder).likeLabel.setTextColor(Color.GRAY);
                ((HeaderViewHolder) holder).iconLike.setImageResource(R.drawable.ic_like_grey_24dp);
            }

            ((HeaderViewHolder) holder).iconLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HashMap<String, String> params = new HashMap<>();
                    params.put("user_id", String.valueOf(UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser_id()));
                    params.put("post_id", String.valueOf(post.getPost_id()));
                    boolean liked = false;
                    for (User user : post.getLikers()) {
                        if (user.getUser_id() == UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser_id()) {
                            liked = true;

                        }


                    }
                    if (!liked) {

                        params.put("set_like", "1");
                        ((HeaderViewHolder) holder).total_likes.setText(String.valueOf(Long.valueOf(post.getLikes_count()+1)+ " likes"));
                        post.setLikes_count(post.getLikes_count()+1);
                        ((HeaderViewHolder) holder).iconLike.setImageResource(R.drawable.ic_like_black_24dp);
                       ((HeaderViewHolder) holder).likeLabel.setTextColor(Color.BLACK);
                        post.getLikers().add(UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser());
                        Toast.makeText(AppSingleton.getInstance().getApplicationContext(), "You liked this post!", Toast.LENGTH_SHORT).show();
                    } else {
                        params.put("set_like", "0");

                        ((HeaderViewHolder) holder).total_likes.setText(String.valueOf(Long.valueOf(post.getLikes_count() - 1) + " likes"));
                        post.setLikes_count(post.getLikes_count()-1);
                        ((HeaderViewHolder) holder).iconLike.setImageResource(R.drawable.ic_like_grey_24dp);
                        ((HeaderViewHolder) holder).likeLabel.setTextColor(Color.GRAY);
                        post.getLikers().remove(UserData.getInstance(AppSingleton.getInstance().getApplicationContext()).getUser());
                    }


                    new NewsFeedAdapter.SetLike().execute(params);

                }
            });



        } else {

            Comment current = listComments.get(position -1);

            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
            commentViewHolder.commentPublisher.setText(current.getAuthor().getName());
            commentViewHolder.commentText.setText(current.getText());
            commentViewHolder.commentDateTime.setText(DateUtils.getRelativeTimeSpanString(current.getTime(),new java.util.Date().getTime(),DateUtils.FORMAT_ABBREV_RELATIVE));
            String profile_image_url= AppSingleton.getInstance().getApplicationContext().getResources().getString(R.string.profile_images)+current.getAuthor().getUser_id()+".png";
            Picasso.with(context)
                    .load(profile_image_url)
                    .placeholder(R.drawable.profile_image_place_holder)
                    .into(commentViewHolder.commentPublisherImage);

        }

    }

    @Override
    public int getItemCount() {
        return listComments.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }

        return VIEW_TYPE_COMMENTS;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView authorName;
        TextView title;
        ImageView profileImage;
        ImageView newsFeedImage;
        TextView newsFeedDescription;
        ImageView iconLike;
        ImageView iconComment;
        LinearLayout iconShare;
        TextView newsFeedDate;
        TextView total_likes;
        TextView total_comments;
        TextView likeLabel;
        LinearLayout comment_view_holder;
        ImageView over_flow_menu;

        public HeaderViewHolder(View rootView) {
            super(rootView);

            authorName = (TextView) itemView.findViewById(R.id.newsfeed_author_name);
            title = (TextView) itemView.findViewById(R.id.newsfeed_title);
            profileImage = (ImageView) itemView.findViewById(R.id.newsfeed_profile_image);
            newsFeedImage = (ImageView) itemView.findViewById(R.id.newsfeed_image);
            newsFeedDescription = (TextView) itemView.findViewById(R.id.newsfeed_description);
            iconLike = (ImageView) itemView.findViewById(R.id.newsfeed_like_icon);
            iconComment = (ImageView) itemView.findViewById(R.id.newsfeed_comment_icon);
            iconShare = (LinearLayout) itemView.findViewById(R.id.news_feed_share_icon_holder);
            newsFeedDate=(TextView)itemView.findViewById(R.id.newsfeed_date);
            total_comments=(TextView)itemView.findViewById(R.id.newsfeed_total_comments);
            total_likes=(TextView)itemView.findViewById(R.id.newsfeed_total_likes);
            likeLabel=(TextView)itemView.findViewById(R.id.newsfeed_like_label);
            comment_view_holder=(LinearLayout)itemView.findViewById(R.id.news_feed_comment_icon_holder);
            over_flow_menu=(ImageView)itemView.findViewById(R.id.newsfeed_overflow_menu);
            newsFeedImage.setOnClickListener(this);
//            iconLike.setOnClickListener(this);
//            iconComment.setOnClickListener(this);
            iconShare.setOnClickListener(this);
            over_flow_menu.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (rcvItemClickListener != null) {
                rcvItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView commentPublisherImage;
        TextView commentPublisher;
        TextView commentText;
        TextView commentDateTime;

        LinearLayout commentHolder;

        public CommentViewHolder(View rootView) {
            super(rootView);

            commentPublisherImage = (ImageView) itemView.findViewById(R.id.comment_publisher_image);
            commentPublisher = (TextView) itemView.findViewById(R.id.comment_publisher);
            commentText = (TextView) itemView.findViewById(R.id.comment_text);
            commentDateTime = (TextView) itemView.findViewById(R.id.comment_date_time);
            commentHolder = (LinearLayout) itemView.findViewById(R.id.comment_holder);

            commentHolder.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (rcvItemClickListener != null) {
                rcvItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
