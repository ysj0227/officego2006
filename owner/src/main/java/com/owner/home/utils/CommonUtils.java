package com.owner.home.utils;

import android.text.TextUtils;
import android.widget.CheckBox;

import com.owner.identity.model.ImageBean;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/4
 **/
public class CommonUtils {
    //网络
    public static String internet(CheckBox rbTelecom, CheckBox rbUnicom, CheckBox rbMobile) {
        StringBuilder buffer = new StringBuilder();
        if (rbTelecom.isChecked()) {
            buffer.append(rbTelecom.getText().toString()).append(",");
        }
        if (rbUnicom.isChecked()) {
            buffer.append(rbUnicom.getText().toString()).append(",");
        }
        if (rbMobile.isChecked()) {
            buffer.append(rbMobile.getText().toString()).append(",");
        }
        String net = buffer.toString();
        if (!TextUtils.isEmpty(net)) {
            return buffer.toString().substring(0, buffer.toString().length() - 1);
        }
        return "";
    }

    //入住企业
    public static String company(List<String> jointCompanyList) {
        StringBuilder settlementLicence = new StringBuilder();
        if (jointCompanyList != null && jointCompanyList.size() > 0) {
            for (int i = 0; i < jointCompanyList.size(); i++) {
                settlementLicence.append(jointCompanyList.get(i)).append(",");
            }
            return settlementLicence.toString().substring(0, settlementLicence.toString().length() - 1);
        }
        return "";
    }

    //添加上传图片
    public static String addUploadImage(List<ImageBean> uploadImageList) {
        StringBuilder addImageBuffer = new StringBuilder();
        if (uploadImageList.size() > 2) {
            for (int i = 0; i < uploadImageList.size(); i++) {
                if (i > 0 && i < uploadImageList.size() - 1) {
                    addImageBuffer.append(uploadImageList.get(i).getPath()).append(",");
                }
            }
            return addImageBuffer.toString().substring(0, addImageBuffer.toString().length() - 1);
        }
        return "";
    }

    //添加上传图片
    public static String addAllUploadImage(List<ImageBean> uploadImageList) {
        StringBuilder addImageBuffer = new StringBuilder();
        if (uploadImageList.size() > 1) {
            for (int i = 0; i < uploadImageList.size(); i++) {
                if (i < uploadImageList.size() - 1) {
                    addImageBuffer.append(uploadImageList.get(i).getPath()).append(",");
                }
            }
            return addImageBuffer.toString().substring(0, addImageBuffer.toString().length() - 1);
        }
        return "";
    }

    //入住企业
    public static String delUploadImage(List<String> deleteList) {
        StringBuilder delImageBuffer = new StringBuilder();
        if (deleteList.size() > 0) {
            for (int i = 0; i < deleteList.size(); i++) {
                delImageBuffer.append(deleteList.get(i)).append(",");
            }
            return delImageBuffer.toString().substring(0, delImageBuffer.toString().length() - 1);
        }
        return "";
    }
}
