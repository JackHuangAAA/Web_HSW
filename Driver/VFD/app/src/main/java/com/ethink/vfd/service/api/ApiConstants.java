package com.ethink.vfd.service.api;
/**
 * 地址配置
 */
public class ApiConstants {

    public static final String MAINURL = "http://ads.ethinkbank.com:8080/";
    public static final String JQHEADIMAGE = "";
    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.ETHIKK_HOST:
                host = MAINURL;
                break;
            case HostType.TYPE_COUNT:
                host = JQHEADIMAGE;
                break;
            default:
                host = "";
                break;
        }
        return host;
    }
}
