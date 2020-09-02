package com.officego.commonlib.common.message;

/**
 * Created by YangShiJie
 * Data 2020/5/25.
 * Descriptions:
 **/

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.R;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.view.LabelsView;
import com.officego.commonlib.view.RoundImageView;

import java.util.Arrays;
import java.util.List;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;

/**
 * PhoneInfo的布局
 */
@ProviderTag(messageContent = BuildingInfo.class, showPortrait = false, centerInHorizontal = true)
public class BuildingProvider extends IContainerItemProvider.MessageProvider<BuildingInfo> {

    private Context context;

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_include_house_message, viewGroup, false);
        BuildingHolder holder = new BuildingHolder();
        holder.ivHouse = view.findViewById(R.id.iv_house);
        holder.tvHouseName = view.findViewById(R.id.tv_house_name);
        holder.tvLocation = view.findViewById(R.id.tv_location);
        holder.tvRouteMap = view.findViewById(R.id.tv_bus);
        holder.tvPrice = view.findViewById(R.id.tv_price);
        holder.tvStartConversationTime = view.findViewById(R.id.tv_start_conversation_time);
        holder.tvCollect = view.findViewById(R.id.tv_collect);
        holder.lvLabel = view.findViewById(R.id.lv_label);
        holder.tvCollect.setVisibility(View.GONE);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, BuildingInfo info, UIMessage uiMessage) {
        BuildingHolder holder = (BuildingHolder) view.getTag();
        Glide.with(context).load(info.getImgUrl()).into(holder.ivHouse);
        holder.tvHouseName.setText(info.getbuildingName());
        holder.tvStartConversationTime.setText(info.getCreateTime());
        holder.tvPrice.setText(info.getMinSinglePrice());
        if (TextUtils.isEmpty(info.getDistrict())) {
            holder.tvLocation.setVisibility(View.GONE);
        } else {
            holder.tvLocation.setVisibility(View.VISIBLE);
            holder.tvLocation.setText(info.getDistrict());
        }
        if (TextUtils.isEmpty(info.getRouteMap())) {
            holder.tvRouteMap.setVisibility(View.GONE);
        } else {
            holder.tvRouteMap.setVisibility(View.VISIBLE);
            holder.tvRouteMap.setText(info.getRouteMap());
        }
        if (info.getTags() != null) {
            List<String> list = Arrays.asList(info.getTags().split(","));
            holder.lvLabel.setLabels(list, (label, position, data) -> data);
        }
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(BuildingInfo info) {
        return new SpannableString("消息");
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, BuildingInfo info, UIMessage uiMessage) {
        ToastUtils.toastForShort(context,"暂未开通");
    }

    class BuildingHolder {
        RoundImageView ivHouse;
        TextView tvHouseName;
        TextView tvLocation;
        TextView tvRouteMap;
        TextView tvPrice;
        TextView tvStartConversationTime;
        TextView tvCollect;
        LabelsView lvLabel;
    }
}