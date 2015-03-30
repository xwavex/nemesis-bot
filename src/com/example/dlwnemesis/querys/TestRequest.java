package com.example.dlwnemesis.querys;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.zip.GZIPInputStream;

import android.os.AsyncTask;
import android.util.Log;

public class TestRequest extends AsyncTask<Object, Void, Object> {
	private HttpCookie cookie = null;
	private String xsrfToken = null;

	/** HttpCookie,xsrfToken,InvokeAbleElement */
	protected Object doInBackground(Object... objs) {
		if (objs[0] instanceof HttpCookie) {
			cookie = (HttpCookie) objs[0];
		}
		if (objs[1] instanceof String) {
			xsrfToken = (String) objs[1];
		}

		if ((cookie != null) && (xsrfToken != null)) {
			String uri = "https://m-dot-betaspike.appspot.com/rpc/playerUndecorated/getInventory";
			
			String payload = "{\"params\":{\"lastQueryTimestamp\":0}}"; //"{\"params\":{\"cells\":null,\"cellsAsHex\":[\"47ba3d1a70000000\",\"47ba3d1970000000\",\"47ba3d10b0000000\",\"47ba3d1770000000\",\"47ba3d1bb0000000\",\"47ba3d1ab0000000\",\"47ba3d19b0000000\",\"47ba3d1070000000\",\"47ba3d1730000000\",\"47ba3d1af0000000\",\"47ba3d19f0000000\",\"47ba3d1a30000000\",\"47ba3d10f0000000\",\"47ba3d1a50000000\",\"47ba3d1090000000\",\"47ba3d1a90000000\",\"47ba3d1990000000\",\"47ba3d1050000000\",\"47ba3d1750000000\",\"47ba3d1bd0000000\",\"47ba3d1ad0000000\",\"47ba3d19d0000000\",\"47ba3d1010000000\",\"47ba3d1b10000000\",\"47ba3d1a10000000\",\"47ba3d10d0000000\"],\"clientBasket\":{\"clientBlob\":null},\"dates\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"energyGlobGuids\":[],\"knobSyncTimestamp\":1368183677720,\"playerLocation\":\"0319c339,00822ccb\"}}"
			
			Log.d("DLWCC", "uri: " + uri);//"https://m-dot-betaspike.appspot.com/rpc/gameplay/getObjectsInCells");
			Log.d("DLWCC", "payload: " + payload);
			
			doActualWork(uri, 
					payload,
					xsrfToken, cookie.toString());
		}

		return null;
	}

	private long readHelper(Readable paramReadable, Appendable paramAppendable)
			throws IOException {
		CharBuffer localCharBuffer = CharBuffer.allocate(2048);
		long l = 0L;
		while (paramReadable.read(localCharBuffer) != -1) {
			localCharBuffer.flip();
			paramAppendable.append(localCharBuffer);
			Log.d("DLWOUT", "DLW__" + localCharBuffer);
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

	protected void onPostExecute(Object result) {
		allDone();
	}

	private String doActualWork(String uri, String payload, String xsrfToken,
			String cookie) {
		URL url;
		HttpURLConnection urlConn;
		DataOutputStream printout;
		try {
			// "https://m-dot-betaspike.appspot.com/rpc/gameplay/collectItemsFromPortal"
			// "https://m-dot-betaspike.appspot.com/rpc/playerUndecorated/getInventory"
			// "https://m-dot-betaspike.appspot.com/rpc/gameplay/getObjectsInCells"

			url = URI.create(uri).toURL();

			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setInstanceFollowRedirects(false);
			urlConn.setRequestMethod("POST");
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-Type",
					"application/json;charset=UTF-8");
			urlConn.setRequestProperty("Accept-Encoding", "gzip");
			urlConn.setRequestProperty("Host", "m-dot-betaspike.appspot.com");
			urlConn.setRequestProperty("User-Agent", "Nemesis (gzip)");
			urlConn.setRequestProperty("X-XsrfToken", xsrfToken);
			urlConn.setRequestProperty("Connection", "Keep-Alive");
			urlConn.setRequestProperty("Cookie", cookie);
			urlConn.setReadTimeout(50000);
			urlConn.setDoInput(true);

			urlConn.connect();

			printout = new DataOutputStream(urlConn.getOutputStream());
			printout.writeBytes(payload);
			printout.flush();
			printout.close();

			Log.d("DLWCC", "ResponseMsg: " + urlConn.getResponseMessage());

			GZIPInputStream v = new GZIPInputStream(urlConn.getInputStream());

			String outputString = readInputReaderToString(new InputStreamReader(
					v, "UTF-8"));

			v.close();
			urlConn.disconnect();
			
			return outputString;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void allDone() {}

}