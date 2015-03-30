package com.example.dlwnemesis;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.dlwnemesis.behavior.SimpleHackBehavior;
import com.example.dlwnemesis.querys.GeneralRequest;
import com.example.dlwnemesis.querys.TestRequest;
import com.example.dlwnemesis.wrapper.BehaviorLauncher;

import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class DLWNemesisActivity extends Activity {

	private static DLWNemesisActivity selfInstance;

	private int b = 0;
	private String pubAuthToken;
	private String pubXsrfToken;
	private HttpCookie pubCookie;

	private BehaviorLauncher behave = null;

	public static DLWNemesisActivity getSelfInstance() {
		return selfInstance;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				String result = data.getStringExtra("result");
				AccountManager am = AccountManager.get(getApplicationContext());
				String token = "ah";
				Account myAccount = null;
				Account[] accounts = am.getAccountsByType("com.google");
				Log.d("DLWCC", "get all accounts");

				for (int i = 0; i < accounts.length; i++) {
					if (accounts[i].name.equalsIgnoreCase(result)) {
						myAccount = accounts[i];
						break;
					}
				}
				if (myAccount != null) {
					Log.d("DLWCC", "account ist nicht null");
					@SuppressWarnings("deprecation")
					AccountManagerFuture<Bundle> amf = am.getAuthToken(
							myAccount, token, false,
							new AccountManagerCallback<Bundle>() {

								@Override
								public void run(
										AccountManagerFuture<Bundle> future) {
									try {
										Bundle returnValue = future.getResult();
										// Log.d("DLWCC", "hole bundle");
										String intentToken = "intent";

										Object retObj = returnValue
												.get(intentToken);
										if ((retObj != null)
												&& (retObj instanceof Intent)) {
											// Credentials
											Log.d("DLWCC", "credentials");

											CredentialsActivity
													.a((Intent) retObj);
										}

										String authToken = "authtoken";
										retObj = returnValue.get(authToken);
										if (retObj != null) {
											if (retObj instanceof String) {
												String realAuthToken = (String) retObj;
												pubAuthToken = realAuthToken;
												Log.d("DLWCC", "authToken: "
														+ realAuthToken);

												activateGetHttpCookie(
														"https://m-dot-betaspike.appspot.com",
														realAuthToken);
											}
										}

									} catch (OperationCanceledException e) {
										e.printStackTrace();
									} catch (AuthenticatorException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}

								}
							}, null);

				}
			}
			if (resultCode == RESULT_CANCELED) {

			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selfInstance = this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

		setContentView(R.layout.activity_dlwnemesis);

		TextView txtLevel = (TextView) findViewById(R.id.txt_level);
		TextView txtAp = (TextView) findViewById(R.id.txt_ap);
		TextView txtPortalName = (TextView) findViewById(R.id.portal_name);
		TextView txtPortalState = (TextView) findViewById(R.id.portal_state);

		// setFonts
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/coda.ttf");
		txtLevel.setTypeface(font);
		txtAp.setTypeface(font);
		txtPortalName.setTypeface(font);
		txtPortalState.setTypeface(font);

		pubAuthToken = null;
		pubXsrfToken = null;
		pubCookie = null;

		/** Nemesis getAuthString */
		Log.d("DLWCC", "Nemesis getAuthString");

		AccountManager am = AccountManager.get(getApplicationContext());
		String token = "ah";
		Account myAccount = null;
		Account[] accounts = am.getAccountsByType("com.google");
		Log.d("DLWCC", "get all accounts");

		for (int i = 0; i < accounts.length; i++) {
			if (accounts[i].name.equalsIgnoreCase("xwavedw@gmail.com")) {
				Log.d("DLWCC", "found my account");
				myAccount = accounts[i];
				break;
			}
		}
		if (myAccount != null) {
			Log.d("DLWCC", "account ist nicht null");
			@SuppressWarnings("deprecation")
			AccountManagerFuture<Bundle> amf = am.getAuthToken(myAccount,
					token, false, new AccountManagerCallback<Bundle>() {

						@Override
						public void run(AccountManagerFuture<Bundle> future) {
							try {
								Bundle returnValue = future.getResult();
								// Log.d("DLWCC", "hole bundle");
								String intentToken = "intent";

								Object retObj = returnValue.get(intentToken);
								if ((retObj != null)
										&& (retObj instanceof Intent)) {
									// Credentials
									Log.d("DLWCC", "credentials");

									CredentialsActivity.a((Intent) retObj);
								}

								String authToken = "authtoken";
								retObj = returnValue.get(authToken);
								if (retObj != null) {
									// Log.d("DLWCC",
									// "ret String auth nicht null");
									if (retObj instanceof String) {
										// Log.d("DLWCC",
										// "is sogar nen string");
										String realAuthToken = (String) retObj;
										pubAuthToken = realAuthToken;
										Log.d("DLWCC", "authToken: "
												+ realAuthToken);

										activateGetHttpCookie(
												"https://m-dot-betaspike.appspot.com",
												realAuthToken);
									}
								}

							} catch (OperationCanceledException e) {
								e.printStackTrace();
							} catch (AuthenticatorException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					}, null);

		}
	}

	/** ################# Nemesis getHttpCookie ################# */

	private void activateGetHttpCookie(String baseUrl, String authToken) {
		String[] params = new String[2];
		params[0] = baseUrl;
		params[1] = authToken;
		new RetrieveHttpCookie().execute(params);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dlwnemesis, menu);
		return true;
	}

	private class RetrieveHttpCookie extends
			AsyncTask<String, Void, HttpCookie> {

		protected HttpCookie doInBackground(String... urls) {
			HttpCookie returnValue = null;
			try {
				String url = urls[0] + "/_ah/login?continue="
						+ URLEncoder.encode(urls[0], "UTF-8") + "&auth="
						+ urls[1];

				URL realUrl = new URI(url).toURL();

				// Log.d("DLWCC", "httpConnection url: " + realUrl);

				HttpURLConnection httpCon = (HttpURLConnection) realUrl
						.openConnection();

				httpCon.setInstanceFollowRedirects(false);

				int respCode = httpCon.getResponseCode();
				Log.d("DLWCC", "httpConnection responseCode: " + respCode);

				if (respCode < 0x190) {
					if (respCode == 0x12e) {
						Map<String, List<String>> headerFieldMap = httpCon
								.getHeaderFields();
						if (headerFieldMap != null) {
							List<String> cookieList = headerFieldMap
									.get("Set-Cookie");
							if (cookieList != null) {
								Iterator<String> cookieIterator = cookieList
										.iterator();
								if (cookieIterator.hasNext()) {
									String tmp = cookieIterator.next();
									List<HttpCookie> cookies = HttpCookie
											.parse(tmp);
									Iterator<HttpCookie> cookiesIterator = cookies
											.iterator();
									if (cookiesIterator.hasNext()) {
										HttpCookie cookie = cookiesIterator
												.next();
										String cookieName = cookie.getName();
										if (cookieName.endsWith("ACSID")) {
											// Log.d("DLWCC",
											// "received cookie: " + cookie);
											returnValue = cookie;
											httpCon.disconnect();
											return returnValue;
										}
									}
								}
							}
						}
					} else {
						// proceede anyway!
					}
				}
				httpCon.disconnect();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return returnValue;
		}

		protected void onPostExecute(HttpCookie cookie) {
			if (cookie != null) {
				pubCookie = cookie;
				Object[] objs = new Object[4];
				objs[0] = cookie;
				objs[1] = "https://m-dot-betaspike.appspot.com/handshake";
				objs[2] = null;
				objs[3] = null;
				Log.d("DLWCC", "Cookie ist da!");
				new ServerResponseLogin().execute(objs);
			} else {
				Log.d("DLWCC", "Cookie ist nicht angekommen!");
			}
		}

	}

	/** ################# Nemesis getHttpCookie ################# */

	// https://m-dot-betaspike.appspot.com/rpc/gameplay/getObjectsInCells
	// https://m-dot-betaspike.appspot.com/rpc/playerUndecorated/getPaginatedPlexts
	// https://m-dot-betaspike.appspot.com/rpc/playerUndecorated/getInventory
	// https://m-dot-betaspike.appspot.com/rpc/playerUndecorated/getGameScore
	/** ################# Nemesis login ################# */

	private class ServerResponseLogin extends AsyncTask<Object, Void, Object> {
		/** HttpCookie,url,xsrfToken,payload */
		protected Object doInBackground(Object... objs) {
			try {
				HttpCookie cookie = null;
				String url = null;
				String xsrfToken = null;
				String payload = null;
				if (objs[0] instanceof HttpCookie) {
					cookie = (HttpCookie) objs[0];
				}
				if (objs[1] instanceof String) {
					url = (String) objs[1];
				}
				if (objs[2] instanceof String) {
					xsrfToken = (String) objs[2];
				}
				if (objs[3] instanceof String) {
					payload = (String) objs[3];
				}
				if (url != null) {
					readUserState(createJsonUrl(url), cookie, xsrfToken,
							payload);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}

		/*
		 * public InputStream prepareInputStreamHandshake(URI uri, HttpCookie
		 * cookie, String xsrfToken, String payload) throws Exception { URL
		 * realUrl = uri.toURL(); HttpURLConnection urlCon = (HttpURLConnection)
		 * realUrl .openConnection(); urlCon.setConnectTimeout(0xea60);
		 * urlCon.setReadTimeout(0xea60);
		 * urlCon.setInstanceFollowRedirects(false);
		 * 
		 * urlCon.setRequestProperty("Content-Type",
		 * "application/json;charset=UTF-8");
		 * urlCon.setRequestProperty("Accept-Encoding", "gzip");
		 * urlCon.setRequestProperty("User-Agent", "Nemesis (gzip)");
		 * 
		 * urlCon.setRequestProperty("Cookie", cookie.toString());
		 * 
		 * if (xsrfToken != null) { urlCon.setRequestProperty("X-XsrfToken",
		 * xsrfToken); } else { Log.d("DLWCC", "XsrfToken is null."); }
		 * 
		 * if (payload != null) { writeOverHttpConnection(payload, urlCon); }
		 * else { Log.d("DLWCC", "Payload is null."); }
		 * 
		 * int respCode = urlCon.getResponseCode(); // int respContentLength =
		 * urlCon.getContentLength(); // String uriPath = uri.getPath();
		 * 
		 * switch (respCode) { case 0xc8: Log.d("DLWCC",
		 * "Switch: HttpResponseOK."); InputStream in = urlCon.getInputStream();
		 * String contentEncoding = urlCon.getContentEncoding(); Log.d("DLWCC",
		 * "Content encoding: " + contentEncoding); int contentLength =
		 * urlCon.getContentLength(); Log.d("DLWCC", "Content length: " +
		 * contentLength); if (contentEncoding.equalsIgnoreCase("gzip")) {
		 * return new GZIPInputStream(in); } else { return in; } case 0x12d:
		 * case 0x12e: case 0x12f: case 0x133: Log.d("DLWCC",
		 * "Switch: HttpResponseRedirect."); break; case 0x190: Log.d("DLWCC",
		 * "Switch: HttpResponseBadRequest."); break; case 0x191: Log.d("DLWCC",
		 * "Switch: HttpResponseUnauthorized."); break; case 0x193:
		 * Log.d("DLWCC", "Switch: HttpResponseForbidden."); break; default:
		 * Log.d("DLWCC", "Switch: HttpResponseUnknown."); } return null;
		 * 
		 * }
		 */

		private URI createJsonUrl(String uri)
				throws UnsupportedEncodingException {
			// NEW 1.25.......
			// https://m-dot-betaspike.appspot.com/handshake?json=%7B%22nemesisSoftwareVersion%22%3A%222013-05-03T19%3A32%3A11Z+929c2cce62eb+opt%22%2C%22deviceSoftwareVersion%22%3A%224.1.1%22%7D

			//encode("{\"nemesisSoftwareVersion\":\"2013-05-03T19:32:11Z 929c2cce62eb opt\",\"deviceSoftwareVersion\":\"4.1.1\"}","UTF-8");
			
			
			// NEW 1.33.......
			// https://m-dot-betaspike.appspot.com/handshake?json=%7B%22nemesisSoftwareVersion%22%3A%222013-08-07T00%3A06%3A39Z+a52083df5202+opt%22%2C%22deviceSoftwareVersion%22%3A%224.1.1%22%2C%22a%22%3A%22Z0QebEN%2FLVAHIB9r83hTgDx6LC8iMFw2RRZ6K7bPsd0ZppshWDg9RUnbklucclSGeo%2BfAZsEaNSExMmvyei%2B3dgHcNwOcHwASYO3OoW8qu9HA3oVwq2Ovwj6rCqQP3%2BAnW9npdH1%2FrIgflVWIm8wdIwSfaZR5OB1Qtj6A39eYybKysSBfrCSAU3411e9ejKCK%2FHlLUMcFyKeeGXSVf34Mp%2FZndgDqfECQm5UIayk6rARXVRKu8%2BEga3Q2LiC0L6z8Nfkrc9gGrj2WRrQbdrUYLIiG9ZA7J9%2BT7TlRD9sPEUz%2FexVgnBR%2B3Wjvg6qreWDUClsSGzE7FGOm4CBUxYFIiYkZUwoNxZEeQoPccmmWIFfalwCXwotaR9wIHT5kvHOJhbwGXghH29GRJkChqZywG9l%2B1bQg1Wl24ubwqnHkOL%2BlDvgWFddfXJS2nialJjnUElCfPTcnP8Bf8lKn5xHm5XdU8LjjN3ISV5PATR3QEfztBjdSXzBGwlJszFJSQkEwsCzzUhTzhpoN%2F5s7NpRyiYO1k8wellki4AromhVzRKRlauZPF6CYTpaNnTmqfDbDFoSLLrUzI3a1I%2F5jpK4u7%2BS7bT5gXzV0dcP7TsG53yD7EazLVLwAx8uwwUZIEstN2%2F9UrmdOrUoFf1aqdqH81lzSjYUKagljaD9tGRgcm4%22%7D

			
			
			String encoded = URLEncoder
					.encode("{\"nemesisSoftwareVersion\":\"2013-08-07T00:06:39Z a52083df5202 opt\",\"deviceSoftwareVersion\":\"4.1.1\"}",
							"UTF-8");
			// Log.d("DLWCC", "Encode: encoded = " + encoded);
			URI fullUrl = URI.create(uri + "?json=" + encoded);
			// Log.d("DLWCC", "Encode: uri = " + fullUrl);
			return fullUrl;
			// {"nemesisSoftwareVersion":"2013-04-08T20:23:14Z 10b76085f06d opt","deviceSoftwareVersion":"4.1.1"}
			// https://m-dot-betaspike.appspot.com/handshake?json=%7B%22nemesisSoftwareVersion%22%3A%222013-04-08T20%3A23%3A14Z+10b76085f06d+opt%22%2C%22deviceSoftwareVersion%22%3A%224.1.1%22%7D
		}

		private long writeOverHttpConnection(String msg, HttpURLConnection con)
				throws IOException {
			byte[] byteMsg = msg.getBytes();

			if (msg.length() >= 0x100) {
				con.setRequestProperty("Content-Encoding", "gzip");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				GZIPOutputStream gzipos = new GZIPOutputStream(baos);
				gzipos.write(byteMsg);
				gzipos.close();
				byteMsg = baos.toByteArray();
			}
			con.setDoOutput(true);
			OutputStream out = con.getOutputStream();
			out.write(byteMsg);
			out.close();

			return byteMsg.length;
		}

		private long readHelper(Readable paramReadable,
				Appendable paramAppendable) throws IOException {
			CharBuffer localCharBuffer = CharBuffer.allocate(2048);
			long l = 0L;
			while (paramReadable.read(localCharBuffer) != -1) {
				localCharBuffer.flip();
				paramAppendable.append(localCharBuffer);
				l += localCharBuffer.remaining();
				localCharBuffer.clear();
			}
			return l;
		}

		private String readInputReaderToString(Readable paramReadable)
				throws IOException {
			StringBuilder localStringBuilder = new StringBuilder();
			readHelper(paramReadable, localStringBuilder);
			return localStringBuilder.toString();
		}

		private InputStream readUserState(URI uri, HttpCookie cookie,
				String xsrfToken, String payload) {
			try {
				URL realUrl = uri.toURL();
				HttpURLConnection urlCon = (HttpURLConnection) realUrl
						.openConnection();
				urlCon.setConnectTimeout(0xea60);
				urlCon.setReadTimeout(0xea60);
				urlCon.setInstanceFollowRedirects(false);

				urlCon.setRequestProperty("Content-Type",
						"application/json;charset=UTF-8");
				urlCon.setRequestProperty("Accept-Encoding", "gzip");
				urlCon.setRequestProperty("User-Agent", "Nemesis (gzip)");

				urlCon.setRequestProperty("Cookie", cookie.toString());

				if (xsrfToken != null) {
					urlCon.setRequestProperty("X-XsrfToken", xsrfToken);
				} else {
					Log.d("DLWCC", "XsrfToken is null.");
				}

				if (payload != null) {
					writeOverHttpConnection(payload, urlCon);
				} else {
					Log.d("DLWCC", "Payload is null.");
				}

				int respCode = urlCon.getResponseCode();

				String input = null;

				switch (respCode) {
				case 0xc8:
					Log.d("DLWCC", "Switch: HttpResponseOK.");
					InputStream in = urlCon.getInputStream();
					String contentEncoding = urlCon.getContentEncoding();
					Log.d("DLWCC", "Content encoding: " + contentEncoding);
					int contentLength = urlCon.getContentLength();
					Log.d("DLWCC", "Content length: " + contentLength);
					if (contentEncoding.equalsIgnoreCase("gzip")) {

						input = readInputReaderToString(new InputStreamReader(
								new GZIPInputStream(in), "UTF-8"));

						in.close();
					} else {
						// return in;
					}
				case 0x12d:
				case 0x12e:
				case 0x12f:
				case 0x133:
					Log.d("DLWCC", "Switch: HttpResponseRedirect.");
					break;
				case 0x190:
					Log.d("DLWCC", "Switch: HttpResponseBadRequest.");
					break;
				case 0x191:
					Log.d("DLWCC", "Switch: HttpResponseUnauthorized.");
					break;
				case 0x193:
					Log.d("DLWCC", "Switch: HttpResponseForbidden.");
					break;
				default:
					Log.d("DLWCC", "Switch: HttpResponseUnknown.");
				}

				urlCon.disconnect();

				if (input != null) {
					Log.d("DLWCC", "input: " + input);
					if (input.startsWith("while(1);")) {
						input = input.substring(9);
						parseXsrfToken(input);
					}
					ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(
							input.getBytes());
					return localByteArrayInputStream;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		private void parseXsrfToken(String input) {
			int occurency = input.indexOf("xsrfToken\":\"");
			if (occurency > 5) {
				String xsrfTokenHelper = input.substring(occurency + 12);
				String[] splits = xsrfTokenHelper.split("\"");
				pubXsrfToken = splits[0];
				Log.d("DLWCC", "xsrfParsing: " + pubXsrfToken);
			}
		}

		protected void onPostExecute(Object result) {

			Object[] objs = new Object[3];
			objs[0] = pubCookie;
			objs[1] = pubXsrfToken;
			objs[2] = DLWNemesisActivity.this;
//			behave = new BehaviorLauncher();
//			// behave.execute(objs);
//
//			Log.d("DLWCC", "Starting main Behavior...!");
//
//			// TEST ######################################################
//			Object[] obj = new Object[2];
//			obj[0] = pubCookie;
//			obj[1] = pubXsrfToken;
//			new TestRequest().execute(obj);
//			// TEST ######################################################
		}

	}

	/** ################# Nemesis login ################# */

	/*
	 * // Let the ViewSwitcher do the animation listening for you
	 * ((ViewSwitcher) findViewById(R.id.switcher)).setOnClickListener(new
	 * View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { ViewSwitcher switcher =
	 * (ViewSwitcher) v;
	 * 
	 * if (switcher.getDisplayedChild() == 0) { switcher.showNext(); } else {
	 * switcher.showPrevious(); } } });
	 */

	@Override
	public void onBackPressed() {
		Log.d("DLWCC", "onBackPressed Called");
		if (behave != null) {
			behave.killAll();
		}
		finish();
	}

}
/**
 * if (b == 0) { Object[] objs = new Object[4]; objs[0] = pubCookie; objs[1] =
 * null; objs[2] = pubXsrfToken; objs[3] =
 * "{\"params\":{\"cellsAsHex\":[\"47ba3d1a70000000\",\"47ba3d1970000000\",\"47ba3d10b0000000\",\"47ba3d1770000000\",\"47ba3d1bb0000000\",\"47ba3d19b0000000\",\"47ba3d1070000000\",\"47ba3d1730000000\",\"47ba3d1af0000000\",\"47ba3d19f0000000\",\"47ba3d1a30000000\",\"47ba3d1830000000\",\"47ba3d10f0000000\",\"47ba3d1a50000000\",\"47ba3d1850000000\",\"47ba3d1090000000\",\"47ba3d1790000000\",\"47ba3d1a90000000\",\"47ba3d1990000000\",\"47ba3d1050000000\",\"47ba3d1750000000\",\"47ba3d1bd0000000\",\"47ba3d19d0000000\",\"47ba3d1710000000\",\"47ba3d1b10000000\",\"47ba3d1a10000000\",\"47ba3d1910000000\",\"47ba3d10d0000000\"],\"dates\":[1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276],\"knobSyncTimestamp\":0,\"playerLocation\":\"0319c3ce,00822836\"}}"
 * ;
 * 
 * 
 * //
 * "{\"params\":{\"playerLocation\":null,\"cellsAsHex\":[\"47a3000000000000\",\"47a5000000000000\",\"47af000000000000\",\"47b4000000000000\",\"47bc000000000000\",\"47c0b00000000000\",\"47c7000000000000\",\"47c9000000000000\"],\"clientBasket\":{\"clientBlob\":null},\"knobSyncTimestamp\":null,\"energyGlobGuids\":null,\"factionOnly\":false,\"minTimestampMs\":1367672668846,\"maxTimestampMs\":-1,\"desiredNumItems\":100,\"ascendingTimestampOrder\":false}}"
 * ;
 * 
 * //"{\"params\":{\"lastQueryTimestamp\":1367692014479}}";
 * 
 * //"{\"params\":[]}";
 * 
 * 
 * //
 * "{\"params\":{\"playerLocation\":null,\"cellsAsHex\":[\"47a3000000000000\",\"47a5000000000000\",\"47af000000000000\",\"47b4000000000000\",\"47bc000000000000\",\"47c0b00000000000\",\"47c7000000000000\",\"47c9000000000000\"],\"clientBasket\":{\"clientBlob\":null},\"knobSyncTimestamp\":null,\"energyGlobGuids\":null,\"factionOnly\":true,\"minTimestampMs\":1367599545029,\"maxTimestampMs\":-1,\"desiredNumItems\":100,\"ascendingTimestampOrder\":false}}"
 * ;
 * 
 * //
 * "{\"params\":{\"cells\":null,\"cellsAsHex\":[\"47ba3d1a70000000\",\"47ba3d1970000000\",\"47ba3d10b0000000\",\"47ba3d1770000000\",\"47ba3d1bb0000000\",\"47ba3d19b0000000\",\"47ba3d1070000000\",\"47ba3d1730000000\",\"47ba3d1af0000000\",\"47ba3d19f0000000\",\"47ba3d1a30000000\",\"47ba3d1830000000\",\"47ba3d10f0000000\",\"47ba3d1a50000000\",\"47ba3d1850000000\",\"47ba3d1090000000\",\"47ba3d1790000000\",\"47ba3d1a90000000\",\"47ba3d1990000000\",\"47ba3d1050000000\",\"47ba3d1750000000\",\"47ba3d1bd0000000\",\"47ba3d19d0000000\",\"47ba3d1710000000\",\"47ba3d1b10000000\",\"47ba3d1a10000000\",\"47ba3d1910000000\",\"47ba3d10d0000000\"],\"clientBasket\":{\"clientBlob\":null},\"dates\":[1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276],\"energyGlobGuids\":[],\"knobSyncTimestamp\":1367685928046,\"playerLocation\":\"0319c3ce,00822836\"}}"
 * ;
 * 
 * //"{\"params\":{\"lastQueryTimestamp\":1367685932530}}"; Log.d("DLWCC",
 * "?????????"); b++; new ServerResponse().execute(objs); } else if (b==1) {
 * finish(); }
 */
