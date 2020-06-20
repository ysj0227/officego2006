package com.officego.ui.adapter;

import android.content.Context;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions: 独立办公室 ，在租写字楼
 **/
public class SearchRecommendAdapter extends CommonListAdapter<String> {

    private Context context;

    public SearchRecommendAdapter(Context context, List<String> list) {
        super(context, R.layout.home_activity_search_recommend_item, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final String bean) {
        holder.setText(R.id.tv_order_num,holder.getAdapterPosition()+"");
    }

}
