package jp.recruit.hps.movie.client;

import java.util.ArrayList;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appspot.hps_movie.movieEndpoint.MovieEndpoint;
import com.appspot.hps_movie.movieEndpoint.MovieEndpoint.MovieV1EndPoint.GetMovieList;
import com.appspot.hps_movie.movieEndpoint.model.MovieV1Dto;
import com.appspot.hps_movie.movieEndpoint.model.MovieV1DtoCollection;

public class TopActivity extends Activity {
	private String mTargetUserKey = CommonUtils.TEST_USER_KEY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_top);

		Button record_button = (Button) findViewById(R.id.go_to_record_from_top);
		record_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TopActivity.this,
						RecordActivity.class);
				startActivity(intent);
			}
		});
		Button video_button = (Button) findViewById(R.id.go_to_video_from_top);
		video_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new MovieTask().execute();
			}
		});
	}

	public class MovieTask extends AsyncTask<Void, Void, Boolean> {
		private ArrayList<String> mFileNameList = new ArrayList<String>();

		@Override
		protected Boolean doInBackground(Void... args) {

			try {
				MovieEndpoint endpoint = RemoteApi.getMovieEndpoint();
				GetMovieList getMovieList = endpoint.movieV1EndPoint()
						.getMovieList(mTargetUserKey);

				MovieV1DtoCollection result = getMovieList.execute();
				if (result.getItems() == null || result.getItems().size() == 0) {
					return false;
				}
				for (MovieV1Dto dto : result.getItems()) {
					mFileNameList.add(dto.getFileName());
				}
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				Intent intent = new Intent(TopActivity.this,
						VideoActivity.class);
				intent.putExtra(CommonUtils.STRING_EXTRA_USER_KEY,
						mTargetUserKey);
				intent.putExtra(CommonUtils.STRING_EXTRA_FILE_NAME,
						mFileNameList.get(0));
				startActivity(intent);
			}
		}
	}
}
