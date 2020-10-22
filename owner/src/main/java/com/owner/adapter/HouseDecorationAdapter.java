package com.owner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.CheckBox;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.DirectoryBean;
import com.owner.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shijie
 * Date 2020/10/15
 * 装修
 **/
public class HouseDecorationAdapter extends CommonListAdapter<DirectoryBean.DataBean> {
    private int checkedPos = -1;

    public DecorationListener getListener() {
        return listener;
    }

    public void setListener(DecorationListener listener) {
        this.listener = listener;
    }

    private DecorationListener listener;

    public interface DecorationListener {
        void decorationResult(int decId);
    }

    @SuppressLint("UseSparseArrays")
    public HouseDecorationAdapter(Context context, int decId, List<DirectoryBean.DataBean> list) {
        super(context, R.layout.item_house_type, list);
        for (int i = 0; i < list.size(); i++) {
            if (decId == list.get(i).getDictValue()) {
                this.checkedPos = i;
            }
        }
    }

    @Override
    public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
        CheckBox cbType = holder.getView(R.id.cb_type);
        cbType.setText(bean.getDictCname());
        cbType.setChecked(holder.getAdapterPosition() == checkedPos);
        cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedPos = holder.getAdapterPosition();
                if (listener != null) {
                    listener.decorationResult(bean.getDictValue());
                }
                notifyDataSetChanged();
            }
        });
    }
}
