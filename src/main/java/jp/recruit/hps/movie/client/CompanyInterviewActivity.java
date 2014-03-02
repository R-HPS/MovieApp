package jp.recruit.hps.movie.client;

import java.io.IOException;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.InterviewAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appspot.hps_movie.interviewEndpoint.InterviewEndpoint;
import com.appspot.hps_movie.interviewEndpoint.model.InterviewV1Dto;
import com.google.analytics.tracking.android.EasyTracker;

public class CompanyInterviewActivity extends HPSActivity {
	String selectionKey;
	String userKey;
	ListView lv;
	boolean wasRead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_companypage);
		Intent i = getIntent();
		selectionKey = i.getStringExtra(CommonUtils.STRING_EXTRA_SELECTION_KEY);

		SharedPreferences pref = getSharedPreferences(
				CommonUtils.STRING_PREF_KEY, Activity.MODE_PRIVATE);
		userKey = pref.getString(CommonUtils.STRING_EXTRA_USER_KEY, null);
		final String companyName = i
				.getStringExtra(CommonUtils.STRING_EXTRA_FILE_NAME);

		wasRead = i.getExtras().getBoolean(
				CommonUtils.STRING_EXTRA_COMPANY_READ);
		TextView tv = (TextView) findViewById(R.id.company_title_text);
		tv.setText(companyName);
		ImageView cancelBtn = (ImageView) findViewById(R.id.companypage_cancelbtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CompanyInterviewActivity.this,
						TopActivity.class);
				startActivity(i);
			}
		});
		// tv = (TextView) findViewById(R.id.companyphase);
		// tv.setText(companyPhase);
		new GetInterviewListAsyncTask(this).execute(selectionKey);
	}

	public class GetInterviewListAsyncTask extends
			AsyncTask<String, Integer, Boolean> {
		private final Context context;
		private InterviewV1Dto interview;

		public GetInterviewListAsyncTask(Context context) {
			this.context = context;
		}

		@Override
		protected Boolean doInBackground(String... queries) {
			InterviewEndpoint endpoint = RemoteApi.getInterviewEndpoint();
			try {
				interview = endpoint.interviewV1EndPoint()
						.getInterview(userKey, selectionKey, wasRead).execute();// .searchInterview(query).execute();

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
			progressBar.setVisibility(View.GONE);
			if (result) {

				if (interview.getQuestionList() != null) {
					lv = (ListView) findViewById(R.id.listView2);
					lv.setAdapter(new InterviewAdapter(context, interview
							.getQuestionList()));
					lv.setVisibility(View.VISIBLE);
				}
			} else {
				findViewById(R.id.company_null_text)
						.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

}
