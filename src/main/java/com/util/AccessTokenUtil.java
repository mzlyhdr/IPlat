package com.util;

import com.config.Constant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.config.URLConstant.URL_GET_TOKKEN;

import java.util.Date;

/**
 * 获取access_token工具类
 */
public class AccessTokenUtil {
    private static final Logger bizLogger = LoggerFactory.getLogger(AccessTokenUtil.class);

    public static String getToken() throws RuntimeException {    	
    	
        try {
        	if (Constant.access_token =="" || (Constant.access_tokengettime.getTime()+7200*1000)>new Date().getTime()) {
	            DefaultDingTalkClient client = new DefaultDingTalkClient(URL_GET_TOKKEN);
	            OapiGettokenRequest request = new OapiGettokenRequest();
	
	            request.setAppkey(Constant.APPKEY);
	            request.setAppsecret(Constant.APPSECRET);
	            request.setHttpMethod("GET");
	            OapiGettokenResponse response = client.execute(request);
	            String accessToken = response.getAccessToken();
	            return accessToken;
        	}else {return Constant.access_token;}
        } catch (ApiException e) {
            bizLogger.error("getAccessToken failed", e);
            throw new RuntimeException();
        }
    	
    }

    
}
