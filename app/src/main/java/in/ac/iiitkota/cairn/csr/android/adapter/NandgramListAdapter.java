package in.ac.iiitkota.cairn.csr.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.interfaces.RCVItemClickListener;
import in.ac.iiitkota.cairn.csr.android.model.Nandgram;

import java.util.List;

/**
 * Created by SS Verma on 20-04-2017.
 */

public class NandgramListAdapter extends RecyclerView.Adapter<NandgramListAdapter.ViewHolder> {

    private List<Nandgram> listNandgram;
    private RCVItemClickListener rcvItemClickListener;

    public NandgramListAdapter(List<Nandgram> listNandgram) {
        this.listNandgram = listNandgram;
    }

    public void setRcvItemClickListener(RCVItemClickListener rcvItemClickListener) {
        this.rcvItemClickListener = rcvItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_nandgram_list, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Nandgram current = listNandgram.get(position);
        holder.name.setText(current.getNandgram_name());
        holder.address.setText(current.getAddress());

        //
    }

    @Override
    public int getItemCount() {
        return listNandgram.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView address;
        Button btnAddAttendance;

        LinearLayout dataHolder;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.nandgram_name);
            address = (TextView) itemView.findViewById(R.id.nandgram_address);
            dataHolder = (LinearLayout) itemView.findViewById(R.id.data_holder);
            btnAddAttendance = (Button) itemView.findViewById(R.id.btn_add_attendance);

            dataHolder.setOnClickListener(this);
            btnAddAttendance.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (rcvItemClickListener != null) {
                rcvItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
