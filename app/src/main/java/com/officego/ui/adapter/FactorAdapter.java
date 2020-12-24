package com.officego.ui.adapter;

import android.content.Context;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.ui.find.WantFindBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class FactorAdapter extends CommonListAdapter<WantFindBean> {
    private final Context context;
    private Map<Integer, String> map;

    public FactorListener getListener() {
        return listener;
    }

    public void setListener(FactorListener listener) {
        this.listener = listener;
    }

    private FactorListener listener;

    public interface FactorListener {
        void factorResult(Map<Integer, String> map);
    }

    public FactorAdapter(Context context,Map<Integer, String> map, List<WantFindBean> list) {
        super(context, R.layout.item_find, list);
        this.context = context;
        this.map = map;
        if (this.map == null) {
            this.map = new HashMap<>();
        }
    }

    @Override
    public void convert(ViewHolder holder, @NonNull final WantFindBean bean) {
        CheckBox tvName = holder.getView(R.id.tv_name);
        tvName.setText(bean.getValue());
        if (map != null) {
            tvName.setChecked(map.containsKey(Integer.parseInt(bean.getKey())));
        }
        tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (map.size() >= 3) {
                    tvName.setChecked(false);
                    ToastUtils.toastForShort(context, "最多选择3项");
                    return;
                }
                if (!map.containsKey(Integer.parseInt(bean.getKey()))) {
                    map.put(Integer.parseInt(bean.getKey()), bean.getValue());
                }
            } else {
                map.remove(Integer.parseInt(bean.getKey()));
            }
            if (listener != null) {
                listener.factorResult(map);
            }
        });
    }
}