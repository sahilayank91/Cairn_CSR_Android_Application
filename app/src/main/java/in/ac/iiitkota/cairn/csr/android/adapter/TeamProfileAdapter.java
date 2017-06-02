package in.ac.iiitkota.cairn.csr.android.adapter;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.model.TeamMember;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by SS Verma on 26-03-2017.
 */

public class TeamProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_HEADER = 0;
    private final int VIEW_TYPE_MEMBER = 1;
    private final int VIEW_TYPE_GRAPH = 2;

    private Activity activityContext;
    private List<TeamMember> teamMemberList;
    private String teamDescription;

    public TeamProfileAdapter(Activity activityCcontext, List<TeamMember> teamMemberList, String teamDescription) {
        this.activityContext = activityCcontext;
        this.teamMemberList = teamMemberList;
        this.teamDescription = teamDescription;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView;

        if (viewType == VIEW_TYPE_HEADER) {
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_team_profile_header, parent, false);
            return new HeaderViewHolder(rootView);

        } else if (viewType == VIEW_TYPE_MEMBER){
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_team_profile, parent, false);
            return new TeamMemberViewHolder(rootView);
        } else {
            rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_header_my_activity, parent, false);
            return new GraphViewHolder(rootView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.teamDescription.setText(teamDescription);

        } else if (holder instanceof  TeamMemberViewHolder){
            TeamMember current = teamMemberList.get(position -2);
            TeamMemberViewHolder teamMemberViewHolder = (TeamMemberViewHolder) holder;
            teamMemberViewHolder.memberName.setText(current.getUser().getName());
            teamMemberViewHolder.memberType.setText(current.getMemberType());
            String profile_image_url = activityContext.getResources().getString(R.string.profile_images) + current.getUser().getUser_id() + ".png";
            Picasso.with(activityContext)
                    .load(profile_image_url).placeholder(R.drawable.profile_image_place_holder)
                    .into(teamMemberViewHolder.memberImage);
        } else {
            //
        }
    }

    @Override
    public int getItemCount() {
        return teamMemberList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }

        if (position == 1) {
            return VIEW_TYPE_GRAPH;
        }

        return VIEW_TYPE_MEMBER;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView teamDescription;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            teamDescription = (TextView) itemView.findViewById(R.id.team_description);

        }
    }

    private class TeamMemberViewHolder extends RecyclerView.ViewHolder {

        TextView memberName;
        TextView memberType;
        ImageView memberImage;

        public TeamMemberViewHolder(View itemView) {
            super(itemView);

            memberName = (TextView) itemView.findViewById(R.id.member_name);
            memberType = (TextView) itemView.findViewById(R.id.member_type);
            memberImage = (ImageView) itemView.findViewById(R.id.team_member_image);
        }
    }

    private class GraphViewHolder extends RecyclerView.ViewHolder {

        public GraphViewHolder(View itemView) {
            super(itemView);

            StatisticsSectionPagerAdapter sectionsPagerAdapter = new StatisticsSectionPagerAdapter(activityContext.getFragmentManager(), "user", UserData.getInstance(activityContext).getUser_id());

            ViewPager viewPager = (ViewPager) itemView.findViewById(R.id.viewpager_my_activity);
            CircleIndicator indicator = (CircleIndicator) itemView.findViewById(R.id.indicator);
            viewPager.setAdapter(sectionsPagerAdapter);
            indicator.setViewPager(viewPager);

        }
    }
}
