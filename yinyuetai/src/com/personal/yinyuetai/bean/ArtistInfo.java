package com.personal.yinyuetai.bean;

import java.io.Serializable;

public class ArtistInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5654316244153604046L;
private String link;
private String title;
private String img;
public String getLink() {
	return link;
}
public void setLink(String link) {
	this.link = link;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getImg() {
	return img;
}
public void setImg(String img) {
	this.img = img;
}

}
