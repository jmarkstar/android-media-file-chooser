package com.jmarkstar.mfc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by jmarkstar on 17/06/2017.
 */
public class GalleryItem implements Parcelable {

    private String pathName;
    private GalleryItemType itemType;
    private boolean selected;

    public GalleryItem(@NonNull String pathName, @NonNull GalleryItemType itemType) {
        this.itemType = itemType;
        this.pathName = pathName;
        this.selected = false;
    }

    public GalleryItem(Parcel in) {
        this.pathName = in.readString();
        int tmpItemType = in.readInt();
        this.itemType = tmpItemType == -1 ? null : GalleryItemType.values()[tmpItemType];
        this.selected = in.readByte() != 0;
    }

    public String getPathName() {
        return pathName;
    }

    public GalleryItemType getItemType() {
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

    public static final Parcelable.Creator<GalleryItem> CREATOR = new Parcelable.Creator<GalleryItem>() {
        @Override public GalleryItem createFromParcel(Parcel source) {
            return new GalleryItem(source);
        }

        @Override public GalleryItem[] newArray(int size) {
            return new GalleryItem[size];
        }
    };

    @Override public String toString() {
        return "GalleryItem{" +
                "pathName='" + pathName + '\'' +
                ", itemType=" + itemType +
                ", selected=" + selected +
                '}';
    }

    @Override public boolean equals(Object obj) {
        if(obj instanceof GalleryItem){
            GalleryItem objGalleryItem = (GalleryItem)obj;
            if(this.pathName.equals(objGalleryItem.getPathName())){
                return true;
            }
        }
        return false;
    }
}
