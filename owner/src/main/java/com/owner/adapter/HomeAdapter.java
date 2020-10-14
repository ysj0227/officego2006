package com.owner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.CommonHelper;
import com.owner.R;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/14
 **/
public class HomeAdapter extends CommonListAdapter<String> {
    private Context context;
    private List<String> list;

    public HomeItemListener getListener() {
        return listener;
    }

    public void setListener(HomeItemListener listener) {
        this.listener = listener;
    }

    private HomeItemListener listener;

    public interface HomeItemListener {
        void itemPreview();

        void itemEdit();

        void itemMore();
    }

    /**
     * @param context 上下文
     * @param list    列表数据
     */
    public HomeAdapter(Context context, List<String> list) {
        super(context, R.layout.item_building_manager, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        onClick(holder);
    }

    private void onClick(ViewHolder holder) {
        TextView tvPreview = holder.getView(R.id.tv_preview);
        TextView tvEdit = holder.getView(R.id.tv_edit);
        TextView tvMore = holder.getView(R.id.tv_more);
        View.OnClickListener clickListener = view -> {
            int id = view.getId();
            if (id == R.id.tv_preview) {
                if (listener != null) {
                    listener.itemPreview();
                }
            } else if (id == R.id.tv_edit) {
                if (listener != null) {
                    listener.itemEdit();
                }
            } else if (id == R.id.tv_more) {
                if (listener != null) {
                    listener.itemMore();
                }
            }
        };
        tvPreview.setOnClickListener(clickListener);
        tvEdit.setOnClickListener(clickListener);
        tvMore.setOnClickListener(clickListener);
    }
}
