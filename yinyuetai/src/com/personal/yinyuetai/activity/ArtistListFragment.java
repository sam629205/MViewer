package com.personal.yinyuetai.activity;


import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.personal.service.getRealURL;
import com.personal.yinyuetai.R;
import com.personal.yinyuetai.adapter.QueryAdapter;
import com.personal.yinyuetai.bean.ArtistInfo;


public class ArtistListFragment extends Fragment{
    List<ArtistInfo> resultList = new ArrayList<ArtistInfo>();
    private static SharedPreferences appPreferences = null;
    private QueryAdapter QueryAdapter;
    private ListView list1;
    private View view;
	private View footer;
    private String str1,str2;
    private int pageNum=1;
    private int lastItem;
    private Fragment mContent;
	String mPageID="";
	Handler handler = new Handler();
	StringBuilder str = new StringBuilder();
	public ArtistListFragment(String artistSort,String artistArea){
		str1=artistArea;
		str2=artistSort;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_artist_list, container, false);
		appPreferences=getActivity().getSharedPreferences("Definition", 0);
		createView();
		initView();
		return view;
	}
	private void createView(){
		
	}
	private void initView(){
		
	}
	public class GetDataTask extends AsyncTask<String, Void, List<ArtistInfo>> {

		@Override
		protected List<ArtistInfo> doInBackground(String... params) {
			resultList=getRealURL.getArtist(str1,str2 ,pageNum);
	        return resultList;
			
		}

		@Override
		protected void onPostExecute(List<ArtistInfo> resultList) {
			if (resultList.size()!=0) {
			footer = getActivity().getLayoutInflater().inflate(R.layout.data_loading_view, null);
			list1.addFooterView(footer);
			QueryAdapter = new QueryAdapter(getActivity(),
					resultList);
			list1.setAdapter(QueryAdapter);
			list1.setSelection(lastItem - 1);
			list1.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					if (arg2!=1&&arg2%20==1) {
						pageNum++;
						new GetDataTask().execute("");
					}
					else{
					ArtistInfo artistInfo = (ArtistInfo)QueryAdapter.getItem(arg2);
//					ArtistFragment af = new ArtistFragment(artistInfo.getLink());
//					switchFragment(af);
					}
				}
			});	
		}
	}
 }
//	private void switchFragment(Fragment fragment) {
//		if (getActivity() == null)
//			return;
//
//		if (getActivity() instanceof MainActivity) {
//			MainActivity ra = (MainActivity) getActivity();
//			ra.switchContent(fragment);
//		}
//	}
}
