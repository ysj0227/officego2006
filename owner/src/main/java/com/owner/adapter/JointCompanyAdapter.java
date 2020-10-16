package com.owner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.owner.R;
import com.owner.identity.model.JointCompanyBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/15
 * 房源特色
 **/
public class JointCompanyAdapter extends CommonListAdapter<JointCompanyBean> {
    private Context context;
    private ArrayList<JointCompanyBean> list;
    private JointCompanyListener listener;

    public JointCompanyListener getListener() {
        return listener;
    }

    public void setListener(JointCompanyListener listener) {
        this.listener = listener;
    }

    public interface JointCompanyListener {
        void addJointCompany(int pos, String contentText);

        void deleteJointCompany(int pos);
    }

    @SuppressLint("UseSparseArrays")
    public JointCompanyAdapter(Context context, ArrayList<JointCompanyBean> list) {
        super(context, R.layout.item_home_joint_company, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public void convert(ViewHolder holder, final JointCompanyBean bean) {
        TextView tvTitle = holder.getView(R.id.tv_title);
        EditText editText = holder.getView(R.id.et_content);
        ImageView ivRightImage = holder.getView(R.id.iv_other_right);
        tvTitle.setVisibility(holder.getAdapterPosition() == 0 ? View.VISIBLE : View.INVISIBLE);
        ivRightImage.setOnClickListener(view -> {
            if (listener != null) {
                if (holder.getAdapterPosition() == list.size() - 1) {
                    if (list.size() >= 5) {
                        return;
                    }
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        Toast.makeText(context, "请输入入住企业", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listener.addJointCompany(holder.getAdapterPosition(), editText.getText().toString());
                } else {
                    listener.deleteJointCompany(holder.getAdapterPosition());
                }
            }
        });
        if (holder.getAdapterPosition() == list.size() - 1) {
            ivRightImage.setBackgroundResource(R.mipmap.ic_add_blue);
        } else {
            ivRightImage.setBackgroundResource(R.mipmap.ic_remove_blue);
        }
    }
}
