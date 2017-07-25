package com.cnsunway.wash.framework.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class InstallUtil {

	public static void openApkFile(Context activity, String fileStr) {
		if (fileStr != null) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			File file = new File(fileStr);
			if (file.exists()) {
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				activity.startActivity(intent);
			}
		}
	}

}
