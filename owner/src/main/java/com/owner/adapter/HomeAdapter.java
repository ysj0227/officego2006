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
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;
import com.owner.R;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/14
 **/
public class HomeAdapter extends CommonListAdapter<String> {
    private Context context;
    private List<String> list;

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
    public HomeAdapter(Context context, List<String> list) {
        super(context, R.layout.item_building_manager, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        onClick(holder);
        ImageView ivFlay = holder.getView(R.id.tv_type);
        RoundImageView ivHouse = holder.getView(R.id.iv_house);
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).
                load("https://img.officego.com/building/1591868939785.jpg?x-oss-process=style/small").into(ivHouse);
        holder.setText(R.id.tv_house_name, "北海道国际");
        holder.setText(R.id.tv_area, 100 + "㎡");
        holder.setText(R.id.tv_price, "¥" + 10);
        int jointWorkType = 0;
        if (jointWorkType == 0) {
            ivFlay.setVisibility(View.VISIBLE);
            ivFlay.setBackgroundResource(jointWorkType == 2 ? R.mipmap.ic_label_independent : R.mipmap.ic_label_open_seats);
        } else {
            ivFlay.setVisibility(View.GONE);
        }
    }

    private void onClick(ViewHolder holder) {
        TextView tvPublishStatus = holder.getView(R.id.tv_publish_status);
        TextView tvShare = holder.getView(R.id.tv_share);
        TextView tvEdit = holder.getView(R.id.tv_edit);
        ImageView tvMore = holder.getView(R.id.tv_more);
        View.OnClickListener clickListener = view -> {
            int id = view.getId();
            if (id == R.id.tv_share) {
                share();
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
    }

    private void share() {
        ShareBean bean = new ShareBean();
        bean.setbType(1);
        bean.setId("buildingId=" + 1084 + "&houseId=" + 1088);
        bean.setHouseChild(true);
        bean.setTitle("AA");
        bean.setDes("aa");
        bean.setImgUrl("https://img.officego.com/building/1591868939785.jpg?x-oss-process=style/small");
        bean.setDetailsUrl("https://img.officego.com/building/1591868939785.jpg?x-oss-process=style/small");
        new WeChatShareDialog(context, bean);
    }
}
