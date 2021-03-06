package com.university.shenyang.air.testing.gateway;

import com.university.shenyang.air.testing.gateway.common.kit.Convert;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.gateway.util.Constants;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.model.ReportInfo;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/12/26.
 */
public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        // 生成登入报文
        String deviceCode = "deviceCode0000001";
        create01Report(deviceCode);

        // 生成实时上报报文
        ReportInfo reportInfo = new ReportInfo();
        reportInfo.setCollectTime(new Date(new java.util.Date().getTime()));
        reportInfo.setDeviceCode(deviceCode);
        reportInfo.setSim("13998184711");
        reportInfo.setPm1_0(950);
        reportInfo.setPm2_5(800);
        reportInfo.setPm10(30);
        reportInfo.setFormaldehyde(120);
        reportInfo.setTemperature(720);
        reportInfo.setHumidity(35);
        reportInfo.setCo(111);
        reportInfo.setCo2(222);
        reportInfo.setNo(333);
        reportInfo.setNo2(444);
        reportInfo.setO3(555);
        reportInfo.setSo2(666);
        reportInfo.setTvoc(777);
        reportInfo.setWindSpeed(888);
        reportInfo.setWindDirection(95);
        reportInfo.setLongitude(121123456L);
        reportInfo.setLatitude(31123456L);
        reportInfo.setElectricity(95);
        create02Report(reportInfo);

        // 生成补发上报报文
        create03Report(reportInfo);

    }

    private static void create01Report(String deviceCode) {
        String request01 = null;
        String response01 = null;
        byte year , month , day , hour , minute , second;
        year = Byte.valueOf(new SimpleDateFormat("yy").format(Calendar.getInstance().getTime()));
        month = Byte.valueOf(new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()));
        day = Byte.valueOf(new SimpleDateFormat("dd").format(Calendar.getInstance().getTime()));
        hour = Byte.valueOf(new SimpleDateFormat("HH").format(Calendar.getInstance().getTime()));
        minute = Byte.valueOf(new SimpleDateFormat("mm").format(Calendar.getInstance().getTime()));
        second = Byte.valueOf(new SimpleDateFormat("ss").format(Calendar.getInstance().getTime()));
        byte[] time = new byte[]{year,month,day,hour,minute,second};
        try {
            request01 = "232301FE"+ Convert.bytesToHexString(ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00))+"010006"+ Convert.bytesToHexString(time)+
                    "B7";
            response01 = "23230101"+ Convert.bytesToHexString(ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00))+"010006"+ Convert.bytesToHexString(time)+
                    "B7";

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] aa = Convert.hexStringToBytes(request01);
        byte[] bb = Convert.hexStringToBytes(response01);
        System.out.println(request01.substring(0,request01.length()-2)+ Convert.bytesToHexString(new byte[]{Convert.checkPackage(aa, 2, aa.length - 2)}));
        System.out.println(response01.substring(0,response01.length()-2)+ Convert.bytesToHexString(new byte[]{Convert.checkPackage(bb, 2, bb.length - 2)}));
    }

    private static void create02Report(ReportInfo reportInfo) throws UnsupportedEncodingException {
        byte[] requestContent = new byte[111];

        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(2, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(255, 1));
        try {
            if (reportInfo.getDeviceCode().getBytes("ASCII").length == 17){
                ArraysUtils.arrayappend(requestContent, 4, reportInfo.getDeviceCode().getBytes("ASCII"));
            }else{
                System.out.println("设备code码错误,deviceCode:" + reportInfo.getDeviceCode());
                return ;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(86, 2));

        // 如果是终端校时指令应答内容为系统当前时间
        byte year = Byte.valueOf(new SimpleDateFormat("yy").format(reportInfo.getCollectTime().getTime()));
        byte month = Byte.valueOf(new SimpleDateFormat("MM").format(reportInfo.getCollectTime().getTime()));
        byte day = Byte.valueOf(new SimpleDateFormat("dd").format(reportInfo.getCollectTime().getTime()));
        byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(reportInfo.getCollectTime().getTime()));
        byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(reportInfo.getCollectTime().getTime()));
        byte second = Byte.valueOf(new SimpleDateFormat("ss").format(reportInfo.getCollectTime().getTime()));

        byte[] collectTime = new byte[]{year, month, day, hour, minute, second};
        // collectTime; // 采集时间 6个字节
        ArraysUtils.arrayappend(requestContent, 24, collectTime);
        // sim; //sim卡号 20位 20个字节
        ArraysUtils.arrayappend(requestContent, 30, reportInfo.getSim().getBytes("ASCII"));
//        ArraysUtils.arrayappend(requestContent, 30, Convert.longTobytes(reportInfo.getSim(),4));
        // pm1_0; // pm1.0 单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 34+16, Convert.intTobytes(reportInfo.getPm1_0(),4));
        // pm2_5; // pm2.5 单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 38+16, Convert.intTobytes(reportInfo.getPm2_5(),4));
        // pm10;      // pm10  单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 42+16, Convert.intTobytes(reportInfo.getPm10(),4));
        // formaldehyde; // 甲醛 0.00-9.99 * 100 2个字节
        ArraysUtils.arrayappend(requestContent, 46+16, Convert.intTobytes(reportInfo.getFormaldehyde(),2));
        // temperature; // 温度 0-255 * 10 偏移500 0表示-50摄氏度 2个字节
        ArraysUtils.arrayappend(requestContent, 48+16, Convert.intTobytes(reportInfo.getTemperature(),2));
        // humidity; // 湿度 0-100 1个字节
        ArraysUtils.arrayappend(requestContent, 50+16, Convert.intTobytes(reportInfo.getHumidity(),1));
        // co; // 一氧化碳 预留4个字节
        ArraysUtils.arrayappend(requestContent, 51+16, Convert.intTobytes(reportInfo.getCo(),4));
        // co2; // 二氧化碳 预留4个字节
        ArraysUtils.arrayappend(requestContent, 55+16, Convert.intTobytes(reportInfo.getCo2(),4));
        // no; // 一氧化氮 预留4个字节
        ArraysUtils.arrayappend(requestContent, 59+16, Convert.intTobytes(reportInfo.getNo(),4));
        // no2; // 二氧化氮 预留4个字节
        ArraysUtils.arrayappend(requestContent, 63+16, Convert.intTobytes(reportInfo.getNo2(),4));
        // o3; // 臭氧 预留4个字节
        ArraysUtils.arrayappend(requestContent, 67+16, Convert.intTobytes(reportInfo.getO3(),4));
        // so2; // 二氧化硫 预留4个字节
        ArraysUtils.arrayappend(requestContent, 71+16, Convert.intTobytes(reportInfo.getSo2(),4));
        // tvoc; // 有机气态物质 预留4个字节
        ArraysUtils.arrayappend(requestContent, 75+16, Convert.intTobytes(reportInfo.getTvoc(),4));
        // windSpeed; // 风速 预留4个字节
        ArraysUtils.arrayappend(requestContent, 79+16, Convert.intTobytes(reportInfo.getWindSpeed(),4));
        // windDirection; // 风向 预留2个字节
        ArraysUtils.arrayappend(requestContent, 83+16, Convert.intTobytes(reportInfo.getWindDirection(),2));
        // longitude; // 经度 以度为单位的经度值乘以10的6次方，精确到百万分之一度。 4个字节
        ArraysUtils.arrayappend(requestContent, 85+16, Convert.longTobytes(reportInfo.getLongitude(), 4));
        // latitude; // 纬度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度。 4个字节
        ArraysUtils.arrayappend(requestContent, 89+16, Convert.longTobytes(reportInfo.getLatitude(), 4));
        // electricity; // 太阳能电源电量0-100 1个字节
        ArraysUtils.arrayappend(requestContent, 93+16, Convert.intTobytes(reportInfo.getElectricity(),1));
        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length-2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("requestContent:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[31];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(2, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(1, 1));
        try {
            if (reportInfo.getDeviceCode().getBytes("ASCII").length == 17){
                ArraysUtils.arrayappend(responseContent, 4, reportInfo.getDeviceCode().getBytes("ASCII"));
            }else{
                System.out.println("设备code码错误,deviceCode:"+reportInfo.getDeviceCode());
                return ;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(6, 2));
        ArraysUtils.arrayappend(responseContent, 24, collectTime);
        crc = Convert.checkPackage(responseContent, 2, responseContent.length-2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("responseContent:" + Convert.bytesToHexString(responseContent));
    }

    private static void create03Report(ReportInfo reportInfo) throws UnsupportedEncodingException {
        byte[] requestContent = new byte[111];

        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(3, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(255, 1));
        try {
            if (reportInfo.getDeviceCode().getBytes("ASCII").length == 17){
                ArraysUtils.arrayappend(requestContent, 4, reportInfo.getDeviceCode().getBytes("ASCII"));
            }else{
                System.out.println("设备code码错误,deviceCode:" + reportInfo.getDeviceCode());
                return ;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(86, 2));

        // 如果是终端校时指令应答内容为系统当前时间
        byte year = Byte.valueOf(new SimpleDateFormat("yy").format(reportInfo.getCollectTime().getTime()));
        byte month = Byte.valueOf(new SimpleDateFormat("MM").format(reportInfo.getCollectTime().getTime()));
        byte day = Byte.valueOf(new SimpleDateFormat("dd").format(reportInfo.getCollectTime().getTime()));
        byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(reportInfo.getCollectTime().getTime()));
        byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(reportInfo.getCollectTime().getTime()));
        byte second = Byte.valueOf(new SimpleDateFormat("ss").format(reportInfo.getCollectTime().getTime()));

        byte[] collectTime = new byte[]{year, month, day, hour, minute, second};
        // collectTime; // 采集时间 6个字节
        ArraysUtils.arrayappend(requestContent, 24, collectTime);
        // sim; //sim卡号 20位 20个字节
        ArraysUtils.arrayappend(requestContent, 30, reportInfo.getSim().getBytes("ASCII"));
//        ArraysUtils.arrayappend(requestContent, 30, Convert.longTobytes(reportInfo.getSim(),4));
        // pm1_0; // pm1.0 单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 34+16, Convert.intTobytes(reportInfo.getPm1_0(),4));
        // pm2_5; // pm2.5 单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 38+16, Convert.intTobytes(reportInfo.getPm2_5(),4));
        // pm10;      // pm10  单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 42+16, Convert.intTobytes(reportInfo.getPm10(),4));
        // formaldehyde; // 甲醛 0.00-9.99 * 100 2个字节
        ArraysUtils.arrayappend(requestContent, 46+16, Convert.intTobytes(reportInfo.getFormaldehyde(),2));
        // temperature; // 温度 0-255 * 10 偏移500 0表示-50摄氏度 2个字节
        ArraysUtils.arrayappend(requestContent, 48+16, Convert.intTobytes(reportInfo.getTemperature(),2));
        // humidity; // 湿度 0-100 1个字节
        ArraysUtils.arrayappend(requestContent, 50+16, Convert.intTobytes(reportInfo.getHumidity(),1));
        // co; // 一氧化碳 预留4个字节
        ArraysUtils.arrayappend(requestContent, 51+16, Convert.intTobytes(reportInfo.getCo(),4));
        // co2; // 二氧化碳 预留4个字节
        ArraysUtils.arrayappend(requestContent, 55+16, Convert.intTobytes(reportInfo.getCo2(),4));
        // no; // 一氧化氮 预留4个字节
        ArraysUtils.arrayappend(requestContent, 59+16, Convert.intTobytes(reportInfo.getNo(),4));
        // no2; // 二氧化氮 预留4个字节
        ArraysUtils.arrayappend(requestContent, 63+16, Convert.intTobytes(reportInfo.getNo2(),4));
        // o3; // 臭氧 预留4个字节
        ArraysUtils.arrayappend(requestContent, 67+16, Convert.intTobytes(reportInfo.getO3(),4));
        // so2; // 二氧化硫 预留4个字节
        ArraysUtils.arrayappend(requestContent, 71+16, Convert.intTobytes(reportInfo.getSo2(),4));
        // tvoc; // 有机气态物质 预留4个字节
        ArraysUtils.arrayappend(requestContent, 75+16, Convert.intTobytes(reportInfo.getTvoc(),4));
        // windSpeed; // 风速 预留4个字节
        ArraysUtils.arrayappend(requestContent, 79+16, Convert.intTobytes(reportInfo.getWindSpeed(),4));
        // windDirection; // 风向 预留2个字节
        ArraysUtils.arrayappend(requestContent, 83+16, Convert.intTobytes(reportInfo.getWindDirection(),2));
        // longitude; // 经度 以度为单位的经度值乘以10的6次方，精确到百万分之一度。 4个字节
        ArraysUtils.arrayappend(requestContent, 85+16, Convert.longTobytes(reportInfo.getLongitude(), 4));
        // latitude; // 纬度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度。 4个字节
        ArraysUtils.arrayappend(requestContent, 89+16, Convert.longTobytes(reportInfo.getLatitude(), 4));
        // electricity; // 太阳能电源电量0-100 1个字节
        ArraysUtils.arrayappend(requestContent, 93+16, Convert.intTobytes(reportInfo.getElectricity(),1));
        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length-2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("requestContent:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[31];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(3, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(1, 1));
        try {
            if (reportInfo.getDeviceCode().getBytes("ASCII").length == 17){
                ArraysUtils.arrayappend(responseContent, 4, reportInfo.getDeviceCode().getBytes("ASCII"));
            }else{
                System.out.println("设备code码错误,deviceCode:"+reportInfo.getDeviceCode());
                return ;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(6, 2));
        ArraysUtils.arrayappend(responseContent, 24, collectTime);
        crc = Convert.checkPackage(responseContent, 2, responseContent.length-2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("responseContent:" + Convert.bytesToHexString(responseContent));
    }
}
