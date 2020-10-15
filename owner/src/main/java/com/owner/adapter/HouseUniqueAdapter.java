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
 * 房源特色
 **/
public class HouseUniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

    //当前选中的数据列表
    private Map<Integer, String> map;
    private String houseTags = "";

    @SuppressLint("UseSparseArrays")
    public HouseUniqueAdapter(Context context, List<DirectoryBean.DataBean> list) {
        super(context, R.layout.item_house_type, list);
        map = new HashMap<>();
    }

    @Override
    public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
        CheckBox cbType = holder.getView(R.id.cb_type);
        cbType.setText(bean.getDictCname());
        cbType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                map.put(bean.getDictValue(), bean.getDictCname());
            } else {
                map.remove(bean.getDictValue());
            }
            houseTags = CommonHelper.getKey(map);
        });
    }
}
