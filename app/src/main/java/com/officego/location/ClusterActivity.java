package com.officego.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;
import com.officego.location.marker.ClusterClickListener;
import com.officego.location.marker.ClusterItem;
import com.officego.location.marker.ClusterOverlay;
import com.officego.location.marker.ClusterRender;
import com.officego.location.marker.RegionItem;
import com.officego.ui.home.BuildingDetailsActivity_;
import com.officego.ui.home.BuildingDetailsJointWorkActivity_;
import com.officego.ui.home.HomeFragment;
import com.officego.ui.home.model.AllBuildingBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClusterActivity extends Activity implements ClusterRender,
        AMap.OnMapLoadedListener, ClusterClickListener {

    private MapView mMapView;
    private RelativeLayout rlQuit;
    private AMap mAMap;

    private int clusterRadius = 100;
    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<>();
    private ClusterOverlay mClusterOverlay;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_cluster);
        context = this;
        mMapView = findViewById(R.id.map);
        rlQuit = findViewById(R.id.rl_quit);
        mMapView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        rlQuit.setOnClickListener(view -> finish());
        if (mAMap == null) {
            // 初始化地图
            mAMap = mMapView.getMap();
            mAMap.setOnMapLoadedListener(this);
            //将地图移动到定位点 上海市
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(31.22, 121.48)));
        }
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
    public void onMapLoaded() {
        new Thread() {
            public void run() {
                List<ClusterItem> items = new ArrayList<ClusterItem>();
                for (int i = 0; i < HomeFragment.beanList.size(); i++) {
                    AllBuildingBean.DataBean bean = HomeFragment.beanList.get(i);
                    double lat = Double.parseDouble(bean.getLatitude());
                    double lon = Double.parseDouble(bean.getLongitude());
                    int btype = bean.getBtype();
                    String title = (btype == Constants.TYPE_BUILDING) ? bean.getBuildingName()
                            : bean.getBranchesName();
                    String mainPic = bean.getMainPic();
                    String districts = bean.getDistricts();
                    String business = TextUtils.equals("其他", bean.getAreas()) ? "附近" : bean.getAreas();
                    String address = bean.getAddress();
                    String price = bean.getMinDayPrice();
                    int buildingId = bean.getId();
                    LatLng latLng = new LatLng(lat, lon, false);
                    RegionItem regionItem = new RegionItem(latLng, btype, buildingId, title,
                            mainPic, districts, business, address, price);
                    items.add(regionItem);
                }
                mClusterOverlay = new ClusterOverlay(mAMap, items,
                        CommonHelper.dp2px(context, clusterRadius), context);
                mClusterOverlay.setClusterRenderer(ClusterActivity.this);
                mClusterOverlay.setOnClusterClickListener(ClusterActivity.this);
            }
        }.start();
    }

    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
        if (clusterItems != null) {
            if (clusterItems.size() == 1) {
                houseListDialog(this, clusterItems);
                return;
            } else if (clusterItems.size() > 1 && clusterItems.size() <= 10) {
                houseListDialog(this, clusterItems);
            }
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (ClusterItem clusterItem : clusterItems) {
            builder.include(clusterItem.getPosition());
        }

        LatLngBounds latLngBounds = builder.build();
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
    }

    //根据聚合数量显示drawCircle的背景颜色
    @Override
    public Drawable getDrawAble(int clusterNum) {
        int radius = CommonHelper.dp2px(context, 80);
        if (clusterNum == 1) {
            Drawable bitmapDrawable = mBackDrawAbles.get(1);
            if (bitmapDrawable == null) {
                bitmapDrawable = getApplication().getResources().getDrawable(
                        R.mipmap.ic_amap_marker_bg);
                mBackDrawAbles.put(1, bitmapDrawable);
            }

            return bitmapDrawable;
        } else if (clusterNum < 5) {
            Drawable bitmapDrawable = mBackDrawAbles.get(2);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(199, 217, 114, 0)));
                mBackDrawAbles.put(2, bitmapDrawable);
            }
            return bitmapDrawable;
        } else if (clusterNum < 10) {
            Drawable bitmapDrawable = mBackDrawAbles.get(3);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(199, 217, 114, 0)));
                mBackDrawAbles.put(3, bitmapDrawable);
            }
            return bitmapDrawable;
        } else {
            Drawable bitmapDrawable = mBackDrawAbles.get(4);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(235, 215, 66, 2)));
                mBackDrawAbles.put(4, bitmapDrawable);
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
            int height = getResources().getDimensionPixelSize(R.dimen.dp_108) * 5;
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

    class MapHouseAdapter extends CommonListAdapter<ClusterItem> {

        public MapHouseAdapter(Context context, List<ClusterItem> list) {
            super(context, R.layout.dialog_map_list_item, list);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void convert(ViewHolder holder, final ClusterItem bean) {
            RoundImageView ivHouse = holder.getView(R.id.iv_house);
            TextView name = holder.getView(R.id.tv_house_name);
            TextView location = holder.getView(R.id.tv_location);
            TextView tvPrice = holder.getView(R.id.tv_price);
            RegionItem mRegionItem = (RegionItem) bean;
            Glide.with(ClusterActivity.this).applyDefaultRequestOptions(GlideUtils.options())
                    .load(mRegionItem.getMainPic()).into(ivHouse);
            name.setText(mRegionItem.getTitle());
            location.setText(mRegionItem.getDistricts()+mRegionItem.getAddress());
            int btype = mRegionItem.getBtype();
            int buildingId = mRegionItem.getBuildingId();
            TextView tvType = holder.getView(R.id.tv_type);
            tvType.setVisibility(btype == Constants.TYPE_BUILDING ? View.GONE : View.VISIBLE);

            String price = mRegionItem.getPrice() == null ? "0" : mRegionItem.getPrice();
            if (btype == 1) {
                tvPrice.setText("¥" + price + "/m²/天起");
            } else if (btype == 2) {
                tvPrice.setText("¥" + price + "/位/月起");
            }
            holder.itemView.setOnClickListener(view -> {
                if (btype == Constants.TYPE_BUILDING) {
                    BuildingDetailsActivity_.intent(context)
                            .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_BUILDING, buildingId)).start();
                } else {
                    BuildingDetailsJointWorkActivity_.intent(context)
                            .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_JOINTWORK, buildingId)).start();
                }
            });
        }
    }
}
