package com.config;

import java.util.Date;

/**
 * 项目中的常量定义类
 */
public class Constant {
    /**
     * 企业corpid, 需要修改成开发者所在企业
     */
	public static final String CORP_ID = "ding26e5e9d58556886c";
    /**
     * 应用的AppKey，登录开发者后台，点击应用管理，进入应用详情可见
     */
    public static final String APPKEY = "ding4c4wy0dxotpij7ru";
    /**
     * 应用的AppSecret，登录开发者后台，点击应用管理，进入应用详情可见
     */
    public static final String APPSECRET = "OzPzFFKYLhOjBXIxRdxF6P4lGgl_t2CpP6ADrQRo-cG1HnShY8uVs8b4KtRmqjmn";

    /**
     * 数据加密密钥。用于回调数据的加密，长度固定为43个字符，从a-z, A-Z, 0-9共62个字符中选取,您可以随机生成
     */
    public static final String ENCODING_AES_KEY = "qwertyuioplkjhgfdsa1234567890zxcvbnmasdfghj";

    /**
     * 加解密需要用到的token，企业可以随机填写。如 "12345"
     */
    public static final String TOKEN = "1340";

    /**
     * 应用的agentdId，登录开发者后台可查看
     */
    public static final Long AGENTID = 250738247L;

    /**
     * 审批模板唯一标识，可以在审批管理后台找到
     */
    public static final String PROCESS_CODE = "PROC-243E1547-9E8C-4CF2-958B-C1BABCCFFAB5";

    /**
     * 回调host
     */
    public static final String CALLBACK_URL_HOST = "***";
    /**
     * access_token 及获得 access_token 时间
     */
    public static final String access_token = "";
    public static final Date access_tokengettime = new Date();
}
