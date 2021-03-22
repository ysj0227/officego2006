package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.RoundImageView;
import com.officego.location.marker.ClusterItem;
import com.officego.location.marker.RegionItem;
import com.officego.ui.home.BuildingDetailsActivity_;
import com.officego.ui.home.BuildingDetailsJointWorkActivity_;

import java.util.List;

/**
 * Created by shijie
 * Date 2021/3/8
 **/
public class MapHouseAdapter extends CommonListAdapter<ClusterItem> {

    private Context context;

    public MapHouseAdapter(Context context, List<ClusterItem> list) {
        super(context, R.layout.dialog_map_list_item, list);
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void convert(ViewHolder holder, final ClusterItem bean) {
        RoundImageView ivHouse = holder.getView(R.id.iv_house);
        TextView name = holder.getView(R.id.tv_house_name);
        TextView location = holder.getView(R.id.tv_location);
        TextView tvLines = holder.getView(R.id.tv_lines);
        TextView tvPrice = holder.getView(R.id.tv_price);
        RegionItem mRegionItem = (RegionItem) bean;
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options())
                .load(mRegionItem.getMainPic()).into(ivHouse);
        name.setText(mRegionItem.getTitle());
        location.setText(mRegionItem.getDistricts() + mRegionItem.getAddress());
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
        String line;
        if (mRegionItem.getMapBean() != null && mRegionItem.getMapBean().getStationline().size() > 0) {
            String workTime = mRegionItem.getMapBean().getNearbySubwayTime().get(0);
            String stationLine = mRegionItem.getMapBean().getStationline().get(0);
            String stationName = mRegionItem.getMapBean().getStationNames().get(0);
            line = lines(workTime, stationLine, stationName);
            tvLines.setVisibility(View.VISIBLE);
        } else {
            line = "";
            tvLines.setVisibility(View.GONE);
        }
        tvLines.setText(line);
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

    private String lines(String workTime, String stationLine, String stationName) {
        return "步行" + workTime + "分钟到「" + stationLine + "号线 · " + stationName + "」";
    }
}
