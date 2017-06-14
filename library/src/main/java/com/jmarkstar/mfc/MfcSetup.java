package com.jmarkstar.mfc;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Created by jmarkstar on 13/06/2017.
 */
public class MfcSetup implements Parcelable {

    private int primaryColor;
    private int primaryDarkColor;
    private int accentColor;
    private String dialogTitle;
    private int dialogGalleryIcon;
    private int dialogCameraIcon;

    private MfcSetup(int primaryColor, int primaryDarkColor, int accentColor, String dialogTitle,
                    int dialogGalleryIcon, int dialogCameraIcon) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
        this.accentColor = accentColor;
        this.dialogTitle = dialogTitle;
        this.dialogGalleryIcon = dialogGalleryIcon;
        this.dialogCameraIcon = dialogCameraIcon;
    }

    public static class Builder{

        private Context context;
        private int primaryColor;
        private int primaryDarkColor;
        private int accentColor;
        private String dialogTitle;
        private int dialogGalleryIcon;
        private int dialogCameraIcon;

        public Builder(Context context){
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

        public MfcSetup build(){
            return new MfcSetup(this.primaryColor,
                    this.primaryDarkColor,
                    this.accentColor,
                    this.dialogTitle,
                    this.dialogGalleryIcon,
                    this.dialogCameraIcon);
        }

    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getPrimaryDarkColor() {
        return primaryDarkColor;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public int getDialogGalleryIcon() {
        return dialogGalleryIcon;
    }

    public int getDialogCameraIcon() {
        return dialogCameraIcon;
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

    protected MfcSetup(Parcel in) {
        this.primaryColor = in.readInt();
        this.primaryDarkColor = in.readInt();
        this.accentColor = in.readInt();
        this.dialogTitle = in.readString();
        this.dialogGalleryIcon = in.readInt();
        this.dialogCameraIcon = in.readInt();
    }

    public static final Parcelable.Creator<MfcSetup> CREATOR = new Parcelable.Creator<MfcSetup>() {
        @Override public MfcSetup createFromParcel(Parcel source) {
            return new MfcSetup(source);
        }

        @Override public MfcSetup[] newArray(int size) {
            return new MfcSetup[size];
        }
    };
}
