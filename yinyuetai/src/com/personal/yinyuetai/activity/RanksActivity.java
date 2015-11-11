package com.personal.yinyuetai.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.ActionBar.LayoutParams;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.internal.Utils;
import com.personal.service.getRealURL;
import com.personal.yinyuetai.R;
import com.personal.yinyuetai.activity.MainFragment.GetDataTask;
import com.personal.yinyuetai.adapter.QueryAdapter;
import com.personal.yinyuetai.bean.ArtistInfo;
import com.personal.yinyuetai.bean.RanksInfo;
import com.personal.yinyuetai.bean.RanksInfo1;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.e;
import com.umeng.update.UmengUpdateAgent;

public class RanksActivity extends FragmentActivity{
	private boolean isExit = false; // ����Ƿ�Ҫ�˳�
	private TimerTask timeTask = null;
	private Timer timer = null;
	private MainFragment mainFragment;
	private YueListFragment yuelistFragment;
	private PopupWindow popupWindow;
	private Button btnPlus,btnMinus,btnPlus1,btnMinus1,btnOK;
	private TextView tvYear,tvNum;
	private RadioGroup rg;
	private Calendar calendar;
	private InputMethodManager imm;
	StringBuilder str = new StringBuilder();
    HashMap<String, String[]> map;
    Handler handler= new Handler();
    Handler handler2 = new Handler();
	private static SharedPreferences appPreferences = null;
	public  int selectIndex,selectIndex1=R.id.rb_billboard;
	private boolean lastline;
	private ProgressDialog waitBar;
	private PullToRefreshListView ptrl;
	private int pageNum=1;
	private String url = "http://www.kugou.com/yy/rank/home/1-4681.html",url1= "http://www.kugou.com/yy/rank/home/1-4681.html";
	private QueryAdapter adapter;
	private int titlesRemain;
	private List<ArtistInfo> resultList;
	private int totalRemain;
	private int rankType;
	private List<RanksInfo1> urlList;
	private int currentYear,currentYearWeeks;
	private String[] defaultUrl = {"http://www.kugou.com/yy/rank/home/1-4681.html",
			"http://www.kugou.com/yy/rank/home/1-4672.html","http://www.kugou.com/yy/rank/home/1-22163.html",
			"http://www.kugou.com/yy/rank/home/1-4688.html","http://www.kugou.com/yy/rank/home/1-4673.html"};
	/**
	 * 是否是点击menu键打开popupWindow的，默认为false
	 */
	boolean isMenuShow = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timer = new Timer();
        setContentView(R.layout.fragment_main);
        appPreferences=getSharedPreferences("Definition", 0);
        createView();
        initView(savedInstanceState);
        new DataTask().execute();
    }
    @Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
    private void createView() {
    	ptrl = (PullToRefreshListView) findViewById(R.id.ptrl);
    	
	}
	private void initView(Bundle savedInstanceState) {
		calendar = Calendar.getInstance();
		currentYear = calendar.get(Calendar.YEAR);
		currentYearWeeks = calendar.get(Calendar.WEEK_OF_YEAR);
		ptrl.setOnRefreshListener(new MyRefreshListener(ptrl));
		ptrl.setMode(Mode.BOTH);
		ptrl.getRefreshableView().setItemsCanFocus(true);
	}
	
	public void loadMore(boolean lastline){
		this.lastline = lastline;
	}
	
	private class DataTask extends AsyncTask<String, Void, RanksInfo>{
		@Override
		protected void onPreExecute() {
				waitBar = ProgressDialog.show(RanksActivity.this, "",
						"正在拼命解析中，请稍候");
						waitBar.setCancelable(true);
		};
		@Override
		protected RanksInfo doInBackground(String... params) {
			getRealURL get = new getRealURL();
			resultList = new ArrayList<ArtistInfo>();
			return get.getRanks(url1);
		}
		@Override
		protected void onPostExecute(RanksInfo result) {
			urlList = result.getUrlList();
			if (result.getTitleList().size()>0) {
				super.onPostExecute(result);
				titlesRemain = result.getTitleList().size();
				for (int i = 0; i < titlesRemain; i++) {
					int location = totalRemain+i;
					new SearchTask().execute(result.getTitleList().get(i),i+"");
				}
				totalRemain = totalRemain + titlesRemain;
				pageNum++;
				url1 = nextPageUrl();
			}else {
				// 关闭等待对话框
				if (waitBar != null) {
					waitBar.dismiss();
					waitBar = null;
				}
				Toast.makeText(RanksActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
			}
		}
	}
	private String nextPageUrl(){
		String newUrl;
		String mUrl = url1.substring(0,url1.lastIndexOf("/")+1);
		String mUrl1 = url1.substring(url1.indexOf("-"));
		newUrl = mUrl + pageNum + mUrl1;
		return newUrl;
	}
	private class SearchTask extends AsyncTask<String, Void, List<ArtistInfo>>{
		private int mLocation;
		private String mTitle;

		@Override
		protected List<ArtistInfo> doInBackground(String... params) {
			getRealURL get = new getRealURL();
			StringBuilder str = new StringBuilder();
			str.append("http://so.yinyuetai.com/mv?keyword=");
			str.append(URLEncoder.encode(params[0]));
			mLocation = Integer.parseInt(params[1]);
			mTitle = params[0];
			return get.parseWeb(str.toString(), 2);
		}
		@Override
		protected void onPostExecute(List<ArtistInfo> result) {
			titlesRemain-- ;
			ArtistInfo mInfo = new ArtistInfo();
			if (result.size()>0&&result.get(0).getImg()!=null) {
				super.onPostExecute(result);
				for (int i = 0; i < result.size(); i++) {
					if (result.get(i).getTitle().indexOf("榜")==-1) {
						mInfo = result.get(i);
						break;
					}
				}
			}else {
				mInfo.setTitle(mTitle);
				mInfo.setImg("assets://nothing.png");
				mInfo.setLink("");
			}
			resultList.add(mLocation, mInfo);
			if (titlesRemain==0) {
				
				// 关闭等待对话框
				if (waitBar != null) {
					waitBar.dismiss();
					waitBar = null;
				}
				
				if (pageNum==2) {
					adapter = new QueryAdapter(RanksActivity.this,resultList);
					SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
					swingBottomInAnimationAdapter.setAbsListView(ptrl.getRefreshableView());
					ptrl.setAdapter(swingBottomInAnimationAdapter);
				}else {
					adapter.addData(resultList);
				}
				ptrl.onRefreshComplete();
			}
		}
	
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode==KeyEvent.KEYCODE_MENU) {
    		isMenuShow = true;  
			showPopupWindow();
		}
    	if (keyCode==KeyEvent.KEYCODE_DPAD_DOWN) {
			if (lastline) {
				new DataTask().execute();
			}
		}
    	return super.onKeyDown(keyCode, event);
    }
    public void showPopupWindow(){
    	if (popupWindow!=null&&popupWindow.isShowing()) {
			popupWindow.dismiss();
		}else {
			View mView = initPopupWindow();
			popupWindow= new PopupWindow(mView,LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT,true);
			popupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
		}
    }

    private View initPopupWindow(){
    	LayoutInflater inflater = LayoutInflater.from(RanksActivity.this);
    	View popWindow = inflater.inflate(R.layout.option_menu_ranks, null);
    	LinearLayout content = (LinearLayout) popWindow.findViewById(R.id.parent);
    	btnPlus = (Button) popWindow.findViewById(R.id.btn_plus);
    	btnMinus = (Button) popWindow.findViewById(R.id.btn_minus);
    	btnPlus1 = (Button) popWindow.findViewById(R.id.btn_plus1);
    	btnMinus1 = (Button) popWindow.findViewById(R.id.btn_minus1);
    	tvYear = (TextView) popWindow.findViewById(R.id.tv_year);
    	tvNum = (TextView) popWindow.findViewById(R.id.tv_num);
    	btnOK=(Button)popWindow.findViewById(R.id.btn_ok);
    	rg = (RadioGroup) popWindow.findViewById(R.id.rg);
    	updateYearNum(Calendar.YEAR,0);
    	((RadioButton)popWindow.findViewById(selectIndex1)).setChecked(true);
    	OnKeyListener popListener = new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				 if (arg1 == KeyEvent.KEYCODE_MENU||arg1 == KeyEvent.KEYCODE_BACK) {  
	                    if (!isMenuShow) {  
	                        popupWindow.dismiss();  
	                    }  
	                    isMenuShow = false;  
	  
	                }  
				return false;
			}
		};
		btnPlus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateYearNum(Calendar.YEAR,1);
			}
		});
		btnMinus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateYearNum(Calendar.YEAR,-1);
				
			}
		});
		btnPlus1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateYearNum(Calendar.WEEK_OF_YEAR,1);
			}
		});
		btnMinus1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateYearNum(Calendar.WEEK_OF_YEAR,-1);
			}
		});
    	btnOK.setOnClickListener(new OnClickListener() {
    		
    		public void onClick(View v) {
    			pageNum=1;
    			if (urlList!=null&&urlList.size()>0) {
    				int mPosition = currentYear - calendar.get(Calendar.YEAR);
    				int mPosition1 = calendar.get(Calendar.WEEK_OF_YEAR);
    				int weekOff = 0;
    				//判断是否当年，计算偏移量
    				if (mPosition==0) {
						weekOff = currentYearWeeks - urlList.get(mPosition).getUrlList().size();
					}else {
						weekOff = com.personal.yinyuetai.util.Utils.getMaxWeekNumOfYear(mPosition) - urlList.get(mPosition).getUrlList().size();
					}
    				int mPosition2 = mPosition1-1-weekOff;
    				if (mPosition2<0||mPosition2>(urlList.get(mPosition).getUrlList().size()-1)) {
						Toast.makeText(RanksActivity.this, "所选日期没有数据", Toast.LENGTH_SHORT).show();
						return;
					}
    				if (url1.equals(url)) {
    					url1 = urlList.get(mPosition).getUrlList().get(mPosition2);
					}else {
						url = url1;
					}
				}
    			new DataTask().execute();
    			popupWindow.dismiss();
    		}

    	});
    	rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_billboard:
					url1 = defaultUrl[0];
					break;
				case R.id.rb_mnet:
					url1 = defaultUrl[1];
					break;
				case R.id.rb_top:
					url1 = defaultUrl[2];
					break;
				case R.id.rb_hito:
					url1 = defaultUrl[3];
					break;
				case R.id.rb_japan:
					url1 = defaultUrl[4];
					break;
				default:
					break;
				}
				selectIndex1 = checkedId;
			}
		});
    	btnPlus.setOnKeyListener(popListener);
    	btnPlus1.setOnKeyListener(popListener);
		btnMinus.setOnKeyListener(popListener);
		btnMinus1.setOnKeyListener(popListener);
		btnOK.setOnKeyListener(popListener);
		rg.setOnKeyListener(popListener);
		return popWindow;
    }
    private void updateYearNum(int position ,int off){
    	calendar.add(position, off);
    	tvYear.setText(calendar.get(Calendar.YEAR)+"年");
    	tvNum.setText(calendar.get(Calendar.WEEK_OF_YEAR)+"期");
    	
    }
	@Override
	public void onBackPressed() {
		if (isExit) {
			finish();
		} else {
			isExit = true;
			Toast.makeText(RanksActivity.this, "再按一次返回键退出", Toast.LENGTH_SHORT)
					.show();
			timeTask = new TimerTask() {

				@Override
				public void run() {
					isExit = false;
				}
			};
			timer.schedule(timeTask, 2000);
		}
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
			new DataTask().execute("");
			
		}

		@Override
		public void onPullUpToRefresh(
				PullToRefreshBase<ListView> refreshView) {
			new DataTask().execute("");
			
		}
		
	}
}
