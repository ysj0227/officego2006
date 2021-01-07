package com.officego.ui.adapter;

import android.content.Context;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class FactorAdapter extends CommonListAdapter<DirectoryBean.DataBean> {
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

    public FactorAdapter(Context context,Map<Integer, String> map, List<DirectoryBean.DataBean> list) {
        super(context, R.layout.item_find, list);
        this.context = context;
        this.map = map;
        if (this.map == null) {
            this.map = new HashMap<>();
        }
    }

    @Override
    public void convert(ViewHolder holder, @NonNull final DirectoryBean.DataBean bean) {
        CheckBox tvName = holder.getView(R.id.tv_name);
        tvName.setText(bean.getDictCname());
        if (map != null) {
            tvName.setChecked(map.containsKey(bean.getDictValue()));
        }
        tvName.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (map.size() >= 3) {
                    tvName.setChecked(false);
                    ToastUtils.toastForShort(context, "最多选择3项");
                    return;
                }
                if (!map.containsKey(bean.getDictValue())) {
                    map.put(bean.getDictValue(), bean.getDictCname());
                }
            } else {
                map.remove(bean.getDictValue());
            }
            if (listener != null) {
                listener.factorResult(map);
            }
        });
    }
}