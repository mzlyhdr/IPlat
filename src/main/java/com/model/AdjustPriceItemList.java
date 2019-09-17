package com.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;

import com.dingtalk.api.request.OapiProcessinstanceCreateRequest.FormComponentValueVo;
import org.springframework.util.CollectionUtils;

/**
 * @author mzdr
 * @date 2019-04-22
 * 调价申请型号清单模板
 */
public class AdjustPriceItemList {
    /**
     * onItemTap
     */
    private String onItemTap;
    /**
     * 申请调价的型号
     */
    private String header;
    /**
     * #
     */
    private String number;

    /**
     * 销售型号
     */
    private String title;

    /**
     * MOQ
     */
    private Long minqty;
    /**
     * 申请价
     */
    private String content;
    /**
     * 标准价
     */
    private String extra;
    
    public String getonItemTap() {
		return onItemTap;
	}
    
	public void setonItemTap(String onItemTap) {
		this.onItemTap = onItemTap;
	}
	
    public String getheader() {
        return header;
    }

    public void setheader(String header) {
        this.header = header;
    }

    public String getnumber() {
        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public Long getminqty() {
        return minqty;
    }

    public void setminqty(Long minqty) {
        this.minqty = minqty;
    }

    public String getcontent() {
        return content;
    }

    public void setcontent(String content) {
        this.content = content;
    }

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

    /**
     * 单行输入框表单数据
     */
    private List<TextForm> data;

    public static class TextForm {
    	 private String number;

    	    /**
    	     * 销售型号
    	     */
    	    private String title;

    	    /**
    	     * MOQ
    	     */
    	    private Long minqty;
    	    /**
    	     * 申请价
    	     */
    	    private String content;
    	    /**
    	     * 标准价
    	     */
    	    private String extra;

	        public String getnumber() {
	            return number;
	        }
	
	        public void setnumber(String number) {
	            this.number = number;
	        }
	
	        public String gettitle() {
	            return title;
	        }
	
	        public void settitle(String title) {
	            this.title = title;
	        }
	
	        public Long getminqty() {
	            return minqty;
	        }
	
	        public void setminqty(Long minqty) {
	            this.minqty = minqty;
	        }
	
	        public String getcontent() {
	            return content;
	        }
	
	        public void setcontent(String content) {
	            this.content = content;
	        }
	
	    	public String getExtra() {
	    		return extra;
	    	}
	
	    	public void setExtra(String extra) {
	    		this.extra = extra;
	    	}
    }
	
    public List<TextForm> getdata() {
        return data;
    }

    public void setdata(List<TextForm> data) {
        this.data = data;
    }
	
	
}
