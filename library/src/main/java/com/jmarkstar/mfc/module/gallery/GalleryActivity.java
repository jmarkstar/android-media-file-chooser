package com.jmarkstar.mfc.module.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.jmarkstar.mfc.MfcDialog;
import com.jmarkstar.mfc.R;
import com.jmarkstar.mfc.model.Bucket;
import com.jmarkstar.mfc.model.GalleryItem;
import com.jmarkstar.mfc.model.GalleryItemType;
import com.jmarkstar.mfc.util.MfcUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmarkstar on 14/06/2017.
 */
public class GalleryActivity extends AppCompatActivity
        implements ItemTypeFragment.OnOpenGalleryItemByBucket {

    private static final String TAG = "GalleryActivity";

    private Toolbar mToolbar;

    private TabLayout mTlItemType;
    private ViewPager mVpItemType;

    private MfcDialog.Builder mBuilder;
    private ArrayList<GalleryItem> mSelectedGalleryItems;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mBuilder = getIntent().getParcelableExtra(MfcDialog.BUILDER_TAG);
        mSelectedGalleryItems = new ArrayList<>();

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mTlItemType = (TabLayout)findViewById(R.id.tl_itemtype);
        mVpItemType = (ViewPager)findViewById(R.id.vp_itemtype);

        int accentColor = ContextCompat.getColor(this, mBuilder.accentColor);
        int primaryColor = ContextCompat.getColor(this, mBuilder.primaryColor);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            MfcUtils.setStatusBarColor(this, mBuilder.primaryDarkColor);
        }

        //set settings colors
        mToolbar.setBackgroundColor(primaryColor);
        mTlItemType.setBackgroundColor(primaryColor);
        mTlItemType.setSelectedTabIndicatorColor(accentColor);

        setTitle();
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        mVpItemType.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTlItemType));
        mTlItemType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override public void onTabSelected(TabLayout.Tab tab) {
                mVpItemType.setCurrentItem(tab.getPosition());
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}

            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
        loadItemTypePagers();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gallery_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_done_all){
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

    @Override public void onGalleryItem(Bucket bucket, GalleryItemType itemType) {
        Intent intent = new Intent(this, GalleryItemActivity.class);
        intent.putExtra(MfcDialog.BUILDER_TAG, mBuilder);
        intent.putExtra(MfcDialog.ITEM_TYPE, itemType);
        intent.putExtra(MfcDialog.BUCKET_NAME, bucket.getName());
        intent.putParcelableArrayListExtra(MfcDialog.SELECTED_GALLERY_ITEMS, mSelectedGalleryItems);
        startActivityForResult(intent, MfcDialog.CHOOSE_GALLERY_ITEMS_REQUEST);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MfcDialog.CHOOSE_GALLERY_ITEMS_REQUEST && resultCode == RESULT_OK){
            mSelectedGalleryItems = data.getParcelableArrayListExtra(MfcDialog.SELECTED_GALLERY_ITEMS);
            setTitle();
        }
    }

    private void setTitle(){
        if(mSelectedGalleryItems!=null && mSelectedGalleryItems.size()>0){
            mToolbar.setTitle(String.valueOf(mSelectedGalleryItems.size()));
        }else{
            mToolbar.setTitle(mBuilder.dialogTitle);
        }
    }

    private void loadItemTypePagers(){
        mTlItemType.addTab(mTlItemType.newTab().setText(getString(R.string.gallery_tab_images)));
        mTlItemType.addTab(mTlItemType.newTab().setText(getString(R.string.gallery_tab_videos)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ItemTypeFragment.newInstance(GalleryItemType.IMAGE));
        fragments.add(ItemTypeFragment.newInstance(GalleryItemType.VIDEO));
        ItemTypeVpAdapter adapter = new ItemTypeVpAdapter(getSupportFragmentManager(), fragments);
        mVpItemType.setAdapter(adapter);
    }
}
