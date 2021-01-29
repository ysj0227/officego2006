package com.officego.ui.home;

import android.content.Context;

import com.officego.ui.home.model.BuildingConditionItem;
import com.officego.ui.home.model.BuildingDetailsBean;
import com.officego.ui.home.model.BuildingJointWorkBean;
import com.officego.utils.ImageLoaderUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2021/1/28
 **/
public class CommonDetails {
    /**
     * 详情轮播图
     */
    public static void bannerSet(Context context, Banner bannerImage, List<String> mBannerList) {
        //数字
        bannerImage.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器，图片加载器在下方
        bannerImage.setImageLoader(new ImageLoaderUtils(context));
        //设置图片网址或地址的集合
        bannerImage.setImages(mBannerList);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        bannerImage.setBannerAnimation(Transformer.Default);
        //设置是否为自动轮播，默认是“是”。
        bannerImage.isAutoPlay(false);
        //bannerImage.setOnBannerListener(this);
        bannerImage.start();
    }

    /**
     * 楼盘详情左右滑动列表
     */
    public static List<BuildingConditionItem> buildingList(BuildingDetailsBean data) {
        List<BuildingConditionItem> conditionList = new ArrayList<>();
        conditionList.add(new BuildingConditionItem("全部", "", data.getFactorMap().getBuildingItem0() + "套"));
        if (data.getFactorMap().getBuildingItem1() > 0) {
            conditionList.add(new BuildingConditionItem("0-100㎡", "0,100", data.getFactorMap().getBuildingItem1() + "套"));
        }
        if (data.getFactorMap().getBuildingItem2() > 0) {
            conditionList.add(new BuildingConditionItem("100-200㎡", "100,200", data.getFactorMap().getBuildingItem2() + "套"));
        }
        if (data.getFactorMap().getBuildingItem3() > 0) {
            conditionList.add(new BuildingConditionItem("200-300㎡", "200,300", data.getFactorMap().getBuildingItem3() + "套"));
        }
        if (data.getFactorMap().getBuildingItem4() > 0) {
            conditionList.add(new BuildingConditionItem("300-400㎡", "300,400", data.getFactorMap().getBuildingItem4() + "套"));
        }
        if (data.getFactorMap().getBuildingItem5() > 0) {
            conditionList.add(new BuildingConditionItem("400-500㎡", "400,500", data.getFactorMap().getBuildingItem5() + "套"));
        }
        if (data.getFactorMap().getBuildingItem6() > 0) {
            conditionList.add(new BuildingConditionItem("500-1000㎡", "500,1000", data.getFactorMap().getBuildingItem6() + "套"));
        }
        if (data.getFactorMap().getBuildingItem7() > 0) {
            conditionList.add(new BuildingConditionItem("1000㎡以上", "1000,999999999", data.getFactorMap().getBuildingItem7() + "套"));
        }
        return conditionList;
    }

    /**
     * 网点详情左右滑动列表
     */
    public static List<BuildingConditionItem> jointWorkList(BuildingJointWorkBean data) {
        List<BuildingConditionItem> conditionList = new ArrayList<>();
        conditionList.add(new BuildingConditionItem("全部", "", data.getFactorMap().getJointworkItem0() + "套"));
        if (data.getFactorMap().getJointworkItem1() > 0) {
            conditionList.add(new BuildingConditionItem("1人", "0,1", data.getFactorMap().getJointworkItem1() + "套"));
        }
        if (data.getFactorMap().getJointworkItem2() > 0) {
            conditionList.add(new BuildingConditionItem("2～3人", "2,3", data.getFactorMap().getJointworkItem2() + "套"));
        }
        if (data.getFactorMap().getJointworkItem3() > 0) {
            conditionList.add(new BuildingConditionItem("4～6人", "4,6", data.getFactorMap().getJointworkItem3() + "套"));
        }
        if (data.getFactorMap().getJointworkItem4() > 0) {
            conditionList.add(new BuildingConditionItem("7～10人", "7,10", data.getFactorMap().getJointworkItem4() + "套"));
        }
        if (data.getFactorMap().getJointworkItem5() > 0) {
            conditionList.add(new BuildingConditionItem("11～15人", "11,15", data.getFactorMap().getJointworkItem5() + "套"));
        }
        if (data.getFactorMap().getJointworkItem6() > 0) {
            conditionList.add(new BuildingConditionItem("16～20人", "16,20", data.getFactorMap().getJointworkItem6() + "套"));
        }
        if (data.getFactorMap().getJointworkItem7() > 0) {
            conditionList.add(new BuildingConditionItem("20人以上", "20,999999999", data.getFactorMap().getJointworkItem7() + "套"));
        }
        return conditionList;
    }

    /**
     * 线路
     */
    public static String routeLine(boolean isExpand, List<String> stationLine, List<String> workTime, List<String> stationName) {
        StringBuilder linePlan = new StringBuilder();
        if (isExpand) {
            for (int i = 0; i < stationLine.size(); i++) {
                if (stationLine.size() == 1 || i == stationLine.size() - 1) {
                    linePlan.append("步行").append(workTime.get(i)).append("分钟到 | ")
                            .append(stationLine.get(i)).append("号线 ·").append(stationName.get(i));
                } else {
                    linePlan.append("步行").append(workTime.get(i)).append("分钟到 | ")
                            .append(stationLine.get(i)).append("号线 ·").append(stationName.get(i)).append("\n");
                }
            }
        } else {
            linePlan.append("步行").append(workTime.get(0)).append("分钟到 | ")
                    .append(stationLine.get(0)).append("号线 ·").append(stationName.get(0));
        }
        return linePlan.toString();
    }

}
