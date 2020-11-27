package com.owner.rpc;

import android.text.TextUtils;

import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.IdentityRejectBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.common.rpc.request.BuildingJointWorkInterface;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.identity.model.BusinessCircleBean;
import com.owner.identity.model.ImageBean;
import com.owner.rpc.request.IdentitySearchInterface;
import com.owner.rpc.request.LoginInterface;
import com.owner.rpc.request.MineMsgInterface;
import com.owner.rpc.request.ScheduleInterface;
import com.owner.schedule.model.ViewingDateBean;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by YangShiJie
 * Data 2020/5/8.
 * Descriptions:
 **/
public class OfficegoApi {
    public static final String TAG = "OfficegoApi";

    public static OfficegoApi getInstance() {
        return Singleton.INSTANCE;
    }

    private static final class Singleton {
        private static final OfficegoApi INSTANCE = new OfficegoApi();
    }

    private RequestBody requestBody(String content) {
        return RequestBody.create(MediaType.parse("text/plain"), content);
    }

    /**
     * 登录
     *
     * @param phone 是	string	手机号
     */
    public void getSmsCode(String phone, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("phone", requestBody(phone));
        OfficegoRetrofitClient.getInstance().create(LoginInterface.class)
                .getSmsCode(map)
                .enqueue(callback);
    }

    /**
     * 修改手机号
     */
    public void modifyMobile(String mobile, String code, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("newPhone", requestBody(mobile));
        map.put("code", requestBody(code));
        map.put("channel", requestBody("2"));
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .modifyMobile(map)
                .enqueue(callback);
    }


    /**
     * 预约看房行程
     * startTime 	是 	int 	开始时间
     * endTime 	是 	int
     */
    public void getScheduleList(long startTime, long endTime, RetrofitCallback<List<ViewingDateBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("startTime", requestBody(startTime + ""));
        map.put("endTime", requestBody(endTime + ""));
        OfficegoRetrofitClient.getInstance().create(ScheduleInterface.class)
                .getScheduleList(map)
                .enqueue(callback);
    }

    /**
     * 看房记录
     * startTime 	是 	int 	开始时间
     * endTime 	是 	int
     */
    public void getOldScheduleList(long startTime, long endTime, RetrofitCallback<List<ViewingDateBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("startTime", requestBody(startTime + ""));
        map.put("endTime", requestBody(endTime + ""));
        OfficegoRetrofitClient.getInstance().create(ScheduleInterface.class)
                .getOldScheduleList(map)
                .enqueue(callback);
    }


    /**
     * 切换身份
     * 用户身份标：0租户，1户主
     */
    public void switchId(String roleType,
                         RetrofitCallback<LoginBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("roleType", requestBody(roleType));
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .switchId(map)
                .enqueue(callback);
    }


    /**
     * 商圈
     *
     * @param callback
     */
    public void getDistrictList(RetrofitCallback<List<BusinessCircleBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
//        map.put("city", requestBody("上海市"));
        map.put("type", requestBody("1")); //1：全部，0：系统已有楼盘的地铁
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .getDistrictList(map)
                .enqueue(callback);
    }

    /**
     * app扫码web登录
     */
    public void scanWebLogin(String content, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("uid", requestBody(SpUtils.getUserId()));
        map.put("token", requestBody(content));
        OfficegoRetrofitClientWeb.getInstance().create(LoginInterface.class)
                .scanLogin(map)
                .enqueue(callback);
    }

    /**
     * 上传多张图片
     */
    public void uploadImageUrl(int type, List<ImageBean> mFilePath, RetrofitCallback<UploadImageBean> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("filedirType", type + "");
        if (mFilePath != null && mFilePath.size() > 0) {
            RequestBody file;
            boolean isNetImg;
            for (int i = 0; i < mFilePath.size(); i++) {
                isNetImg = mFilePath.get(i).isNetImage();
                if (!isNetImg && i < mFilePath.size() - 1) {
                    file = RequestBody.create(MediaType.parse("image/*"), new File(mFilePath.get(i).getPath()));
                    builder.addFormDataPart("files", "owner_files" + i + ".png", file);
                }
            }
        }
        OfficegoRetrofitClient1.getInstance().create(BuildingJointWorkInterface.class)
                .uploadResourcesUrl(builder.build())
                .enqueue(callback);
    }

    /**
     * 提交认证
     * token 	是 	string 	用户名
     * btype 	是 	int 	1楼2网点
     * buildingName 	是 	string 	楼名称
     * mainPic 	是 	file 	封面图
     * <p>
     * buildId 	否 	int 	有值必传无值无须传 关联楼id
     * buildingId 	否 	int 	有值必传无值无须传 创建楼id
     * districtId 	否 	int 	大区id 关联的时候不需要传
     * businessDistrict 	否 	int 	商圈id 关联的时候不需要传
     * address 	否 	string 	楼地址
     * <p>
     * premisesPermit 	是 	String 	房产证
     * businessLicense 	是 	String 	营业执照
     * idFront 	是 	String 	身份证正面
     * idBack 	是 	String 	身份证反面
     * materials 	是 	String 	补充资料
     * isHolder 	是 	String 	权利人类型1个人2企业
     * isFrist 	是 	String 	用户第一个认证的楼：1 后面添加的楼：2
     */
    public void submitIdentity(int btype, int isFrist, String buildingName, String mainPic,
                               String premisesPermit, String businessLicense, String materials, String idFront,
                               String idBack, int isHolder, String buildId, int buildingId,
                               int districtId, int businessDistrict, String address,
                               RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("btype", requestBody(btype + ""));
        map.put("isFrist", requestBody(isFrist + ""));
        map.put("buildingName", requestBody(buildingName));
        map.put("mainPic", requestBody(mainPic));
        map.put("premisesPermit", requestBody(premisesPermit));
        if (Constants.TYPE_BUILDING == btype) {//网点不传
            map.put("isHolder", requestBody(isHolder + ""));
        }
        if (isHolder == 1) {//个人
            map.put("idFront", requestBody(idFront));
            map.put("idBack", requestBody(idBack));
        }
        if (Constants.TYPE_JOINTWORK == btype || isHolder == 2) {//公司或网点
            map.put("businessLicense", requestBody(businessLicense));//营业执照
        }
        map.put("materials", requestBody(materials));
        map.put("buildId", requestBody(buildId + ""));
        if (TextUtils.isEmpty(buildId) || TextUtils.equals("0", buildId)) {
            map.put("districtId", requestBody(districtId + ""));
            map.put("businessDistrict", requestBody(businessDistrict + ""));
            map.put("address", requestBody(address));
        }
        if (buildingId != 0) {
            map.put("buildingId", requestBody(buildingId + ""));
        }
        OfficegoRetrofitClient1.getInstance().create(BuildingJointWorkInterface.class)
                .addAttestationApp(map)
                .enqueue(callback);
    }

    //认证信息回显，驳回
    public void getIdentityMessage(int buildingId, RetrofitCallback<IdentityRejectBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId + ""));
        OfficegoRetrofitClient1.getInstance().create(BuildingJointWorkInterface.class)
                .getAttestation(map)
                .enqueue(callback);
    }
}
