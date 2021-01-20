package com.officego.ui.adapter;

import android.content.Context;
import android.widget.CheckBox;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.ui.mine.model.SayHiBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class SayHiAdapter extends CommonListAdapter<SayHiBean.DataBean> {
    private final Map<Integer, String> map = new HashMap<>();
    private boolean onBind;

    public SayHiAdapter(Context context, List<SayHiBean.DataBean> list) {
        super(context, R.layout.item_say_hi, list);
    }

    @Override
    public void convert(ViewHolder holder, final SayHiBean.DataBean bean) {
        int position = holder.getAdapterPosition();
        CheckBox tvName = holder.getView(R.id.cb_name);
        tvName.setText(bean.getContent());
        tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                map.clear();
                map.put(position, bean.getContent());
            }
            if (!onBind) {
                notifyDataSetChanged();
            }
        });
        onBind = true;
        tvName.setChecked(map.containsKey(position));
        onBind = false;
    }
}