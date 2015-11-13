package com.personal.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.personal.yinyuetai.bean.ArtistInfo;
import com.personal.yinyuetai.bean.BaikeInfo;
import com.personal.yinyuetai.bean.RanksInfo;
import com.personal.yinyuetai.bean.RanksInfo1;
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
			titleList = new String[24];
			titleList2 = new String[24];
			titleList3 = new String[24];
			imgList = new String[24];
			videoList = new String[24];
			Elements elements5 = doc.getElementsByClass("mv");
			for (Element ele:elements5) {
					String img = ele.getElementsByTag("img").get(0).attr("src");
					imgList[j]=img;
					String link = ele.getElementsByClass("name").get(0).select("a").attr("href");
					String title = ele.getElementsByClass("name").text();
					String title2 = ele.getElementsByClass("artist").text();
					titleList[i]=title;
					titleList2[l]=title2;
					l++;
					i++;
					videoList[k]=link;
					k++;
					j++;
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
//			Elements elements7 = root2.getElementsByClass("artist");
//			for (Element ele:elements7) {
//				Elements mElements = ele.getElementsByTag("a");
//				String title = "";
//				if (mElements.size()>1) {
//					for (Element subEle:mElements) {
//						title=title+subEle.select("a").attr("title")+"&";
//					}
//				}else {
//					title=ele.select("a").attr("title");
//				}
//				titleList2[l]=title;
//				l++;
//			}
			break;
		case 3://悦单视频列表
			Element content = doc.getElementById("mv_picker");
			Elements elements8 = content.getElementsByClass("mv2_1x2");
			titleList = new String[elements8.size()];
			titleList2 = new String[elements8.size()];
			titleList3 = new String[elements8.size()];
			imgList = new String[elements8.size()];
			videoList = new String[elements8.size()];
			for (Element ele : elements8) {
				titleList[i] = ele.select("a").attr("title");
				Elements actorEles = ele.getElementsByClass("c_cf9");
				titleList2[i] = actorEles.get(0).text();
				imgList[i] = ele.select("img").attr("src");
				Elements linkEles = ele.getElementsByTag("div");
				String link = null;
				if (linkEles.get(0).attr("data-id").equals("")) {
					String[] tempArray = imgList[i].split("/");
					link = "http://v.yinyuetai.com/video/"+tempArray[5];
				}else {
					link = "http://v.yinyuetai.com/video/"+linkEles.get(0).attr("data-id");
				}
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
			if (titleList2[i2]!=null) {
				String[] mArtists = titleList2[i2].split("&");
				mInfo.setArtist(mArtists);
			}
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
public BaikeInfo parseBaike(String url,int status){
	BaikeInfo result = new BaikeInfo();
	Document doc=null;
	List<String> urlList = new ArrayList<>();
	final String DESKTOP_USERAGENT = "Mozilla/5.0 (Macintosh; " +  
			"U; Intel Mac OS X 10_6_3; en-us) AppleWebKit/533.16 (KHTML, " +  
			"like Gecko) Version/5.0 Safari/533.16";  
	try {
		doc = Jsoup.connect(url).timeout(20000).userAgent(DESKTOP_USERAGENT).get();
		switch (status) {
		case 0:
			Elements elements = doc.getElementsByClass("result-title");
			for (Element ele:elements) {
				urlList.add(ele.attr("href"));
			}
			break;
		case 1:
			
			break;
		default:
			break;
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	result.setUrlList(urlList);
	return result;
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
public RanksInfo getRanks(String url){
	RanksInfo info = new RanksInfo();
	Document doc=null;
	List<String> titleList = new ArrayList<String>();
	List<String> artistList = new ArrayList<String>();
	List<String> nameList = new ArrayList<String>();
	List<RanksInfo1> infoList = new ArrayList<RanksInfo1>(); 
	final String DESKTOP_USERAGENT = "Mozilla/5.0 (Macintosh; " +  
			"U; Intel Mac OS X 10_6_3; en-us) AppleWebKit/533.16 (KHTML, " +  
			"like Gecko) Version/5.0 Safari/533.16";  
	try {
		doc = Jsoup.connect(url).timeout(20000).userAgent(DESKTOP_USERAGENT).get();
		Element ele = doc.getElementById("ul");
		Elements eles = ele.getElementsByTag("li");
		for (Element one:eles) {
			titleList.add(one.attr("title"));
			String mTitle = one.attr("title");
			String mName = mTitle.split("-")[1].trim();
			if (mName.indexOf("(")!=-1) {
				mName = mName.substring(0,mName.indexOf("("));
			}
			nameList.add(mName);
			artistList.add(mTitle.split("-")[0].trim());
		}
		Elements eles1 = doc.getElementsByClass("list");
		for (Element one:eles1) {
			Elements eles2 = one.getElementsByTag("a");
			List<String> urlList = new ArrayList<String>();
			for (int i = eles2.size(); i > 0; i--) {
				urlList.add(eles2.get(i-1).attr("href"));
			}
			RanksInfo1 subInfo = new RanksInfo1();
			subInfo.setUrlList(urlList);
			infoList.add(subInfo);
		}
		info.setNameList(nameList);
		info.setArtistList(artistList);
		info.setTitleList(titleList);
		info.setUrlList(infoList);
	} catch (IOException e) {
		e.printStackTrace();
	}
	return info;
}
public String parseLink(String URL) {
	Document doc=null;
	String reaLink = null;
	
	try {
		Log.e("URL", URL);
		doc = Jsoup.connect(URL).header("User-Agent", "Mozilla/5.0 (Macintosh; U; "
				+ "Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/"
				+ "3.6.2").timeout(20000).get();
		Elements root = doc.getElementsByTag("a");
		reaLink = root.get(8).attr("href");
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
