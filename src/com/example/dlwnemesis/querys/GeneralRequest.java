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
import com.example.dlwnemesis.elements.InvokeAbleElement;

import android.os.AsyncTask;
import android.util.Log;

public class GeneralRequest extends AsyncTask<Object, Void, Object> {
	private HttpCookie cookie = null;
	private String xsrfToken = null;
	private InvokeAbleElement element = null;
	
	public boolean killAll = false;

	/** HttpCookie,xsrfToken,InvokeAbleElement */
	protected Object doInBackground(Object... objs) {
		if (objs[0] instanceof HttpCookie) {
			cookie = (HttpCookie) objs[0];
		}
		if (objs[1] instanceof String) {
			xsrfToken = (String) objs[1];
		}
		if (objs[2] instanceof InvokeAbleElement) {
			element = (InvokeAbleElement) objs[2];

		}

		if ((cookie != null) && (xsrfToken != null) && (element != null)) {
			//Log.d("DLWCC", "uri: " + element.getUri());
			//Log.d("DLWCC", "payload: " + element.getPayLoad());
			
			element.parseContent(doActualWork(element.getUri(), element.getPayLoad(),
					xsrfToken, cookie.toString()));
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
			//Log.d("DLWCC", "DLW__" + localCharBuffer);
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
		if (!killAll) {
			allDone();
		}
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
					
					//"{\"params\":{\"clientBasket\":{\"clientBlob\":\"6ZTuSqqxlbr2WRLwAT+Ityie1D14vTVKuMH3xb4aVnmQ26yHdbB3C4G/KKeSfn8RsoymWuvvHwnouy86De10vGvaC0ug54IbONrrDPpCLZHSZ0Uw3PDqxh7U9Lt7HlHGCvNjG1i1M7PcnJeEd9QwybncPbHWa/FneE3E7lji+8/i1eper+rCFE/jxSHZn4OoOAfLeb7/1cYM+ecEHxtZO9yAgW2929L8LrZ42XSjcok28QNrus/tAwtd5IvN3NF/rdmRyy1eTvCXvE1RPURX6MOw0QYDnJAW8dyfIfBEqOIXeR/gqSHlj5vp1/CvuYaEquZtUIycRvrl+dAH9gfK7A\"},\"energyGlobGuids\":[],\"itemGuid\":\"1f9315a72b0742549326c71860c01953.11\",\"knobSyncTimestamp\":0,\"playerLocation\":\"0319c063,00821be3\"}}");

			// Sehr Wichtig
			// "{\"params\":{\"cells\":null,\"cellsAsHex\":[\"47ba3d1a50000000\",\"47ba3d1950000000\",\"47ba3d1850000000\",\"47ba3d1790000000\",\"47ba3d1970000000\",\"47ba3d1870000000\",\"47ba3d1770000000\",\"47ba3d1990000000\",\"47ba3d1890000000\",\"47ba3d1bb0000000\",\"47ba3d1750000000\",\"47ba3d19b0000000\",\"47ba3d18b0000000\",\"47ba3d1bd0000000\",\"47ba3d19d0000000\",\"47ba3d18d0000000\",\"47ba3d1bf0000000\",\"47ba3d19f0000000\",\"47ba3d18f0000000\",\"47ba3d1a10000000\",\"47ba3d1910000000\",\"47ba3d1810000000\",\"47ba3d1a30000000\",\"47ba3d1930000000\",\"47ba3d1830000000\"],\"clientBasket\":{\"clientBlob\":null},\"dates\":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],\"energyGlobGuids\":[],\"knobSyncTimestamp\":0,\"playerLocation\":\"0319bcf1,008219f8\"}}");

			// ????
			// "{\"params\":{\"cells\":null,\"cellsAsHex\":[\"47ba3d1a70000000\",\"47ba3d1970000000\",\"47ba3d1870000000\",\"47ba3d10b0000000\",\"47ba3d1770000000\",\"47ba3d1bb0000000\",\"47ba3d19b0000000\",\"47ba3d1bf0000000\",\"47ba3d19f0000000\",\"47ba3d18f0000000\",\"47ba3d1a30000000\",\"47ba3d1930000000\",\"47ba3d1830000000\",\"47ba3d1a50000000\",\"47ba3d1950000000\",\"47ba3d1850000000\",\"47ba3d1790000000\",\"47ba3d1b90000000\",\"47ba3d1990000000\",\"47ba3d1890000000\",\"47ba3d1750000000\",\"47ba3d1bd0000000\",\"47ba3d19d0000000\",\"47ba3d18d0000000\",\"47ba3d1a10000000\",\"47ba3d1910000000\",\"47ba3d1810000000\"],\"clientBasket\":{\"clientBlob\":null},\"dates\":[1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849,1367933687849],\"energyGlobGuids\":[],\"knobSyncTimestamp\":0,\"playerLocation\":\"0319be53,00821e4a\"}}");

			// GEHT!
			// "{\"params\":{\"cellsAsHex\":[\"47ba3d1a70000000\",\"47ba3d1970000000\",\"47ba3d10b0000000\",\"47ba3d1770000000\",\"47ba3d1bb0000000\",\"47ba3d19b0000000\",\"47ba3d1070000000\",\"47ba3d1730000000\",\"47ba3d1af0000000\",\"47ba3d19f0000000\",\"47ba3d1a30000000\",\"47ba3d1830000000\",\"47ba3d10f0000000\",\"47ba3d1a50000000\",\"47ba3d1850000000\",\"47ba3d1090000000\",\"47ba3d1790000000\",\"47ba3d1a90000000\",\"47ba3d1990000000\",\"47ba3d1050000000\",\"47ba3d1750000000\",\"47ba3d1bd0000000\",\"47ba3d19d0000000\",\"47ba3d1710000000\",\"47ba3d1b10000000\",\"47ba3d1a10000000\",\"47ba3d1910000000\",\"47ba3d10d0000000\"],\"dates\":[1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276],\"knobSyncTimestamp\":0,\"playerLocation\":\"0319c3ce,00822836\"}}");

			// HACK PORTAL
			// "{\"params\":{\"energyGlobGuids\":[],\"itemGuid\":\"6539233fac304258b5131e72b1c37a4e.11\",\"knobSyncTimestamp\":1367860491751,\"playerLocation\":\"0319bfc9,00821b16\"}}");

			// "{\"params\":{\"\"playerLocation\":\"0319c3ce,00822836\",\"knobSyncTimestamp\":0,\"itemGuid\":\"476f7db181644a1b81b6724ec65511a1.11\"}}");

			// \"hKrtSJTlzZi9LwLSTkL/tELl2HZewnUR2L3C8/U4KDOArYuyK91UNo7qcL2QDxkhyp/jCNSaTGrM4BpObeB5zUqKX07an79TWo25ZIhgItnAZ2Rd/fij+3Xqts5OEhDhDroeBx6wHLGxpb/vI6Uu6pCoUd3haIZkTCLqihTXlIze5+M+p4CUETO67WeO9+P0XlDyMLvoyYsvzdoYP0UjH7fvxXTtiLf/I/dy633Qbbtn7TdAgpi4dR598MX1hbJh6fLwtCN6Kof37VI9aScT6oq6klRWqso1mpe2DYZos6xxbw3JmjDikdKNr8j1z4/Snr0sT+W9dfjpuI5YvUfu1Q\"
			// \"clientBasket\":{\"clientBlob\":},

			// "{\"params\":{\"lastQueryTimestamp\":0}}");
			// "{\"params\":{\"cellsAsHex\":[\"47ba3d1a70000000\",\"47ba3d1970000000\",\"47ba3d10b0000000\",\"47ba3d1770000000\",\"47ba3d1bb0000000\",\"47ba3d19b0000000\",\"47ba3d1070000000\",\"47ba3d1730000000\",\"47ba3d1af0000000\",\"47ba3d19f0000000\",\"47ba3d1a30000000\",\"47ba3d1830000000\",\"47ba3d10f0000000\",\"47ba3d1a50000000\",\"47ba3d1850000000\",\"47ba3d1090000000\",\"47ba3d1790000000\",\"47ba3d1a90000000\",\"47ba3d1990000000\",\"47ba3d1050000000\",\"47ba3d1750000000\",\"47ba3d1bd0000000\",\"47ba3d19d0000000\",\"47ba3d1710000000\",\"47ba3d1b10000000\",\"47ba3d1a10000000\",\"47ba3d1910000000\",\"47ba3d10d0000000\"],\"dates\":[1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276,1367685931276],\"knobSyncTimestamp\":0,\"playerLocation\":\"0319c3ce,00822836\"}}");
			printout.flush();
			printout.close();

			// ["476f7db181644a1b81b6724ec65511a1.11",1367857067040,{"locationE6":{"latE6":52018389,"lngE6":8526939}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void allDone() {}

}