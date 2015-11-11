package com.personal.yinyuetai.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RanksInfo implements Serializable{
private List<RanksInfo1> urlList;
private List<String> titleList;

public List<RanksInfo1> getUrlList() {
	return urlList;
}
public void setUrlList(List<RanksInfo1> urlList) {
	this.urlList = urlList;
}
public List<String> getTitleList() {
	return titleList;
}
public void setTitleList(List<String> titleList) {
	this.titleList = titleList;
}

}
