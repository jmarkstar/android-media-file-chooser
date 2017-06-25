package com.jmarkstar.mfcexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jmarkstar.mfc.MfcDialog;
import com.jmarkstar.mfc.model.GalleryItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView)findViewById(R.id.tv_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                new MfcDialog.Builder(MainActivity.this)
                        .build();
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MfcDialog.MFC_RESPONSE && resultCode == RESULT_OK){
            List<GalleryItem> galleryItems = data.getParcelableArrayListExtra(MfcDialog.SELECTED_GALLERY_ITEMS);
            Log.v("MainActivity", "galleryItems size = "+galleryItems.size());
            for(GalleryItem item : galleryItems){
                Log.v("MainActivity", item.toString());
            }
        }
    }
}
