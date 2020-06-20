package com.officego.commonlib.base.recycle.listener;


import com.officego.commonlib.base.recycle.BaseViewHolder;

/**
 * @author yinhui
 * @since 19-4-18
 */
public interface OnViewClickListener<T> {

    /**
     * Called when a view has been clicked.
     *
     * @param holder   clicked view holder.
     * @param model    the item_house_type model attach to clicked view.
     * @param position position in list of clicked view.
     */
    void onClick(BaseViewHolder<T> holder, T model, int position);
}
