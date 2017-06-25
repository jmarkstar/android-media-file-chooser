package com.jmarkstar.mfcexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.jmarkstar.mfc.MfcDialog;
import com.jmarkstar.mfc.model.MediaFile;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MfcDialog.OnMfcResultListener {

    private static final String TAG = "MainActivity";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView)findViewById(R.id.tv_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new MfcDialog.Builder(MainActivity.this,
                        MainActivity.this).build();
            }
        });
    }

    @Override public void onMfcResult(List<MediaFile> items) {
        Log.v(TAG, "items size = "+items.size());
        for(MediaFile item : items){
            Log.v(TAG, item.toString());
        }
    }
}
