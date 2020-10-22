package com.owner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.CheckBox;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.CommonHelper;
import com.owner.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shijie
 * Date 2020/10/15
 * 特色
 **/
public class UniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

    private Map<Integer, String> map;

    public UniqueListener getListener() {
        return listener;
    }

    public void setListener(UniqueListener listener) {
        this.listener = listener;
    }

    private UniqueListener listener;

    public interface UniqueListener {
        void uniqueResult(Map<Integer, String> uniqueMap);
    }

    @SuppressLint("UseSparseArrays")
    public UniqueAdapter(Context context, Map<Integer, String> map, List<DirectoryBean.DataBean> list) {
        super(context, R.layout.item_house_type, list);
        this.map = map;
        if (this.map == null) {
            this.map = new HashMap<>();
        }
    }

    @Override
    public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
        CheckBox cbType = holder.getView(R.id.cb_type);
        cbType.setText(bean.getDictCname());
        if (map != null) {
            cbType.setChecked(map.containsKey(bean.getDictValue()));
        }
        cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (map.size() >= 4) {
                    cbType.setChecked(false);
                    return;
                }
                if (!map.containsKey(bean.getDictValue())) {
                    map.put(bean.getDictValue(), bean.getDictCname());
                }
            } else {
                map.remove(bean.getDictValue());
            }
            if (listener != null) {
                listener.uniqueResult(map);
            }
        });
    }
}
