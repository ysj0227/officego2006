package com.officego.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.officego.R;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.ChatListBean;
import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.view.LabelsView;
import com.officego.commonlib.view.RoundImageView;
import com.officego.model.LabelBean;
import com.officego.ui.home.BuildingDetailsActivity_;
import com.officego.ui.home.BuildingDetailsJointWorkActivity_;
import com.officego.ui.home.model.BuildingBean;
import com.officego.ui.home.model.ConditionBean;
import com.officego.ui.message.ConversationActivity_;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions:
 **/
public class ChatListAdapter extends CommonListAdapter<ChatListBean> {

    private Context context;


    public ChatListAdapter(Context context, List<ChatListBean> list) {
        super(context, R.layout.item_chat_list, list);
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void convert(ViewHolder holder, final ChatListBean bean) {
        RoundImageView rivAvatar = holder.getView(R.id.riv_avatar);
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getAvatar()).into(rivAvatar);
        holder.setText(R.id.tv_name, bean.getNickname());
        holder.setText(R.id.tv_message, "消息");
        holder.setText(R.id.tv_date, "2020-11-12");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConversationActivity_.intent(context).start();
            }
        });
    }
}
