package com.owner.home;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.model.BuildingManagerBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.PermissionUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.ClearableEditText;
import com.owner.home.contract.UploadVideoVrContract;
import com.owner.home.presenter.UploadVideoVrPresenter;
import com.owner.zxing.QRScanActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by shijie
 * Date 2020/10/13
 **/
@EActivity(resName = "activity_home_upload_video_vr")
public class UploadVideoVrActivity extends BaseMvpActivity<UploadVideoVrPresenter>
        implements UploadVideoVrContract.View {
    @ViewById(resName = "btn_scan")
    Button btnScan;
    @ViewById(resName = "iv_close_scan")
    ImageView ivCloseScan;
    @ViewById(resName = "et_vr")
    ClearableEditText etVr;
    @Extra
    int flay;
    @Extra
    BuildingManagerBean buildingManagerBean;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new UploadVideoVrPresenter();
        mPresenter.attachView(this);
    }

    @Click(resName = "btn_next")
    void publishOnClick() {
        String vr = etVr.getText() == null ? "" : etVr.getText().toString();
        if (flay == Constants.FLAG_BUILDING) {
            mPresenter.publishBuilding(buildingManagerBean.getBuildingId(), buildingManagerBean.getIsTemp(), vr);
        }else {
            //TODO
        }

    }

    @Click(resName = "iv_close_scan")
    void closeScanOnClick() {
        btnScan.setVisibility(View.GONE);
        ivCloseScan.setVisibility(View.GONE);
    }

    //web 去编辑
    @Click(resName = "btn_scan")
    void toWebEditOnClick() {
        if (!PermissionUtils.checkSDCardCameraPermission(this)) {
            return;
        }
        startActivity(new Intent(context, QRScanActivity.class));
    }


    @Override
    public void publishSuccess() {
        shortTip("发布成功");
    }
}
