package com.officego.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.ui.home.model.SearchListBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class KeywordsAdapter extends CommonListAdapter<SearchListBean.DataBean> {

    public void setListener(SearchKeywordsListener listener) {
        this.listener = listener;
    }

    private SearchKeywordsListener listener;

    public interface SearchKeywordsListener {

        void searchListItemOnClick(SearchListBean.DataBean data);
    }

    public KeywordsAdapter(Context context, List<SearchListBean.DataBean> list) {
        super(context, R.layout.keywords_popupwindow_item, list);
    }

    @Override
    public void convert(ViewHolder holder, final SearchListBean.DataBean bean) {
        TextView tvName = holder.getView(R.id.tv_building_name);
        TextView tvBusiness = holder.getView(R.id.tv_business);
        TextView tvLocation = holder.getView(R.id.tv_location);
        showHtmlView(tvName, bean.getBuildingName());
        if (bean.getDistrict() != null && bean.getBusiness() == null) {
            showHtmlView(tvBusiness, bean.getDistrict().toString());
        } else if (bean.getDistrict() == null && bean.getBusiness() != null) {
            showHtmlView(tvBusiness, bean.getBusiness().toString());
        } else if (bean.getDistrict() != null && bean.getBusiness() != null) {
            showHtmlView(tvBusiness, bean.getDistrict().toString() + " · " + bean.getBusiness().toString());
        }
        showHtmlView(tvLocation, bean.getAddress());
        holder.setText(R.id.tv_price, "¥" + bean.getDayPrice() + "m²/天起");
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.searchListItemOnClick(bean);
            }
        });
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