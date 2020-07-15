package com.owner.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.owner.R;
import com.owner.identity.model.IdentityCompanyBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class IdentityCompanyAdapter extends CommonListAdapter<IdentityCompanyBean.DataBean> {
    private IdentityCompanyListener listener;

    public IdentityCompanyListener getListener() {
        return listener;
    }

    public void setListener(IdentityCompanyListener listener) {
        this.listener = listener;
    }

    public interface IdentityCompanyListener {
        void associateCompany(IdentityCompanyBean.DataBean bean, boolean isCreate);
    }

    private List<IdentityCompanyBean.DataBean> list;

    public IdentityCompanyAdapter(Context context, List<IdentityCompanyBean.DataBean> list) {
        super(context, R.layout.item_id_company_search, list);
        this.list = list;
    }

    @Override
    public void convert(ViewHolder holder, final IdentityCompanyBean.DataBean bean) {
        TextView tvIdentity = holder.getView(R.id.tv_identity);
        TextView tvCompanyName = holder.getView(R.id.tv_company_name);
        TextView tvTip = holder.getView(R.id.tv_tip);
        TextView tvAdd = holder.getView(R.id.tv_add);
        if (list != null && list.size() > 0 && holder.getAdapterPosition() == list.size()-1) {
            tvIdentity.setVisibility(View.INVISIBLE);
            tvCompanyName.setVisibility(View.GONE);
            tvAdd.setText("创建公司");
            tvTip.setText("公司不存在，去创建公司");
            holder.itemView.setOnClickListener(v -> listener.associateCompany(bean, true));
        } else {
            tvIdentity.setVisibility(View.VISIBLE);
            tvCompanyName.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(bean.getCompany())) {
                tvCompanyName.setText(Html.fromHtml(bean.getCompany()));
            }
            tvAdd.setText("关联公司");
            tvTip.setText("加入公司，即可共同管理公司房源");
            holder.itemView.setOnClickListener(v -> listener.associateCompany(bean, false));
        }
    }
}