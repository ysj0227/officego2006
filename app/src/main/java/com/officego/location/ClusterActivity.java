package com.officego.location;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.config.DataConfig;
import com.officego.location.marker.ClusterClickListener;
import com.officego.location.marker.ClusterItem;
import com.officego.location.marker.ClusterOverlay;
import com.officego.location.marker.ClusterRender;
import com.officego.location.marker.RegionItem;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.adapter.MapHouseAdapter;
import com.officego.ui.adapter.PoiAdapter;
import com.officego.ui.home.SearchHouseListActivity_;
import com.officego.ui.home.model.AllBuildingBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClusterActivity extends BaseActivity implements ClusterRender,
        AMap.OnMapLoadedListener, ClusterClickListener,
        TextWatcher, PoiSearch.OnPoiSearchListener, PoiAdapter.PoiListener {

    private MapView mMapView;
    private RelativeLayout rlQuit;
    private ImageView ivLocation;
    private ClearableEditText etSearch;
    private RecyclerView rvSearchList;
    private TextView tvMapFind;

    private AMap mAMap;
    private PoiSearch poiSearch;
    private Marker marker;
    private Marker locationMarker;

    private int clusterRadius = 100;
    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<>();
    private ClusterOverlay mClusterOverlay;
    private Context context;

    @Override
    protected int activityLayoutId() {
        return R.layout.activity_map_cluster;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        context = ClusterActivity.this;
        mMapView = findViewById(R.id.map);
        rlQuit = findViewById(R.id.rl_quit);
        tvMapFind = findViewById(R.id.tv_map_find);
        ivLocation = findViewById(R.id.iv_location);
        etSearch = findViewById(R.id.et_search);
        rvSearchList = findViewById(R.id.rv_search_list);
        mMapView.onCreate(savedInstanceState);
        rvSearchList.setLayoutManager(new LinearLayoutManager(context));
        init();
        initClick();
    }

    private void init() {
        if (mAMap == null) {
            // 初始化地图
            mAMap = mMapView.getMap();
            mAMap.setOnMapLoadedListener(this);
            //将地图移动到定位点 上海市
            // mAMap.moveCamera(CameraUpdateFactory.zoomTo(10.3F));
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(31.22, 121.48)));
            //点击地图
            mAMap.setOnMapClickListener(latLng -> {
                if (rvSearchList.getVisibility() == View.VISIBLE) {
                    rvSearchList.setVisibility(View.GONE);
                }
            });
        }
    }

    private void initClick() {
        etSearch.addTextChangedListener(this);
        rlQuit.setOnClickListener(view -> finish());
        //定位
        ivLocation.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(Constants.LATITUDE) && mAMap != null) {
                LatLng latLng = new LatLng(Double.parseDouble(Constants.LATITUDE),
                        Double.parseDouble(Constants.LONGITUDE));
                mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                addMarker(latLng);
            }
        });
        tvMapFind.setOnClickListener(view ->
                SearchHouseListActivity_.intent(context).start());
    }

    @Override
    public void onMapLoaded() {
        getMapList();
    }

    private void poiSearch(String keyWord) {
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", "021");
        query.setPageSize(30);// 设置每页最多返回poiitem
        query.setPageNum(1);//设置查询页码
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        mClusterOverlay.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
        if (clusterItems != null) {
            if (clusterItems.size() == 1) {
                houseListDialog(this, clusterItems);
                return;
            } else if (clusterItems.size() > 1 && clusterItems.size() <= 6) {
                houseListDialog(this, clusterItems);
                //如果聚合点经纬度相同
                RegionItem item1 = (RegionItem) clusterItems.get(0);
                RegionItem item2 = (RegionItem) clusterItems.get(clusterItems.size() - 1);
                if (item1.getPosition().latitude == item2.getPosition().latitude &&
                        item1.getPosition().longitude == item2.getPosition().longitude) {
                    return;
                }
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (ClusterItem clusterItem : clusterItems) {
                builder.include(clusterItem.getPosition());
            }
            LatLngBounds latLngBounds = builder.build();
            mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
        }
    }

    //根据聚合数量显示drawCircle的背景颜色
    @Override
    public Drawable getDrawAble(int clusterNum) {
        int radius = CommonHelper.dp2px(context, 100);
        int color = Color.argb(255, 70, 195, 194);
        if (clusterNum == 1) {
            Drawable bitmapDrawable = mBackDrawAbles.get(1);
            if (bitmapDrawable == null) {
                bitmapDrawable = getApplication().getResources().getDrawable(
                        R.mipmap.ic_amap_marker_bg);
                mBackDrawAbles.put(1, bitmapDrawable);
            }
            return bitmapDrawable;
        } else if (clusterNum < 6) {
            Drawable bitmapDrawable = mBackDrawAbles.get(2);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius, color));
                mBackDrawAbles.put(2, bitmapDrawable);
            }
            return bitmapDrawable;
        } else {
            Drawable bitmapDrawable = mBackDrawAbles.get(3);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius, color));
                mBackDrawAbles.put(3, bitmapDrawable);
            }
            return bitmapDrawable;
        }
    }

    private Bitmap drawCircle(int radius, int color) {
        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.drawArc(rectF, 0, 360, true, paint);
        return bitmap;
    }

    private void houseListDialog(Context context, List<ClusterItem> clusterItems) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_map_list, null);
        RecyclerView recyclerView = viewLayout.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MapHouseAdapter(context, clusterItems));
        viewLayout.findViewById(R.id.btn_cancel).setOnClickListener(view -> dialog.dismiss());
        if (clusterItems.size() > 5) {
            int height = getResources().getDimensionPixelSize(R.dimen.dp_108) * 4;
            CommonHelper.setLinearLayoutParams(recyclerView, height);
        }
        dialog.setContentView(viewLayout);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogWindow.setAttributes(lp);
            dialog.show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (TextUtils.isEmpty(charSequence.toString())) {
            rvSearchList.setVisibility(View.GONE);
        } else {
            rvSearchList.setVisibility(View.VISIBLE);
            poiSearch(charSequence.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private PoiAdapter adapter;

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        List<PoiItem> list = poiResult.getPois();
        if (adapter == null) {
            adapter = new PoiAdapter(context, list);
            adapter.setListener(ClusterActivity.this);
            rvSearchList.setAdapter(adapter);
            return;
        }
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void poiItemOnClick(PoiItem data) {
        rvSearchList.setVisibility(View.GONE);
        if (mAMap != null) {
            LatLng latLng = new LatLng(data.getLatLonPoint().getLatitude(),
                    data.getLatLonPoint().getLongitude());
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(15.5F));
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            addSearchMarker(latLng);
        }
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_set_location));//大头针图标
        if (marker != null) {
            marker.remove();
        }
        marker = mAMap.addMarker(markerOptions);
    }

    private void addSearchMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_amap_mark));//大头针图标
        if (locationMarker != null) {
            locationMarker.remove();
        }
        locationMarker = mAMap.addMarker(markerOptions);
    }

    public void getMapList() {
        if (DataConfig.mapList == null) {
            ProgressDialog pd = new ProgressDialog(context);
            pd.setMessage("正在努力加载房源中...");
            pd.setCancelable(true);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            new Thread(() -> OfficegoApi.getInstance().getBuildingList(
                    new RetrofitCallback<List<AllBuildingBean.DataBean>>() {
                        @Override
                        public void onSuccess(int code, String msg, List<AllBuildingBean.DataBean> data) {
                            if (DataConfig.mapList != null) {
                                DataConfig.mapList.clear();
                            }
                            DataConfig.mapList = data;
                            setMapData();
                            pd.dismiss();
                        }

                        @Override
                        public void onFail(int code, String msg, List<AllBuildingBean.DataBean> data) {
                            pd.dismiss();
                        }
                    })).start();
        } else {
            setMapData();
        }
    }

    private void setMapData() {
        List<ClusterItem> items = new ArrayList<>();
        for (int i = 0; i < DataConfig.mapList.size(); i++) {
            AllBuildingBean.DataBean bean = DataConfig.mapList.get(i);
            if (!TextUtils.isEmpty(bean.getLatitude()) && !TextUtils.isEmpty(bean.getLongitude())) {
                double lat = Double.parseDouble(bean.getLatitude());
                double lon = Double.parseDouble(bean.getLongitude());
                int btype = bean.getBtype();
                int buildingId = bean.getId();
                String title = (btype == Constants.TYPE_BUILDING) ? bean.getBuildingName()
                        : bean.getBranchesName();
                String mainPic = bean.getMainPic();
                String districts = bean.getDistricts();
                String business = TextUtils.equals("其他", bean.getAreas()) ? "附近" : bean.getAreas();
                String address = bean.getAddress();
                String price = bean.getMinDayPrice();
                String houseCount = bean.getHouseCount();
                String stationName = bean.getBuildingMap() == null ||
                        bean.getBuildingMap().getStationNames() == null ||
                        bean.getBuildingMap().getStationNames().size() == 0 ||
                        TextUtils.isEmpty(bean.getBuildingMap().getStationNames().get(0)) ? business
                        : bean.getBuildingMap().getStationNames().get(0);
                LatLng latLng = new LatLng(lat, lon, false);
                RegionItem regionItem = new RegionItem(latLng, btype, buildingId, title,
                        mainPic, districts, business, stationName, address, price, houseCount, bean.getBuildingMap());
                items.add(regionItem);
            }
        }
        mClusterOverlay = new ClusterOverlay(mAMap, items,
                CommonHelper.dp2px(context, clusterRadius), context);
        mClusterOverlay.setClusterRenderer(ClusterActivity.this);
        mClusterOverlay.setOnClusterClickListener(ClusterActivity.this);
    }
}
