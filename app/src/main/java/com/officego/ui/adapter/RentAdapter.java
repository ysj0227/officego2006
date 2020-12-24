package com.officego.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.ui.find.WantFindBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class RentAdapter extends CommonListAdapter<WantFindBean> {
    private final RecyclerView rvRent;
    private int checkedPos = -1;

    public RentListener getListener() {
        return listener;
    }

    public void setListener(RentListener listener) {
        this.listener = listener;
    }

    private RentListener listener;

    public interface RentListener {
        void rentResult(String value);
    }

    public RentAdapter(Context context, RecyclerView rvRent, String value, List<WantFindBean> list) {
        super(context, R.layout.item_find, list);
        this.rvRent = rvRent;
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.equals(value, list.get(i).getValue())) {
                this.checkedPos = i;
            }
        }
    }

    @Override
    public void convert(ViewHolder holder, final WantFindBean bean) {
        CheckBox tvName = holder.getView(R.id.tv_name);
        tvName.setText(bean.getKey());
        tvName.setChecked(holder.getAdapterPosition() == checkedPos);
        tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedPos = holder.getAdapterPosition();
                if (listener != null) {
                    listener.rentResult(bean.getValue());
                }
            }
            if (!rvRent.isComputingLayout()) {
                notifyDataSetChanged();
            }
        });
    }
}