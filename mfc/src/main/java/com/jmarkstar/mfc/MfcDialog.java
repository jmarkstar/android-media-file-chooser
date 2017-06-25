package com.jmarkstar.mfc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jmarkstar.mfc.model.MediaFile;
import com.jmarkstar.mfc.model.MediaFileType;
import com.jmarkstar.mfc.module.gallery.GalleryActivity;
import com.jmarkstar.mfc.util.DrawableUtils;
import com.jmarkstar.mfc.util.MfcUtils;
import com.jmarkstar.mfc.util.MediaUtils;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmarkstar on 14/06/2017.
 */
public class MfcDialog extends AppCompatActivity {

    public static final String BUILDER_TAG = "builder";
    public static final String SELECTED_GALLERY_ITEMS = "selected_items";
    public static final String ITEM_TYPE = "item_type";
    public static final String BUCKET_NAME = "bucket_name";

    public static final int CHOOSE_GALLERY_ITEMS_REQUEST = 1991;
    public static final int TAKE_PHOTO_REQUEST = 1992;
    public static final int RECORD_VIDEO_REQUEST = 1993;
    public static final int MFC_RESPONSE = 1994;

    private static final int PERMISSION_REQUEST_FOR_GALLERY_CODE = 1000;
    private static final int PERMISSION_REQUEST_FOR_TAKE_PHOTO_CODE = 2000;
    private static final int PERMISSION_REQUEST_FOR_RECORD_VIDEO_CODE = 3000;

    private RelativeLayout mRlDialogBody;
    private ProgressBar mPgLoaging;

    private RelativeLayout mRlLayout;
    private RelativeLayout mRlDialogContent;
    private TextView mTvTitle;
    private TextView mTvGalleryOption;
    private TextView mTvTakePhotoOption;
    private TextView mTvRecordVideoOption;

    private Builder mBuilder;

    private String mImageFileLocation;
    private String mVideoFileLocation;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfc_dialog);

        mBuilder = (Builder)getIntent().getSerializableExtra(BUILDER_TAG);

        onInitViews();
        onSetup();
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_FOR_GALLERY_CODE :{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }
                break;
            }
            case PERMISSION_REQUEST_FOR_TAKE_PHOTO_CODE :{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }
                break;
            }
            case PERMISSION_REQUEST_FOR_RECORD_VIDEO_CODE :{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recordVideo();
                }
                break;
            }
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            new GetPhotoTask().execute();
        }else if (requestCode == RECORD_VIDEO_REQUEST && resultCode == RESULT_OK) {
            MediaFile item = new MediaFile(mVideoFileLocation, MediaFileType.VIDEO);
            ArrayList<MediaFile> items = new ArrayList<>();
            items.add(item);
            if(mBuilder.onMfcResultListener !=null)
                mBuilder.onMfcResultListener.onMfcResult(items);
            finish();
        } else if(requestCode == CHOOSE_GALLERY_ITEMS_REQUEST && resultCode == RESULT_OK){
            List<MediaFile> galleryItems = data.getParcelableArrayListExtra(MfcDialog.SELECTED_GALLERY_ITEMS);
            if(mBuilder.onMfcResultListener !=null)
            mBuilder.onMfcResultListener.onMfcResult(galleryItems);
            finish();
        }
    }

    private void onInitViews(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            MfcUtils.setStatusBarColor(this, android.R.color.transparent);
        }

        mRlDialogBody = (RelativeLayout)findViewById(R.id.rl_dialog_body);
        mPgLoaging = (ProgressBar)findViewById(R.id.pg_loading);

        mRlDialogContent = (RelativeLayout)findViewById(R.id.rl_dialog_content);
        mRlLayout = (RelativeLayout)findViewById(R.id.rl_layout);
        mTvTitle = (TextView)findViewById(R.id.mfc_dialog_title);
        mTvGalleryOption = (TextView)findViewById(R.id.mfc_gallery_option);
        mTvTakePhotoOption = (TextView)findViewById(R.id.mfc_camera_option);
        mTvRecordVideoOption = (TextView)findViewById(R.id.mfc_video_option);
        mRlDialogContent.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {/*empty action*/}
        });
        mRlLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        mTvGalleryOption.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(MfcDialog.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MfcDialog.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_FOR_GALLERY_CODE);
                    }else{
                        openGallery();
                    }
                }else{
                    openGallery();
                }
            }
        });
        mTvTakePhotoOption.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    boolean readExternalStogeIsGranted = ContextCompat.checkSelfPermission(MfcDialog.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                    boolean cameraIdGranted = ContextCompat.checkSelfPermission(MfcDialog.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                    if(!readExternalStogeIsGranted || !cameraIdGranted) {
                        String askPermissions [];
                        if(!readExternalStogeIsGranted && !cameraIdGranted){
                            askPermissions = new String[2];
                            askPermissions[0] = Manifest.permission.CAMERA;
                            askPermissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                        }else{
                            askPermissions = new String[1];
                            if(!readExternalStogeIsGranted)
                                askPermissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                            if(!cameraIdGranted)
                                askPermissions[0] = Manifest.permission.CAMERA;
                        }
                        ActivityCompat.requestPermissions(MfcDialog.this,
                                askPermissions , PERMISSION_REQUEST_FOR_TAKE_PHOTO_CODE);
                    }else{
                        takePicture();
                    }
                }else{
                    takePicture();
                }
            }
        });
        mTvRecordVideoOption.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    boolean readExternalStogeIsGranted = ContextCompat.checkSelfPermission(MfcDialog.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                    boolean cameraIdGranted = ContextCompat.checkSelfPermission(MfcDialog.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                    if(!readExternalStogeIsGranted || !cameraIdGranted) {
                        String askPermissions [];
                        if(!readExternalStogeIsGranted && !cameraIdGranted){
                            askPermissions = new String[2];
                            askPermissions[0] = Manifest.permission.CAMERA;
                            askPermissions[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
                        }else{
                            askPermissions = new String[1];
                            if(!readExternalStogeIsGranted)
                                askPermissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
                            if(!cameraIdGranted)
                                askPermissions[0] = Manifest.permission.CAMERA;
                        }
                        ActivityCompat.requestPermissions(MfcDialog.this,
                                askPermissions , PERMISSION_REQUEST_FOR_RECORD_VIDEO_CODE);
                    }else{
                        recordVideo();
                    }
                }else{
                    recordVideo();
                }
            }
        });
    }

    private void onSetup(){
        int accentColor = ContextCompat.getColor(this, mBuilder.accentColor);

        mTvTitle.setText(mBuilder.dialogTitle);
        mTvGalleryOption.setCompoundDrawablesWithIntrinsicBounds( DrawableUtils.tint(this,
                mBuilder.dialogGalleryIcon, accentColor), null, null, null);

        mTvTakePhotoOption.setCompoundDrawablesWithIntrinsicBounds( DrawableUtils.tint(this,
                mBuilder.dialogCameraIcon, accentColor), null, null, null);

        mTvRecordVideoOption.setCompoundDrawablesWithIntrinsicBounds( DrawableUtils.tint(this,
                R.drawable.ic_video, accentColor), null, null, null);
    }

    private void openGallery(){
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra(BUILDER_TAG, mBuilder);
        startActivityForResult(intent, CHOOSE_GALLERY_ITEMS_REQUEST);
    }

    private void takePicture(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageFileLocation = MediaUtils.createImageFile(getString(mBuilder.folderName));
        if(mImageFileLocation!=null){
            File photoFile = new File(mImageFileLocation);
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".mfc.provider", photoFile);
            } else {
                uri = Uri.fromFile(photoFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, TAKE_PHOTO_REQUEST);
        }
    }

    private void recordVideo(){
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, mBuilder.videoDuration);

            mVideoFileLocation = MediaUtils.createVideoFile(getString(mBuilder.folderName));
            if(mVideoFileLocation!=null){
                File videFile = new File(mVideoFileLocation);
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".mfc.provider", videFile);
                } else {
                    uri = Uri.fromFile(videFile);
                }
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                startActivityForResult(takeVideoIntent, RECORD_VIDEO_REQUEST);
            }
        }
    }

    public static class Builder implements Serializable{

        private OnMfcResultListener onMfcResultListener;
        private transient Context context;
        public int primaryColor;
        public int primaryDarkColor;
        public int accentColor;
        public String dialogTitle;
        public int dialogGalleryIcon;
        public int dialogCameraIcon;
        public int videoDuration;
        public int photoBitmapWidth;
        public int photoBitmapHeigth;
        public int folderName;

        public Builder(@NonNull Context context, OnMfcResultListener onMfcResultListener){
            this.context = context;
            this.onMfcResultListener = onMfcResultListener;
            initDefaultValues();
        }

        private void initDefaultValues(){
            this.primaryColor = R.color.colorPrimary;
            this.primaryDarkColor = R.color.colorPrimaryDark;
            this.accentColor = R.color.colorAccent;
            this.dialogTitle = context.getString(R.string.mfc_dialog_title);
            this.dialogGalleryIcon = R.drawable.ic_media_library;
            this.dialogCameraIcon = R.drawable.ic_camera;
            this.videoDuration = 10;
            this.photoBitmapWidth = 300;
            this.photoBitmapHeigth = 600;
            this.folderName = R.string.app_name;
        }

        public Builder appName(@StringRes int folderName){
            this.folderName = folderName;
            return this;
        }

        public Builder photoSizes(int width, int heigth){
            this.photoBitmapWidth = width;
            this.photoBitmapHeigth = heigth;
            return this;
        }

        public Builder videoDuration(int seconds){
            this.videoDuration = seconds;
            return this;
        }

        public Builder primaryColor(@ColorRes int color){
            this.primaryColor = color;
            return this;
        }

        public Builder primaryDarkColor(@ColorRes int color){
            this.primaryDarkColor = color;
            return this;
        }

        public Builder accentColor(@ColorRes int color){
            this.accentColor = color;
            return this;
        }

        public Builder dialogTitle(String title){
            this.dialogTitle = title;
            return this;
        }

        public Builder dialogTitle(@StringRes int title){
            this.dialogTitle = context.getString(title);
            return this;
        }

        public Builder dialogGalleryIcon(@DrawableRes int icon){
            this.dialogGalleryIcon = icon;
            return this;
        }

        public Builder dialogCameraIcon(@DrawableRes int icon){
            this.dialogCameraIcon = icon;
            return this;
        }

        @UiThread public void build(){
            Intent intent = new Intent(context, MfcDialog.class);
            intent.putExtra(BUILDER_TAG, this);
            ((FragmentActivity)context).startActivityForResult(intent, MFC_RESPONSE);
        }
    }

    public interface OnMfcResultListener extends Serializable{
        void onMfcResult(List<MediaFile> items);
    }

    private class GetPhotoTask extends AsyncTask<Void, Void, Boolean>{
        @Override protected void onPreExecute() {
            super.onPreExecute();
            mPgLoaging.setVisibility(View.VISIBLE);
            mRlDialogBody.setVisibility(View.GONE);
        }

        @Override protected Boolean doInBackground(Void... params) {
            try{
                Bitmap bitmap = MediaUtils.setReducedImageSize(mBuilder.photoBitmapWidth,
                        mBuilder.photoBitmapHeigth, mImageFileLocation);
                bitmap = MediaUtils.rotateBitmap(bitmap, mImageFileLocation);
                MediaUtils.storeBitmap(bitmap, mImageFileLocation);
                return true;
            }catch(Exception ex){
                ex.printStackTrace();
                return false;
            }
        }

        @Override protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if(success){
                MediaFile item = new MediaFile(mImageFileLocation, MediaFileType.IMAGE);
                ArrayList<MediaFile> items = new ArrayList<>();
                items.add(item);
                if(mBuilder.onMfcResultListener !=null)
                    mBuilder.onMfcResultListener.onMfcResult(items);
                finish();
            }else{
                Toast.makeText(MfcDialog.this, "Ocurrio un error al guardar la foto.", Toast.LENGTH_SHORT).show();
                File photoError = new File(mImageFileLocation);
                if(photoError.exists()){
                    photoError.delete();
                }
                mPgLoaging.setVisibility(View.GONE);
                mRlDialogBody.setVisibility(View.VISIBLE);
            }
        }
    }
}
