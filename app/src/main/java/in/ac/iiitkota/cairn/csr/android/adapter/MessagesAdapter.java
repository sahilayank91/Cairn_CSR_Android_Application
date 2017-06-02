package in.ac.iiitkota.cairn.csr.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.ac.iiitkota.cairn.csr.android.AppSingleton;
import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.model.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by SS Verma on 17-03-2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private Context context;
    private List<Message> listMessages;

    public MessagesAdapter(Context context, List<Message> listMessages) {
        this.context = context;
        this.listMessages = listMessages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_message, parent, false);
        return new MessageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message current = listMessages.get(position);
        holder.messageText.setText(current.getText());
        holder.messageSender.setText(current.getAuthor().getName());
        holder.messageDate.setText(DateUtils.getRelativeTimeSpanString(current.getTime(),new java.util.Date().getTime(),DateUtils.FORMAT_ABBREV_RELATIVE));
        String profile_image_url= AppSingleton.getInstance().getApplicationContext().getResources().getString(R.string.profile_images)+current.getAuthor().getUser_id()+".png";
        Picasso.with(context)
                .load(profile_image_url)
                .placeholder(R.drawable.profile_image_place_holder)
                .into(holder.senderProfilePic);       //
    }

    @Override
    public int getItemCount() {
        return listMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageSender;
        TextView messageText;
        ImageView senderProfilePic;
        TextView messageDate;

        public MessageViewHolder(View itemView) {
            super(itemView);

            messageSender = (TextView) itemView.findViewById(R.id.message_sender);
            messageText = (TextView) itemView.findViewById(R.id.message_text);
            senderProfilePic = (ImageView) itemView.findViewById(R.id.message_profile_image);
            messageDate = (TextView) itemView.findViewById(R.id.message_date);
        }
    }

}
