package com.university.shenyang.air.testing.gateway.task;

import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.util.Constants;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.model.ReportInfo;
import com.university.shenyang.air.testing.service.DeviceInfoService;
import com.university.shenyang.air.testing.service.ReportInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by cjcqqqq on 2017/5/18.
 */
@Component
public class DataTransferTask {
    @Autowired
    DeviceInfoService deviceInfoService;
    @Autowired
    ReportInfoService reportInfoService;
    @Autowired
    public RedisTemplate<String, ReportInfo> redisTemplate;

    @Value("${data.transfer.batch.size:100}")
    private int batchSize;

    @Scheduled(cron = "0 5 0 * * ?")
    public void run() {
        List<DeviceInfo> deviceInfoList = deviceInfoService.queryAll();
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);

        long startTime = todayStart.getTime().getTime() - 1000 * 60 * 60 * 24 * 2;
        long endTime = todayStart.getTime().getTime() - 1000 * 60 * 60 * 24;

        for (DeviceInfo deviceInfo : deviceInfoList) {
            List<ReportInfo> reportInfoList = new ArrayList<>();
            Set<ReportInfo> reportInfos = redisTemplate.opsForZSet().rangeByScore(Constants.REPORT_REDIS_KEY_PREFIX + deviceInfo.getDeviceCode(), startTime, endTime);
            reportInfoList.addAll(reportInfos);

            if (reportInfoList.size() > 0) {
                if (reportInfoList.size() > batchSize) {
                    for (int i = 0; i < reportInfoList.size() / batchSize; i++) {
                        reportInfoService.batchInsert(reportInfoList.subList(i * batchSize, (i + 1) * batchSize - 1));
                    }
                    if (reportInfoList.size() % batchSize != 0) {
                        reportInfoService.batchInsert(reportInfoList.subList(reportInfoList.size() / batchSize * batchSize, reportInfoList.size() - 1));
                    }
                } else {
                    reportInfoService.batchInsert(reportInfoList);
                }
            }

            redisTemplate.opsForZSet().removeRangeByScore(Constants.REPORT_REDIS_KEY_PREFIX + deviceInfo.getDeviceCode(), startTime, endTime);
        }
    }
}
