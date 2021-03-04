package com.officego.location.marker;

import com.amap.api.maps.model.LatLng;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class RegionItem implements ClusterItem {
    private LatLng mLatLng;
    private String mTitle;
    private String mStreet;//自定义

    public RegionItem(LatLng latLng, String title, String street) {
        mLatLng = latLng;
        mTitle = title;
        mStreet = street;
    }

    @Override
    public LatLng getPosition() {
        return mLatLng;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getStreet() {
        return mStreet;
    }

}
