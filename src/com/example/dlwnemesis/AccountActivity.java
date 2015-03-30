package com.example.dlwnemesis;

import java.util.ArrayList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AccountActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_layout);

		AccountManager am = AccountManager.get(getApplicationContext());
		Account[] accounts = am.getAccountsByType("com.google");

		ArrayList<String> valueList = new ArrayList<String>();

		for (int i = 0; i < accounts.length; i++) {
			valueList.add(accounts[i].name);
		}

		ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_list_item_1, valueList);
		final ListView lv = (ListView) findViewById(R.id.account_list);
		lv.setAdapter(adapter);
		lv.setClickable(true);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Object o = lv.getItemAtPosition(arg2);
				if (o instanceof String) {
					Intent returnIntent = new Intent();
					returnIntent.putExtra("result", (String) o.toString());
					setResult(RESULT_OK, returnIntent);
					finish();
				}
			}
		});
	}
	/*
	 * 
	 * Intent returnIntent = new Intent();
	 * returnIntent.putExtra("result",result);
	 * setResult(RESULT_OK,returnIntent); finish();
	 */
	/*
	 * Intent returnIntent = new Intent(); setResult(RESULT_CANCELED,
	 * returnIntent); finish();
	 */
}
