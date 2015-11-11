package com.personal.yinyuetai.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import com.personal.service.getRealURL;
import com.personal.yinyuetai.R;
import com.personal.yinyuetai.adapter.MyPagerAdapter;
import com.personal.yinyuetai.bean.ArtistInfo;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MenuFragment extends Fragment{
	private static SharedPreferences appPreferences = null;
	private List<View> views = null;
	private ViewPager vpViewPager=null;
	private TextView tv_tag1,tv_tag2;
	private View view1,view2;
	private View view;
    List<ArtistInfo> resultList = new ArrayList<ArtistInfo>();
    private Button btn_ok,btn_ok2;
	private int offset; // 间隔
	private int cursorWidth; // 游标的长度
	private Animation animation = null;
	private int originalIndex = 0;
	private ImageView cursor = null;
	private EditText ett_mvSearch,ett_artistSearch;
	String[] array1 = {"all","ML","US","KR","HT","JP"};
	String[] array2 = {"all","Boy","Girl","Combo"};
	String[] array3 = {"pubdate","dayViews","weekViews","monthViews"};
	String[] array4 = {"all","FirstShow","music_video","live","concert"};
	String[] array5 = {"super","high",""};
	int item1=0,item2=0,item3=0,item4=0,item5=0,item6=0,item7=0;
    private Spinner spn_artist_area,spn_artist_sort,spn_video_sort,spn_sort_way,spn_page,spn_artist_area2,spn_artist_sort2;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	view=inflater.inflate(R.layout.sliding_menu, container,false);
	appPreferences=getActivity().getSharedPreferences("Definition", 0);
	createView();
	initView();
	return view;
}
private void createView(){
	tv_tag1=(TextView) view.findViewById(R.id.tv1_menu);
	tv_tag2=(TextView) view.findViewById(R.id.tv2_menu);
	views=new ArrayList<View>();
	view1=LayoutInflater.from(getActivity()).inflate(R.layout.option_menu, null);
	view2=LayoutInflater.from(getActivity()).inflate(R.layout.option_artist, null);
	btn_ok2=(Button) view2.findViewById(R.id.btn_ok);
	views.add(view1);
	views.add(view2);
}
private void initView(){
	ett_mvSearch=(EditText) view1.findViewById(R.id.ett_search);
	ett_artistSearch=(EditText) view2.findViewById(R.id.ett_search);
	btn_ok2.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ArtistListFragment alf = new ArtistListFragment(array2[item6], array1[item7]);
//			switchFragment(alf);
		}
	});
	ett_mvSearch.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			Thread tr = new Thread(new Runnable() {
				
				@Override
				public void run() {
					StringBuilder str = new StringBuilder();
					str.append("http://so.yinyuetai.com/mv?keyword=");
					try {
						str.append(URLEncoder.encode(ett_mvSearch.getText().toString(),"UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MainFragment mf = new MainFragment(1, str);
//					switchFragment(mf);
				}
			});
			try {
				tr.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tr.start();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	});
	ett_artistSearch.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	});
	spn_artist_area=(Spinner)view1.findViewById(R.id.artist_area);
	ArrayAdapter<CharSequence> adapter0=ArrayAdapter.createFromResource(getActivity(),R.array.artist_area, android.R.layout.simple_spinner_item);
	adapter0.setDropDownViewResource(R.layout.dropdown_item);
	// 将数据绑定到Spinner视图上
	spn_artist_area.setAdapter(adapter0);
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
	spn_artist_sort=(Spinner)view1.findViewById(R.id.artist_sort);
	ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(getActivity(),R.array.artist_sort, android.R.layout.simple_spinner_item);
	adapter1.setDropDownViewResource(R.layout.dropdown_item);
	// 将数据绑定到Spinner视图上
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
	spn_sort_way=(Spinner)view1.findViewById(R.id.sort_way);
	ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(getActivity(),R.array.sort_way, android.R.layout.simple_spinner_item);
	adapter2.setDropDownViewResource(R.layout.dropdown_item);
	// 将数据绑定到Spinner视图上
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
	spn_video_sort=(Spinner)view1.findViewById(R.id.video_sort);
	ArrayAdapter<CharSequence> adapter3=ArrayAdapter.createFromResource(getActivity(),R.array.video_sort, android.R.layout.simple_spinner_item);
	adapter3.setDropDownViewResource(R.layout.dropdown_item);
	// 将数据绑定到Spinner视图上
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
	spn_page=(Spinner)view1.findViewById(R.id.definition);
	ArrayAdapter<CharSequence> adapter4=ArrayAdapter.createFromResource(getActivity(),R.array.definition, android.R.layout.simple_spinner_item);
	adapter4.setDropDownViewResource(R.layout.dropdown_item);
	// 将数据绑定到Spinner视图上
	spn_page.setAdapter(adapter4);
	spn_page.setSelection(item5);
	spn_page.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
			Spinner spinner = (Spinner) parent;
			item5 = position;
			appPreferences.edit().putString("Definition", array5[item5]).commit();
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	});
	btn_ok=(Button)view1.findViewById(R.id.btn_ok);
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
//			str.append("&page=");
//			int pageNum=item5+1;
//			str.append(pageNum);
			MainFragment mf = new MainFragment(1, str);
//			if (mf != null)
//				switchFragment(mf);
		}


	});
	spn_artist_sort2=(Spinner) view2.findViewById(R.id.artist_kind);
	ArrayAdapter<CharSequence> adapter5=ArrayAdapter.createFromResource(getActivity(),R.array.artist_sort, android.R.layout.simple_spinner_item);
	adapter5.setDropDownViewResource(R.layout.dropdown_item);
	// 将数据绑定到Spinner视图上
	spn_artist_sort2.setAdapter(adapter5);
	spn_artist_sort2.setSelection(item6);
	spn_artist_sort2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
			Spinner spinner = (Spinner) parent;
			item6 = position;
			
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	});
	spn_artist_area2=(Spinner) view2.findViewById(R.id.artist_area);
	ArrayAdapter<CharSequence> adapter6=ArrayAdapter.createFromResource(getActivity(),R.array.artist_area, android.R.layout.simple_spinner_item);
	adapter6.setDropDownViewResource(R.layout.dropdown_item);
	// 将数据绑定到Spinner视图
	spn_artist_area2.setAdapter(adapter6);
	spn_artist_area2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
			Spinner spinner = (Spinner) parent;
			item7 = position;
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	});
	tv_tag1.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			vpViewPager.setCurrentItem(0);
		}
	});
	tv_tag2.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			vpViewPager.setCurrentItem(1);
		}
	});
	vpViewPager = (ViewPager)view.findViewById(R.id.vpViewPager1);
	vpViewPager.setAdapter(new MyPagerAdapter(views));
	vpViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	initCursor(views.size());
}

private class MyOnPageChangeListener implements OnPageChangeListener {
	@Override
	public void onPageSelected(int arg0) {
		int one = 2 * offset + cursorWidth;
		switch (originalIndex) {
		case 0:
			if (arg0 == 1) {
				animation = new TranslateAnimation(0, one, 0, 0);
			}
			break;
		case 1:
			if (arg0 == 0) {
				animation = new TranslateAnimation(one, 0, 0, 0);
			}
			break;
		}
		animation.setFillAfter(true);
		animation.setDuration(300);
		cursor.startAnimation(animation);

		originalIndex = arg0;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

}
/**
 * 根据tagd的数量初始化游标的位置
 * 
 * @param tagNum
 */
public void initCursor(int tagNum) {
	cursorWidth = BitmapFactory.decodeResource(getResources(),
			R.drawable.tab_cursor).getWidth();
	DisplayMetrics dm = new DisplayMetrics();
	getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
	offset = ((dm.widthPixels / tagNum) - cursorWidth) / 2;

	cursor = (ImageView) view.findViewById(R.id.ivCursor);
	Matrix matrix = new Matrix();
	matrix.setTranslate(offset, 0);
	cursor.setImageMatrix(matrix);
}
}
