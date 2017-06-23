package com.jmarkstar.mfc.module.gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jmarkstar.mfc.MfcDialog;
import com.jmarkstar.mfc.R;
import com.jmarkstar.mfc.model.Bucket;
import com.jmarkstar.mfc.model.GalleryItemType;
import java.util.List;

/**
 * Created by jmarkstar on 17/06/2017.
 */
public class ItemTypeFragment extends Fragment implements BucketAdapter.OnBucketClickListener{

    private static final String TAG = "ItemTypeFragment";

    private  RecyclerView mRvBuckets;

    private OnOpenGalleryItemByBucket onOpenGalleryItemByBucket;
    private GalleryItemType itemType;

    public static ItemTypeFragment newInstance(GalleryItemType itemType){
        ItemTypeFragment fragment = new ItemTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MfcDialog.ITEM_TYPE, itemType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onOpenGalleryItemByBucket = (OnOpenGalleryItemByBucket)context;
        }catch (ClassCastException ex){
            throw new ClassCastException(context.toString()
                    + " must implement OnOpenGalleryItemByBucket");
        }
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_item_type_pager, container, false);
        mRvBuckets = (RecyclerView)viewRoot.findViewById(R.id.rv_buckets);

        if(getArguments()!=null){
            itemType = (GalleryItemType) getArguments().get(MfcDialog.ITEM_TYPE);
        }
        Log.v(TAG, "itemType = "+itemType);

        return viewRoot;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GalleryUtils galleryUtils = new GalleryUtils(getContext());
        List<Bucket> buckets = galleryUtils.getBucketsByItemType(itemType);
        Log.v(TAG, "bucket size = "+buckets.size());

        BucketAdapter adapter = new BucketAdapter(getContext(), this);
        adapter.addBuckets(buckets);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),3);
        mRvBuckets.setLayoutManager(mLayoutManager);
        mRvBuckets.setItemAnimator(new DefaultItemAnimator());
        mRvBuckets.setAdapter(adapter);
    }

    @Override public void onItemClick(Bucket bucket) {
        if(onOpenGalleryItemByBucket!=null)
            onOpenGalleryItemByBucket.onGalleryItem(bucket, itemType);
    }

    interface OnOpenGalleryItemByBucket{
        void onGalleryItem(Bucket bucket, GalleryItemType itemType);
    }
}
