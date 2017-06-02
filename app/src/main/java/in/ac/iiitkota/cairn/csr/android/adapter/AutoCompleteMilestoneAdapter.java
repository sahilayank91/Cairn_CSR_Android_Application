package in.ac.iiitkota.cairn.csr.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import in.ac.iiitkota.cairn.csr.android.model.Milestone;
import in.ac.iiitkota.cairn.csr.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SS Verma on 15-03-2017.
 */

public class AutoCompleteMilestoneAdapter extends ArrayAdapter<Milestone> {

    private Context context;
    private List<Milestone> listMilestones;
    private List<Milestone> listFilteredMilestones;

    public AutoCompleteMilestoneAdapter(@NonNull Context context, @NonNull List<Milestone> listMilestones) {
        super(context, 0, listMilestones);
        this.context = context;
        this.listMilestones = listMilestones;
        this.listFilteredMilestones = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return listFilteredMilestones.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new MilestoneFilter(this, listMilestones);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Milestone current = listFilteredMilestones.get(position);

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_auto_complete_suggestions, parent, false);

        TextView suggestionText = (TextView) convertView.findViewById(R.id.suggestion_text);
        suggestionText.setText(current.getMilestoneName());

        return convertView;
    }

    private class MilestoneFilter extends Filter {

        private AutoCompleteMilestoneAdapter adapter;
        private List<Milestone> listOriginalMilestones;
        private List<Milestone> listFilteredMilestones;

        public MilestoneFilter(AutoCompleteMilestoneAdapter adapter, List<Milestone> listOriginalMilestones) {
            super();
            this.adapter = adapter;
            this.listOriginalMilestones = listOriginalMilestones;
            listFilteredMilestones = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            listFilteredMilestones.clear();

            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                listFilteredMilestones.addAll(listOriginalMilestones);
            } else {

                String filterPattern = constraint.toString().toLowerCase().trim();

                //filtering logic
                for (Milestone milestone : listOriginalMilestones) {
                    if (milestone.getMilestoneName().toLowerCase().contains(filterPattern)) {
                        listFilteredMilestones.add(milestone);
                    }
                }
            }

            results.values = listFilteredMilestones;
            results.count = listFilteredMilestones.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.listFilteredMilestones.clear();
            adapter.listFilteredMilestones.addAll((List) results.values);
            adapter.notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Milestone)resultValue).getMilestoneName();
        }
    }

}
