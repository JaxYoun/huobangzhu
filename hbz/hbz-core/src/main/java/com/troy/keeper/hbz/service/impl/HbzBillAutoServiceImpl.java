package com.troy.keeper.hbz.service.impl;

import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.troy.keeper.hbz.dto.HbzBillDTO;
import com.troy.keeper.hbz.helper.StringHelper;
import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.FormatedDate;
import com.troy.keeper.hbz.type.BillType;
import com.troy.keeper.hbz.type.PayType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by leecheng on 2018/3/8.
 */
@Slf4j
@Service
public class HbzBillAutoServiceImpl implements HbzBillAutoService {

    @Autowired
    AlipayService alipayService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HbzBillService hbzBillService;

    @Autowired
    HbzPayService hbzPayService;

    @Autowired
    HbzRefundService hbzRefundService;

    @Override
    public void autodo() {
        Long currentTimeMillis = System.currentTimeMillis();
        FormatedDate current = new FormatedDate(currentTimeMillis).getDayFirst().addDate(-1);
        String currentDate = current.getFormat("yyyy-MM-dd");

        //支付宝账单
        AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
        model.setBillDate(currentDate);
        model.setBillType("trade");
        AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayService.bill_get(model);
        if (response != null && response.isSuccess()) {
            Gson gson = new Gson();
            JsonObject bj = gson.fromJson(response.getBody(), JsonObject.class);
            log.info("body:{}", gson.toJson(bj));
            try {
                byte[] dat = downloadNet(response.getBillDownloadUrl());
                ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(dat), Charset.forName("GBK"));
                ZipEntry zipEntry = null;
                while ((zipEntry = zip.getNextEntry()) != null) {
                    String name = zipEntry.getName();
                    if (name.endsWith("业务明细.csv")) {
                        byte[] data = IOUtils.toByteArray(zip);
                        String csv = new String(data, "GBK");
                        log.info("csv:" + csv);
                        List<String> texts = Arrays.asList(csv.split("(\\r|\\n)+"));
                        Stream<String> list = texts.stream().filter(line -> !line.startsWith("#"));
                        List<String> lines = list.collect(Collectors.toList());
                        for (int i = 0; i < lines.size(); i++) {
                            String[] parts = lines.get(i).split("\\,");
                            if (parts[0].trim().matches("\\d+")) {
                                String alipayTradeNo = parts[0].trim();
                                String tradeNo = parts[1].trim();
                                String billType = parts[2].trim();
                                String fee = parts[10].trim();
                                String out = parts[11].trim();
                                String requestNo = parts[21].trim();

                                HbzBillDTO queryExists = new HbzBillDTO();
                                boolean isExists = false;
                                if (billType.contains("交易")) {
                                    queryExists.setStatus("1");
                                    queryExists.setPayType(PayType.Alipay);
                                    queryExists.setBillType(BillType.PAY);
                                    queryExists.setDate(current.getTimeMillis());
                                    queryExists.setTradeNo(tradeNo);
                                    isExists = hbzBillService.count(queryExists) > 0L;
                                } else if (billType.contains("退款")) {
                                    queryExists.setStatus("1");
                                    queryExists.setBillType(BillType.REFUND);
                                    queryExists.setPayType(PayType.Alipay);
                                    queryExists.setDate(current.getTimeMillis());
                                    queryExists.setTradeNo(tradeNo);
                                    queryExists.setRequestNo(requestNo);
                                    isExists = hbzBillService.count(queryExists) > 0L;
                                }
                                if (!isExists) {
                                    HbzBillDTO bill = new HbzBillDTO();
                                    bill.setStatus("1");
                                    bill.setBillContent(lines.get(i));
                                    bill.setPayType(PayType.Alipay);
                                    bill.setDate(current.getTimeMillis());
                                    bill.setBillType(billType.contains("交易") ? BillType.PAY : BillType.REFUND);
                                    bill.setRequestNo(billType.contains("退款") ? requestNo : null);
                                    bill.setTradeNo(tradeNo);
                                    bill = hbzBillService.save(bill);
                                    switch (bill.getBillType()) {
                                        case PAY:
                                            hbzPayService.bill(bill);
                                            break;
                                        case REFUND:
                                            hbzRefundService.bill(bill);
                                            break;
                                    }

                                }
                            }
                        }


                    }
                }


            } catch (Exception e) {

            }


        }
    }


    public static byte[] downloadNet(String httpUrl) {
        int bytesum = 0;
        int byteread = 0;
        try {
            URL url = new URL(httpUrl);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            ByteArrayOutputStream fs = new ByteArrayOutputStream();
            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            return fs.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
