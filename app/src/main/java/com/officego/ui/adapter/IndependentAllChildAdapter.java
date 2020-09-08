package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.ui.home.BuildingDetailsChildActivity_;
import com.officego.ui.home.model.BuildingDetailsChildBean;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.view.RoundImageView;
import com.officego.commonlib.utils.GlideUtils;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions: 独立办公室 ，在租写字楼   child列表
 **/
public class IndependentAllChildAdapter extends CommonListAdapter<BuildingDetailsChildBean.ListBean> {

    private Context context;

    public IndependentAllChildAdapter(Context context, List<BuildingDetailsChildBean.ListBean> childList) {
        super(context, R.layout.home_include_house_independent_office_list_item_child, childList);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, final BuildingDetailsChildBean.ListBean bean) {
        RoundImageView ivItemListChild = holder.getView(R.id.iv_item_list_child);
        TextView tvItemListChildLeftUp = holder.getView(R.id.tv_item_list_child_left_up);
        TextView tvItemListChildLeftDown = holder.getView(R.id.tv_item_list_child_left_down);
        TextView tvItemListChildCenterUp = holder.getView(R.id.tv_item_list_child_center_up);
        TextView tvItemListChildCenterDown = holder.getView(R.id.tv_item_list_child_center_down);
        TextView tvItemListChildRightUp = holder.getView(R.id.tv_item_list_child_right_up);
        TextView tvItemListChildRightDown = holder.getView(R.id.tv_item_list_child_right_down);

        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getMainPic()).into(ivItemListChild);
        //楼盘 办公室
        String seats = "";
        if (bean.getSimple().contains(",")) {
            String str1 = bean.getSimple().substring(0, bean.getSimple().indexOf(","));
            seats = bean.getSimple().substring(str1.length() + 1);
        }
        tvItemListChildLeftUp.setText(CommonHelper.bigDecimal(bean.getArea().toString(), true)  + "㎡");
        tvItemListChildLeftDown.setText("最多" + seats + "个工位");
        tvItemListChildCenterUp.setText(Html.fromHtml("<font color='#46C3C2'>¥" + bean.getDayPrice() + "</font>/㎡/天"));
        tvItemListChildCenterDown.setText("¥"+bean.getMonthPrice() + "/月");
        tvItemListChildRightUp.setText(bean.getDecoration());
        tvItemListChildRightUp.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
        tvItemListChildRightDown.setText(bean.getFloor() + "楼");

        holder.itemView.setOnClickListener(v -> {
            BuildingDetailsChildActivity_.intent(context)
                    .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_BUILDING, bean.getId())).start();
        });
    }
}
