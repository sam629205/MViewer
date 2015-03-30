package com.personal.yinyuetai.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.personal.yinyuetai.R;
import com.personal.yinyuetai.activity.VideoPlayActivity;
import com.personal.yinyuetai.bean.ArtistInfo;


import com.personal.yinyuetai.view.AlwaysMarqueeTextView;
import com.personal.yinyuetai.view.LancherLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class QueryAdapter extends BaseAdapter {

	private List<ArtistInfo> infoList;
	private Context mContext;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	
	class viewHolder{
		ImageView iv1,iv2,iv3;
		AlwaysMarqueeTextView tv1,tv2,tv3;
	}
	public QueryAdapter(Context context, List<ArtistInfo> infoList) {
		this.infoList = infoList;
		this.mContext = context;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
		.displayer(new RoundedBitmapDisplayer(R.color.all_track_color,10))
		.cacheInMemory()
		.cacheOnDisc()
		.build();
	}

	@Override
	public int getCount() {
		int count = 0;
		if (infoList.size()%3==0) {
			count=infoList.size()/3;
		}else {
			count=infoList.size()/3+1;
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		viewHolder holder;
//		if (convertView==null) {
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater(); 
			holder = new viewHolder();
			convertView = new LancherLayout(mContext,position,infoList);
			convertView.setTag(holder);
//		}else {
//			holder = (viewHolder) convertView.getTag();
//		}
		
//		final ArtistInfo info = (ArtistInfo)getItem(position*3);
//		final ArtistInfo info1 = (ArtistInfo)getItem(position*3+1);
//		final ArtistInfo info2 = (ArtistInfo)getItem(position*3+2);
//		imageLoader.displayImage(info.getImg(), holder.iv1,options);
//		imageLoader.displayImage(info1.getImg(), holder.iv2,options);
//		imageLoader.displayImage(info2.getImg(), holder.iv3,options);
		return convertView;		
	}
	public void addData(List<ArtistInfo> data){
		for(ArtistInfo info:data){
			infoList.add(info);
		}
		notifyDataSetChanged();
	}
}
