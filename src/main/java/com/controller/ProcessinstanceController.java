package com.controller;

import com.alibaba.fastjson.JSON;

import com.config.Constant;
import com.config.URLConstant;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiProcessinstanceCreateRequest;
import com.dingtalk.api.request.OapiProcessinstanceGetRequest;
import com.dingtalk.api.request.OapiProcessinstanceListidsRequest;
import com.dingtalk.api.request.OapiProcessinstanceCreateRequest.FormComponentValueVo;
import com.dingtalk.api.response.OapiProcessinstanceCreateResponse;
import com.dingtalk.api.response.OapiProcessinstanceGetResponse;
import com.dingtalk.api.response.OapiProcessinstanceListidsResponse;
import com.model.ProcessInstanceInputVO;
import com.model.ProcessInstanceInputTL;
import com.util.AccessTokenUtil;
import com.util.LogFormatter;
import com.util.LogFormatter.LogEvent;
import com.util.ServiceResult;
import com.util.ServiceResultCode;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 企业 E应用审批解决方案示例代码
 * 实现了审批的基础功能
 */
@RestController
public class ProcessinstanceController {
	private static final Logger bizLogger = LoggerFactory.getLogger(ProcessinstanceController.class);

	/**
	 * 欢迎页面
	 */
	@RequestMapping(value = "/Processinstance/welcome", method = RequestMethod.GET)
	public String welcome() {
		return "welcome";
	}


	/**
	 * 发起审批
	 */
	@RequestMapping(value = "/processinstance/start", method = RequestMethod.POST)
	@ResponseBody
	public ServiceResult<String> startProcessInstance(@RequestBody ProcessInstanceInputTL processInstance) {
		try {
			DefaultDingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_PROCESSINSTANCE_START);
			OapiProcessinstanceCreateRequest request = new OapiProcessinstanceCreateRequest();
			request.setProcessCode(processInstance.getProcessCode());
			List<FormComponentValueVo> generateForms =processInstance.generateForms();
			request.setFormComponentValues(generateForms);

			/**
			 * 如果想复用审批固定流程，使用或签会签的话，可以不传审批人，具体请参考文档： https://open-doc.dingtalk.com/microapp/serverapi2/ebkwx8
			 * 本次quickstart，演示不传审批人的场景
			 */
//			request.setApprovers(processInstance.getOriginatorUserId());
			request.setOriginatorUserId(processInstance.getOriginatorUserId());
			request.setDeptId(processInstance.getDeptId());
			request.setCcList(processInstance.getOriginatorUserId());
			request.setCcPosition("FINISH");

			OapiProcessinstanceCreateResponse response = client.execute(request, AccessTokenUtil.getToken());

			if (response.getErrcode().longValue() != 0) {
				return ServiceResult.failure(String.valueOf(response.getErrorCode()), response.getErrmsg());
			}
			return ServiceResult.success(response.getProcessInstanceId());

		} catch (Exception e) {
			String errLog = LogFormatter.getKVLogData(LogEvent.END,
				LogFormatter.KeyValue.getNew("processInstance", JSON.toJSONString(processInstance)));
			bizLogger.info(errLog,e);
			return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
		}
	}
	@RequestMapping(value = "/processinstance/startdemo", method = RequestMethod.POST)
	@ResponseBody
	public ServiceResult<String> startdemoProcessInstance(@RequestBody ProcessInstanceInputVO processInstance) {
		try {
			DefaultDingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_PROCESSINSTANCE_START);
			OapiProcessinstanceCreateRequest request = new OapiProcessinstanceCreateRequest();
			request.setProcessCode(processInstance.getProcessCode());

			request.setFormComponentValues(processInstance.generateForms());

			/**
			 * 如果想复用审批固定流程，使用或签会签的话，可以不传审批人，具体请参考文档： https://open-doc.dingtalk.com/microapp/serverapi2/ebkwx8
			 * 本次quickstart，演示不传审批人的场景
			 */
//			request.setApprovers(processInstance.getOriginatorUserId());
			request.setOriginatorUserId(processInstance.getOriginatorUserId());
			request.setDeptId(processInstance.getDeptId());
			request.setCcList(processInstance.getOriginatorUserId());
			request.setCcPosition("FINISH");

			OapiProcessinstanceCreateResponse response = client.execute(request, AccessTokenUtil.getToken());

			if (response.getErrcode().longValue() != 0) {
				return ServiceResult.failure(String.valueOf(response.getErrorCode()), response.getErrmsg());
			}
			return ServiceResult.success(response.getProcessInstanceId());

		} catch (Exception e) {
			String errLog = LogFormatter.getKVLogData(LogEvent.END,
				LogFormatter.KeyValue.getNew("processInstance", JSON.toJSONString(processInstance)));
			bizLogger.info(errLog,e);
			return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
		}
	}
	/**
	 * 批量获取审批实例id
	 * @param processCode,startTime
	 * @return
	 */
	@RequestMapping(value = "/pricessinstance/getlistids", method = RequestMethod.POST)
	@ResponseBody
	public ServiceResult getProcessinstanceListIds(@RequestParam String processCode,Long startTime,Long endTime ,String useridList) {
		try {
			DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/listids");
			OapiProcessinstanceListidsRequest req = new OapiProcessinstanceListidsRequest();
			req.setProcessCode(processCode);
			req.setStartTime(1496678400000L);
			req.setEndTime(endTime);
			req.setSize(10L);
			req.setCursor(0L);
			req.setUseridList(useridList);
			OapiProcessinstanceListidsResponse response = client.execute(req, AccessTokenUtil.getToken());	
			if (response.getErrcode().longValue() != 0) {
				return ServiceResult.failure(String.valueOf(response.getErrorCode()), response.getErrmsg());
			}
			return ServiceResult.success(response.getResult().getList());
		} catch (Exception e) {
			String errLog = LogFormatter.getKVLogData(LogEvent.END,
				LogFormatter.KeyValue.getNew("processCode", processCode));
			bizLogger.info(errLog,e);
			return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
		}
	}
	/**
	 * 根据审批实例id获取审批详情
	 * @param instanceId
	 * @return
	 */
	@RequestMapping(value = "/pricessinstance/get", method = RequestMethod.POST)
	@ResponseBody
	public ServiceResult getProcessinstanceById(@RequestParam String instanceId) {
		try {
			DingTalkClient client = new DefaultDingTalkClient(URLConstant.URL_PROCESSINSTANCE_GET);
			OapiProcessinstanceGetRequest request = new OapiProcessinstanceGetRequest();
			request.setProcessInstanceId(instanceId);
			OapiProcessinstanceGetResponse response = client.execute(request, AccessTokenUtil.getToken());
			if (response.getErrcode().longValue() != 0) {
				return ServiceResult.failure(String.valueOf(response.getErrorCode()), response.getErrmsg());
			}
			return ServiceResult.success(response.getProcessInstance());
		} catch (Exception e) {
			String errLog = LogFormatter.getKVLogData(LogEvent.END,
				LogFormatter.KeyValue.getNew("instanceId", instanceId));
			bizLogger.info(errLog,e);
			return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
		}
	}
	
}


