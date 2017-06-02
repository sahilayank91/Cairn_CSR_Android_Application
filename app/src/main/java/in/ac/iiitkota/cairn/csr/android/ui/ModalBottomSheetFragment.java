package in.ac.iiitkota.cairn.csr.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.interfaces.BottomSheetClickListener;

/**
 * Created by SS Verma on 18-04-2017.
 */

public class ModalBottomSheetFragment extends BottomSheetDialogFragment {


    private BottomSheetClickListener bottomSheetClickListener;

    public void setBottomSheetClickListener (BottomSheetClickListener bottomSheetClickListener) {
        this.bottomSheetClickListener = bottomSheetClickListener;
    }

    static ModalBottomSheetFragment newInstance(String string){
        ModalBottomSheetFragment fragment = new ModalBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("string" , string);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.modal_bottom_sheet , parent , false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout cameraHolder = (LinearLayout) view.findViewById(R.id.camera_holder);
        LinearLayout galleryHolder = (LinearLayout) view.findViewById(R.id.gallery_holder);

        cameraHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetClickListener != null) {
                    bottomSheetClickListener.onBottomSheetItemClick(v, 0);
                }
            }
        });

        galleryHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetClickListener != null) {
                    bottomSheetClickListener.onBottomSheetItemClick(v, 1);
                }
            }
        });

    }
}
