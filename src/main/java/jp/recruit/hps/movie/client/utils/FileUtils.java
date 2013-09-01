package jp.recruit.hps.movie.client.utils;

import java.io.File;

import android.os.Environment;

public class FileUtils {
	public static File createNewFile(String fileName) {
		File dir = Environment.getExternalStorageDirectory();
		File appDir = new File(dir, "HPSMovie");

		if (!appDir.exists()) {
			appDir.mkdir();
		}

		return new File(appDir, fileName);
	}
}
