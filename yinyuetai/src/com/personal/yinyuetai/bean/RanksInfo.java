package com.personal.yinyuetai.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RanksInfo implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private List<RanksInfo1> urlList;
private List<String> titleList;
private List<String> nameList;
private List<String> artistList;

public List<String> getNameList() {
	return nameList;
}
public void setNameList(List<String> nameList) {
	this.nameList = nameList;
}
public List<String> getArtistList() {
	return artistList;
}
public void setArtistList(List<String> artistList) {
	this.artistList = artistList;
}
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
