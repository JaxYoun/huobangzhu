package com.troy.keeper.hbz.service.boot;

import com.troy.keeper.hbz.service.*;
import com.troy.keeper.hbz.sys.annotation.Boot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by leecheng on 2017/10/13.
 */
@Service
@Slf4j
public class Booter {

    @Autowired
    OrderScheduleService orderScheduleService;

    @Autowired
    HbzUserAutoService hbzUserAutoService;

    @Autowired
    HbzRefundAutoService hbzRefundAutoService;

    @Autowired
    HbzFlushService hbzFlushService;

    @Autowired
    HbzBillAutoService hbzBillAutoService;

    @Boot
    public void run() {

        /**帮买帮送订单超时检测任务*/
        TimerTask flush = new TimerTask() {
            @Override
            public void run() {
                hbzFlushService.auto();
            }
        };
        Timer flushTimer = new Timer();
        flushTimer.schedule(flush, 0L, 60L * 1000L);

        /**专线超期检测任务*/
        TimerTask fslTask = new TimerTask() {
            @Override
            public void run() {
                orderScheduleService.run();
            }
        };
        Timer fslTimer = new Timer();
        fslTimer.schedule(fslTask, 0L, 1800L * 1000L);

        /**专线输询
         * 1、在普通订单司机接单后，如果20分钟货主不同意，则取消该订单
         * */
        TimerTask sTask = new TimerTask() {
            @Override
            public void run() {
                orderScheduleService.s();
            }
        };
        Timer sTimer = new Timer();
        sTimer.schedule(sTask, 0L, 60L * 1000L);

        /**用户星级评级任务*/
        TimerTask userUpdateTask = new TimerTask() {
            @Override
            public void run() {
                hbzUserAutoService.auto();
            }
        };
        Timer userUpdateTimer = new Timer();
        userUpdateTimer.schedule(userUpdateTask, 0L, 60L * 1000L);

        /**退款查询任务*/
        TimerTask refundTask = new TimerTask() {
            @Override
            public void run() {
                hbzRefundAutoService.auto();
            }
        };
        Timer refundTimerTask = new Timer();
        refundTimerTask.schedule(refundTask, 0L, 60L * 1000L);

        /**对账单下载任务*/
        TimerTask billTask = new TimerTask() {
            @Override
            public void run() {
                hbzBillAutoService.autodo();
            }
        };
        Timer bt = new Timer();
        bt.schedule(billTask, 0L, 2 * 60 * 60L * 1000L);
    }

}
