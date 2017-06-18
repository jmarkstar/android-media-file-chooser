package com.jmarkstar.mfc.module.gallery;

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
import java.util.List;

public class GalleryItemActivity extends AppCompatActivity implements GalleryItemAdapter.OnGalleryItemClickListener {

    private Toolbar mToolbar;
    private RecyclerView mRvGalleryItems;

    private MfcDialog.Builder mBuilder;
    private GalleryItemType mType;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_item);

        mBuilder = getIntent().getParcelableExtra(MfcDialog.BUILDER_TAG);
        mType = (GalleryItemType)getIntent().getSerializableExtra(ItemTypeFragment.ITEM_TYPE);
        String bucketName = getIntent().getStringExtra(ItemTypeFragment.BUCKET_NAME);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mRvGalleryItems = (RecyclerView)findViewById(R.id.rv_gallery_items);

        int primaryColor = ContextCompat.getColor(this, mBuilder.primaryColor);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            MfcUtils.setStatusBarColor(this, mBuilder.primaryDarkColor);
        }

        mToolbar.setBackgroundColor(primaryColor);
        if(mType == GalleryItemType.IMAGE){
            mToolbar.setTitle(getString(R.string.gallery_select_images));
        }else if(mType == GalleryItemType.VIDEO){
            mToolbar.setTitle(getString(R.string.gallery_select_videos));
        }

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        GalleryUtils galleryUtils = new GalleryUtils(this);
        List<GalleryItem> galleryItems = galleryUtils.getGalleryItemsByBucket(bucketName, mType);
        Log.v("fragment", "bucket size = "+galleryItems.size());

        GalleryItemAdapter adapter = new GalleryItemAdapter(this, this);
        adapter.addGalleryItems(galleryItems);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mRvGalleryItems.setLayoutManager(mLayoutManager);
        mRvGalleryItems.setItemAnimator(new DefaultItemAnimator());
        mRvGalleryItems.setAdapter(adapter);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gallery_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_done){

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

    }
}
