package com.troy.keeper.hbz.helper;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author：YangJx
 * @Description：web端支付工具类
 * @DateTime：2017/12/29 11:03
 */
public final class WebPayUtil {

    private static DateTimeFormatter formatter_yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 生成订单编码
     *
     * @param commodityId 商品id
     * @param commodityId 商品类型（如：保证金，仓储租赁诚意金等）
     * @return
     */
    public static final String generateOrderCode(final String orderType, final Long commodityId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(orderType);
        stringBuilder.append('-');
        if (commodityId != null) {
            stringBuilder.append(commodityId);
            stringBuilder.append('-');
        }
        LocalDateTime now = LocalDateTime.now();
        stringBuilder.append(formatter_yyyyMMddHHmmss.format(now));
        stringBuilder.append('-');
        stringBuilder.append(new SecureRandom().nextInt(9999));
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(generateOrderCode("FSL", null));

    }

}
