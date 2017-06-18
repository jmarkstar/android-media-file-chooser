package com.jmarkstar.mfc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jmarkstar.mfc.module.gallery.GalleryActivity;
import com.jmarkstar.mfc.util.DrawableUtils;
import com.jmarkstar.mfc.util.MfcUtils;

/**
 * Created by jmarkstar on 14/06/2017.
 */
public class MfcDialog extends AppCompatActivity {

    public static final String BUILDER_TAG = "builder";
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1000;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2000;

    private RelativeLayout mRlLayout;
    private RelativeLayout mRlDialogContent;
    private TextView mTvTitle;
    private TextView mTvGalleryOption;
    private TextView mTvTakePhotoOption;
    private TextView mTvRecordVideoOption;

    private Builder mBuilder;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfc_dialog);

        mBuilder = getIntent().getParcelableExtra(BUILDER_TAG);

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
            case READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE :{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }
                break;
            }
        }
    }

    private void onInitViews(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            MfcUtils.setStatusBarColor(this, android.R.color.transparent);
        }

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
                                READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
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

            }
        });
        mTvRecordVideoOption.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

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
        startActivity(intent);
        finish();
    }

    public static class Builder implements Parcelable {

        private Context context;
        public int primaryColor;
        public int primaryDarkColor;
        public int accentColor;
        public String dialogTitle;
        public int dialogGalleryIcon;
        public int dialogCameraIcon;

        public Builder(@NonNull Context context){
            this. context = context;
            this.primaryColor = R.color.colorPrimary;
            this.primaryDarkColor = R.color.colorPrimaryDark;
            this.accentColor = R.color.colorAccent;
            this.dialogTitle = context.getString(R.string.mfc_dialog_title);
            this.dialogGalleryIcon = R.drawable.ic_media_library;
            this.dialogCameraIcon = R.drawable.ic_camera;
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
            context.startActivity(intent);
        }

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.primaryColor);
            dest.writeInt(this.primaryDarkColor);
            dest.writeInt(this.accentColor);
            dest.writeString(this.dialogTitle);
            dest.writeInt(this.dialogGalleryIcon);
            dest.writeInt(this.dialogCameraIcon);
        }

        protected Builder(Parcel in) {
            this.primaryColor = in.readInt();
            this.primaryDarkColor = in.readInt();
            this.accentColor = in.readInt();
            this.dialogTitle = in.readString();
            this.dialogGalleryIcon = in.readInt();
            this.dialogCameraIcon = in.readInt();
        }

        public static final Parcelable.Creator<Builder> CREATOR = new Parcelable.Creator<Builder>() {
            @Override public Builder createFromParcel(Parcel source) {
                return new Builder(source);
            }

            @Override public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };
    }
}
