package com.officego.ui.adapter;

import android.content.Context;
import android.widget.TextView;

import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.ui.mine.SayHiEditActivity_;
import com.officego.ui.mine.model.SayHiBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class SayHiManagerAdapter extends CommonListAdapter<SayHiBean.DataBean> {
    private final Context context;
    private SayHiListener listener;

    public SayHiListener getListener() {
        return listener;
    }

    public void setListener(SayHiListener listener) {
        this.listener = listener;
    }

    public interface SayHiListener {
        void delete(int id);
    }

    public SayHiManagerAdapter(Context context, List<SayHiBean.DataBean> list) {
        super(context, R.layout.item_say_hi_manager, list);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, final SayHiBean.DataBean bean) {
        TextView tvName = holder.getView(R.id.tv_name);
        tvName.setText(bean.getContent());
        holder.getView(R.id.iv_edit).setOnClickListener(view ->
                SayHiEditActivity_.intent(context)
                        .contextText(bean.getContent()).start());
        //长按删除
        holder.itemView.setOnLongClickListener(view -> {
            delete(bean.getId());
            return false;
        });
    }

    private void delete(int id) {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setMessage("是否删除此条内容？")
                .setCancelButton(R.string.sm_cancel)
                .setConfirmButton("删除", (dialog12, which) -> {
                    if (listener != null) {
                        listener.delete(id);
                    }
                }).create();
        dialog.showWithOutTouchable(true);
    }
}