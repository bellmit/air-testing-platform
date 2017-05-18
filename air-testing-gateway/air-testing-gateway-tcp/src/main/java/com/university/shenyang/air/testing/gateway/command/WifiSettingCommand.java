package com.university.shenyang.air.testing.gateway.command;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class WifiSettingCommand extends BaseCommand {
    @NotEmpty(message = "deviceCode cannot be empty")
    private String deviceCode;
    @NotEmpty(message = "account cannot be empty")
    private String account;
    @NotEmpty(message = "password cannot be empty")
    private String password;


    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
