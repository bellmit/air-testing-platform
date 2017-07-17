package com.university.shenyang.air.testing.monitoring.command;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class QueryLatestReportCommand extends BaseCommand {
    @NotEmpty(message = "deviceCode cannot be empty")
    private String deviceCode;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

}
