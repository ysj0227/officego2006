package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.view.LabelsView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.model.LabelBean;
import com.officego.ui.collect.model.CollectBuildingBean;
import com.officego.ui.home.BuildingDetailsActivity_;
import com.officego.ui.home.BuildingDetailsJointWorkActivity_;
import com.officego.ui.home.utils.BundleUtils;
import com.officego.commonlib.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions:
 **/
public class CollectOfficeBuildingAdapter extends CommonListAdapter<CollectBuildingBean.ListBean> {

    private Context context;
    private View view;

    public CollectOfficeBuildingAdapter(Context context, List<CollectBuildingBean.ListBean> list) {
        super(context, R.layout.item_house_message, list);
        this.context = context;

    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void convert(ViewHolder holder, final CollectBuildingBean.ListBean bean) {
        ImageView ivHouse = holder.getView(R.id.iv_house);
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getMainPic()).into(ivHouse);
        holder.setText(R.id.tv_house_name, bean.getName());
        holder.setText(R.id.tv_location, bean.getBusinessDistrict());
        TextView tvBus = holder.getView(R.id.tv_bus);
        String line;
        if (bean.getBuildingMap() != null && bean.getBuildingMap().getStationline().size() > 0) {
            String workTime = bean.getBuildingMap().getNearbySubwayTime().get(0);
            String stationLine = bean.getBuildingMap().getStationline().get(0);
            String stationName = bean.getBuildingMap().getStationNames().get(0);
            line = "步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName;
            tvBus.setText(line);
            tvBus.setVisibility(View.VISIBLE);
        } else {
            tvBus.setVisibility(View.GONE);
        }
        holder.setText(R.id.tv_km, bean.getDistance());
        TextView price = holder.getView(R.id.tv_price);
        TextView unit = holder.getView(R.id.tv_unit);
        //1:楼盘 写字楼,2:网点 联合办公
        if (bean.getBtype() == 1) {
            price.setText("¥" + (bean.getMinDayPrice() == null ? "0.0" : bean.getMinDayPrice()));
            unit.setText("/m²/天起");
        } else if (bean.getBtype() == 2) {
            price.setText("¥" + (bean.getSeatMonthPrice() == null ? "0.0" : bean.getSeatMonthPrice()));
            unit.setText("/位/月");
        }
        addLabel(holder, bean);
        gotoDetails(holder, bean);
    }

    //add label
    private void addLabel(ViewHolder holder, CollectBuildingBean.ListBean bean) {
        TextView tvMore = holder.getView(R.id.tv_more);
        TextView tvTagMore = holder.getView(R.id.tv_tag_more);
        tvMore.setVisibility(View.GONE);
        LabelsView labelsView = holder.getView(R.id.ll_house_area);
        //1:楼盘 写字楼
        if (bean.getBtype() == 1) {
            List<Object> areaMap = new ArrayList<>();
            if (bean.getAreaMap() != null && bean.getAreaMap().size() > 0) {
                labelsView.setLabelTextSize(CommonHelper.sp2px(context, 12));
                labelsView.setWordMargin(24);
                labelsView.setLabelTextPadding(24, 8, 24, 8);
                labelsView.setLabelTextColor(ContextCompat.getColor(context, R.color.text_66));
                labelsView.setLabelBackgroundResource(R.drawable.text_label_gray);
                labelsView.setVisibility(View.VISIBLE);
                areaMap = bean.getAreaMap();
            } else {
                labelsView.setVisibility(View.GONE);
            }
            labelsView.setLabels(areaMap, (label, position, data) -> data == null ? "0" : data.toString() + "m²");
        } else {//2:网点 联合办公
            labelsView.setLabelTextSize(CommonHelper.sp2px(context, 12));
            labelsView.setWordMargin(40);
            labelsView.setLabelTextPadding(8, 5, 8, 5);
            labelsView.setLabelTextColor(ContextCompat.getColor(context, R.color.white));
            labelsView.setLabelBackgroundResource(R.drawable.text_label_deep_blue);
            ArrayList<LabelBean> officeList = new ArrayList<>();
            if (bean.getOpenStation() == 0 && bean.getIndependenceOffice() == 0) {
                labelsView.setVisibility(View.GONE);
            } else if (bean.getIndependenceOffice() > 0) {
                labelsView.setVisibility(View.VISIBLE);
                officeList.add(new LabelBean("独立办公室" + bean.getIndependenceOffice() + "间", 1));
            } else if (bean.getOpenStation() > 0) {
                officeList.add(new LabelBean("开放工位" + bean.getOpenStation() + "个", 2));
            }
            labelsView.setLabels(officeList, (label, position, data) -> data.getName());
        }
        //房源特色
        LabelsView lvHouseTags = holder.getView(R.id.ll_house_characteristic);
        List<CollectBuildingBean.ListBean.TagsBean> setLabelList = new ArrayList<>();
        List<CollectBuildingBean.ListBean.TagsBean> tagsList = bean.getTags();
        if (tagsList == null || tagsList.size() == 0) {
            lvHouseTags.setVisibility(View.GONE);
        } else if (tagsList.size() <= 4) {
            lvHouseTags.setVisibility(View.VISIBLE);
            tvTagMore.setVisibility(View.GONE);
            setLabelList = tagsList;
        } else {//当大于四个时只显示4个
            lvHouseTags.setVisibility(View.VISIBLE);
            tvTagMore.setVisibility(View.VISIBLE);
            for (int i = 0; i < 4; i++) {
                setLabelList.add(tagsList.get(i));
            }
        }
        lvHouseTags.setLabels(setLabelList, (label, position, data) -> data.getDictCname());
    }

    /**
     * 进入详情
     * 0: 下架(未发布),1: 上架(已发布) ;2:资料待完善 ,3: 置顶推荐;4:已售完;5:删除'
     */
    private void gotoDetails(ViewHolder holder, CollectBuildingBean.ListBean bean) {
        holder.itemView.setOnClickListener(v -> {
            int isFailed = bean.getIsfailure();
            if (isFailed == 1 || isFailed == 2 || isFailed == 3) {
                if (bean.getBtype() == 1) {
                    BuildingDetailsActivity_.intent(context).
                            mBuildingBean(BundleUtils.BuildingMessage(1, bean.getId())).start();
                } else {
                    BuildingDetailsJointWorkActivity_.intent(context)
                            .mBuildingBean(BundleUtils.BuildingMessage(2, bean.getId())).start();
                }
            } else {
                dialog(isFailed);
            }
        });
    }

    private void dialog(int isFailed) {
        String title = "";
        if (isFailed == 0) {
            title = "楼盘已下架";
        } else if (isFailed == 4) {
            title = "楼盘已售完";
        } else if (isFailed == 5) {
            title = "楼盘已删除";
        }
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(title)
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    dialog12.dismiss();
                }).create();
        dialog.showWithOutTouchable(false);
    }
}
