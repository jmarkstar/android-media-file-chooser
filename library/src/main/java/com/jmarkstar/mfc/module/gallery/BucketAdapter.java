package com.jmarkstar.mfc.module.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jmarkstar.mfc.R;
import com.jmarkstar.mfc.model.Bucket;

import java.util.List;

/**
 * Created by jmarkstar on 18/06/2017.
 */
class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.BucketVH> {

    private Context mContext;
    private List<Bucket> mBuckets;

    public BucketAdapter(Context mContext, List<Bucket> mBuckets) {
        this.mContext = mContext;
        this.mBuckets = mBuckets;
    }

    @Override public BucketVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gallery_bucket_item, parent, false);
        return new BucketVH(view);
    }

    @Override public void onBindViewHolder(BucketVH holder, int position) {
        Bucket bucket = mBuckets.get(position);
        holder.tvBucketName.setText(bucket.getName());
        Glide.with(mContext).load("file://"+bucket.getFirstImageContainedPath()).override(300,300).centerCrop().into(holder.ivFirstImage);
    }

    @Override public int getItemCount() {
        if(mBuckets==null) return 0;
        else return mBuckets.size();
    }

    class BucketVH extends RecyclerView.ViewHolder {

        ImageView ivFirstImage;
        TextView tvBucketName;

        public BucketVH(View itemView) {
            super(itemView);
            ivFirstImage = (ImageView)itemView.findViewById(R.id.iv_first_image);
            tvBucketName = (TextView)itemView.findViewById(R.id.tv_bucket_name);
        }
    }
}
