package com.officego.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.common.model.SearchListBean;

import java.util.List;

import static com.owner.utils.CommUtils.searchHtmlTextView;

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
        searchHtmlTextView(tvName, bean.getBuildingName());
        if (bean.getDistrict() != null && bean.getBusiness() == null) {
            searchHtmlTextView(tvBusiness, bean.getDistrict().toString());

        } else if (bean.getDistrict() == null && bean.getBusiness() != null) {
            searchHtmlTextView(tvBusiness, bean.getBusiness().toString());
        } else if (bean.getDistrict() != null && bean.getBusiness() != null) {
            searchHtmlTextView(tvBusiness, bean.getDistrict().toString() + " · " + bean.getBusiness().toString());
        }
        searchHtmlTextView(tvLocation, bean.getAddress());
        holder.setText(R.id.tv_price, "¥" + bean.getDayPrice() + (bean.getBuildType() == Constants.TYPE_BUILDING ? "/m²/天起" : "/位/月起"));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.searchListItemOnClick(bean);
            }
        });
    }
}