package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.ui.home.BuildingDetailsJointWorkChildActivity_;
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
public class JointWorkAllChildAdapter extends CommonListAdapter<BuildingDetailsChildBean.ListBean> {

    private Context context;

    public JointWorkAllChildAdapter(Context context, List<BuildingDetailsChildBean.ListBean> childList) {
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
        //网点
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getMainPic()).into(ivItemListChild);
        tvItemListChildCenterUp.setVisibility(View.INVISIBLE);
        tvItemListChildCenterDown.setVisibility(View.INVISIBLE);
        tvItemListChildLeftUp.setText(CommonHelper.bigDecimal(bean.getArea().toString(), true)  + "㎡");
        tvItemListChildLeftDown.setText(bean.getSeats() + "工位");
        tvItemListChildRightUp.setText(Html.fromHtml("<font color='#46C3C2'>¥" + bean.getMonthPrice() + "</font>/月"));
        tvItemListChildRightDown.setText(Html.fromHtml("<font color='#46C3C2'>¥" + bean.getDayPrice() + "</font>/位/天"));
        holder.itemView.setOnClickListener(v -> {
            BuildingDetailsJointWorkChildActivity_.intent(context)
                    .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_JOINTWORK, bean.getId())).start();
        });
    }
}
