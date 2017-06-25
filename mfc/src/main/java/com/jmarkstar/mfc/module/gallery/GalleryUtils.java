package com.jmarkstar.mfc.module.gallery;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import com.jmarkstar.mfc.model.Bucket;
import com.jmarkstar.mfc.model.MediaFile;
import com.jmarkstar.mfc.model.MediaFileType;
import com.jmarkstar.mfc.util.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by jmarkstar on 18/06/2017.
 */
class GalleryUtils {

    private Context mContext;

    GalleryUtils(Context mContext) {
        this.mContext = mContext;
    }

    List<Bucket> getBucketsByItemType(@NonNull MediaFileType type){

        List<Bucket> buckets = new ArrayList<>();
        Uri uri = null;
        String [] projection = new String[2];
        String orderBy = Constants.EMPTY;

        if(type == MediaFileType.IMAGE){
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection[0] = MediaStore.Images.Media.BUCKET_DISPLAY_NAME;
            projection[1] = MediaStore.Images.Media.DATA;
            orderBy = MediaStore.Images.Media.DATE_ADDED+" DESC";
        }else if(type == MediaFileType.VIDEO){
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection[0] = MediaStore.Video.Media.BUCKET_DISPLAY_NAME;
            projection[1] = MediaStore.Video.Media.DATA;
            orderBy = MediaStore.Video.Media.DATE_ADDED+" DESC";
        }

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, orderBy);
        if(cursor != null){
            HashSet<String> bucketSet = new HashSet<>();
            File file;
            while (cursor.moveToNext()){
                String bucketName = cursor.getString(cursor.getColumnIndex(projection[0]));
                String fisrtImage = cursor.getString(cursor.getColumnIndex(projection[1]));
                file = new File(fisrtImage);
                if (file.exists() && !bucketSet.contains(bucketName)) {
                    buckets.add(new Bucket(bucketName, fisrtImage));
                    bucketSet.add(bucketName);
                }
            }
            cursor.close();
        }
        return buckets;
    }

    public List<MediaFile> getGalleryItemsByBucket(@NonNull String bucketName, @NonNull MediaFileType type){

        Uri uri = null;
        String [] projection = new String[2];
        String selection = Constants.EMPTY;
        String orderBy = Constants.EMPTY;

        if(type == MediaFileType.IMAGE){
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            projection[0] = MediaStore.Images.Media.DISPLAY_NAME;
            projection[1] = MediaStore.Images.Media.DATA;
            selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" =?";
            orderBy = MediaStore.Images.Media.DATE_ADDED+" DESC";
        }else if(type == MediaFileType.VIDEO){
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            projection[0] = MediaStore.Video.Media.DISPLAY_NAME;
            projection[1] = MediaStore.Video.Media.DATA;
            selection = MediaStore.Video.Media.BUCKET_DISPLAY_NAME+" =?";
            orderBy = MediaStore.Video.Media.DATE_ADDED+" DESC";
        }

        List<MediaFile> images = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(uri, projection, selection,new String[]{bucketName}, orderBy);

        if(cursor != null){
            HashSet<String> imageSet = new HashSet<>();
            File file;
            while (cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndex(projection[1]));
                file = new File(path);
                if (file.exists() && !imageSet.contains(path)) {
                    imageSet.add(path);
                    images.add(new MediaFile(path, type));
                }
            }
            cursor.close();
        }
        return images;
    }
}
