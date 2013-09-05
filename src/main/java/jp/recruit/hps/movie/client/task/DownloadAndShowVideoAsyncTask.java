package jp.recruit.hps.movie.client.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.recruit.hps.movie.client.VideoActivity;
import jp.recruit.hps.movie.client.utils.AWSUtils;
import jp.recruit.hps.movie.client.utils.FileUtils;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.VideoView;

import com.amazonaws.services.s3.model.S3Object;

public class DownloadAndShowVideoAsyncTask extends DialogAsyncTask {
	private File mFile;

	public DownloadAndShowVideoAsyncTask(Context context) {
		super(context);
		this.title = "ダウンロード中";
		this.message = "動画をダウンロードしています...";
	}

	@Override
	protected Boolean doInBackground(Object... args) {
		if (args.length < 2) {
			return false;
		}
		String userKey = (String) args[0];
		String fileName = (String) args[1];
		S3Object object = AWSUtils.downloadFromS3(userKey, fileName);
		InputStream is = object.getObjectContent();
		mFile = FileUtils.createNewFile("tmp_"
				+ System.currentTimeMillis() + ".3gp");
		double fileSize = object.getObjectMetadata().getContentLength();
		double totalByteRead = 0.0d;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(mFile);
			byte[] buf = new byte[4000];
			int length;
			while ((length = is.read(buf)) != -1) {
				fos.write(buf, 0, length);
				totalByteRead += length;
				publishProgress((int) ((totalByteRead / fileSize) * 100));
			}
		} catch (FileNotFoundException e) {
			Log.d("Error", e.getMessage());
			return false;
		} catch (IOException e) {
			Log.d("Error", e.getMessage());
			return false;
		} finally {
			try {
				is.close();
				fos.close();
			} catch (IOException e) {
				Log.d("Error", e.getMessage());
			}
		}
		return true;
	}

	@Override
	public void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			VideoView  video = ((VideoActivity) context).getVideo();
			video.setVideoPath(mFile.getAbsolutePath());
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		this.cancel(true);
	}
}
