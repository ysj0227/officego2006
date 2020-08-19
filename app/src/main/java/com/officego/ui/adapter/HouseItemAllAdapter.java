package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.ui.home.model.BuildingConditionItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions: 独立办公室 ，在租写字楼
 **/
public class HouseItemAllAdapter extends CommonListAdapter<BuildingConditionItem> {

    private Context context;
    @SuppressLint("UseSparseArrays")
    private Map<Integer, Boolean> map = new HashMap<>();
    private boolean onBind;
    private int checkedPosition = 0;//默认选中第一个

    public HouseItemAllAdapter(Context context, List<BuildingConditionItem> list) {
        super(context, R.layout.home_include_house_independent_office_list_item_all, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final BuildingConditionItem bean) {
        RelativeLayout rlItemHouseAll = holder.getView(R.id.rl_item_house_all);
        TextView tvTop = holder.getView(R.id.tv_item_list_top);
        TextView tvBottom = holder.getView(R.id.tv_item_list_bottom);
        tvTop.setText(bean.getTitle());
        tvBottom.setText(bean.getBuildingNum());
        if (checkedPosition == 0) {
            map.put(0, true);
        }
        holder.itemView.setOnClickListener(v -> {
            clickChildNotification(bean.getValue());
            map.clear();
            checkedPosition = holder.getAdapterPosition();
            map.put(holder.getAdapterPosition(), true);
            if (!onBind) {
                notifyDataSetChanged();
            }
        });
        //显示选中的文本
        onBind = true;
        if (map != null && map.containsKey(holder.getAdapterPosition())) {
            setBackGroundColor(true, rlItemHouseAll, tvTop, tvBottom);
        } else {
            setBackGroundColor(false, rlItemHouseAll, tvTop, tvBottom);
        }
        onBind = false;
    }

    /**
     * 点击all  child筛选不同的list 通知
     */
    private void clickChildNotification(String value) {
        BaseNotification.newInstance().postNotificationName(CommonNotifications.independentAll, value);
    }

    /**
     * 设置选中文本颜色
     *
     * @param isSelected
     * @param relativeLayout
     * @param tvTop
     * @param tvBottom
     */
    private void setBackGroundColor(boolean isSelected, RelativeLayout relativeLayout, TextView tvTop, TextView tvBottom) {
        if (isSelected) {
            tvTop.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            tvBottom.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            relativeLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.text_label_main));
        } else {
            tvTop.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
            tvBottom.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
            relativeLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.text_label_edge_white));
        }
    }

}
