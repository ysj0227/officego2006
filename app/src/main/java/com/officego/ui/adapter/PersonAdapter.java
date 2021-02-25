package com.officego.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.ui.find.WantFindBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class PersonAdapter extends CommonListAdapter<WantFindBean> {
    private Context context;
    private boolean isSetWidth;
    private final Map<Integer, String> map = new HashMap<>();
    private boolean onBind;

    public PersonListener getListener() {
        return listener;
    }

    public void setListener(PersonListener listener) {
        this.listener = listener;
    }

    private PersonListener listener;

    public interface PersonListener {
        void personResult(String key,String value);
    }

    public PersonAdapter(Context context, String value, List<WantFindBean> list, boolean isSetWidth) {
        super(context, R.layout.item_find2, list);
        this.context = context;
        this.isSetWidth = isSetWidth;
        for (int i = 0; i < list.size(); i++) {
            if (TextUtils.equals(value, list.get(i).getValue())) {
                map.put(i, list.get(i).getKey());
            }
        }
    }

    @Override
    public void convert(ViewHolder holder, WantFindBean bean) {
        int position = holder.getAdapterPosition();
        CheckBox tvName = holder.getView(R.id.tv_name);
        if (context != null && isSetWidth) {
            ViewGroup.LayoutParams lp = tvName.getLayoutParams();
            lp.width = (int) context.getResources().getDimension(R.dimen.dp_80);
            tvName.setLayoutParams(lp);
        }
        tvName.setText(bean.getKey());
        tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                map.clear();
                map.put(position, bean.getKey());
                if (listener != null) {
                    listener.personResult(bean.getKey(),bean.getValue());
                }
            } else {
                map.remove(position);
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