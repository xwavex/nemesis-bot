package com.example.dlwnemesis.wrapper;

import java.net.HttpCookie;

import com.example.dlwnemesis.DLWNemesisActivity;
import com.example.dlwnemesis.behavior.SimpleHackBehavior;

import android.os.AsyncTask;

public class BehaviorLauncher extends AsyncTask<Object, Void, Object> {
	private SimpleHackBehavior s = null;

	protected Object doInBackground(Object... objs) {
		String token = null;
		HttpCookie cookie = null;
		DLWNemesisActivity thiss = null;
		if (objs[0] instanceof HttpCookie) {
			cookie = (HttpCookie) objs[0];
		}
		if (objs[1] instanceof String) {
			token = (String) objs[1];
		}
		if (objs[2] instanceof DLWNemesisActivity) {
			thiss = (DLWNemesisActivity) objs[2];
		}
		s = new SimpleHackBehavior(cookie, token, thiss);
		return null;
	}
	
	public void killAll() {
		if (s != null)
			s.killAll = true;
	}
	
	protected void onPostExecute(Object result) {

	}
}