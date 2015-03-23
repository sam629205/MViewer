package com.personal.yinyuetai.adapter;

import android.R.color;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter<T> extends ArrayAdapter<T> {
	public CustomArrayAdapter(Context ctx, T [] objects)
    {
        super(ctx, android.R.layout.simple_spinner_item, objects);
    }
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		TextView text = (TextView)view.findViewById(android.R.id.text1);
        text.setTextColor(color.black);
		return super.getDropDownView(position, convertView, parent);
	}

}
