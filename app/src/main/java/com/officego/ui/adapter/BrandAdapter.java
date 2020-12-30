package com.officego.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.SearchListBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.RoundImageView;
import com.officego.ui.home.model.BrandRecommendBean;

import java.util.List;

import static com.owner.utils.CommUtils.searchHtmlTextView;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class BrandAdapter extends CommonListAdapter<BrandRecommendBean.DataBean> {
    private final Context context;

    public BrandAdapter(Context context, List<BrandRecommendBean.DataBean> list) {
        super(context, R.layout.item_home_brand, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final BrandRecommendBean.DataBean bean) {
        if (bean != null) {
            RoundImageView ivBrand = holder.getView(R.id.riv_image);
            Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).
                    load(bean.getImg() != null ? bean.getImg() : "").into(ivBrand);
            TextView tvName = holder.getView(R.id.tv_name);
            tvName.setText(bean.getTitleName() != null ? bean.getTitleName() : "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}