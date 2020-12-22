package com.officego.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class NewsAdapter extends CommonListAdapter<String> {
    private Context context;

    public NewsAdapter(Context context, List<String> list) {
        super(context, R.layout.item_home_news, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final String bean) {
        RoundImageView ivBrand = holder.getView(R.id.riv_image);
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(ivBrand);
        TextView tvName = holder.getView(R.id.tv_name);

    }
}