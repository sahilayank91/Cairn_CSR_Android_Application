package in.ac.iiitkota.cairn.csr.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.interfaces.CheckboxCheckChangeListener;

import java.util.List;

/**
 * Created by SS Verma on 17-04-2017.
 */

public class ShareWithAdapter extends RecyclerView.Adapter<ShareWithAdapter.ViewHolder> {

    private List<String> listTeamNames;
    private CheckboxCheckChangeListener checkboxCheckChangeListener;
    private boolean hasPublic;
    public ShareWithAdapter(List<String> listTeams,boolean hasPublic) {
        this.listTeamNames = listTeams;
        this.hasPublic=hasPublic;
    }

    public void setCheckboxCheckChangeListener(CheckboxCheckChangeListener checkboxCheckChangeListener) {
        this.checkboxCheckChangeListener = checkboxCheckChangeListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_dialog_share_with, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(hasPublic){
            if (position == 0) {
                holder.teamCheckbox.setText("Public");
            } else {
                String current = listTeamNames.get(position -1);
                holder.teamCheckbox.setText(current);
            }
        }
        else{
            String current = listTeamNames.get(position);
            holder.teamCheckbox.setText(current);
        }

    }

    @Override
    public int getItemCount() {
        if(hasPublic)
        return listTeamNames.size() + 1; // +1 for public
        else return listTeamNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        CheckBox teamCheckbox;

        public ViewHolder(View itemView) {
            super(itemView);
            teamCheckbox = (CheckBox) itemView.findViewById(R.id.checkbox_team);
            teamCheckbox.setOnCheckedChangeListener(this);
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (checkboxCheckChangeListener != null) {
                checkboxCheckChangeListener.onTeamCheckChange(buttonView, isChecked, getAdapterPosition());
            }
        }
    }
}
