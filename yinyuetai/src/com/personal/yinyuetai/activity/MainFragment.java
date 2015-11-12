package com.personal.yinyuetai.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.scheme.LayeredSocketFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.personal.service.getRealURL;
import com.personal.yinyuetai.R;
import com.personal.yinyuetai.adapter.QueryAdapter;
import com.personal.yinyuetai.bean.ArtistInfo;
import com.personal.yinyuetai.view.LancherLayout.loadMore;

public class MainFragment extends Fragment {
	private View view;
    private List<ArtistInfo> infoList = new ArrayList<ArtistInfo>();
	private ImageLoader imageLoader = null;
	private	DisplayImageOptions options = null;
	private int mStatus=0;
    private FrameLayout[] flList= new FrameLayout[20];
    private ImageView[] ivList=new ImageView[20];
    private TextView[] tvList=new TextView[20];
    private String mDefiniton;
    private String urlStr;
    private StringBuilder mUrlStr;
    private int pageNum=1;
    private TextView tv_next;
    private PullToRefreshListView ptrl;
	private ProgressDialog waitBar;
	private QueryAdapter adapter;
	public MainFragment(){
		
	}
    public MainFragment(int status, StringBuilder str) {
    	mStatus=status;
    	urlStr=str.toString();
    }
    public void changeContent(int status,StringBuilder str){
    	pageNum=1;
		mStatus=status;
		urlStr=str.toString();
		new GetDataTask().execute("");
	}
    public void loadMore(){
    	new GetDataTask().execute("");
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_main, container,false);
//		appPreferences=getActivity().getSharedPreferences("Definition", 0);
		createView();
		initView();
		new GetDataTask().execute("");
		return view;
	}
	private void createView(){
		ptrl = (PullToRefreshListView) view.findViewById(R.id.ptrl);
	}
	private void initView(){
		ptrl.setOnRefreshListener(new MyRefreshListener(ptrl));
		ptrl.setMode(Mode.BOTH);
		ptrl.getRefreshableView().setItemsCanFocus(true);
	}

	public class GetDataTask extends AsyncTask<String, Void, List<ArtistInfo>> {
		@Override
		protected void onPreExecute() {
			if (getActivity()!=null) {
				waitBar = ProgressDialog.show(getActivity(), "",
						"正在拼命解析中，请稍候");
						waitBar.setCancelable(true);
			}
		};
		@Override
		protected List<ArtistInfo> doInBackground(String... params) {
			getRealURL get = new getRealURL();
			String url;
			if (mStatus==0) {
			url = urlStr+"&page="+pageNum;
	        infoList=get.parseWeb(url,0);
			}
			if (mStatus==1) {
				url = urlStr+"&page="+pageNum;
			infoList=get.parseWeb(url, 0);
			}
			if (mStatus==2) {
				url = urlStr+"&page="+pageNum;
			infoList=get.parseWeb(url.toString(), 1);
				}
			if (mStatus==3) {
				url = urlStr+"&page="+pageNum;
			infoList=get.parseWeb(url.toString(), 2);
			}
			if (mStatus==4) {
			pageNum=1;
			url = urlStr;
			infoList=get.parseWeb(url.toString(), 3);
			}
	        return infoList;
			
		}

		@Override
		protected void onPostExecute(final List<ArtistInfo> resultList) {
			// 关闭等待对话框
			if (waitBar != null) {
				waitBar.dismiss();
				waitBar = null;
			}
			if (resultList.size()>0) {
			super.onPostExecute(resultList);
			if (pageNum==1) {
				adapter = new QueryAdapter(getActivity(),resultList);
				SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
				swingBottomInAnimationAdapter.setAbsListView(ptrl.getRefreshableView());
				ptrl.setAdapter(swingBottomInAnimationAdapter);
			}else {
				adapter.addData(resultList);
			}
			pageNum++;
			ptrl.onRefreshComplete();
		}else {
			Toast.makeText(getActivity(), "没有查询到数据，重新设置查询条件", Toast.LENGTH_SHORT).show();
		}
	}
}
	public void loadYueList(String link,Context context){
		urlStr = link;
		mStatus=4;
		new GetDataTask().execute("");
	}
	
	private class MyRefreshListener implements OnRefreshListener2<ListView>{

		private PullToRefreshListView mPtflv;

		public MyRefreshListener(PullToRefreshListView lv_result) {
			this.mPtflv = lv_result;
		}
		
		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ListView> refreshView) {
			pageNum=1;
			new GetDataTask().execute("");
			
		}

		@Override
		public void onPullUpToRefresh(
				PullToRefreshBase<ListView> refreshView) {
			new GetDataTask().execute("");
			
		}
		
	}
}
