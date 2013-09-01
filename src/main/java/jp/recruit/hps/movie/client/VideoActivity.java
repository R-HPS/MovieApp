package jp.recruit.hps.movie.client;

import jp.recruit.hps.movie.client.task.DownloadAndShowVideoAsyncTask;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.VideoView;

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
}
