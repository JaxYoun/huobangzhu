package com.troy.keeper.hbz.consts;

/**
 * Created by leecheng on 2017/10/16.
 */
public class Const {

    /**
     * 系统审计字段
     */
    public final static String[] AUDIT_FIELDS = new String[]{"createdDate", "createdBy", "lastUpdatedBy", "lastUpdatedDate"};
    public final static String[] ID_AUDIT_FIELDS = new String[]{"id", "createdDate", "createdBy", "lastUpdatedBy", "lastUpdatedDate"};
    public final static String[] ID_FIELDS = new String[]{"id"};

    /**
     * 订单类型
     */
    public final static String SCORE_ORDER = "score";

    /**评价常量*/
    //货主订单评分
    public final static String CREDIT_SCORE_TYPE_PROV = "1";
    //车主订单评分
    public final static String CREDIT_SCORE_TYPE_CONS = "2";
    //评星业务
    public final static String CREDIT_SCORE_ADJUEST_ACTION_ALL = "1";
    //订单业务
    public final static String CREDIT_SCORE_ADJUEST_TYPE_ALL = "1";
    //综合积分-分值变化
    public final static String SCORE_CHG_ACTION_ALL = "1";
    //积分商城兑换
    public final static String SCORE_CHG_TYPE_CHG = "1";
    //消费完成
    public final static String SCORE_CHG_TYPE_ORDE = "2";
    //积分商城
    public final static String SCORE_CHG_ADJUEST_TYPE_CHG = "1";
    //订单
    public final static String SCORE_CHG_ADJUEST_TYPE_ORDE = "2";



    /**
     * 保证金类型
     */
    public final static String BOND_BUY = "BOND_BUY";     //帮我买
    public final static String BOND_SEND = "BOND_SEND";   //帮我送
    public final static String BOND_SPL = "BOND_SPL";     //专线运输

    /**
     * 基础常量
     */
    public final static <T> T NULL() {
        return null;
    }

    /**
     * 认证码字段定义
     */
    public final static String AUTH_CODE_NAME = "authCode";

    /**
     * 删除标记
     */
    public final static String STATUS_DISABLED = "0";
    public final static String STATUS_ENABLED = "1";

    /**
     * 状态码设置
     */
    public final static String STATUS_OK = "200";   //正常返回
    public final static String STATUS_VALIDATION_ERROR = "501"; //验证失败
    public final static String STATUS_PRE_ERROR = "504";
    public final static String STATUS_ERROR = "500"; //服务器内部错误
    public final static String STATUS_UN_AUTHENCATIIONED = "401"; //未认证
    public final static String STATUS_AUTH_REJECT = "403";

    /**
     * 日期及时间格式 常量 by Yang jx
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 手机号验证正则表达式
     */
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    /**
     * 日期正则表达式
     */
    public static final String REGEX_YYYY_MM_DD_HH_MM = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d$";

    /**
     * 仓储流转状态 新建未作后续处理
     */
    public static final Integer WAREHOUSE_CREATED = Integer.valueOf(0);

    /**
     * 仓储流转状态 已发布
     */
    public static final Integer WAREHOUSE_PUBLISHED = Integer.valueOf(1);

    /**
     * 仓储流转状态 拒绝
     */
    public static final Integer WAREHOUSE_OUT_OF_TIME = Integer.valueOf(2);

    /**
     * 仓储流转状态 已下架
     */
    public static final Integer WAREHOUSE_LOSE_EFFICACY_ = Integer.valueOf(4);
}
