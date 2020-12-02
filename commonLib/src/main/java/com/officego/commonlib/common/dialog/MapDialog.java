package com.officego.commonlib.common.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

import com.officego.commonlib.R;
import com.officego.commonlib.utils.ToastUtils;

/**
 * Created by shijie
 * Date 2020/12/2
 **/
public class MapDialog {

    private Context context;
    private double dlat, dlon;
    private String dname;

    public MapDialog(Context context, double dlat, double dlon, String dname) {
        this.context = context;
        this.dlat = dlat;
        this.dlon = dlon;
        this.dname = dname;
        dialog();
    }

    private void dialog() {
        boolean isAMap = checkMapAppsIsExist(context, "com.autonavi.minimap");
        boolean isBaiduMap = checkMapAppsIsExist(context, "com.baidu.BaiduMap");
        final String[] items = {isAMap ? "高德地图" : "高德地图（未安装）",
                isBaiduMap ? "百度地图" : "百度地图（未安装）"};
        new AlertDialog.Builder(context)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
                        if (isAMap) {
                            openGaoDeMap(dlat, dlon, dname);
                        } else {
                            toStoreOpenAMap();
                        }
                    } else if (i == 1) {
                        if (isBaiduMap) {
                            openBaiduMap(dlat, dlon, dname);
                        } else {
                            toStoreOpenBaiDu();
                        }
                    }
                }).create().show();
    }

    /**
     * 打开高德地图（公交出行，起点位置使用地图当前位置）
     * <p>
     * t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车）
     *
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    private void openGaoDeMap(double dlat, double dlon, String dname) {
        if (checkMapAppsIsExist(context, "com.autonavi.minimap")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("androidamap://route?sourceApplication=" + R.string.app_name
                    + "&sname=我的位置&dlat=" + dlat
                    + "&dlon=" + dlon
                    + "&dname=" + dname
                    + "&dev=0&m=0&t=1"));
            context.startActivity(intent);
        } else {
            ToastUtils.toastForShort(context, "高德地图未安装");
        }
    }


    /**
     * 打开百度地图（公交出行，起点位置使用地图当前位置）
     * *
     * * mode = transit（公交）、driving（驾车）、walking（步行）和riding（骑行）. 默认:driving
     * * 当 mode=transit 时 ： sy = 0：推荐路线 、 2：少换乘 、 3：少步行 、 4：不坐地铁 、 5：时间短 、 6：地铁优先
     * *
     * * @param dlat  终点纬度
     * * @param dlon  终点经度
     * * @param dname 终点名称
     */
    private void openBaiduMap(double dlat, double dlon, String dname) {
        if (checkMapAppsIsExist(context, "com.baidu.BaiduMap")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("baidumap://map/direction?origin=我的位置&destination=name:"
                    + dname
                    + "|latlng:" + dlat + "," + dlon
                    + "&mode=transit&sy=3&index=0&target=1"));
            context.startActivity(intent);
        } else {
            ToastUtils.toastForShort(context, "百度地图未安装");
        }
    }

    //去下载高德
    private void toStoreOpenAMap() {
        try {
            String str = "market://details?id=" + "com.autonavi.minimap";
            Intent localIntent = new Intent(Intent.ACTION_VIEW);
            localIntent.setData(Uri.parse(str));
            context.startActivity(localIntent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.toastForShort(context, "高德地图未安装");
        }
    }

    //去下载百度
    private void toStoreOpenBaiDu() {
        try {
            String str = "market://details?id=" + "com.baidu.BaiduMap";
            Intent localIntent = new Intent(Intent.ACTION_VIEW);
            localIntent.setData(Uri.parse(str));
            context.startActivity(localIntent);
        } catch (Exception e) {
            ToastUtils.toastForShort(context, "百度地图未安装");
            e.printStackTrace();
        }
    }


    /**
     * 检测地图应用是否安装
     *
     * @param context     context
     * @param packagename packagename
     * @return false
     */
    private boolean checkMapAppsIsExist(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

}
