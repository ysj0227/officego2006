package com.officego.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class PoiNearbyAdapter extends CommonListAdapter<PoiItem> {

    public void setListener(PoiListener listener) {
        this.listener = listener;
    }

    private PoiListener listener;

    public interface PoiListener {

        void poiItemOnClick(PoiItem data);
    }

    public PoiNearbyAdapter(Context context, List<PoiItem> list) {
        super(context, R.layout.item_poi_nearby_name, list);
    }

    @Override
    public void convert(ViewHolder holder, final PoiItem bean) {
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvAddress = holder.getView(R.id.tv_address);
        tvName.setText(bean.getTitle());
        tvAddress.setText(String.format("| %s%s", bean.getAdName(), bean.getSnippet()));
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.poiItemOnClick(bean);
            }
        });
    }
}