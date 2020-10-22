package com.owner.adapter;

/**
 * Created by shijie
 * Date 2020/10/16
 **/

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.utils.ToastUtils;
import com.owner.R;

import java.util.List;

/**
 * Created by qzs on 2017/9/04.
 */
public class JointCompanyAdapter extends RecyclerView.Adapter<JointCompanyAdapter.MyViewHolder> {
    private Context context;
    private List<String> list;

    public JointCompanyAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_joint_company, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.editText.setText(list.get(position));
        holder.ivRightImage.setOnClickListener(v -> {
            if (list.size() > 1 && holder.getAdapterPosition() < list.size() - 1) {
                removeData(position);  //删除数据
            }
            if (holder.getAdapterPosition() == list.size() - 1 && list.size() < 5) {
                if (TextUtils.isEmpty(holder.editText.getText().toString())) {
                    ToastUtils.toastForShort(context, "请输入企业名称");
                    return;
                }
                addData(list.size());//添加数据
            }
        });
        holder.tvTitle.setVisibility(holder.getAdapterPosition() == 0 ? View.VISIBLE : View.INVISIBLE);
        holder.ivRightImage.setBackgroundResource(holder.getAdapterPosition() == list.size() - 1
                ? R.mipmap.ic_add_blue : R.mipmap.ic_remove_blue);
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //设置数据
                list.set(holder.getAdapterPosition(), holder.editText.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 添加数据
    private void addData(int position) {
        list.add(position, "");
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    // 删除数据
    private void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        EditText editText;
        ImageView ivRightImage;

        MyViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
            editText = view.findViewById(R.id.et_content);
            ivRightImage = view.findViewById(R.id.iv_other_right);
        }
    }

}

