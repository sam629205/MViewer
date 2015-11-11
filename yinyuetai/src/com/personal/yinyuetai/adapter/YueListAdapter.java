package com.personal.yinyuetai.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.personal.yinyuetai.R;
import com.personal.yinyuetai.bean.ArtistInfo;
import com.personal.yinyuetai.bean.YueListInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class YueListAdapter extends BaseAdapter{
	private int type;
	private List<YueListInfo> infoList;
	private Context context;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;
	
	public YueListAdapter(List<YueListInfo> infoList,int type,Context context){
		this.infoList = infoList;
		this.type = type;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
		.displayer(new RoundedBitmapDisplayer(R.color.all_track_color,10))
		.cacheInMemory()
		.cacheOnDisc()
		.build();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return infoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.yuelist_item, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.iv);
			holder.tvTtitle = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage(infoList.get(position).getImgUrl(), holder.iv);
		holder.tvTtitle.setText(infoList.get(position).getTitle());
		return convertView;
	}
	public void addData(List<YueListInfo> data){
		for(YueListInfo info:data){
			infoList.add(info);
		}
		notifyDataSetChanged();
	}
	class ViewHolder {
		ImageView iv;
		TextView tvTtitle;
	}
}
