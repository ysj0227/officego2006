package com.owner.pay;

import com.owner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijie
 * Date 2021/3/29
 **/
public class PayUtils {

    public static List<RightsBean> rightsList() {
        List<RightsBean> list = new ArrayList<>();
        RightsBean bean = new RightsBean(R.mipmap.ic_rights_contacts,
                "1.与客户直接沟通", "每天聊天人数、聊天次数不限量，满足与大量客户的沟通需求。");
        list.add(bean);
        RightsBean bean1 = new RightsBean(R.mipmap.ic_rights_views,
                "2.双向预约看房", "可主动向意向客户发起“换微信”“换电话”。");
        list.add(bean1);
        RightsBean bean2 = new RightsBean(R.mipmap.ic_rights_free,
                "3.免佣金", "0佣金，成交客户OfficeGo平台不抽佣。");
        list.add(bean2);
        RightsBean bean3 = new RightsBean(R.mipmap.ic_rights_publish,
                "4.房源发布不限量", "同一楼盘/网点，可无限量上传房源，可随时修改各房源信息，满足您多套房源出租需求。");
        list.add(bean3);
        RightsBean bean4 = new RightsBean(R.mipmap.ic_rights_no_ads,
                "5.免费广告", "OfficeGo APP首页列表广告位，免费赠送3天，价值3600元。");
        list.add(bean4);
        RightsBean bean5 = new RightsBean(R.mipmap.ic_rights_recommend,
                "6.精准推荐", "通过大数据技术，OfficeGo会将您的房源主动推荐给潜在意向用户，助您获取更多精准客户。");
        list.add(bean5);
        RightsBean bean6 = new RightsBean(R.mipmap.ic_rights_free_tools,
                "7.免费工具", "OfficeGo开发的在线营销功能，您能免费使用。");
        list.add(bean6);
        RightsBean bean7 = new RightsBean(R.mipmap.ic_rights_report,
                "8.专享报告", "OfficeGo会定期生成有关租金数据、行业趋势、营销效果等的白皮书，会员将免费获得，赋能招租策略。");
        list.add(bean7);
        return list;
    }
}
