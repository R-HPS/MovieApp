package jp.recruit.hps.movie.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.recruit.hps.movie.client.task.DialogAsyncTask;
import jp.recruit.hps.movie.client.utils.AWSUtils;
import jp.recruit.hps.movie.client.utils.FileUtils;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.VideoView;

import com.amazonaws.services.s3.model.S3Object;

public class VideoActivity extends Activity {
	private VideoView mVideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_video);

		Intent i = getIntent();
		mVideo = (VideoView) findViewById(R.id.videoView1);
		new DownloadAndShowVideoAsyncTask(this).execute(
				i.getStringExtra("USER_KEY"), i.getStringExtra("FILE_NAME"));
	}

	public VideoView getVideo() {
		return mVideo;
	}

	class DownloadAndShowVideoAsyncTask extends DialogAsyncTask {
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
			File mFile = FileUtils.createNewFile("tmp_"
					+ System.currentTimeMillis() + ".mp4");
			double fileSize = object.getObjectMetadata().getContentLength();
			double totalByteRead = 0.0d;
			try {
				FileOutputStream fos = new FileOutputStream(mFile);
				byte[] buf = new byte[4000];
				int length;
				while ((length = is.read(buf)) != -1) {
					fos.write(buf);
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
				VideoView video = ((VideoActivity) context).getVideo();
				video.setVideoPath(mFile.getAbsolutePath());
				video.start();
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			this.cancel(true);
		}
	}
}
