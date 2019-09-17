package com.controller;

import com.config.URLConstant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.request.OapiUserListbypageRequest;
import com.dingtalk.api.request.OapiUserSimplelistRequest;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.dingtalk.api.response.OapiUserListbypageResponse;
import com.dingtalk.api.response.OapiUserSimplelistResponse;
import com.model.AdjustPriceItemList;
import com.taobao.api.ApiException;
import com.util.AccessTokenUtil;
import com.util.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import com.controller.HttpClient;

/**
 * 企业 E应用解决方案示例代码
 * 实现了最简单的免密登录（免登）功能
 */
@RestController
public class IndexController {
	private static final Logger bizLogger = LoggerFactory.getLogger(IndexController.class);

	/**
	 *0、欢迎页面,通过url访问，判断后端服务是否启动
	 */
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String welcome() {
		return "welcome";
	}

	/**
	 *1、钉钉用户登录，显示当前登录用户的userId和名称
	 *
	 * @param requestAuthCode 免登临时code
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ServiceResult login(@RequestParam(value = "authCode") String requestAuthCode) {
		//获取accessToken,注意正是代码要有异常流处理
		String accessToken = AccessTokenUtil.getToken();

		//获取用户信息
		DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_GET_USER_INFO);
		OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
		request.setCode(requestAuthCode);
		request.setHttpMethod("GET");

		OapiUserGetuserinfoResponse response;
		try {
			response = client.execute(request, accessToken);
		} catch (ApiException e) {
			e.printStackTrace();
			return null;
		}
		//3.查询得到当前用户的userId
		// 获得到userId之后应用应该处理应用自身的登录会话管理（session）,避免后续的业务交互（前端到应用服务端）每次都要重新获取用户身份，提升用户体验
		String userId = response.getUserid();

		OapiUserGetResponse userProfile = getUserProfile(accessToken, userId);
		String userName = userProfile.getName();
		Long deptId = userProfile.getDepartment().get(0);
		// 审批里的部门id，1和-1要互相转换一下
		if (deptId.longValue() == 1L) {
			deptId = -1L;
		}
		//返回结果
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("userId", userId);
		resultMap.put("userName", userName);
		resultMap.put("deptId", deptId);
		ServiceResult serviceResult = ServiceResult.success(resultMap);
		return serviceResult;
	}

	/**
	 * M.1、获取用户详情
	 *
	 * @param accessToken
	 * @param userId
	 * @return
	 */
	private OapiUserGetResponse getUserProfile(String accessToken, String userId) {
		try {
			DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_USER_GET);
			OapiUserGetRequest request = new OapiUserGetRequest();
			request.setUserid(userId);
			request.setHttpMethod("GET");
			OapiUserGetResponse response = client.execute(request, accessToken);

			return response;
		} catch (ApiException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *2、 根据部门和姓名来获取钉钉部门ID及用户ID：deptID/deptName 
	 */
	@RequestMapping(value = "/getUserIDs", method = RequestMethod.POST)
	@ResponseBody
	public ServiceResult getUserIDs(@RequestParam(value = "deptName") String requestdeptName
								   ,@RequestParam(value = "userName") String requestuserName) {
		
		ServiceResult serviceResult ;
		Map<String, Object> resultMap = new HashMap<>();
		
		//a.获取accessToken,注意正是代码要有异常流处理
		String accessToken = AccessTokenUtil.getToken();
		//b.获取部门列表		
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
		OapiDepartmentListRequest request = new OapiDepartmentListRequest();
		request.setId("1");
		request.setHttpMethod("GET");
		OapiDepartmentListResponse deptList ;		
		try {
			deptList = client.execute(request, accessToken);
		} catch (ApiException e) {
//			e.printStackTrace();			
			serviceResult = ServiceResult.failure(e.getErrCode(), e.getMessage());
			return serviceResult;
		}
		//c.根据得到所有部门信息查到部门ID
		Long deptId = 0L;
		for(int i=0;i<deptList.getDepartment().size();i++) {
			String deptname =deptList.getDepartment().get(i).getName();
			if(deptname.equals(requestdeptName)) {
				deptId = deptList.getDepartment().get(i).getId();
				break;
			}
		}
		if(deptId <= 0L) 
		{
			serviceResult = ServiceResult.failure("部门ID错误", "此部门不存在："+requestdeptName);
			return serviceResult;
		}
		DingTalkClient userclient = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/simplelist");
		OapiUserSimplelistRequest userrequest = new OapiUserSimplelistRequest();
		userrequest.setDepartmentId(deptId);
		userrequest.setOffset(0L);
		userrequest.setSize(10L);
		userrequest.setHttpMethod("GET");
		OapiUserSimplelistResponse userList;
		try {
			userList = userclient.execute(userrequest, accessToken);
		}catch(ApiException e) {
			serviceResult = ServiceResult.failure(e.getErrCode(), e.getMessage());
			return serviceResult;
		}
		String userId ="";
		for(int i=0;i<userList.getUserlist().size();i++) {
			String userName = userList.getUserlist().get(i).getName();
			if(userName.equals(requestuserName)) {
				userId=userList.getUserlist().get(i).getUserid();
				break;
			}
		}
		if(userId.isEmpty()) {
			serviceResult = ServiceResult.failure("用户ID错误", "此用户不存在："+requestuserName);
			return serviceResult;
		}
		resultMap.put("deptId", deptId.toString());
		resultMap.put("userId", userId);
//				resultMap.put("department", deptId);
//				resultMap.put("user", userId);
		serviceResult = ServiceResult.success(resultMap);
		return serviceResult;
	
	}
	/**
	 *3、 根据姓名来获取钉钉用户ID及部门ID：deptID/deptName 
	 */
	@RequestMapping(value = "/getUserId", method = RequestMethod.POST)
	@ResponseBody
	public ServiceResult getUserID(@RequestParam(value = "userName") String requestuserName) {
		
		ServiceResult serviceResult ;
		Map<String, Object> resultMap = new HashMap<>();
		
		//a.获取accessToken,注意正是代码要有异常流处理
		String accessToken = AccessTokenUtil.getToken();
		//b.获取公司用户详细信息列表		
		
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/listbypage");
		OapiUserListbypageRequest request = new OapiUserListbypageRequest();
		request.setDepartmentId(1L);
		request.setOffset(0L);
		request.setSize(10L);
		request.setOrder("entry_desc");
		request.setHttpMethod("GET");
		OapiUserListbypageResponse userList ;
		try {
			userList = client.execute(request,accessToken);
		}catch(ApiException e) {
			serviceResult = ServiceResult.failure(e.getErrCode(), e.getMessage());
			return serviceResult;
		}
		String userId ="";
		String deptId="";
		for(int i=0;i<userList.getUserlist().size();i++) {
			String userName = userList.getUserlist().get(i).getName();
			if(userName.equals(requestuserName)) {
				userId=userList.getUserlist().get(i).getUserid();
				deptId=userList.getUserlist().get(i).getDepartment();
				break;
			}
		}
		if(userId.isEmpty()) {
			serviceResult = ServiceResult.failure("用户ID错误", "此用户不存在："+requestuserName);
			return serviceResult;
		}
		resultMap.put("deptId", deptId.toString());
		resultMap.put("userId", userId);
		serviceResult = ServiceResult.success(resultMap);
		return serviceResult;
	
	}

	/**
	 * 5、从WCF服务器上获取数据
	 *
	 * @param methodname 服务提供的方法及参数
	 */
	@RequestMapping(value = "/getListdata", method = RequestMethod.POST)
	@ResponseBody
	public ServiceResult getListdata(@RequestParam(value = "listName") String requestlistName) {
		
       String httpurl =URLConstant.URL_WCF+"/"+requestlistName;
       AdjustPriceItemList listdata = new AdjustPriceItemList();
       listdata.setonItemTap("");
       try {
    	   //返回结果
    	   	String mthResult = HttpClient.doGet(httpurl);    	 
   			Map<String, Object> resultMap = new HashMap<>();
   			resultMap.put("mthResult", mthResult);
   			ServiceResult serviceResult = ServiceResult.success(resultMap);
   			return serviceResult;
       }catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 9、从WCF服务器上获取数据
	 *
	 * @param methodname 服务提供的方法及参数
	 */
	@RequestMapping(value = "/wcfClient", method = RequestMethod.POST)
	@ResponseBody
	public ServiceResult loginOn(@RequestParam(value = "methodName") String requestmethodName) {
		
       String httpurl =URLConstant.URL_WCF+"/"+requestmethodName;
       try {
    	   //返回结果
    	   	String mthResult = HttpClient.doGet(httpurl);    	 
   			Map<String, Object> resultMap = new HashMap<>();
   			resultMap.put("mthResult", mthResult);
   			ServiceResult serviceResult = ServiceResult.success(resultMap);
   			return serviceResult;
       }catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}


