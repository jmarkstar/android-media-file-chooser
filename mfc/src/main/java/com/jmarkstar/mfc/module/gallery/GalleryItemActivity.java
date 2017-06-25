package com.jmarkstar.mfc.module.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.jmarkstar.mfc.MfcDialog;
import com.jmarkstar.mfc.R;
import com.jmarkstar.mfc.model.GalleryItem;
import com.jmarkstar.mfc.model.GalleryItemType;
import com.jmarkstar.mfc.util.MfcUtils;
import java.util.ArrayList;
import java.util.List;

/** @author jmarkstar
 * */
public class GalleryItemActivity extends AppCompatActivity
        implements GalleryItemAdapter.OnGalleryItemClickListener {

    private static final String TAG = "GalleryItemActivity";

    private Toolbar mToolbar;
    private RecyclerView mRvGalleryItems;

    private GalleryItemAdapter mAdapter;
    private MfcDialog.Builder mBuilder;
    private GalleryItemType mType;
    private ArrayList<GalleryItem> mSelectedGalleryItems;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_item);

        mBuilder = (MfcDialog.Builder)getIntent().getSerializableExtra(MfcDialog.BUILDER_TAG);
        mType = (GalleryItemType)getIntent().getSerializableExtra(MfcDialog.ITEM_TYPE);
        mSelectedGalleryItems = getIntent().getParcelableArrayListExtra(MfcDialog.SELECTED_GALLERY_ITEMS);
        String bucketName = getIntent().getStringExtra(MfcDialog.BUCKET_NAME);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mRvGalleryItems = (RecyclerView)findViewById(R.id.rv_gallery_items);

        int primaryColor = ContextCompat.getColor(this, mBuilder.primaryColor);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            MfcUtils.setStatusBarColor(this, mBuilder.primaryDarkColor);
        }

        mToolbar.setBackgroundColor(primaryColor);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setTitle();
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        GalleryUtils galleryUtils = new GalleryUtils(this);
        List<GalleryItem> galleryItems = galleryUtils.getGalleryItemsByBucket(bucketName, mType);
        Log.v(TAG, "gallery items size = "+galleryItems.size());
        Log.v(TAG, "selected items size = "+mSelectedGalleryItems.size());
        for(GalleryItem selectedItem : mSelectedGalleryItems){
            for(GalleryItem item: galleryItems){
                if(selectedItem.getPathName().equals(item.getPathName())){
                    item.setSelected(true);
                    break;
                }
            }
        }

        mAdapter = new GalleryItemAdapter(this, this);
        mAdapter.addGalleryItems(galleryItems);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mRvGalleryItems.setLayoutManager(mLayoutManager);
        mRvGalleryItems.setItemAnimator(new DefaultItemAnimator());
        mRvGalleryItems.setAdapter(mAdapter);
    }

    private void setTitle(){
        if(mSelectedGalleryItems!=null && mSelectedGalleryItems.size()>0){
            mToolbar.setTitle(String.valueOf(mSelectedGalleryItems.size()));
        }else{
            if(mType == GalleryItemType.IMAGE){
                mToolbar.setTitle(getString(R.string.gallery_select_images));
            }else if(mType == GalleryItemType.VIDEO){
                mToolbar.setTitle(getString(R.string.gallery_select_videos));
            }
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gallery_item_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_done){
            Intent intent = getIntent();
            intent.putParcelableArrayListExtra(MfcDialog.SELECTED_GALLERY_ITEMS, mSelectedGalleryItems);
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override public void onItemClick(GalleryItem galleryItem) {
        if(galleryItem.isSelected()){
            galleryItem.setSelected(false);
            mSelectedGalleryItems.remove(galleryItem);
        }else{
            galleryItem.setSelected(true);
            mSelectedGalleryItems.add(galleryItem);
        }
        setTitle();
        mAdapter.notifyDataSetChanged();
    }
}
