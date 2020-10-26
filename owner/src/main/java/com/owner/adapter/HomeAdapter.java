package com.owner.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.dialog.WeChatShareDialog;
import com.officego.commonlib.common.model.ShareBean;
import com.officego.commonlib.common.model.owner.HouseBean;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;
import com.owner.R;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/14
 **/
public class HomeAdapter extends CommonListAdapter<HouseBean.DataBean> {
    private Context context;
    private List<HouseBean.DataBean> list;
    private int buildingId;

    public HomeItemListener getListener() {
        return listener;
    }

    public void setListener(HomeItemListener listener) {
        this.listener = listener;
    }

    private HomeItemListener listener;

    public interface HomeItemListener {
        void itemPublishStatus();

        void itemEdit();

        void itemMore();
    }

    /**
     * @param context 上下文
     * @param list    列表数据
     */
    public HomeAdapter(Context context, int buildingId, List<HouseBean.DataBean> list) {
        super(context, R.layout.item_building_manager, list);
        this.buildingId = buildingId;
        this.list = list;
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, HouseBean.DataBean bean) {
        onClick(holder, bean);
        ImageView ivFlay = holder.getView(R.id.tv_type);
        RoundImageView ivHouse = holder.getView(R.id.iv_house);
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getMainPic()).into(ivHouse);
        holder.setText(R.id.tv_house_name, bean.getBuildingName());
        holder.setText(R.id.tv_area, bean.getArea() + "㎡");
        holder.setText(R.id.tv_price, "¥" + bean.getMonthPrice());
        //楼盘下房源
        if (Constants.TYPE_BUILDING == bean.getBtype()) {
            ivFlay.setVisibility(View.GONE);
        } else {//网点下房源 1是独立办公室，2是开放工位
            ivFlay.setVisibility(View.VISIBLE);
            ivFlay.setBackgroundResource(bean.getOfficeType() == 1 ? R.mipmap.ic_label_independent : R.mipmap.ic_label_open_seats);
        }
    }

    private void onClick(ViewHolder holder, HouseBean.DataBean bean) {
        ImageView ivFlagOff = holder.getView(R.id.iv_flag_off);
        TextView tvPublishStatus = holder.getView(R.id.tv_publish_status);
        TextView tvShare = holder.getView(R.id.tv_share);
        TextView tvEdit = holder.getView(R.id.tv_edit);
        ImageView tvMore = holder.getView(R.id.tv_more);
        //HouseStatus0未发布，1发布，2下架,3:待完善
        ivFlagOff.setVisibility(bean.getHouseStatus() == 2 ? View.VISIBLE : View.GONE);
        if (bean.getHouseStatus() == 0) {
            tvPublishStatus.setText("发布");
        } else if (bean.getHouseStatus() == 1) {
            tvPublishStatus.setVisibility(View.GONE);
        } else if (bean.getHouseStatus() == 2) {
            tvPublishStatus.setText("已下架");
        }
        View.OnClickListener clickListener = view -> {
            int id = view.getId();
            if (id == R.id.tv_share) {
                share(bean);
            } else if (id == R.id.tv_edit) {
                if (listener != null) {
                    listener.itemEdit();
                }
            } else if (id == R.id.tv_more) {
                if (listener != null) {
                    listener.itemMore();
                }
            } else if (id == R.id.tv_publish_status) {
                if (listener != null) {
                    listener.itemPublishStatus();
                }
            }
        };
        tvPublishStatus.setOnClickListener(clickListener);
        tvShare.setOnClickListener(clickListener);
        tvEdit.setOnClickListener(clickListener);
        tvMore.setOnClickListener(clickListener);
        //房源详情
        holder.itemView.setOnClickListener(view ->
                BundleUtils.ownerGotoDetailsActivity(mContext, false, bean.getBtype(), bean.getHouseId()));
    }

    private void share(HouseBean.DataBean bn) {
        String dec = bn.getDictCname();
        ShareBean bean = new ShareBean();
        bean.setbType(1);
        bean.setId("buildingId=" + buildingId + "&houseId=" + bn.getHouseId());
        bean.setHouseChild(true);
        bean.setTitle(bn.getBuildingName());
        bean.setDes(dec);
        bean.setImgUrl(bn.getMainPic());
        bean.setDetailsUrl(bn.getMainPic());
        new WeChatShareDialog(context, bean);
    }
}
