package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.utils.CommonList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class CustomisedHouseAdapter extends CommonListAdapter<String> {
    private final Context context;
    private final List<DirectoryBean.DataBean> factorList;

    private String mPerson = "", mRent = "", mFactor = "";
    private Map<Integer, String> factorMap = new HashMap<>();

    public CustomisedHouseAdapter(Context context, List<String> list, List<DirectoryBean.DataBean> factorList) {
        super(context, R.layout.item_customized_house, list);
        this.context = context;
        this.factorList = factorList;
    }

    @Override
    public void convert(ViewHolder holder, String bean) {
        int position = holder.getAdapterPosition();
        TextView tvContentLeft = holder.getView(R.id.tv_content_left);
        TextView tvContentRight = holder.getView(R.id.tv_content_right);
        RelativeLayout rlLeft = holder.getView(R.id.rl_left);
        RelativeLayout rlItem = holder.getView(R.id.rl_item);
        TextView tvType = holder.getView(R.id.tv_type);
        RecyclerView rvItem = holder.getView(R.id.rv_item);
        RelativeLayout rlRight = holder.getView(R.id.rl_right);
        if (position == 0) {
            rlItem.setVisibility(View.GONE);
            rlRight.setVisibility(View.GONE);
        } else if (position == 1) {
            rlLeft.setVisibility(View.VISIBLE);
            rvItem.setVisibility(View.VISIBLE);
            rlRight.setVisibility(View.GONE);
        } else if (position == 2) {
            holder.itemView.setVisibility(View.GONE);
        } else if (position == 3) {
            holder.itemView.setVisibility(View.GONE);
        }
        showText(position, tvContentLeft, tvType);
        initViews(position, rlLeft, rvItem, rlRight, tvContentRight);
    }

    @SuppressLint("SetTextI18n")
    private void showText(int position, TextView tvContentLeft, TextView tvType) {
        if (position == 0) {
            tvContentLeft.setText("您好！我是OfficeGo的找房助手小欧,请给我们一个了解您的机会吧~");
        } else if (position == 1) {
            tvContentLeft.setText("请问您的团队规模是?");
        } else if (position == 2) {
            tvContentLeft.setText("请问您想租多久?");
        } else if (position == 3) {
            tvContentLeft.setText("请问您优先考虑哪方面因素?");
        }
        tvType.setText(position == 3 ? "多选" : "单选");
    }

    private void initViews(int position, RelativeLayout rlLeft, RecyclerView rvItem,
                           RelativeLayout rlRight, TextView tvContentRight) {
        if (position == 1) {
            GridLayoutManager layoutManager1 = new GridLayoutManager(context, 3);
            rvItem.setLayoutManager(layoutManager1);
            PersonAdapter personAdapter = new PersonAdapter(context, mPerson, CommonList.peopleNumList(), true);
            personAdapter.setListener(new PersonAdapter.PersonListener() {
                @Override
                public void personResult(String key, String value) {
                    rlRight.setVisibility(View.VISIBLE);
                    tvContentRight.setText(key);
                }
            });
            rvItem.setAdapter(personAdapter);
        } else if (position == 2) {
            GridLayoutManager layoutManager2 = new GridLayoutManager(context, 2);
            rvItem.setLayoutManager(layoutManager2);
            RentAdapter rentAdapter = new RentAdapter(context, mRent, CommonList.rentTimeList(), true);
            rentAdapter.setListener(new RentAdapter.RentListener() {
                @Override
                public void rentResult(String key, String value) {
                    rlRight.setVisibility(View.VISIBLE);
                    tvContentRight.setText(key);
                }
            });
            rvItem.setAdapter(rentAdapter);
        } else if (position == 3) {
            GridLayoutManager layoutManager3 = new GridLayoutManager(context, 2);
            rvItem.setLayoutManager(layoutManager3);
            FactorAdapter factorAdapter = new FactorAdapter(context, factorMap, factorList, true);
            factorAdapter.setListener(new FactorAdapter.FactorListener() {
                @Override
                public void factorResult(Map<Integer, String> map) {
                    tvContentRight.setText(readValue(map));
                }
            });
            rvItem.setAdapter(factorAdapter);
        }
    }

    private String readValue(Map<Integer, String> map) {
        if (map == null) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        for (Map.Entry<Integer, String> vo : map.entrySet()) {
            if (map.size() == 1) {
                buffer.append(vo.getValue());
            } else {
                buffer.append(vo.getValue()).append("、");
            }
        }
        if (map.size() > 1) {
            buffer = buffer.replace(buffer.length() - 1, buffer.length(), "");
        }
        return buffer.toString();
    }
}