
package com.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiProcessinstanceCreateRequest.FormComponentValueVo;
import com.model.ProcessInstanceInputVO.PictureForm;
import com.taobao.api.internal.util.StringUtils;

import org.springframework.util.CollectionUtils;

	/**
 * @author mzdr
 * @date 2019/04/20
 * 审批实例
 */
public class ProcessInstanceInputTL {
    /**
     * 审批流程代码
     */
    private String processCode;
    /**
     * 审批人列表
     */
    private String approvers;
    /**
     * 抄送人列表
     */
    private String ccList;

    /**
     * 抄送时机
     */
    private String ccPosition;

    /**
     * 审批发起人部门id
     */
    private Long deptId;

    /**
     * 单行输入框、多行输入框的表单数据
     */
    private List<TextForm> textForms;

    /**
     * 图片表单数据
     */
    private List<PictureForm> pictureForms;

    /**
     * 明细表单数据
     */
    private List<DetailForm> detailForms;

    /**
     * 审批发起人
     */
    private String originatorUserId;

    public static class TextForm {
        /**
         * 表单控件名称
         */
        private String name;

        /**
         * 表单值
         */
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class PictureForm {
        /**
         * 表单控件名称
         */
        private String name;

        /**
         * 表单值，数组格式
         */
        private List<String> value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }

    public static class DetailForm {
        /**
         * 表单控件名称
         */
        private String name;

        /**
         * 明细里的文本控件列表
         */
        private List<TextForm> textForms;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<TextForm> getTextForms() {
            return textForms;
        }

        public void setTextForms(List<TextForm> textForms) {
            this.textForms = textForms;
        }

    }
    
    public String getProcessCode() {
		return processCode;
	}
    
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}
	
    public String getApprovers() {
        return approvers;
    }

    public void setApprovers(String approvers) {
        this.approvers = approvers;
    }

    public String getCcList() {
        return ccList;
    }

    public void setCcList(String ccList) {
        this.ccList = ccList;
    }

    public String getCcPosition() {
        return ccPosition;
    }

    public void setCcPosition(String ccPosition) {
        this.ccPosition = ccPosition;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public List<TextForm> getTextForms() {
        return textForms;
    }

    public void setTextForms(List<TextForm> textForms) {
        this.textForms = textForms;
    }

    public List<PictureForm> getPictureForms() {
        return pictureForms;
    }

    public void setPictureForms(List<PictureForm> pictureForms) {
        this.pictureForms = pictureForms;
    }

    public List<DetailForm> getDetailForms() {
        return detailForms;
    }

    public void setDetailForms(List<DetailForm> detailForms) {
        this.detailForms = detailForms;
    }

    public String getOriginatorUserId() {
        return originatorUserId;
    }

    public void setOriginatorUserId(String originatorUserId) {
        this.originatorUserId = originatorUserId;
    }

    /**
     * 生成FormComponentValueVo，用于调用发起审批实例的接口
     * @return
     */
    public List<FormComponentValueVo> generateForms() {
        List<FormComponentValueVo> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(textForms)) {
            for (TextForm textForm : textForms) {
                result.add(generateFormWithTextForm(textForm));
            }
        }

        if (!CollectionUtils.isEmpty(pictureForms)) {
            for (PictureForm pictureForm : pictureForms) {
                result.add(generateFormWithPictureForm(pictureForm));
            }
        }

        if (!CollectionUtils.isEmpty(detailForms)) {
            for (DetailForm detailForm : detailForms) {
                result.add(generateFormWithDetailForm(detailForm));
            }
        }
        return result;
    }

    private FormComponentValueVo generateFormWithTextForm(TextForm textForm) {
        FormComponentValueVo form = new FormComponentValueVo();
        form.setName(textForm.getName());
        form.setValue(textForm.getValue());
        return form;
    }

    private FormComponentValueVo generateFormWithPictureForm(PictureForm pictureForm) {
        FormComponentValueVo form = new FormComponentValueVo();
        form.setName(pictureForm.getName());
        form.setValue(JSON.toJSONString(pictureForm.getValue()));
        return form;
    }

    private FormComponentValueVo generateFormWithDetailForm(DetailForm detailForm) {
        FormComponentValueVo form = new FormComponentValueVo();
        form.setName(detailForm.getName());
        if (!CollectionUtils.isEmpty(detailForm.getTextForms())) {	
	        int size =detailForm.getTextForms().size();
	        String[] lines = new String[size];
	        for(int i=0;i<size;i++) {
	        	List<FormComponentValueVo> formline = new ArrayList<>();
		                	
	             	formline.add(generateFormWithTextForm(detailForm.getTextForms().get(i)));
		        
		        lines[i]=JSON.toJSONString(formline);
	        }
	        form.setValue(Arrays.toString(lines));
        }
        return form;
    }

	
}

