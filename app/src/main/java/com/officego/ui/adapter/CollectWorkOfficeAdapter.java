package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.ui.collect.model.CollectHouseBean;
import com.officego.ui.home.BuildingDetailsChildActivity_;
import com.officego.ui.home.BuildingDetailsJointWorkChildActivity_;
import com.officego.commonlib.common.model.utils.BundleUtils;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions: 独立办公室 ，在租写字楼   child列表
 **/
public class CollectWorkOfficeAdapter extends CommonListAdapter<CollectHouseBean.ListBean> {

    private Context context;

    public CollectWorkOfficeAdapter(Context context, List<CollectHouseBean.ListBean> childList) {
        super(context, R.layout.item_collect_work_office, childList);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, final CollectHouseBean.ListBean bean) {
        RoundImageView ivItemListChild = holder.getView(R.id.iv_item_list_child);
        TextView tvHouseName = holder.getView(R.id.tv_house_name);
        TextView tvLocation = holder.getView(R.id.tv_location);
        TextView tvIsIndependentOffice = holder.getView(R.id.tv_is_independent_office);
        TextView tvItemListChildLeftUp = holder.getView(R.id.tv_item_list_child_left_up);
        TextView tvItemListChildLeftDown = holder.getView(R.id.tv_item_list_child_left_down);
        TextView tvItemListChildCenterUp = holder.getView(R.id.tv_item_list_child_center_up);
        TextView tvItemListChildCenterDown = holder.getView(R.id.tv_item_list_child_center_down);
        TextView tvItemListChildRightUp = holder.getView(R.id.tv_item_list_child_right_up);
        TextView tvItemListChildRightDown = holder.getView(R.id.tv_item_list_child_right_down);
        tvHouseName.setText(bean.getBuildingName());
        if (TextUtils.isEmpty(bean.getBusinessDistrict())) {
            tvLocation.setVisibility(View.GONE);
        } else {
            tvLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(bean.getBusinessDistrict());
        }
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getMainPic()).into(ivItemListChild);
        holder.setText(R.id.tv_type, bean.getBtype() == Constants.TYPE_BUILDING ? "写字楼" : "共享办公");
        //"officeType": 1是独立办公室，2是开放工位
        if (TextUtils.equals("1", bean.getOfficeType())) {
            tvIsIndependentOffice.setVisibility(View.VISIBLE);
            tvItemListChildCenterUp.setVisibility(View.INVISIBLE);
            tvItemListChildCenterDown.setVisibility(View.INVISIBLE);
            tvItemListChildLeftUp.setText(bean.getArea() + "㎡");
            tvItemListChildLeftDown.setText(bean.getSeats() + "工位");
            tvItemListChildRightUp.setText(Html.fromHtml("<font color='#46C3C2'>¥" + bean.getMonthPrice() + "</font>/月"));
            tvItemListChildRightDown.setText(Html.fromHtml("<font color='#46C3C2'>¥" + bean.getDayPrice() + "</font>/位/天"));
        } else {
            tvIsIndependentOffice.setVisibility(View.GONE);
            tvItemListChildCenterUp.setVisibility(View.VISIBLE);
            tvItemListChildCenterDown.setVisibility(View.VISIBLE);
            String seats = "";
            if (bean.getSimple().contains(",")) {
                String str1 = bean.getSimple().substring(0, bean.getSimple().indexOf(","));
                seats = bean.getSimple().substring(str1.length() + 1);
            }
            tvItemListChildLeftUp.setText(CommonHelper.bigDecimal(bean.getArea().toString(), true) + "㎡");
            tvItemListChildLeftDown.setText("最多" + seats + "个工位");
            tvItemListChildCenterUp.setText(Html.fromHtml("<font color='#46C3C2'>¥" + bean.getDayPrice() + "</font>/㎡/天"));
            tvItemListChildCenterDown.setText("¥" + (bean.getMonthPrice() == null ? "0.0" : bean.getMonthPrice()) + "/月");
            tvItemListChildRightUp.setText(bean.getDecoration());
            tvItemListChildRightUp.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
            tvItemListChildRightDown.setText(bean.getFloor() + "/共" + bean.getTotalFloor() + "层");
        }
        gotoDetails(holder, bean);
    }

    /**
     * 进入详情
     * 0: 下架(未发布),1: 上架(已发布) ;2:资料待完善 ,3: 置顶推荐;4:已售完;5:删除'
     */
    private void gotoDetails(ViewHolder holder, CollectHouseBean.ListBean bean) {
        holder.itemView.setOnClickListener(v -> {
            int isFailed = bean.isIsfailure();
            if (isFailed == 1 || isFailed == 2 || isFailed == 3) {
                //独立办公室
                if (TextUtils.equals("1", bean.getOfficeType())) {
                    BuildingDetailsChildActivity_.intent(context)
                            .mChildHouseBean(BundleUtils.houseMessage(1, bean.getId())).start();
                } else {//开放工位
                    BuildingDetailsJointWorkChildActivity_.intent(context)
                            .mChildHouseBean(BundleUtils.houseMessage(2, bean.getId())).start();
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
