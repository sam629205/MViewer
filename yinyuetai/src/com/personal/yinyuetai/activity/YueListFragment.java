package com.personal.yinyuetai.activity;

import java.util.List;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.personal.service.getRealURL;
import com.personal.yinyuetai.R;
import com.personal.yinyuetai.activity.MainFragment.GetDataTask;
import com.personal.yinyuetai.adapter.YueListAdapter;
import com.personal.yinyuetai.bean.YueListInfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class YueListFragment extends Fragment{
	private View view;
	private RadioGroup rg;
	private RadioButton rb1,rb2,rb3,rb4;
	private GridView gv;
	private ProgressDialog waitBar=null;
	private String urlStr;
	private int page=1;
	private YueListAdapter adapter;
	public YueListFragment(){
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_yuelist, container,false);
		createView();
		initView();
		return view;
	}
	private void createView() {
		rg = (RadioGroup) view.findViewById(R.id.rg);
		rb1 = (RadioButton) view.findViewById(R.id.rb1);
		rb2 = (RadioButton) view.findViewById(R.id.rb2);
		rb3 = (RadioButton) view.findViewById(R.id.rb3);
		rb4 = (RadioButton) view.findViewById(R.id.rb4);
		gv = (GridView) view.findViewById(R.id.gv);
	}
	private void initView() {
		
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.rb1:
					urlStr = "http://pl.yinyuetai.com/playlist_hotView/today?page=";
					break;
				case R.id.rb2:
					urlStr = "http://pl.yinyuetai.com/playlist_promo?page=";
					break;
				case R.id.rb3:
					urlStr = "http://pl.yinyuetai.com/playlist_hotView/all?page=";
					break;
				case R.id.rb4:
					urlStr = "http://pl.yinyuetai.com/playlist_all?page=";
					break;
				default:
					break;
				}
				page = 1;
				new LoadList().execute();
			}
		});
		rb1.setChecked(true);
	}
	public void loadMore(){
    	new LoadList().execute();
    }
	
	private class LoadList extends AsyncTask<Void, Void, List<YueListInfo>>{
		@Override
		protected void onPreExecute() {
			waitBar = ProgressDialog.show(getActivity(), "",
			"正在拼命解析中，请稍候");
			waitBar.setCancelable(true);
		};
		
		@Override
		protected List<YueListInfo> doInBackground(Void... arg0) {
			getRealURL get = new getRealURL();
			String url = urlStr+page;
			List<YueListInfo> result = get.parseYueList(url, 0);
			return result;
		}
		@Override
		protected void onPostExecute(final List<YueListInfo> result) {
			super.onPostExecute(result);
			if (waitBar!=null&&waitBar.isShowing()) {
				waitBar.dismiss();
			}
			if (result!=null&&result.size()>0) {
				if (page==1) {
					 adapter = new YueListAdapter(result, 0, getActivity());
//					SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
//					swingBottomInAnimationAdapter.setAbsListView(gv);
					 gv.setAdapter(adapter);
					 gv.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							MainActivity ma = (MainActivity) getActivity();
							int temp = (page-1)*20-3;
							if (arg2>(temp)) {
								ma.loadMore1(true);
							}else {
								ma.loadMore1(false);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
					 gv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Intent intent = new Intent();
							intent.setClass(getActivity(), YueListLoadActivity.class);
							intent.putExtra(YueListLoadActivity.URL, result.get(arg2).getLink());
							startActivity(intent);
						}
					});
				}else {
					adapter.addData(result);
				}
				page++;
			}else {
				Toast.makeText(getActivity(), "没有查询到数据，重新设置查询条件", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
