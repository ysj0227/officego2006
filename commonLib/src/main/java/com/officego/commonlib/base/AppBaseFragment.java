package com.officego.commonlib.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AppBaseFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(activityLayoutId(), container, false);
        initView(inflater, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected abstract int activityLayoutId();

    protected abstract void initView(LayoutInflater inflater, View v);

}
