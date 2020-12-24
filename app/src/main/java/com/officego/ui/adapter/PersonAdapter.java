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
public class PersonAdapter extends CommonListAdapter<WantFindBean> {
    private int checkedPos = -1;
    private final RecyclerView rvPerson;

    public PersonListener getListener() {
        return listener;
    }

    public void setListener(PersonListener listener) {
        this.listener = listener;
    }

    private PersonListener listener;

    public interface PersonListener {
        void personResult(String value);
    }

    public PersonAdapter(Context context, RecyclerView rvPerson, String value, List<WantFindBean> list) {
        super(context, R.layout.item_find, list);
        this.rvPerson = rvPerson;
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
                    listener.personResult(bean.getValue());
                }
            }
            if (!rvPerson.isComputingLayout()) {
                notifyDataSetChanged();
            }
        });
    }
}