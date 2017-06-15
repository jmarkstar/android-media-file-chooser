package com.jmarkstar.mfc;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.jmarkstar.mfc.util.DrawableUtils;

/**
 * Created by jmarkstar on 14/06/2017.
 */
public class MfcDialog extends Dialog {

    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1000;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;

    private Builder mBuilder;

    private TextView mTvTitle;
    private TextView mTvGalleryOption;
    private TextView mTvCameraOption;

    public MfcDialog(Builder builder) {
        super(builder.context);
        mBuilder = builder;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog);
        onInitViews();
        onSetup();
    }

    private void onInitViews(){
        mTvTitle = (TextView)findViewById(R.id.mfc_dialog_title);
        mTvGalleryOption = (TextView)findViewById(R.id.mfc_gallery_option);
        mTvCameraOption = (TextView)findViewById(R.id.mfc_camera_option);
        mTvGalleryOption.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions((Activity) mBuilder.context,
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
        mTvCameraOption.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

            }
        });
    }

    private void onSetup(){
        int accentColor = ContextCompat.getColor(getContext(), mBuilder.accentColor);

        mTvTitle.setText(mBuilder.dialogTitle);
        mTvGalleryOption.setCompoundDrawablesWithIntrinsicBounds( DrawableUtils.tint(getContext(),
                mBuilder.dialogGalleryIcon, accentColor), null, null, null);

        mTvCameraOption.setCompoundDrawablesWithIntrinsicBounds( DrawableUtils.tint(getContext(),
                mBuilder.dialogCameraIcon, accentColor), null, null, null);
    }

    private void openGallery(){
        Intent intent = new Intent(getContext(), GalleryActivity.class);
        getContext().startActivity(intent);
    }

    public static class Builder {

        private Context context;
        private int primaryColor;
        private int primaryDarkColor;
        private int accentColor;
        private String dialogTitle;
        private int dialogGalleryIcon;
        private int dialogCameraIcon;

        public Builder(@NonNull Context context){
            this. context = context;
            this.primaryColor = R.color.colorPrimary;
            this.primaryDarkColor = R.color.colorPrimaryDark;
            this.accentColor = R.color.colorAccent;
            this.dialogTitle = context.getString(R.string.mfc_dialog_title);
            this.dialogGalleryIcon = R.drawable.ic_media_library;
            this.dialogCameraIcon = R.drawable.ic_camera;
        }

        public MfcDialog.Builder primaryColor(@ColorRes int color){
            this.primaryColor = color;
            return this;
        }

        public MfcDialog.Builder primaryDarkColor(@ColorRes int color){
            this.primaryDarkColor = color;
            return this;
        }

        public MfcDialog.Builder accentColor(@ColorRes int color){
            this.accentColor = color;
            return this;
        }

        public MfcDialog.Builder dialogTitle(String title){
            this.dialogTitle = title;
            return this;
        }

        public MfcDialog.Builder dialogTitle(@StringRes int title){
            this.dialogTitle = context.getString(title);
            return this;
        }

        public MfcDialog.Builder dialogGalleryIcon(@DrawableRes int icon){
            this.dialogGalleryIcon = icon;
            return this;
        }

        public MfcDialog.Builder dialogCameraIcon(@DrawableRes int icon){
            this.dialogCameraIcon = icon;
            return this;
        }

        @UiThread public MfcDialog build(){
            return new MfcDialog(this);
        }

        @UiThread public MfcDialog show(){
            MfcDialog dialog = build();
            dialog.show();
            return dialog;
        }
    }

}
