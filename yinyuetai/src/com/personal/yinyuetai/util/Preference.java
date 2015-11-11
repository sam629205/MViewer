package com.personal.yinyuetai.util;


import com.personal.yinyuetai.MyApplication;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


/****
 * SharedPreferences的一个代理类
 * @author lidw
 */
public class Preference {

	private static SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getApplication());

	public static void putString(String key, String value) {
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void putInt(String key, int value) {
		Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void putBoolean(String key, boolean value) {
		Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void putLong(String key, long value) {
		Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	
	public static int getInt(String key) {
		return pref.getInt(key, 0);
	}

	public static int getInt(String key, int default_value) {
		return pref.getInt(key, default_value);
	}

	public static String getString(String key) {
		return pref.getString(key, "");
	}

	public static String getString(String key, String default_value) {
		return pref.getString(key, default_value);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return pref.getBoolean(key, defValue);
	}

	public static long getLong(String key) {
		return pref.getLong(key, 0);
	}

}
