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
    private OnBucketClickListener onBucketClickListener;

    BucketAdapter(Context mContext, OnBucketClickListener onBucketClickListener) {
        if(onBucketClickListener==null){
            throw new NullPointerException("OnBucketClickListener can't be null");
        }
        this.mContext = mContext;
        this.onBucketClickListener = onBucketClickListener;
    }

    public void addBuckets(List<Bucket> mBuckets){
        this.mBuckets = mBuckets;
    }

    @Override public BucketVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gallery_bucket, parent, false);
        return new BucketVH(view);
    }

    @Override public void onBindViewHolder(BucketVH holder, int position) {
        final Bucket bucket = mBuckets.get(position);
        holder.tvName.setText(bucket.getName());
        Glide.with(mContext).load("file://"+bucket.getFirstImageContainedPath()).override(300,300).centerCrop().into(holder.ivImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(onBucketClickListener!=null)
                    onBucketClickListener.onItemClick(bucket);
            }
        });
    }

    @Override public int getItemCount() {
        if(mBuckets==null) return 0;
        else return mBuckets.size();
    }

    class BucketVH extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvName;

        BucketVH(View itemView) {
            super(itemView);
            ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            tvName = (TextView)itemView.findViewById(R.id.tv_name);
        }
    }

    interface OnBucketClickListener{
        void onItemClick(Bucket bucket);
    }
}
