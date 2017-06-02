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
import in.ac.iiitkota.cairn.csr.android.model.Fontus;
import in.ac.iiitkota.cairn.csr.android.model.Nandgram;

import java.util.List;

/**
 * Created by SS Verma on 20-04-2017.
 */

public class FontusLocationsAdapter extends RecyclerView.Adapter<FontusLocationsAdapter.ViewHolder> {

    private List<Fontus> listFontus;
    private RCVItemClickListener rcvItemClickListener;

    public FontusLocationsAdapter(List<Fontus> listNandgram) {
        this.listFontus = listNandgram;
    }

    public void setRcvItemClickListener(RCVItemClickListener rcvItemClickListener) {
        this.rcvItemClickListener = rcvItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_fontus_dept_list, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fontus current = listFontus.get(position);
        holder.name.setText(current.getArea_name());
        holder.address.setText(current.getLaunch_date());

        //
    }

    @Override
    public int getItemCount() {
        return listFontus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView address;


        LinearLayout dataHolder;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.area_name);
            address = (TextView) itemView.findViewById(R.id.area_address);
            dataHolder = (LinearLayout) itemView.findViewById(R.id.data_holder);
            dataHolder.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (rcvItemClickListener != null) {
                rcvItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
