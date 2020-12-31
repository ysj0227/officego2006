package com.officego.ui.adapter;

import android.app.Activity;
import android.text.Html;
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
import com.officego.h5.WebViewCouponActivity_;
import com.officego.ui.holder.DiscountHolder;
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
    private final List<String> mData;
    private final Activity context;

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
        ITEM_TYPE_DISCOUNT,
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
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_DISCOUNT.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_banner_type_discount, parent, false);
            return new DiscountHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String bean = mData.get(position);
        if (holder instanceof HotsHolder) {
            hotsView(holder, bean);
        } else if (holder instanceof TipsHolder) {
            tipsView(holder, bean);
        } else if (holder instanceof House1Holder) {
            house1View(holder, bean);
        } else if (holder instanceof House2Holder) {
            house2View(holder, bean);
        } else if (holder instanceof MeetingHolder) {
            meetingView(holder, bean);
        } else if (holder instanceof DiscountHolder) {
            discountView(holder, bean);
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
        } else if (position == 9) {
            return ITEM_TYPE.ITEM_TYPE_DISCOUNT.ordinal();
        } else {
            return ITEM_TYPE.ITEM_TYPE_HOT.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //热门
    private void hotsView(RecyclerView.ViewHolder holder, String bean) {
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(((HotsHolder) holder).ivImageHots);
        ((HotsHolder) holder).tvName.setText("金融街海伦中心");
        ((HotsHolder) holder).tvLocation.setText("2.0Km | 闵行区-莘庄");
        ((HotsHolder) holder).tvLines.setText("步行20分钟到「16号线·龙阳路」");
        ((HotsHolder) holder).tvRmbMoney.setText("2000");
        ((HotsHolder) holder).tvUnit.setText("/m²/天起");
        ((HotsHolder) holder).tvChatTime.setText("房东2天前来过");
        ((HotsHolder) holder).tvOfficeIndependent.setText(Html.fromHtml("办公室<font color='#46C3C2'>6</font>间"));
        ((HotsHolder) holder).tvOpenSeats.setText(Html.fromHtml("开放工位<font color='#46C3C2'>9</font>个"));
    }

    //生活小知识
    private void tipsView(RecyclerView.ViewHolder holder, String bean) {
        List<String> list = new ArrayList();
        list.add("#白月光#");
        list.add("#蓝色的天空#");
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(((TipsHolder) holder).ivImageTips);
        ((TipsHolder) holder).llLabelsTips.setLabels(list, (label, pos, data) -> data);
        ((TipsHolder) holder).tvTips.setText("大家都知道价格、位置、交通、物业等决定着你的企业选址的重要因素。但如果某些写字楼、产业园、创意园能受到政策的扶持，比如能享受到减免税费、折扣租…");
    }

    //房源-单图
    private void house1View(RecyclerView.ViewHolder holder, String bean) {
        List<String> list = new ArrayList();
        list.add("#蓝色的天空#");
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(((House1Holder) holder).ivImage);
        ((House1Holder) holder).llLabelsHouse.setLabels(list, (label, pos, data) -> data);
        ((House1Holder) holder).tvName.setText("陆家嘴世纪金融广场3号楼");
        ((House1Holder) holder).tvLocation.setText("2.0Km | 闵行区-莘庄");
        ((House1Holder) holder).tvLines.setText("步行20分钟到「2号线·龙阳路」");
        ((House1Holder) holder).tvRmbMoney.setText("1000");
        ((House1Holder) holder).tvUnit.setText("/月/起");
    }

    //房源-多图
    private void house2View(RecyclerView.ViewHolder holder, String bean) {
        List<String> list = new ArrayList();
        list.add("#白月光#");
        String a = "https://img.officego.com/building/1599535447621.jpg?x-oss-process=style/small";
        String b = "https://img.officego.com/building/1591868828854.jpg?x-oss-process=style/small";
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(a).into(((House2Holder) holder).ivImage);
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(((House2Holder) holder).rivHouseRightUp);
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(b).into(((House2Holder) holder).rivHouseRightDown);
        ((House2Holder) holder).llLabelsHouse.setLabels(list, (label, pos, data) -> data);
        ((House2Holder) holder).tvName.setText("陆家嘴世纪金融广场6号楼");
        ((House2Holder) holder).tvRmbMoney.setText("6600");
        ((House2Holder) holder).tvUnit.setText("/月/起");
        ((House2Holder) holder).tvTips.setText("嘉华中心是一座 45 层的甲级办公楼，位于淮海中路战略要地。作为上海最著名的商务区之一");
    }

    //会议室
    private void meetingView(RecyclerView.ViewHolder holder, String bean) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        ((MeetingHolder) holder).rvMeeting.setLayoutManager(layoutManager);
        List<String> listBrand = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            listBrand.add("https://img.officego.com/building/1594194990729.jpg?x-oss-process=style/small");
        }
        ((MeetingHolder) holder).rvMeeting.setAdapter(new MeetingAdapter(context, listBrand));
        ((MeetingHolder) holder).tvMeetingMore.setOnClickListener(view -> WebViewCouponActivity_.intent(context).amountRange("").start());
    }

    //打折
    private void discountView(RecyclerView.ViewHolder holder, String bean) {
        List<String> lists = new ArrayList();
        lists.add("地铁上盖");
        lists.add("精装修");
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(((DiscountHolder) holder).ivHouse);
        ((DiscountHolder) holder).llLabelsDiscount.setLabels(lists, (label, pos, data) -> data);
        ((DiscountHolder) holder).tvName.setText("大西洋国际大厦");
        ((DiscountHolder) holder).tvLocation.setText("2.0Km | 闵行区-莘庄");
        ((DiscountHolder) holder).tvLines.setText("步行20分钟到「2号线·龙阳路」");
        ((DiscountHolder) holder).tvRmbMoney.setText("1000");
        ((DiscountHolder) holder).tvUnit.setText("/月/起");
        ((DiscountHolder) holder).tvDiscount.setText("¥2100.0/月起");
    }

}