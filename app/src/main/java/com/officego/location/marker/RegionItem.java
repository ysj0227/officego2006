package com.officego.location.marker;

import com.amap.api.maps.model.LatLng;

/**
 * Created by yiyi.qi on 16/10/10.
 */

public class RegionItem implements ClusterItem {
    private LatLng mLatLng;
    private String mTitle;
    private String districts;//自定义
    private String business;
    private String mainPic;
    private String address;
    private String price;
    private int btype;
    private int buildingId;
    private String stationName;

    public RegionItem(LatLng latLng, int btype, int buildingId, String title,
                      String mainPic, String districts, String business,
                      String stationName,String address,String price) {
        this.mLatLng = latLng;
        this.btype = btype;
        this.buildingId = buildingId;
        this.mTitle = title;
        this.mainPic = mainPic;
        this.districts = districts;
        this.business = business;
        this.address = address;
        this.price = price;
        this.stationName = stationName;
    }

    @Override
    public LatLng getPosition() {
        return mLatLng;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDistricts() {
        return districts;
    }

    public String getBusiness() {
        return business;
    }

    public String getMainPic() {
        return mainPic;
    }

    public int getBtype() {
        return btype;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public String getStationName() {
        return stationName;
    }
}
