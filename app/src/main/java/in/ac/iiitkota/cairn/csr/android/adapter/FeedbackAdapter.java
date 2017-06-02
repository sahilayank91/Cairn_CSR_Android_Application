package in.ac.iiitkota.cairn.csr.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.ac.iiitkota.cairn.csr.android.interfaces.RCVItemClickListener;
import in.ac.iiitkota.cairn.csr.android.model.Feedback;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import com.hsalf.smilerating.SmileRating;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import in.ac.iiitkota.cairn.csr.android.R;

/**
 * Created by SS Verma on 02-04-2017.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private Context context;
    private List<Feedback> listFeedback;
    private RCVItemClickListener rcvItemClickListener;

    public FeedbackAdapter(Context context, List<Feedback> listFeedback) {
        this.context = context;
        this.listFeedback = listFeedback;
    }

    public void setRcvItemClickListener(RCVItemClickListener rcvItemClickListener) {
        this.rcvItemClickListener = rcvItemClickListener;
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_feedback, parent, false);
        return new FeedbackViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final FeedbackViewHolder holder, int position) {
     final   Feedback current = listFeedback.get(position);
        if(current.getAdmin().getUser_id()== UserData.getInstance(context).getUser_id()) {
            holder.feedbackAuthorName.setText(current.getUser().getName());
            String profile_image_url = context.getResources().getString(R.string.profile_images) + current.getUser().getUser_id() + ".png";
            Picasso.with(context)
                    .load(profile_image_url).placeholder(R.drawable.profile_image_place_holder)
                    .into(holder.profileImage);
            holder.smileRatingBar.setSelectedSmile(2);
            holder.etFeedbackText.setText("");
            holder.etFeedbackText.setEnabled(true);
            holder.btnSubmit.setVisibility(View.VISIBLE);
            holder.btnSeeActivities.setVisibility(View.VISIBLE);
            holder.smileRatingBar.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
                @Override
                public void onSmileySelected(int smiley, boolean reselected) {
                        holder.smileRatingBar.setSelectedSmile(smiley);
                }
            });



        }
        else{
            holder.feedbackAuthorName.setText(current.getAdmin().getName());
            String profile_image_url = context.getResources().getString(R.string.profile_images) + current.getAdmin().getUser_id() + ".png";
            Picasso.with(context)
                    .load(profile_image_url).placeholder(R.drawable.profile_image_place_holder)
                    .into(holder.profileImage);
            holder.smileRatingBar.setSelectedSmile(current.getReaction(),true);


            holder.smileRatingBar.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
                @Override
                public void onSmileySelected(int smiley, boolean reselected) {
                    holder.smileRatingBar.setSelectedSmile(current.getReaction(),true);
                }
            });
            holder.etFeedbackText.setText(current.getComment());
            holder.etFeedbackText.setEnabled(false);
            holder.btnSubmit.setVisibility(View.GONE);
            holder.btnSeeActivities.setVisibility(View.GONE);


        }
        holder.feedbackTitle.setText(current.getText());
        holder.feedbackDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(current.getDate()));


    }

    @Override
    public int getItemCount() {
        return listFeedback.size();
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView profileImage;
        TextView feedbackAuthorName, feedbackDate, feedbackTitle;
        SmileRating smileRatingBar;
        Button btnSubmit;
        Button btnSeeActivities;
        EditText etFeedbackText;
        LinearLayout btnHolder;
        //

        public FeedbackViewHolder(View itemView) {
            super(itemView);

            profileImage = (ImageView) itemView.findViewById(R.id.feedback_profile_image);
            feedbackAuthorName = (TextView) itemView.findViewById(R.id.feedback_user_name);
            etFeedbackText = (EditText) itemView.findViewById(R.id.et_feedback); //hide this in user moder
            btnSubmit = (Button) itemView.findViewById(R.id.btn_submit_feedback);
            btnSeeActivities = (Button) itemView.findViewById(R.id.btn_see_activities); //hide this in user moder
            btnHolder = (LinearLayout) itemView.findViewById(R.id.btn_holder);
            feedbackDate = (TextView) itemView.findViewById(R.id.feedback_date);
            feedbackTitle = (TextView) itemView.findViewById(R.id.feedback_title);
            smileRatingBar = (SmileRating) itemView.findViewById(R.id.smile_rating);

            btnSubmit.setOnClickListener(this);
            btnSeeActivities.setOnClickListener(this);
            //
        }

        @Override
        public void onClick(View itemView) {
            if (rcvItemClickListener != null) {
                rcvItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }
}
