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
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appspot.hps_movie.interviewEndpoint.InterviewEndpoint;
import com.appspot.hps_movie.interviewEndpoint.model.InterviewV1Dto;

public class CompanyInterviewActivity extends Activity {
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
		// final String companyPhase = i
		// .getStringExtra(CommonUtils.STRING_EXTRA_COMPANY_PHASE);
		wasRead = i.getExtras().getBoolean(
				CommonUtils.STRING_EXTRA_COMPANY_READ);
		 TextView tv = (TextView) findViewById(R.id.companyname);
		 tv.setText(companyName);
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

				 findViewById(R.id.companystatus).setVisibility(View.VISIBLE);
				// setAtmosphere();
				// setTime();
				// setCategory();
//				 FrameLayout layout =(FrameLayout)
//				 findViewById(R.id.companystatus);
//				 layout.setVisibility(View.VISIBLE);
//				 setText();
				//
				if (interview.getQuestionList() != null) {
					lv = (ListView) findViewById(R.id.listView2);
					lv.setAdapter(new InterviewAdapter(context, interview
							.getQuestionList()));
					lv.setVisibility(View.VISIBLE);
				}
			} else {
				findViewById(R.id.null_text).setVisibility(View.VISIBLE);
			}
		}

		// private void setCategory() {
		// // TODO 自動生成されたメソッド・スタブ
		// TextView text = (TextView) findViewById(R.id.categorytext);
		// String category;
		// if (interview.getCategory() == 0) {
		// category = getResources().getString(R.string.indivisual);
		// } else if (interview.getCategory() == 1) {
		// category = getResources().getString(R.string.group);
		// } else {
		// category = getResources().getString(R.string.group_dis);
		// }
		// text.setText(category);
		// }

		// private void setTime() {
		// // TODO 自動生成されたメソッド・スタブ
		// TextView text = (TextView) findViewById(R.id.timetext);
		// double score = interview.getDurationAvg();
		// String s = String.format(getText(R.string.companytime).toString(),
		// score);
		// text.setText(Html.fromHtml(s));
		// }
		//
		// private void setAtmosphere() {
		// // TODO 自動生成されたメソッド・スタブ
		// ProgressBar appaku = (ProgressBar) findViewById(R.id.appaku_bar);
		// appaku.setProgressDrawable(getResources().getDrawable(
		// R.drawable.my_progress));
		// int meter = (int) (100 * interview.getAtmosphereAvg() / 2);
		// appaku.setProgress(meter);
		// }
	}
}
