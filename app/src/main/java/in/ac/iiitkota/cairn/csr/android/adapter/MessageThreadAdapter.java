package in.ac.iiitkota.cairn.csr.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.interfaces.RCVItemClickListener;
import in.ac.iiitkota.cairn.csr.android.model.Team;
import in.ac.iiitkota.cairn.csr.android.model.User;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SS Verma on 15-03-2017.
 */

public class MessageThreadAdapter extends RecyclerView.Adapter<MessageThreadAdapter.MessageThreadViewHolder> {

    private Context context;
    private List<Team> teams;
    private RCVItemClickListener rcvItemClickListener;

    public MessageThreadAdapter(Context context, List<Team> listMessageThread) {
        this.context = context;
        this.teams = listMessageThread;
    }

    public void setRcvItemClickListener(RCVItemClickListener rcvItemClickListener) {
        this.rcvItemClickListener = rcvItemClickListener;
    }

    @Override
    public MessageThreadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_message_thread, parent, false);
        return new MessageThreadViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MessageThreadViewHolder holder, int position) {
        Team current = teams.get(position);
        holder.description.setText(current.getDescription());
        holder.name.setText(current.getName());
        String members = "";

        Iterator<User> member_iterator = current.getMembers().iterator();
        while (member_iterator.hasNext()) {
            members += member_iterator.next().getName();
            if (member_iterator.hasNext()) members += ", ";
        }
        holder.members.setText(members);

        holder.teamImage.setBackgroundResource(pickBackground(position));

    }

    public static int pickBackground(int position) {

        if (position % 5 == 1) {
            return R.drawable.custom_circle_shape_one;
        }

        if (position % 5 == 2) {
            return R.drawable.custom_circle_shape_two;
        }

        if (position % 5 == 3) {
            return R.drawable.custom_circle_shape_three;
        }

        if (position % 5 == 4) {
            return R.drawable.custom_circle_shape_four;
        }

        if (position % 5 == 0) {
            return R.drawable.custom_circle_shape_five;
        }

        return R.drawable.custom_circle_shape_five;
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class MessageThreadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView teamImage;
        TextView description;
        TextView name;
        TextView members;


        LinearLayout dataHolder;

        public MessageThreadViewHolder(View itemView) {
            super(itemView);

            teamImage = (ImageView) itemView.findViewById(R.id.thread_image);
            description = (TextView) itemView.findViewById(R.id.team_description);
            name = (TextView) itemView.findViewById(R.id.thread_title);
            members = (TextView) itemView.findViewById(R.id.team_members);

            dataHolder = (LinearLayout) itemView.findViewById(R.id.data_holder);
            dataHolder.setOnClickListener(this);
            teamImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (rcvItemClickListener != null) {
                rcvItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
