package com.officego.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static enum ITEM_TYPE {
        ITEM_TYPE_HOT,
        ITEM_TYPE_TIPS
    }

    //数据集
    public List<String> mData;

    public HomeAdapter(List<String> data) {
        super();
        this.mData = data;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_HOT.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_hots_house, parent, false);
            return new ThemeVideoHolder(view);

        } else if (viewType == ITEM_TYPE.ITEM_TYPE_TIPS.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_banner_type_tips, parent, false);
            return new VideoViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String bean = mData.get(position);
        if (holder instanceof ThemeVideoHolder) {
//            ((ThemeVideoHolder) holder).tvName.setText("sss");
            Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(((ThemeVideoHolder) holder).ivImageHots);
        } else if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).tvTips.setText("大家都知道价格、位置、交通、物业等决定着你的企业选址的重要因素。但如果某些写字楼、产业园、创意园能受到政策的扶持，比如能享受到减免税费、折扣租…");
            Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(((VideoViewHolder) holder).ivImageTips);
        }
    }


    public int getItemViewType(int position) {
        if (position==2){
            return ITEM_TYPE.ITEM_TYPE_TIPS.ordinal();
        }else {
            return ITEM_TYPE.ITEM_TYPE_HOT.ordinal();
        }
    }
    
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ThemeVideoHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public RoundImageView ivImageHots;

        public ThemeVideoHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_house_name);
            ivImageHots = itemView.findViewById(R.id.iv_house);
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTips;
        public RoundImageView ivImageTips;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ivImageTips = itemView.findViewById(R.id.riv_tips);
            tvTips = itemView.findViewById(R.id.tv_tips);
        }
    }
}