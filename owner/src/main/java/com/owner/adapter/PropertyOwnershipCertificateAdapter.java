package com.owner.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.owner.R;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/7/13.
 * Descriptions:
 **/
public class PropertyOwnershipCertificateAdapter extends CommonListAdapter<String> {

    public PropertyOwnershipCertificateAdapter(Context context, List<String> list) {
        super(context, R.layout.item_id_company_img, list);

    }

    @Override
    public void convert(ViewHolder holder, final String bean) {
        ImageView ivItem = holder.getView(R.id.iv_item);
        ImageView ivDelete = holder.getView(R.id.iv_delete);
//        ivItem.setImageBitmap();
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}