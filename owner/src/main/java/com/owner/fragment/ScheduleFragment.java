package com.owner.fragment;

import com.officego.commonlib.base.BaseFragment;
import com.officego.commonlib.utils.StatusBarUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@EFragment(resName = "schedule_fragment")
public class ScheduleFragment extends BaseFragment {

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);

    }


}
