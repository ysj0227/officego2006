package com.officego.ui.adapter;

import android.app.Activity;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.h5.WebViewCouponActivity_;
import com.officego.ui.holder.DiscountHolder;
import com.officego.ui.holder.HotsHolder;
import com.officego.ui.holder.House1Holder;
import com.officego.ui.holder.House2Holder;
import com.officego.ui.holder.MeetingHolder;
import com.officego.ui.holder.TipsHolder;
import com.officego.ui.home.BuildingDetailsActivity_;
import com.officego.ui.home.BuildingDetailsJointWorkActivity_;
import com.officego.ui.home.model.HomeHotBean;
import com.officego.ui.home.model.HomeMeetingBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final HomeMeetingBean.DataBean meetData;
    private final List<HomeHotBean.DataBean.ListBean> mData;
    private final Activity context;
    private final String DAY_UNIT = "/m²/天起";
    private final String MONTH_UNIT = "/位/月起";

    public HomeAdapter(BaseActivity mActivity, HomeMeetingBean.DataBean meetData,
                       List<HomeHotBean.DataBean.ListBean> data) {
        this.context = mActivity;
        this.meetData = meetData;
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
        HomeHotBean.DataBean.ListBean bean = mData.get(position);
        if (holder instanceof HotsHolder) {
            hotsView(holder, bean);
        } else if (holder instanceof TipsHolder) {
            tipsView(holder, bean);
        } else if (holder instanceof House1Holder) {
            house1View(holder, bean);
        } else if (holder instanceof House2Holder) {
            house2View(holder, bean);
        } else if (holder instanceof MeetingHolder) {
            meetingView(holder);
        } else if (holder instanceof DiscountHolder) {
            discountView(holder, bean);
        }
    }

    //1:楼盘重点版,2:楼盘文案版,3:网点版,4:房源特价
    public int getItemViewType(int position) {
        HomeHotBean.DataBean.ListBean bean = mData.get(position);
        if (bean == null || bean.getBannerMap() == null || bean.getBannerMap().getLayoutType() == null) {
            if (!TextUtils.isEmpty(meetData.getMeetingRoomLocation()) &&
                    Integer.parseInt(meetData.getMeetingRoomLocation()) == position) {
                return ITEM_TYPE.ITEM_TYPE_MEETING.ordinal();
            }
            return ITEM_TYPE.ITEM_TYPE_HOT.ordinal();
        } else if (TextUtils.equals("1", bean.getBannerMap().getLayoutType())) {
            return ITEM_TYPE.ITEM_TYPE_HOUSE1.ordinal();
        } else if (TextUtils.equals("2", bean.getBannerMap().getLayoutType())) {
            return ITEM_TYPE.ITEM_TYPE_TIPS.ordinal();
        } else if (TextUtils.equals("3", bean.getBannerMap().getLayoutType())) {
            return ITEM_TYPE.ITEM_TYPE_HOUSE2.ordinal();
        } else if (TextUtils.equals("4", bean.getBannerMap().getLayoutType())) {
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
    private void hotsView(RecyclerView.ViewHolder holder, HomeHotBean.DataBean.ListBean bean) {
        String mPrice = bean.getMinDayPrice() == null ? "0" : bean.getMinDayPrice().toString();
        String distance = TextUtils.isEmpty(bean.getDistance()) ? "" : bean.getDistance() + "Km | ";
        String business = bean.getBusinessDistrict();
        String line;
        if (bean.getBuildingMap() != null && bean.getBuildingMap().getStationline().size() > 0) {
            String workTime = bean.getBuildingMap().getNearbySubwayTime().get(0);
            String stationLine = bean.getBuildingMap().getStationline().get(0);
            String stationName = bean.getBuildingMap().getStationNames().get(0);
            line = "步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName;
            ((HotsHolder) holder).tvLines.setVisibility(View.VISIBLE);
        } else {
            line = "";
            ((HotsHolder) holder).tvLines.setVisibility(View.GONE);
        }
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getMainPic()).into(((HotsHolder) holder).ivImageHots);
        ((HotsHolder) holder).ivVrFlag.setVisibility(TextUtils.equals("1", bean.getVr()) ? View.VISIBLE : View.GONE);
        ((HotsHolder) holder).tvType.setVisibility(bean.getBtype() == Constants.TYPE_BUILDING ? View.GONE : View.VISIBLE);
        ((HotsHolder) holder).tvName.setText(bean.getName());
        ((HotsHolder) holder).tvLocation.setText(String.format("%s%s", distance, business));
        ((HotsHolder) holder).tvLines.setText(line);
        ((HotsHolder) holder).tvChatTime.setText(bean.getInfotext());
        ((HotsHolder) holder).tvRmbMoney.setText(mPrice);
        ((HotsHolder) holder).tvUnit.setText(bean.getBtype() == Constants.TYPE_BUILDING ? DAY_UNIT : MONTH_UNIT);
        if (bean.getBtype() == Constants.TYPE_BUILDING) {
            ((HotsHolder) holder).tvOpenSeats.setVisibility(View.GONE);
            setOfficeCounts(holder, bean.getHouseCount() == null ? 0 : bean.getHouseCount());
        } else {
            setOfficeCounts(holder, bean.getIndependenceOffice());
            if (bean.getOpenStation() == null || bean.getOpenStation() == 0) {
                ((HotsHolder) holder).tvOpenSeats.setVisibility(View.GONE);
            } else {
                ((HotsHolder) holder).tvOpenSeats.setVisibility(View.VISIBLE);
                String source = "开放工位<font color='#46C3C2'>" + bean.getOpenStation() + "</font>个";
                ((HotsHolder) holder).tvOpenSeats.setText(Html.fromHtml(source));
            }
        }
        gotoDetails(holder, bean);
    }

    private void setOfficeCounts(RecyclerView.ViewHolder holder, int counts) {
        if (counts == 0) {
            ((HotsHolder) holder).tvOfficeIndependent.setVisibility(View.GONE);
        } else {
            ((HotsHolder) holder).tvOfficeIndependent.setVisibility(View.VISIBLE);
            String source = "办公室<font color='#46C3C2'>" + counts + "</font>间";
            ((HotsHolder) holder).tvOfficeIndependent.setText(Html.fromHtml(source));
        }
    }

    //生活小知识_文案版
    private void tipsView(RecyclerView.ViewHolder holder, HomeHotBean.DataBean.ListBean bean) {
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).
                load(bean.getBannerMap().getImg()).into(((TipsHolder) holder).ivImageTips);
        List<HomeHotBean.DataBean.ListBean.BannerMapBean.LabelIdBean> list = bean.getBannerMap().getLabelId();
        if (list == null || list.size() == 0) {
            ((TipsHolder) holder).llLabelsTips.setVisibility(View.GONE);
        } else {
            ((TipsHolder) holder).llLabelsTips.setVisibility(View.VISIBLE);
            ((TipsHolder) holder).llLabelsTips.setLabels(list, (label, pos, data) -> "#" + data.getDictCname() + "#");
        }
        if (bean.getBannerMap().getRemark() == null || TextUtils.isEmpty(bean.getBannerMap().getRemark().toString())) {
            ((TipsHolder) holder).tvTips.setVisibility(View.GONE);
        } else {
            ((TipsHolder) holder).tvTips.setVisibility(View.VISIBLE);
            ((TipsHolder) holder).tvTips.setText(bean.getBannerMap().getRemark().toString());
        }
        gotoDetails(holder, bean);
    }

    //房源-单图
    private void house1View(RecyclerView.ViewHolder holder, HomeHotBean.DataBean.ListBean bean) {
        String price = bean.getBannerMap().getDayPrice().toString();
        String distance = TextUtils.isEmpty(bean.getDistance()) ? "" : bean.getDistance() + "Km | ";
        String business = bean.getBusinessDistrict();
        String line;
        if (bean.getBuildingMap() != null && bean.getBuildingMap().getStationline().size() > 0) {
            String workTime = bean.getBuildingMap().getNearbySubwayTime().get(0);
            String stationLine = bean.getBuildingMap().getStationline().get(0);
            String stationName = bean.getBuildingMap().getStationNames().get(0);
            line = "步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName;
            ((House1Holder) holder).tvLines.setVisibility(View.VISIBLE);
        } else {
            line = "";
            ((House1Holder) holder).tvLines.setVisibility(View.GONE);
        }
        List<HomeHotBean.DataBean.ListBean.BannerMapBean.LabelIdBean> list = bean.getBannerMap().getLabelId();
        if (list == null || list.size() == 0) {
            ((House1Holder) holder).llLabelsHouse.setVisibility(View.GONE);
        } else {
            ((House1Holder) holder).llLabelsHouse.setVisibility(View.VISIBLE);
            ((House1Holder) holder).llLabelsHouse.setLabels(list, (label, pos, data) -> "#" + data.getDictCname() + "#");
        }
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options())
                .load(bean.getBannerMap().getImg()).into(((House1Holder) holder).ivImage);
        ((House1Holder) holder).tvName.setText(bean.getBannerMap().getBannerName());
        ((House1Holder) holder).tvLocation.setText(String.format("%s%s", distance, business));
        ((House1Holder) holder).tvLines.setText(line);
        ((House1Holder) holder).tvRmbMoney.setText(price);
        ((House1Holder) holder).tvUnit.setText(bean.getBannerMap().getBtype() == Constants.TYPE_BUILDING ? DAY_UNIT : MONTH_UNIT);
        gotoDetails(holder, bean);
    }

    //房源-多图
    private void house2View(RecyclerView.ViewHolder holder, HomeHotBean.DataBean.ListBean bean) {
        String price = bean.getBannerMap().getDayPrice().toString();
        String mainImg = bean.getBannerMap().getImg();
        List<HomeHotBean.DataBean.ListBean.BannerMapBean.LabelIdBean> list = bean.getBannerMap().getLabelId();
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(mainImg).into(((House2Holder) holder).ivImage);
        List<String> imgList = bean.getBannerMap().getImgList();
        if (imgList != null) {
            if (imgList.size() > 1) {
                Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(imgList.get(0)).into(((House2Holder) holder).rivHouseRightUp);
                Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(imgList.get(1)).into(((House2Holder) holder).rivHouseRightDown);
            } else if (imgList.size() == 1) {
                Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(imgList.get(0)).into(((House2Holder) holder).rivHouseRightUp);
                Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(mainImg).into(((House2Holder) holder).rivHouseRightDown);
            } else {
                Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(mainImg).into(((House2Holder) holder).rivHouseRightUp);
                Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options()).load(mainImg).into(((House2Holder) holder).rivHouseRightDown);
            }
        }
        if (list == null || list.size() == 0) {
            ((House2Holder) holder).llLabelsHouse.setVisibility(View.GONE);
        } else {
            ((House2Holder) holder).llLabelsHouse.setVisibility(View.VISIBLE);
            ((House2Holder) holder).llLabelsHouse.setLabels(list, (label, pos, data) -> "#" + data.getDictCname() + "#");
        }
        ((House2Holder) holder).tvName.setText(bean.getBannerMap().getBannerName());
        ((House2Holder) holder).tvRmbMoney.setText(price);
        ((House2Holder) holder).tvUnit.setText(bean.getBannerMap().getBtype() == Constants.TYPE_BUILDING ? DAY_UNIT : MONTH_UNIT);
        if (bean.getBannerMap().getRemark() == null || TextUtils.isEmpty(bean.getBannerMap().getRemark().toString())) {
            ((House2Holder) holder).tvTips.setVisibility(View.GONE);
        } else {
            ((House2Holder) holder).tvTips.setVisibility(View.VISIBLE);
            ((House2Holder) holder).tvTips.setText(bean.getBannerMap().getRemark().toString());
        }
        gotoDetails(holder, bean);
    }

    //会议室
    private void meetingView(RecyclerView.ViewHolder holder) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        ((MeetingHolder) holder).rvMeeting.setLayoutManager(layoutManager);
        ((MeetingHolder) holder).rvMeeting.setAdapter(new MeetingAdapter(context, meetData.getList()));
        ((MeetingHolder) holder).tvMeetingTitle.setText(meetData.getMeetingRoomTitle());
        ((MeetingHolder) holder).tvMeetingMore.setOnClickListener(view -> WebViewCouponActivity_.intent(context).amountRange("").start());
    }

    //打折
    private void discountView(RecyclerView.ViewHolder holder, HomeHotBean.DataBean.ListBean bean) {
        String price = bean.getBannerMap().getDayPrice().toString();
        String salePrice = bean.getBannerMap().getSalePrice().toString();
        String distance = TextUtils.isEmpty(bean.getDistance()) ? "" : bean.getDistance() + "Km | ";
        String business = bean.getBusinessDistrict();
        String line;
        if (bean.getBuildingMap() != null && bean.getBuildingMap().getStationline().size() > 0) {
            String workTime = bean.getBuildingMap().getNearbySubwayTime().get(0);
            String stationLine = bean.getBuildingMap().getStationline().get(0);
            String stationName = bean.getBuildingMap().getStationNames().get(0);
            line = "步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName;
            ((DiscountHolder) holder).tvLines.setVisibility(View.VISIBLE);
        } else {
            line = "";
            ((DiscountHolder) holder).tvLines.setVisibility(View.GONE);
        }
        List<HomeHotBean.DataBean.ListBean.BannerMapBean.LabelIdBean> list = bean.getBannerMap().getLabelId();
        if (list == null || list.size() == 0) {
            ((DiscountHolder) holder).llLabelsDiscount.setVisibility(View.GONE);
        } else {
            ((DiscountHolder) holder).llLabelsDiscount.setVisibility(View.VISIBLE);
            ((DiscountHolder) holder).llLabelsDiscount.setLabels(list, (label, pos, data) -> data.getDictCname());
        }
        Glide.with(holder.itemView).applyDefaultRequestOptions(GlideUtils.options())
                .load(bean.getBannerMap().getImg()).into(((DiscountHolder) holder).ivHouse);
        ((DiscountHolder) holder).ivVrFlag.setVisibility(TextUtils.equals("1", bean.getVr()) ? View.GONE : View.VISIBLE);
        ((DiscountHolder) holder).tvName.setText(bean.getBannerMap().getBannerName());
        ((DiscountHolder) holder).tvLocation.setText(String.format("%s%s", distance, business));
        ((DiscountHolder) holder).tvLines.setText(line);
        ((DiscountHolder) holder).tvRmbMoney.setText(price);
        if (bean.getBannerMap().getBtype() == Constants.TYPE_BUILDING) {
            ((DiscountHolder) holder).tvUnit.setText(DAY_UNIT);
            ((DiscountHolder) holder).tvDiscount.setText(String.format("¥%s" + DAY_UNIT, salePrice));
        } else {
            ((DiscountHolder) holder).tvUnit.setText(MONTH_UNIT);
            ((DiscountHolder) holder).tvDiscount.setText(String.format("¥%s" + MONTH_UNIT, salePrice));
        }
        gotoDetails(holder, bean);
    }

    //查看详情
    private void gotoDetails(RecyclerView.ViewHolder holder, HomeHotBean.DataBean.ListBean bean) {
        holder.itemView.setOnClickListener(v -> {
            if (bean.getBtype() == Constants.TYPE_BUILDING) {
                BuildingDetailsActivity_.intent(context).mConditionBean(null)
                        .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_BUILDING, bean.getId())).start();
            } else if (bean.getBtype() == Constants.TYPE_JOINTWORK) {
                BuildingDetailsJointWorkActivity_.intent(context).mConditionBean(null)
                        .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_JOINTWORK, bean.getId())).start();
            }
        });
    }
}