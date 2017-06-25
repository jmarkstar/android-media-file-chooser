package com.jmarkstar.mfcexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jmarkstar.mfc.MfcDialog;
import com.jmarkstar.mfc.model.MediaFile;

import java.util.List;

/**
 * Created by jmarkstar on 24/06/2017.
 */
public class MainFragment extends Fragment implements MfcDialog.OnMfcResultListener {

    private static final String TAG = "MainFragment";

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        TextView tvOpenDialog = (TextView)view.findViewById(R.id.tv_text_from_fragment);
        tvOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new MfcDialog.Builder(getContext(), MainFragment.this).build();
            }
        });

        return view;
    }

    @Override public void onMfcResult(List<MediaFile> items) {
        Log.v(TAG, "galleryItems size = "+items.size());
        for(MediaFile item : items){
            Log.v(TAG, item.toString());
        }
    }
}
