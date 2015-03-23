package com.personal.yinyuetai.activity;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar.LayoutParams;
import android.app.Service;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.personal.yinyuetai.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends FragmentActivity {
	private boolean isExit = false; // ����Ƿ�Ҫ�˳�
	private TimerTask timeTask = null;
	private Timer timer = null;
	private MainFragment mContent;
	private PopupWindow popupWindow;
	private EditText ett_mvSearch;
	private Button btn_confirm,btn_ok,btn_cancel;
	private InputMethodManager imm;
	String[] array1 = {"","ML","US","KR","HT","JP"};
	String[] array2 = {"","Boy","Girl","Combo"};
	String[] array3 = {"pubdate","dayViews","weekViews","monthViews"};
	String[] array4 = {"","FirstShow","music_video","live","concert"};
	String[] array5 = {"super","high",""};
	int item1=0,item2=0,item3=0,item4=0,item5=0,item6=0,item7=0;
    private Spinner spn_artist_area,spn_artist_sort,spn_video_sort,spn_sort_way,spn_page,spn_artist_area2,spn_artist_sort2;
	StringBuilder str = new StringBuilder();
    HashMap<String, String[]> map;
    Handler handler= new Handler();
    Handler handler2 = new Handler();
	private static SharedPreferences appPreferences = null;
	private boolean lastline;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timer = new Timer();
        setContentView(R.layout.activity_main);
        appPreferences=getSharedPreferences("Definition", 0);
        createView();
        initView(savedInstanceState);        
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

	}
	private void initView(Bundle savedInstanceState) {
		//同步友盟统计数据
				MobclickAgent.updateOnlineConfig(MainActivity.this);

		//友盟更新
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.forceUpdate(MainActivity.this);
		// ����������Fragment��ͼ����
		if (savedInstanceState != null){
			mContent = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		}
		if (mContent == null){
			str.append("http://mv.yinyuetai.com/all?sort=weekViews");
			mContent = new MainFragment(0, str);
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.content_fl, mContent).commit();

	
	}
	
	public void loadMore(boolean lastline){
		this.lastline = lastline;
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode==KeyEvent.KEYCODE_MENU) {
			showPopupWindow();
		}
    	if (keyCode==KeyEvent.KEYCODE_BACK) {
    		if (popupWindow!=null&&popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
		}
    	if (keyCode==KeyEvent.KEYCODE_DPAD_DOWN) {
			if (lastline) {
				mContent.loadMore();
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
    	LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
    	View popWindow = inflater.inflate(R.layout.option_menu, null);
    	ett_mvSearch=(EditText) popWindow.findViewById(R.id.ett_search);
    	btn_confirm=(Button) popWindow.findViewById(R.id.btn_confirm);
    	btn_ok=(Button) popWindow.findViewById(R.id.btn_ok);
    	btn_cancel=(Button) popWindow.findViewById(R.id.btn_cancel);
    	btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (popupWindow!=null&&popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
    	btn_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StringBuilder str = new StringBuilder();
				str.append("http://so.yinyuetai.com/mv?keyword=");
				try {
					str.append(URLEncoder.encode(ett_mvSearch.getText().toString(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mContent.changeContent(3, str);
//				MainFragment mf = new MainFragment(3, str);
//				getSupportFragmentManager().beginTransaction().replace(R.id.content_fl, mf).commit();
				popupWindow.dismiss();
				
			}
		});
    	spn_artist_area=(Spinner)popWindow.findViewById(R.id.artist_area);
    	ArrayAdapter<CharSequence> adapter0=ArrayAdapter.createFromResource(MainActivity.this,R.array.artist_area, android.R.layout.simple_spinner_item);
    	adapter0.setDropDownViewResource(R.layout.dropdown_item);
    	// ����ݰ󶨵�Spinner��ͼ��
    	spn_artist_area.setAdapter(adapter0);
    	spn_artist_area.setSelection(item1);
    	spn_artist_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    		@Override
    		public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
    			Spinner spinner = (Spinner) parent;
    			item1 = position;
    		}
    		@Override
    		public void onNothingSelected(AdapterView<?> parent) {
    			// TODO Auto-generated method stub

    		}
    	});
    	spn_artist_sort=(Spinner)popWindow.findViewById(R.id.artist_sort);
    	ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(MainActivity.this,R.array.artist_sort, android.R.layout.simple_spinner_item);
    	adapter1.setDropDownViewResource(R.layout.dropdown_item);
    	// ����ݰ󶨵�Spinner��ͼ��
    	spn_artist_sort.setAdapter(adapter1);
    	spn_artist_sort.setSelection(item2);
    	spn_artist_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    		@Override
    		public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
    			Spinner spinner = (Spinner) parent;
    			item2 = position;
    		}
    		@Override
    		public void onNothingSelected(AdapterView<?> parent) {
    			// TODO Auto-generated method stub

    		}
    	});
    	spn_sort_way=(Spinner)popWindow.findViewById(R.id.sort_way);
    	ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(MainActivity.this,R.array.sort_way, android.R.layout.simple_spinner_item);
    	adapter2.setDropDownViewResource(R.layout.dropdown_item);
    	// ����ݰ󶨵�Spinner��ͼ��
    	spn_sort_way.setAdapter(adapter2);
    	spn_sort_way.setSelection(item3);
    	spn_sort_way.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    		@Override
    		public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
    			Spinner spinner = (Spinner) parent;
    			item3 = position;
    		}
    		@Override
    		public void onNothingSelected(AdapterView<?> parent) {
    			// TODO Auto-generated method stub

    		}
    	});
    	spn_video_sort=(Spinner)popWindow.findViewById(R.id.video_sort);
    	ArrayAdapter<CharSequence> adapter3=ArrayAdapter.createFromResource(MainActivity.this,R.array.video_sort, android.R.layout.simple_spinner_item);
    	adapter3.setDropDownViewResource(R.layout.dropdown_item);
    	// ����ݰ󶨵�Spinner��ͼ��
    	spn_video_sort.setAdapter(adapter3);
    	spn_video_sort.setSelection(item4);
    	spn_video_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    		@Override
    		public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
    			Spinner spinner = (Spinner) parent;
    			item4 = position;
    		}
    		@Override
    		public void onNothingSelected(AdapterView<?> parent) {
    			// TODO Auto-generated method stub

    		}
    	});
    	spn_page=(Spinner)popWindow.findViewById(R.id.definition);
    	ArrayAdapter<CharSequence> adapter4=ArrayAdapter.createFromResource(MainActivity.this,R.array.definition, android.R.layout.simple_spinner_item);
    	adapter4.setDropDownViewResource(R.layout.dropdown_item);
    	// ����ݰ󶨵�Spinner��ͼ��
    	spn_page.setAdapter(adapter4);
    	spn_page.setSelection(item5);
    	spn_page.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    		@Override
    		public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
    			Spinner spinner = (Spinner) parent;
    			item5 = position;
    			appPreferences.edit().putString("Definition", array5[item5]).commit();
    			System.out.print(array5[item5]);
    		}
    		@Override
    		public void onNothingSelected(AdapterView<?> parent) {
    			// TODO Auto-generated method stub

    		}
    	});
    	btn_ok=(Button)popWindow.findViewById(R.id.btn_ok);
    	btn_ok.setOnClickListener(new OnClickListener() {
    		
    		public void onClick(View v) {
    			final StringBuilder str = new StringBuilder();
    			str.append("http://mv.yinyuetai.com/all?area=");
    			str.append(array1[item1]);
    			str.append("&artist=");
    			str.append(array2[item2]);
    			if (item4!=1) {
    			str.append("&version=");
    			str.append(array4[item4]);
    			}else {
    				str.append("&tag=");
    				str.append(array4[item4]);
    			}
    			str.append("&sort=");
    			str.append(array3[item3]);
    			mContent.changeContent(1, str);
//    			MainFragment mf = new MainFragment(1, str);
//    			getSupportFragmentManager().beginTransaction().replace(R.id.content_fl, mf).commit();
    			popupWindow.dismiss();
    		}

    	});
		return popWindow;
    }
	/**
	 * �������뷨����
	 */
	private void popupInputMethodWindow() {
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				imm = (InputMethodManager) ett_mvSearch.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 400);
	}
	@Override
	public void onBackPressed() {
		if (isExit) {
			finish();
		} else {
			isExit = true;
			Toast.makeText(MainActivity.this, "再按一次返回键退出", Toast.LENGTH_SHORT)
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
}
