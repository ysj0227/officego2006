package com.owner.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.utils.GlideUtils;
import com.owner.R;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/7/13.
 * Descriptions:
 **/
public class PropertyOwnershipCertificateAdapter extends CommonListAdapter<String> {

    private List<String> list;
    private Context context;

    public CertificateListener getCertificateListener() {
        return certificateListener;
    }

    public void setCertificateListener(CertificateListener certificateListener) {
        this.certificateListener = certificateListener;
    }

    private CertificateListener certificateListener;

    public PropertyOwnershipCertificateAdapter(Context context, List<String> list) {
        super(context, R.layout.item_id_company_img, list);
        this.list = list;
        this.context = context;
    }

    public interface CertificateListener {
        void addCertificate();

        void deleteCertificate(int position);
    }

    @Override
    public void convert(ViewHolder holder, final String bean) {
        ImageView ivItem = holder.getView(R.id.iv_item);
        ImageView ivDelete = holder.getView(R.id.iv_delete);
        if (bean.contains("http") || bean.contains("https")) {
            Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean).into(ivItem);
        } else {
            ivItem.setImageBitmap(BitmapFactory.decodeFile(bean));
        }
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(bean)) {
                    certificateListener.deleteCertificate(holder.getAdapterPosition());
                }
            }
        });
        if (list != null && list.size() > 0 && holder.getAdapterPosition() == list.size() - 1) {
            ivDelete.setVisibility(View.GONE);
            ivItem.setOnClickListener(v -> certificateListener.addCertificate());
        } else {
            ivDelete.setVisibility(View.VISIBLE);
        }
    }
}