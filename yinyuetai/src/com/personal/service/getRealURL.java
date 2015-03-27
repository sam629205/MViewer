package com.personal.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.personal.yinyuetai.bean.ArtistInfo;
import com.personal.yinyuetai.bean.YueListInfo;

public class getRealURL {
public List<ArtistInfo> parseWeb(String URL,int status) {
	Document doc=null;
	List<ArtistInfo> resultList = new ArrayList<ArtistInfo>();
	try {
		final String DESKTOP_USERAGENT = "Mozilla/5.0 (Macintosh; " +  
		"U; Intel Mac OS X 10_6_3; en-us) AppleWebKit/533.16 (KHTML, " +  
		"like Gecko) Version/5.0 Safari/533.16";  
		doc = Jsoup.connect(URL).timeout(20000).userAgent(DESKTOP_USERAGENT).get();
		String[] videoList = new String[20];
		String[] titleList = new String[20];
		String[] titleList2 = new String[20];
		String[] titleList3 = new String[20];
		String[] imgList = new String[20];
		String[] banner = new String[2];
		int i=0,j=0,k=0,l=0;
		switch (status) {
		//MV解析
		case 0:
			Element root = doc.getElementById("mvlist");
			if (root==null) {
				break;
			}
			Elements mvItem = root.getElementsByTag("a");
			for (Element ele:mvItem) {
				String img = ele.select("img").attr("src");
				String link = ele.attr("href");
				String title= ele.select("img").attr("title");
				if (ele.attr("class").equals("c3 name")) {
					titleList2[l]=ele.attr("title");
					l++;
				}
				if (!title.equals("")) {
					titleList[i]=title;
					i++;
				}
				if (!img.equals("")) {
					imgList[j]=img;
					j++;
				}
				if (ele.attr("class").equals("special")&&!ele.attr("title").equals("")) {
					videoList[k]=link;
					k++;
					}
				
			}
			break;
			//����MV����
		case 1:
			Elements elements3 = doc.getElementsByClass("fan_banner");
			Elements elements2 = doc.getElementsByClass("thumb");
			Elements elements4 = doc.getElementsByClass("title");
			for (Element ele:elements2) {
				if (!ele.select("a").attr("title").equals("")) {
				String img = ele.select("img").attr("src");
				String title = ele.select("a").attr("title");
				String link  = ele.select("a").attr("href");
				titleList[i]=title;
				videoList[i]="http://www.yinyuetai.com"+link;
				imgList[i]=img;
				i++;
				}
			}
			for (Element ele:elements4) {
				titleList2[l]=ele.select("a").attr("title");
			}
			for(Element ele:elements3){
				banner[0] = ele.select("img").attr("src");
			}
			break;
			//MV搜索解析
		case 2:
			Element root2 = doc.getElementById("search_result_list");
			Elements elements5 = root2.getElementsByTag("a");
			for (Element ele:elements5) {
				if (ele.select("a").attr("class").equals("img")) {
					String img = ele.select("img").attr("src");
					imgList[j]=img;
					String link = ele.select("a").attr("href");
					String title = ele.select("img").attr("alt");
					titleList[i]=title;
					i++;
					videoList[k]=link;
					k++;
					j++;
				}
			}
//			Elements elements6 = root2.getElementsByClass("title mv_title");
//			for (Element ele:elements6) {
//				String link = ele.select("a").attr("href");
//				String title = ele.select("a").attr("title");
//				titleList[i]=title;
//				i++;
//				videoList[k]=link;
//				k++;
//			}
			Elements elements7 = root2.getElementsByClass("artist");
			for (Element ele:elements7) {
				Elements mElements = ele.getElementsByTag("a");
				String title = "";
				if (mElements.size()>1) {
					for (Element subEle:mElements) {
						title=title+subEle.select("a").attr("title")+"&";
					}
				}else {
					title=ele.select("a").attr("title");
				}
				titleList2[l]=title;
				l++;
			}
			break;
		case 3://悦单视频列表
			Elements elements8 = doc.getElementsByClass("mv_pic");
			for (Element ele : elements8) {
				titleList[i] = ele.select("a").attr("title");
				imgList[i] = ele.select("img").attr("src");
				String[] temp = imgList[i].split("/");
				String link = "http://v.yinyuetai.com/video/"+temp[6];
				videoList[i] = link;
				i++;
			}
			break;
		default:
			break;
		}
		for (int m = 0; m < titleList.length; m++) {
			titleList3[m]=titleList[m]+"-"+titleList2[m];
		}
		for (int i2 = 0; i2 < videoList.length; i2++) {
			ArtistInfo mInfo = new ArtistInfo();
			mInfo.setImg(imgList[i2]);
			mInfo.setLink(videoList[i2]);
			mInfo.setTitle(titleList3[i2]);
			resultList.add(mInfo);
		}
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}  
	//Document doc = Jsoup.connect(URL).get();
	return resultList;

}
public List<YueListInfo> parseYueList(String URL,int status){
	Document doc=null;
	List<YueListInfo> resultList = new ArrayList<YueListInfo>();
	try {
		final String DESKTOP_USERAGENT = "Mozilla/5.0 (Macintosh; " +  
		"U; Intel Mac OS X 10_6_3; en-us) AppleWebKit/533.16 (KHTML, " +  
		"like Gecko) Version/5.0 Safari/533.16";  
		doc = Jsoup.connect(URL).timeout(20000).userAgent(DESKTOP_USERAGENT).get();
		String[] videoList = new String[20];
		String[] titleList = new String[20];
		String[] titleList2 = new String[20];
		String[] titleList3 = new String[20];
		String[] imgList = new String[20];
		String[] banner = new String[2];
		int i=0,j=0,k=0,l=0;
		switch (status) {
		case 0://悦单列表
			Elements elements = doc.getElementsByClass("thumb_box");
			for (Element ele : elements) {
				Elements mElements = ele.getElementsByTag("a");
				String title = mElements.get(0).attr("title");
				String link = mElements.get(0).attr("href");
				String imgUrl = mElements.get(0).select("img").attr("src");
				YueListInfo info = new YueListInfo();
				info.setTitle(title);
				info.setImgUrl(imgUrl);
				info.setLink(link);
				resultList.add(info);
			}
			break;
		default:
			break;
		}
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}  
	//Document doc = Jsoup.connect(URL).get();
	return resultList;
}
public String parseLink(String URL) {
	Document doc=null;
	String reaLink = null;
	
	try {
		doc = Jsoup.connect(URL).timeout(20000).get();
		Elements root = doc.getElementsByTag("a");
		reaLink = root.get(5).attr("href");
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
	//Document doc = Jsoup.connect(URL).get();
	return reaLink;

}
//��ȡ�����б�
public static List<ArtistInfo> getArtist(String area,String sort,int page){
	Document doc=null;
	List<ArtistInfo> resultList = new ArrayList<ArtistInfo>();
	try {
		String URL = "http://www.yinyuetai.com/fanAll?area="+area+"&property="+sort+"&page="+page;
		doc = Jsoup.connect(URL).timeout(20000).get();
		Elements root = doc.getElementsByClass("title");
		for (Element ele:root) {
			String link = ele.select("a").attr("href");
			String name = ele.select("a").attr("title");
			ArtistInfo info = new ArtistInfo();
			info.setLink(link.substring(link.lastIndexOf("/")));
//			info.setName(name);
			resultList.add(info);
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	return resultList;
}
}
