package com.owner.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.owner.R;
import com.owner.identity.model.IdentityBuildingBean;
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
        void associateCompany(IdentityCompanyBean.DataBean bean);
    }


    public IdentityCompanyAdapter(Context context, List<IdentityCompanyBean.DataBean> list) {
        super(context, R.layout.item_id_company_search, list);
    }

    @Override
    public void convert(ViewHolder holder, final IdentityCompanyBean.DataBean bean) {
        TextView tvCompanyName = holder.getView(R.id.tv_company_name);
        TextView tvAdd = holder.getView(R.id.tv_add);
        tvCompanyName.setText(Html.fromHtml(bean.getCompany()));
        tvAdd.setOnClickListener(v -> listener.associateCompany(bean));
    }

}