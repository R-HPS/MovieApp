package jp.recruit.hps.movie.client;



import java.io.IOException;
import java.util.List;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.InterviewAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appspot.hps_movie.interviewEndpoint.InterviewEndpoint;
import com.appspot.hps_movie.interviewEndpoint.model.InterviewV1Dto;
import com.appspot.hps_movie.interviewEndpoint.model.InterviewV1DtoCollection;


public class CompanyInterviewActivity extends Activity {
		private List<InterviewV1Dto> list;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_companypage);
			Intent i = getIntent();
			final String companyKey = i
					.getStringExtra(CommonUtils.STRING_EXTRA_USER_KEY);
			new GetInterviewListAsyncTask(this).execute(companyKey);
			final String companyName = i
					.getStringExtra(CommonUtils.STRING_EXTRA_FILE_NAME);
			TextView tv = (TextView)findViewById(R.id.companyname);
			tv.setText(companyName);
		}
		
		

		public class GetInterviewListAsyncTask extends
				AsyncTask<String, Integer, Boolean> {
			private final Context context;
			private List<InterviewV1Dto> list;

			public GetInterviewListAsyncTask(Context context) {
				this.context = context;
			}

			@Override
			protected Boolean doInBackground(String... queries) {
				String query = queries[0];
				InterviewEndpoint endpoint = RemoteApi.getInterviewEndpoint();
				try {
					InterviewV1DtoCollection collection = endpoint
							.interviewV1EndPoint().getInterviews(query).execute();//.searchInterview(query).execute();
					if (collection != null) {
						list = collection.getItems();
					} else {
						return false;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
					progressBar.setVisibility(View.GONE);
					LinearLayout layout =(LinearLayout) findViewById(R.id.companystatus);
					layout.setVisibility(View.VISIBLE);
					ListView lv = (ListView) findViewById(R.id.listView2);
					lv.setAdapter(new InterviewAdapter(context, list));
					
				}
			}
		}

}
