package com.cnsunway.saas.wash.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cnsunway.saas.wash.cnst.Const;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class FileDownLoadHelper extends Thread {

	String url;
	Handler handle;
	Context context;
	boolean is_cancel = false;
	String filePath;

	public FileDownLoadHelper(Context context, String url, Handler handle,
							  String filePath) {
		this.context = context;
		this.handle = handle;
		this.url = url;
		is_cancel = false;
		this.filePath = filePath;

	}

	public void cancel() {
		is_cancel = true;
	}

	@Override
	public void run() {
		try {
			handle.sendEmptyMessage(Const.Message.MSG_DOWNLOAD_START);
			URL url = new URL(this.url);
			String filename = this.url.substring(this.url.lastIndexOf("/") + 1);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(100 * 10000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", this.url);
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			try {
				conn.connect();
			} catch (Exception e) {
				// TODO: handle exception
			}
			int length = conn.getContentLength();
			BigDecimal bigLength = new BigDecimal(length);
			BigDecimal b = new BigDecimal(100);
			InputStream is = conn.getInputStream();
			File file = new File(filePath, filename);
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream fos = new FileOutputStream(file);
			int len = 0;
			long count = 0;
			byte buf[] = new byte[1024*1024];
			while (((len = is.read(buf)) != -1) && (!is_cancel)) {
				count += len;

				if (handle != null) {
					int progress = new BigDecimal(count).divide(bigLength,10,BigDecimal.ROUND_HALF_DOWN).multiply(b).intValue();
					handle.sendEmptyMessage(Const.Message.MSG_DOWNLOAD);
					Message message = new Message();
					message.what = Const.Message.MSG_DOWNLOAD;
					message.arg1 = progress;
					handle.sendMessage(message);
				}

				fos.write(buf, 0, len);
			}
			fos.close();
			is.close();
			if (handle != null) {
				handle.sendEmptyMessage(Const.Message.MSG_DOWNLOAD_FINISH);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (handle != null) {
				handle.sendEmptyMessage(Const.Message.MSG_DOWNLOAD_FAIL);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (handle != null) {
				handle.sendEmptyMessage(Const.Message.MSG_DOWNLOAD_FAIL);
			}
		}
	}

}
