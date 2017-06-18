package com.jmarkstar.mfc.model;

import android.support.annotation.NonNull;
import java.io.File;

/**
 * Created by jmarkstar on 17/06/2017.
 */
public class GalleryItem extends File {

    private String itemName;
    private GalleryItemType itemType;
    private boolean selected;

    public GalleryItem(@NonNull String pathname, @NonNull GalleryItemType itemType) {
        super(pathname);
        this.itemType = itemType;
        this.itemName = pathname.substring(pathname.lastIndexOf("/")+1);
        this.selected = false;
    }

    public String getItemName() {
        return itemName;
    }

    public GalleryItemType getItemType() {
        return itemType;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override public String toString() {
        return "GalleryItem{" +
                "itemName='" + itemName + '\'' +
                ", itemType=" + itemType +
                ", selected=" + selected +
                "} " + super.toString();
    }
}
