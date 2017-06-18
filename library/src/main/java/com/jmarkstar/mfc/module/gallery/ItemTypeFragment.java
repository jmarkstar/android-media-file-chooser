package com.jmarkstar.mfc.module.gallery;

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
import com.jmarkstar.mfc.R;
import com.jmarkstar.mfc.model.Bucket;
import com.jmarkstar.mfc.model.GalleryItem;
import com.jmarkstar.mfc.model.GalleryItemType;

import java.util.List;

/**
 * Created by jmarkstar on 17/06/2017.
 */
public class ItemTypeFragment extends Fragment {

    public static final String ITEM_TYPE = "item_type";

    private RecyclerView mRvBuckets;

    private GalleryItemType itemType;

    public static ItemTypeFragment newInstance(GalleryItemType itemType){
        ItemTypeFragment fragment = new ItemTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ITEM_TYPE, itemType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_item_type_pager, container, false);
        mRvBuckets = (RecyclerView)viewRoot.findViewById(R.id.rv_buckets);

        if(getArguments()!=null){
            itemType = (GalleryItemType) getArguments().get(ITEM_TYPE);
        }
        Log.v("fragment", "itemType = "+itemType);

        GalleryUtils galleryUtils = new GalleryUtils(getContext());
        List<Bucket> buckets = galleryUtils.getBucketsByItemType(itemType);
        Log.v("fragment", "bucket size = "+buckets.size());

        BucketAdapter adapter = new BucketAdapter(getContext(), buckets);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),3);
        mRvBuckets.setLayoutManager(mLayoutManager);
        mRvBuckets.setItemAnimator(new DefaultItemAnimator());
        mRvBuckets.setAdapter(adapter);

        return viewRoot;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
