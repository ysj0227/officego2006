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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.location.marker.ClusterClickListener;
import com.officego.location.marker.ClusterItem;
import com.officego.location.marker.ClusterOverlay;
import com.officego.location.marker.ClusterRender;
import com.officego.location.marker.RegionItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_cluster);
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

            //将地图移动到定位点
            // mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(, ));
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
        //销毁资源
        mClusterOverlay.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onMapLoaded() {
        new Thread() {
            public void run() {
                List<ClusterItem> items = new ArrayList<ClusterItem>();
                //随机10000个点
                for (int i = 0; i < 2000; i++) {
                    double lat = Math.random() + 39.474923;
                    double lon = Math.random() + 116.027116;

                    LatLng latLng = new LatLng(lat, lon, false);
                    RegionItem regionItem = new RegionItem(latLng, "大楼" + i, "长寿路" + 1);
                    items.add(regionItem);
                }
                mClusterOverlay = new ClusterOverlay(mAMap, items,
                        dp2px(getApplicationContext(), clusterRadius),
                        getApplicationContext());
                mClusterOverlay.setClusterRenderer(ClusterActivity.this);
                mClusterOverlay.setOnClusterClickListener(ClusterActivity.this);
            }
        }.start();
    }

    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
        if (clusterItems != null) {
            if (clusterItems.size() == 1) {
                detailsDialog(this);
                return;
            } else if (clusterItems.size() > 1 && clusterItems.size() <= 10) {
                houseListDialog(this,clusterItems);
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
        int radius = dp2px(getApplicationContext(), 80);
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
//                        Color.argb(159, 210, 154, 6)));
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

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void detailsDialog(Context context) {
        Dialog dialog = new Dialog(context, com.owner.R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_map_house_details, null);
        dialog.setContentView(viewLayout);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        dialogWindow.setAttributes(lp);
        viewLayout.findViewById(R.id.btn_cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void houseListDialog(Context context, List<ClusterItem> clusterItems) {
        Dialog dialog = new Dialog(context, com.owner.R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_map_list, null);
        dialog.setContentView(viewLayout);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        dialogWindow.setAttributes(lp);
        RecyclerView recyclerView = viewLayout.findViewById(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MapHouseAdapter(context,clusterItems));
        viewLayout.findViewById(R.id.btn_cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    class MapHouseAdapter extends CommonListAdapter<ClusterItem> {

        public MapHouseAdapter(Context context, List<ClusterItem> list) {
            super(context, R.layout.dialog_map_list_item, list);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void convert(ViewHolder holder, final ClusterItem bean) {
        }
    }
}
