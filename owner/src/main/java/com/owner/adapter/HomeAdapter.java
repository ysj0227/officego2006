package com.owner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.dialog.WeChatShareDialog;
import com.officego.commonlib.common.model.BuildingManagerBean;
import com.officego.commonlib.common.model.ShareBean;
import com.officego.commonlib.common.model.owner.HouseBean;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.view.RoundImageView;
import com.owner.R;
import com.owner.home.AddEditHouseActivity_;
import com.owner.home.AddEditIndependentActivity_;
import com.owner.home.EditOpenSeatsActivity_;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/14
 **/
public class HomeAdapter extends CommonListAdapter<HouseBean.ListBean> {
    private Context context;
    private List<HouseBean.ListBean> list;

    public HomeItemListener getListener() {
        return listener;
    }

    public void setListener(HomeItemListener listener) {
        this.listener = listener;
    }

    private HomeItemListener listener;

    public interface HomeItemListener {
        void itemPublishStatus(int pos, HouseBean.ListBean bean, boolean isOpenSeats);

        void itemMore(HouseBean.ListBean bean, int position);
    }

    /**
     * @param context 上下文
     * @param list    列表数据
     */
    public HomeAdapter(Context context, List<HouseBean.ListBean> list) {
        super(context, R.layout.item_building_manager, list);
        this.list = list;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, HouseBean.ListBean bean) {
        onClick(holder, bean);
        TextView ivFlay = holder.getView(R.id.tv_type);
        TextView tvArea = holder.getView(R.id.tv_area);
        RoundImageView ivHouse = holder.getView(R.id.iv_house);
        TextView tvUnit = holder.getView(R.id.tv_unit);
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getMainPic()).into(ivHouse);
        holder.setText(R.id.tv_house_name, bean.getTitle());
        //楼盘下房源
        if (Constants.TYPE_BUILDING == bean.getBtype()) {
            ivFlay.setVisibility(View.GONE);
            holder.setText(R.id.tv_price, "¥" + bean.getDayPrice());
            tvUnit.setText("/㎡/天起");
        } else {
            holder.setText(R.id.tv_price, "¥" + bean.getDayPrice());
            tvUnit.setText("/位/月");
            //网点下房源 1是独立办公室，2是开放工位
            ivFlay.setVisibility(View.VISIBLE);
            ivFlay.setText(bean.getOfficeType() == 1 ? "独立办公室" : "开放工位");
            ivFlay.setBackgroundResource(bean.getOfficeType() == 1 ? R.drawable.text_label_green_no_stroke
                    : R.drawable.text_label_purple_no_stroke);
        }
        if (Constants.TYPE_JOINTWORK == bean.getBtype() && bean.getOfficeType() == 2) {
            tvArea.setText("共" + bean.getSeats() + "工位");
        } else {
            tvArea.setText(bean.getArea() + "㎡");
        }
    }

    //houseStatus0未发布，1发布，2下架,3:待完善
    private void onClick(ViewHolder holder, HouseBean.ListBean bean) {
        ImageView ivFlagOff = holder.getView(R.id.iv_flag_off);
        TextView tvPublishStatus = holder.getView(R.id.tv_publish_status);
        TextView tvShare = holder.getView(R.id.tv_share);
        TextView tvEdit = holder.getView(R.id.tv_edit);
        ImageView tvMore = holder.getView(R.id.tv_more);
        //分享
        tvShare.setVisibility(bean.getBtype() == 2 && bean.getOfficeType() == 2 ? View.GONE : View.VISIBLE);
        //开放工位
        boolean isOpenSeats = bean.getBtype() == 2 && bean.getOfficeType() == 2 && bean.getHouseStatus() == 1;

        if (bean.getHouseStatus() == 1) {//1发布
            tvPublishStatus.setVisibility(View.GONE);
        } else if (bean.getHouseStatus() == 2) {//2下架
            tvPublishStatus.setVisibility(View.VISIBLE);
            tvPublishStatus.setText("重新发布");
        } else {//0未发布 3:待完善
            tvPublishStatus.setVisibility(View.VISIBLE);
            tvPublishStatus.setText("发布");
        }
        //开放工位
        if (bean.getBtype() == 2 && bean.getOfficeType() == 2) {
            tvPublishStatus.setVisibility(View.VISIBLE);
            tvPublishStatus.setText(isOpenSeats ? "关闭" : "重新发布");
            ivFlagOff.setVisibility(isOpenSeats ? View.GONE : View.VISIBLE);
        } else {
            ivFlagOff.setVisibility(bean.getHouseStatus() == 2 ? View.VISIBLE : View.GONE);
        }

        View.OnClickListener clickListener = view -> {
            int id = view.getId();
            if (id == R.id.tv_share) {
                if (bean.getHouseStatus() == 2) {
                    ToastUtils.toastForShort(context, "房源已下架，请先上架后再分享");
                } else if (bean.getHouseStatus() == 0 || bean.getHouseStatus() == 3) {
                    ToastUtils.toastForShort(context, "房源未发布，请先发布后再分享");
                } else {
                    share(bean);
                }
            } else if (id == R.id.tv_edit) {
                gotoEditHouseActivity(bean);
            } else if (id == R.id.tv_more) {
                if (listener != null) {
                    listener.itemMore(bean, holder.getAdapterPosition());
                }
            } else if (id == R.id.tv_publish_status) {
                if (listener != null) {//如果是独立办公室是发布， 开放工位是关闭
                    listener.itemPublishStatus(holder.getAdapterPosition(), bean, isOpenSeats);
                }
            }
        };
        tvPublishStatus.setOnClickListener(clickListener);
        tvShare.setOnClickListener(clickListener);
        tvEdit.setOnClickListener(clickListener);
        tvMore.setOnClickListener(clickListener);
        holder.itemView.setOnClickListener(view -> gotoDetailsActivity(bean));
    }

    //房源详情
    private void gotoDetailsActivity(HouseBean.ListBean bean) {
        if (Constants.TYPE_JOINTWORK == bean.getBtype() && bean.getOfficeType() == 2) {
            return;
        }
        BundleUtils.ownerGotoDetailsActivity(mContext, bean.getHouseStatus() != 1, false,
                bean.getBtype(), bean.getHouseId(), bean.getIsTemp());
    }

    //编辑房源
    private void gotoEditHouseActivity(HouseBean.ListBean bean) {
        if (Constants.TYPE_BUILDING == bean.getBtype()) {
            //楼盘下房源
            AddEditHouseActivity_.intent(context)
                    .buildingFlag(Constants.BUILDING_FLAG_EDIT)
                    .buildingManagerBean(new BuildingManagerBean(bean.getHouseId(), bean.getIsTemp()))
                    .start();
        } else {
            if (bean.getOfficeType() == 2) {//2开放工位
                EditOpenSeatsActivity_.intent(context)
                        .buildingFlag(Constants.BUILDING_FLAG_EDIT)
                        .buildingManagerBean(new BuildingManagerBean(bean.getHouseId(), bean.getIsTemp()))
                        .start();
            } else {//1独立办公室
                AddEditIndependentActivity_.intent(context)
                        .buildingFlag(Constants.BUILDING_FLAG_EDIT)
                        .buildingManagerBean(new BuildingManagerBean(bean.getHouseId(), bean.getIsTemp()))
                        .start();
            }
        }
    }

    private void share(HouseBean.ListBean bn) {
        String dec = bn.getDictCname();
        ShareBean bean = new ShareBean();
        bean.setbType(1);
        bean.setId("buildingId=" + bn.getBuildingId() + "&houseId=" + bn.getHouseId());
        bean.setHouseChild(true);
        bean.setTitle(bn.getTitle());
        bean.setDes(dec);
        bean.setImgUrl(bn.getMainPic());
        bean.setDetailsUrl(bn.getMainPic());
        new WeChatShareDialog(context, bean);
    }
}
