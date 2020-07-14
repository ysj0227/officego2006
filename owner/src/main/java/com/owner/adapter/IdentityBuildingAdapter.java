package com.owner.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.owner.R;
import com.owner.identity.model.IdentityBuildingBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class IdentityBuildingAdapter extends CommonListAdapter<IdentityBuildingBean.DataBean> {

    public IdentityBuildingListener getListener() {
        return listener;
    }

    public void setListener(IdentityBuildingListener listener) {
        this.listener = listener;
    }

    private IdentityBuildingListener listener;
    public interface IdentityBuildingListener{
        void associateBuilding(IdentityBuildingBean.DataBean bean);
    }


    public IdentityBuildingAdapter(Context context, List<IdentityBuildingBean.DataBean> list) {
        super(context, R.layout.item_id_building_search, list);
    }

    @Override
    public void convert(ViewHolder holder, final IdentityBuildingBean.DataBean bean) {
        TextView tvBuildingName = holder.getView(R.id.tv_building_name);
        TextView tvAddress = holder.getView(R.id.tv_address);
        TextView tvAdd = holder.getView(R.id.tv_add);
        tvBuildingName.setText(Html.fromHtml(bean.getBuildingName()));
        tvAddress.setText(Html.fromHtml(bean.getAddress()));
        tvAdd.setOnClickListener(v -> listener.associateBuilding(bean));
    }

    private void showHtmlView(TextView textView, String info) {
        if (info.contains("strong style='color:")) {
            String pre = info.replace("strong style='color:#46C3C2'", "font color=#07B2B0");
            String next = pre.replace("</strong>", "</font>");
            textView.setText(Html.fromHtml(next));
        } else {
            textView.setText(Html.fromHtml(info));
        }
    }
}