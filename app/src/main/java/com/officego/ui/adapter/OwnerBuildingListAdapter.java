package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.owner.ChatBuildingBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.view.RoundImageView;
import com.owner.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class OwnerBuildingListAdapter extends CommonListAdapter<ChatBuildingBean.DataBean> {

    private final Context context;
    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;
    private int checkedPosition = -1;

    public ViewDateBuildingListener listener;

    public interface ViewDateBuildingListener {
        void selectedBuilding(ChatBuildingBean.DataBean dataBean);
    }

    public ViewDateBuildingListener getListener() {
        return listener;
    }

    public void setListener(ViewDateBuildingListener listener) {
        this.listener = listener;
    }

    public OwnerBuildingListAdapter(Context context, List<ChatBuildingBean.DataBean> list) {
        super(context, R.layout.conversation_viewing_date_house_message_selected, list);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, final ChatBuildingBean.DataBean data) {
        RoundImageView ivHouseImg = holder.getView(R.id.iv_house);
        TextView tvHouseName = holder.getView(R.id.tv_house_name);
        TextView tvLocation = holder.getView(R.id.tv_location);
        TextView tvRouteMap = holder.getView(R.id.tv_bus);
        TextView tvPrice = holder.getView(R.id.tv_price);
        RadioButton cbBox = holder.getView(R.id.cb_box);

        Glide.with(context).load(data.getMainPic()).into(ivHouseImg);
        tvHouseName.setText(data.getBuildingName());
        if (TextUtils.isEmpty(data.getDistrict())) {
            tvLocation.setVisibility(View.GONE);
        } else {
            tvLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(data.getDistrict());
        }
        if (data.getStationline().size() > 0) {
            tvRouteMap.setVisibility(View.VISIBLE);
            String workTime = data.getNearbySubwayTime().get(0);
            String stationLine = data.getStationline().get(0);
            String stationName = data.getStationNames().get(0);
            tvRouteMap.setText("步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName);
        } else {
            tvRouteMap.setVisibility(View.GONE);
        }
        if (data.getBtype() == Constants.TYPE_BUILDING) {
            tvPrice.setText("¥" + (data.getMinSinglePrice() == null ? "0.0" : data.getMinSinglePrice()) + "/㎡/天起");
        } else {
            tvPrice.setText("¥" + (data.getMinSinglePrice() == null ? "0.0" : data.getMinSinglePrice()) + "/位/月起");
        }

        int position = holder.getAdapterPosition();
        cbBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    map.clear();
                    map.put(position, true);
                    checkedPosition = position;
                } else {
                    map.remove(position);
                    if (map.size() == 0) {
                        checkedPosition = -1; //-1 代表一个都未选择
                    }
                }
                if (listener != null) {
                    listener.selectedBuilding(map != null && map.size() > 0 ? data : null);
                }
                if (!onBind) {
                    notifyDataSetChanged();
                }
            }
        });
        onBind = true;
        cbBox.setChecked(map != null && map.containsKey(position));
        onBind = false;
    }

    //得到当前选中的位置
    public int getCheckedPosition() {
        return checkedPosition;
    }
}