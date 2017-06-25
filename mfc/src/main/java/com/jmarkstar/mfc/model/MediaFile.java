package com.jmarkstar.mfc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by jmarkstar on 17/06/2017.
 */
public class MediaFile implements Parcelable {

    private String pathName;
    private MediaFileType itemType;
    private boolean selected;

    public MediaFile(@NonNull String pathName, @NonNull MediaFileType itemType) {
        this.itemType = itemType;
        this.pathName = pathName;
        this.selected = false;
    }

    public MediaFile(Parcel in) {
        this.pathName = in.readString();
        int tmpItemType = in.readInt();
        this.itemType = tmpItemType == -1 ? null : MediaFileType.values()[tmpItemType];
        this.selected = in.readByte() != 0;
    }

    public String getPathName() {
        return pathName;
    }

    public MediaFileType getItemType() {
        return itemType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pathName);
        dest.writeInt(this.itemType == null ? -1 : this.itemType.ordinal());
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    public static final Parcelable.Creator<MediaFile> CREATOR = new Parcelable.Creator<MediaFile>() {
        @Override public MediaFile createFromParcel(Parcel source) {
            return new MediaFile(source);
        }

        @Override public MediaFile[] newArray(int size) {
            return new MediaFile[size];
        }
    };

    @Override public String toString() {
        return "MediaFile{" +
                "pathName='" + pathName + '\'' +
                ", itemType=" + itemType +
                ", selected=" + selected +
                '}';
    }

    @Override public boolean equals(Object obj) {
        if(obj instanceof MediaFile){
            MediaFile objGalleryItem = (MediaFile)obj;
            if(this.pathName.equals(objGalleryItem.getPathName())){
                return true;
            }
        }
        return false;
    }
}
