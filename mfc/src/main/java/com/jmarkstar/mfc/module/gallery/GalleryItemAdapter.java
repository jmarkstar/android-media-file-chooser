package com.jmarkstar.mfc.module.gallery;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jmarkstar.mfc.R;
import com.jmarkstar.mfc.model.MediaFile;

import java.util.List;

/**
 * Created by jmarkstar on 18/06/2017.
 */
class GalleryItemAdapter extends RecyclerView.Adapter<GalleryItemAdapter.GalleryItemVH> {

    private List<MediaFile> mGalleryItems;
    private Context mContext;
    private OnGalleryItemClickListener onGalleryItemClickListener;

    GalleryItemAdapter(Context context, OnGalleryItemClickListener onGalleryItemClickListener){
        if(onGalleryItemClickListener==null){
            throw new NullPointerException("OnGalleryItemClickListener can't be null");
        }
        this.onGalleryItemClickListener = onGalleryItemClickListener;
        this.mContext = context;
    }

    public void addGalleryItems(List<MediaFile> galleryItems){
        this.mGalleryItems = galleryItems;
    }

    @Override public GalleryItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gallery_item_list, parent, false);
        return new GalleryItemVH(view);
    }

    @Override public void onBindViewHolder(GalleryItemVH holder, int position) {
        final MediaFile galleryItem = mGalleryItems.get(position);
        Glide.with(mContext).load("file://"+galleryItem.getPathName()).override(300,300).centerCrop().into(holder.ivImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(onGalleryItemClickListener!=null)
                    onGalleryItemClickListener.onItemClick(galleryItem);
            }
        });
        if(galleryItem.isSelected()){
            holder.ivCheck.setVisibility(View.VISIBLE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                holder.ivCheck.setImageAlpha(120);
            }else{
                holder.ivCheck.setAlpha(120);
            }
        }else{
            holder.ivCheck.setVisibility(View.GONE);
        }
    }

    @Override public int getItemCount() {
        if(mGalleryItems==null) return 0;
        else return mGalleryItems.size();
    }

    class GalleryItemVH extends RecyclerView.ViewHolder {

        ImageView ivImage;
        ImageView ivCheck;

        GalleryItemVH(View itemView) {
            super(itemView);
            ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            ivCheck = (ImageView)itemView.findViewById(R.id.iv_check);
        }
    }

    interface OnGalleryItemClickListener{
        void onItemClick(MediaFile galleryItem);
    }
}
