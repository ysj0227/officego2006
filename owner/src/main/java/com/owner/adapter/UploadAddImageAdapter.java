package com.owner.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.GlideUtils;
import com.owner.R;
import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/7/13.
 * Descriptions:
 **/
public class UploadAddImageAdapter extends CommonListAdapter<ImageBean> {

    private List<ImageBean> list;
    private Context context;

    public UploadImageListener getListener() {
        return listener;
    }

    public void setListener(UploadImageListener listener) {
        this.listener = listener;
    }

    private UploadImageListener listener;

    public UploadAddImageAdapter(Context context, List<ImageBean> list) {
        super(context, R.layout.item_id_company_img, list);
        this.list = list;
        this.context = context;
    }

    public interface UploadImageListener {
        void addUploadImage();

        void deleteUploadImage(ImageBean bean, int position);

    }

    @Override
    public void convert(ViewHolder holder, final ImageBean bean) {
        ImageView ivItem = holder.getView(R.id.iv_item);
        ImageView ivDelete = holder.getView(R.id.iv_delete);
        TextView tvSetFirstImage = holder.getView(R.id.tv_set_first_image);
        tvSetFirstImage.setVisibility(View.GONE);
        if (bean.isNetImage()) {//网络图片
            Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getPath()).into(ivItem);
        } else {
            ivItem.setImageBitmap(BitmapFactory.decodeFile(bean.getPath()));
        }
        //删除
        ivDelete.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(bean.getPath())) {
                listener.deleteUploadImage(bean, holder.getAdapterPosition());
            }
        });
        if (list != null && list.size() > 0 && holder.getAdapterPosition() == list.size() - 1) {
            //添加图片
            ivDelete.setVisibility(View.GONE);
            ivItem.setOnClickListener(v -> listener.addUploadImage());
        } else {
            ivDelete.setVisibility(View.VISIBLE);
        }
    }
}