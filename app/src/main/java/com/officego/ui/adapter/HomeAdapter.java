package com.officego.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.ui.holder.HotsHolder;
import com.officego.ui.holder.House1Holder;
import com.officego.ui.holder.House2Holder;
import com.officego.ui.holder.MeetingHolder;
import com.officego.ui.holder.TipsHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<String> mData;
    private Activity context;

    public HomeAdapter(BaseActivity mActivity, List<String> data) {
        this.context = mActivity;
        this.mData = data;
    }

    public enum ITEM_TYPE {
        ITEM_TYPE_HOT,
        ITEM_TYPE_TIPS,
        ITEM_TYPE_HOUSE1,
        ITEM_TYPE_HOUSE2,
        ITEM_TYPE_MEETING,
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_HOT.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_hots_house, parent, false);
            return new HotsHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_TIPS.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_banner_type_tips, parent, false);
            return new TipsHolder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_HOUSE1.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_banner_type_house1, parent, false);
            return new House1Holder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_HOUSE2.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_banner_type_house2, parent, false);
            return new House2Holder(view);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_MEETING.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_banner_type_meeting, parent, false);
            return new MeetingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String bean = mData.get(position);
        List<String> list = new ArrayList();
        list.add("#白月光#");
        list.add("#蓝色的天空#");

        if (holder instanceof HotsHolder) {
            Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(((HotsHolder) holder).ivImageHots);
        } else if (holder instanceof TipsHolder) {
            //生活小知识
            ((TipsHolder) holder).tvTips.setText("大家都知道价格、位置、交通、物业等决定着你的企业选址的重要因素。但如果某些写字楼、产业园、创意园能受到政策的扶持，比如能享受到减免税费、折扣租…");
            Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(((TipsHolder) holder).ivImageTips);
            ((TipsHolder) holder).llLabelsTips.setLabels(list, (label, pos, data) -> data);
        } else if (holder instanceof House1Holder) {
            //房源1
            Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(((House1Holder) holder).ivImage);
            ((House1Holder) holder).llLabelsHouse.setLabels(list, (label, pos, data) -> data);
        } else if (holder instanceof House2Holder) {
            //房源2
            String a = "https://img.officego.com/building/1599535447621.jpg?x-oss-process=style/small";
            String b = "https://img.officego.com/building/1591868828854.jpg?x-oss-process=style/small";
            ((House2Holder) holder).tvTips.setText("嘉华中心是一座 45 层的甲级办公楼，位于淮海中路战略要地。作为上海最著名的商务区之一，这里吸引了众多《财富》世界 500 强企业入驻。时尚的室内环境为嘉华中心…");
            Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(a).into(((House2Holder) holder).ivImage);
            Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(((House2Holder) holder).rivHouseRightUp);
            Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(b).into(((House2Holder) holder).rivHouseRightDown);
            ((House2Holder) holder).llLabelsHouse.setLabels(list, (label, pos, data) -> data);
        } else if (holder instanceof MeetingHolder) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            ((MeetingHolder) holder).rvMeeting.setLayoutManager(layoutManager);
            List<String> listBrand = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                listBrand.add("https://img.officego.com/building/1600411301880.png?x-oss-process=style/small");
            }
            ((MeetingHolder) holder).rvMeeting.setAdapter(new NewsAdapter(context, listBrand));
        }
    }


    public int getItemViewType(int position) {
        if (position == 2) {
            return ITEM_TYPE.ITEM_TYPE_TIPS.ordinal();
        } else if (position == 3) {
            return ITEM_TYPE.ITEM_TYPE_MEETING.ordinal();
        } else if (position == 5) {
            return ITEM_TYPE.ITEM_TYPE_HOUSE1.ordinal();
        } else if (position == 7) {
            return ITEM_TYPE.ITEM_TYPE_HOUSE2.ordinal();
        } else {
            return ITEM_TYPE.ITEM_TYPE_HOT.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}