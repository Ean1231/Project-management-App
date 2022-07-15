package com.example.pimoscanner.basemaps;

import android.graphics.Bitmap;

import com.esri.arcgisruntime.portal.PortalItem;

public class BasemapItem {
    public PortalItem item;

    public Bitmap itemThumbnail;

    public BasemapItem(PortalItem item, Bitmap bt) {
        this.item = item;
        itemThumbnail = bt;
    }
}
