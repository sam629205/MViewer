package com.personal.yinyuetai.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.VideoView;

public class MyVideoView extends VideoView{

	public MyVideoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public MyVideoView(Context context, AttributeSet attrs) {
		  super(context, attrs);
	}

	public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
		  super(context, attrs, defStyle);
		  // TODO Auto-generated constructor stub
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
}
