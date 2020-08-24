package com.owner.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
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
    private Context context;
    private boolean isCompany;

    public IdentityCompanyAdapter(Context context, List<IdentityCompanyBean.DataBean> list, boolean isCompany) {
        super(context, R.layout.item_id_company_search, list);
        this.context = context;
        this.list = list;
        this.isCompany = isCompany;
    }

    @Override
    public void convert(ViewHolder holder, final IdentityCompanyBean.DataBean bean) {
        RelativeLayout rlRoot = holder.getView(R.id.rl_root);
        TextView tvIdentity = holder.getView(R.id.tv_identity);
        TextView tvUp = holder.getView(R.id.tv_up);
        TextView tvDown = holder.getView(R.id.tv_down);
        TextView tvAdd = holder.getView(R.id.tv_add);
        if (list != null && list.size() > 0 && holder.getAdapterPosition() == list.size() - 1) {
            tvIdentity.setVisibility(View.GONE);
            tvUp.setVisibility(View.VISIBLE);
            tvDown.setVisibility(View.GONE);
            tvAdd.setVisibility(View.VISIBLE);
            tvAdd.setText("创建公司");
            tvUp.setText("公司不存在，去创建公司");
            holder.itemView.setEnabled(true);
            holder.itemView.setOnClickListener(v -> listener.associateCompany(bean, true));
        } else {
            tvIdentity.setVisibility(isCompany ? View.VISIBLE : View.GONE);
            tvUp.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(bean.getCompany())) {
                tvUp.setText(Html.fromHtml(bean.getCompany()));
            }
            if (isCompany && TextUtils.equals("2", bean.getIdentityType())) {
                tvAdd.setVisibility(View.GONE);
                tvDown.setVisibility(View.VISIBLE);
                tvDown.setText("该公司已认证为共享办公，不可重复认证");
                holder.itemView.setEnabled(false);
            } else if (!isCompany && TextUtils.equals("1", bean.getIdentityType())) {
                tvAdd.setVisibility(View.GONE);
                tvDown.setVisibility(View.VISIBLE);
                tvDown.setText("该公司已认证为标准办公，不可重复认证");
                holder.itemView.setEnabled(false);
            } else {
                tvAdd.setText(isCompany ? "申请加入" : "加入公司");
                tvAdd.setVisibility(View.VISIBLE);
                tvDown.setVisibility(View.GONE);
                holder.itemView.setEnabled(true);
                holder.itemView.setOnClickListener(v -> listener.associateCompany(bean, false));
            }
        }
    }
}