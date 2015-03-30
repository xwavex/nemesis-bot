package com.example.dlwnemesis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CredentialsActivity extends Activity {

	private static Intent a(Context paramContext) {
		Intent localIntent = new Intent(paramContext, CredentialsActivity.class);
		localIntent.addFlags(268435456);
		return localIntent;
	}

	public static void a() {
		DLWNemesisActivity self = DLWNemesisActivity.getSelfInstance();
		self.startActivity(a(self));
	}

	public static void a(Intent paramIntent) {
		DLWNemesisActivity self = DLWNemesisActivity.getSelfInstance();
		Intent localIntent = a(self);
		localIntent.putExtra("android.intent.extra.INTENT", paramIntent);
		self.startActivity(localIntent);
	}

	protected void onActivityResult(int paramInt1, int paramInt2,
			Intent paramIntent) {
		Object[] arrayOfObject = new Object[3];
		arrayOfObject[0] = Integer.valueOf(paramInt1);
		arrayOfObject[1] = Integer.valueOf(paramInt2);
		arrayOfObject[2] = paramIntent;

		switch (paramInt1) {
		case 0:
			if (paramInt2 == -1) {
				// c->e();
				finish();
			} else {
				CredentialsActivity.a();
				finish();
			}
			break;
		default:
			super.onActivityResult(paramInt1, paramInt2, paramIntent);
			CredentialsActivity.a();
			finish();
		}
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		// setContentView(2130903050);
		// this.b = findViewById(16908290);
		// ((Button)findViewById(2131165223)).setOnClickListener(new ah(this));
	}

	protected void onNewIntent(Intent paramIntent) {
		super.onNewIntent(paramIntent);
		setIntent(paramIntent);
	}

	protected void onResume() {
		super.onResume();
		Intent localIntent = (Intent) getIntent().getParcelableExtra(
				"android.intent.extra.INTENT");
		if (localIntent != null) {
			localIntent.setFlags(0);
			startActivityForResult(localIntent, 0);
			return;
		}
	}
}