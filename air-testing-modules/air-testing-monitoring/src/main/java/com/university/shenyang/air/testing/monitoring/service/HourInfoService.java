package com.university.shenyang.air.testing.monitoring.service;

import com.university.shenyang.air.testing.model.HourInfo;


import java.util.List;
/**
 * Created by cjcqqqq on 2017/5/15.
 */
public interface HourInfoService {


    int batchInsert(List<HourInfo> records);
    int insert(HourInfo record);
    List<HourInfo> selectLatestByDeviceCode(String deviceCode);

}
