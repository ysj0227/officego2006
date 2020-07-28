package com.owner.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

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
public class RentalAgreementAdapter extends CommonListAdapter<ImageBean> {

    private List<ImageBean> list;
    private Context context;

    public RentalAgreementListener getAgreementListener() {
        return agreementListener;
    }

    public void setAgreementListener(RentalAgreementListener agreementListener) {
        this.agreementListener = agreementListener;
    }

    private RentalAgreementListener agreementListener;

    public interface RentalAgreementListener {
        void addRentalAgreement();

        void deleteRentalAgreement(ImageBean bean,int position);
    }

    public RentalAgreementAdapter(Context context, List<ImageBean> list) {
        super(context, R.layout.item_id_company_img, list);
        this.list = list;
        this.context=context;
    }

    @Override
    public void convert(ViewHolder holder, final ImageBean bean) {
        ImageView ivItem = holder.getView(R.id.iv_item);
        ImageView ivDelete = holder.getView(R.id.iv_delete);
        if (bean.isNetImage()) {//网络图片
            Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getPath()).into(ivItem);
        } else {
            ivItem.setImageBitmap(BitmapFactory.decodeFile(bean.getPath()));
        }
        ivDelete.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(bean.getPath())){
                agreementListener.deleteRentalAgreement(bean,holder.getAdapterPosition());
            }
        });
        if (list != null && list.size() > 0 && holder.getAdapterPosition() == list.size() - 1) {
            ivDelete.setVisibility(View.GONE);
            ivItem.setOnClickListener(v -> agreementListener.addRentalAgreement());
        } else {
            ivDelete.setVisibility(View.VISIBLE);
        }
    }
}