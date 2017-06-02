package in.ac.iiitkota.cairn.csr.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SS Verma on 16-04-2017.
 */

public class AllLikesAdapter extends RecyclerView.Adapter<AllLikesAdapter.ViewHolder> {

    private Context context;
    private List<User> listUsers;

    public AllLikesAdapter(Context context, List<User> listUsers) {
        this.context = context;
        this.listUsers = listUsers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_dialog_likes, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User current = listUsers.get(position);
        holder.userName.setText(current.getName());
        String profile_image_url = context.getResources().getString(R.string.profile_images) + current.getUser_id() + ".png";
        Picasso.with(context)
                .load(profile_image_url).placeholder(R.drawable.profile_image_place_holder)
                .into(holder.userImage);

    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView userName;

        public ViewHolder(View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.user_name_dialog_likes);
            userImage = (CircleImageView) itemView.findViewById(R.id.user_image_dialog_likes);

        }
    }
}
