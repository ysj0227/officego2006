package com.officego.ui.adapter;

import android.content.Context;
import android.widget.CheckBox;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.common.model.DirectoryBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions: 房源特色
 **/
public class HouseUniqueAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

    //当前选中的数据列表
    private Map<Integer, String> map;

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
            selectedHouseListener.onSelectedHouseUnique(getKey());
        });
    }

    private String getKey() {
        StringBuilder key = new StringBuilder();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (map.size() == 1) {
                key.append(entry.getKey());
            } else {
                key.append(entry.getKey()).append(",");
            }
        }
        if (map.size() > 1) {
            key = key.replace(key.length() - 1, key.length(), "");
        }
        LogCat.e("TAG", "key = " + key);
        return key.toString();
    }


    public onSelectedHouseListener getSelectedHouseListener() {
        return selectedHouseListener;
    }

    public void setSelectedHouseListener(onSelectedHouseListener selectedHouseListener) {
        this.selectedHouseListener = selectedHouseListener;
    }

    private onSelectedHouseListener selectedHouseListener;

    public interface onSelectedHouseListener {
        void onSelectedHouseUnique(String data);
    }
}
