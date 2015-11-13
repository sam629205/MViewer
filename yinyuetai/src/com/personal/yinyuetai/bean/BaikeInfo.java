package com.personal.yinyuetai.bean;

import java.io.Serializable;
import java.util.List;

public class BaikeInfo implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private List<String> urlList;

public List<String> getUrlList() {
	return urlList;
}

public void setUrlList(List<String> urlList) {
	this.urlList = urlList;
}

}
