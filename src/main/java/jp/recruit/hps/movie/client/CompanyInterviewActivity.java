package jp.recruit.hps.movie.client;



import java.io.IOException;
import java.util.List;

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
	boolean wasRead;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_companypage);
			Intent i = getIntent();
			selectionKey = i
					.getStringExtra(CommonUtils.STRING_EXTRA_SELECTION_KEY);
			
			SharedPreferences pref = getSharedPreferences(
					CommonUtils.STRING_PREF_KEY, Activity.MODE_PRIVATE);
			userKey = pref.getString(CommonUtils.STRING_EXTRA_USER_KEY, null);
			final String companyName = i
					.getStringExtra(CommonUtils.STRING_EXTRA_FILE_NAME);
			final String companyPhase = i
					.getStringExtra(CommonUtils.STRING_EXTRA_COMPANY_PHASE);
			wasRead = i
					.getExtras().getBoolean(CommonUtils.STRING_EXTRA_COMPANY_READ);
			TextView tv = (TextView)findViewById(R.id.companyname);
			tv.setText(companyName);
			tv = (TextView)findViewById(R.id.companyphase);
			tv.setText(companyPhase);
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
				String query = queries[0];
				InterviewEndpoint endpoint = RemoteApi.getInterviewEndpoint();
				try {
					interview= endpoint
							.interviewV1EndPoint().getInterview(userKey, selectionKey, wasRead).execute();//.searchInterview(query).execute();

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
//					FrameLayout layout =(FrameLayout) findViewById(R.id.companystatus);
//					layout.setVisibility(View.VISIBLE);
//					setText();
//					
//					ListView lv = (ListView) findViewById(R.id.listView2);
//					lv.setAdapter(new InterviewAdapter(context, list));
//					lv.setVisibility(View.VISIBLE);
					
				}
			}
			
//			//Textにセット
//			private void setText() {
//				// TODO 自動生成されたメソッド・スタブ
//				//Atmosphereをセット
//				for(int i=0;i<TEXT_VIEWS.length;i++){
//					TextView text = (TextView)findViewById(TEXT_VIEWS[i]);
//					int score = checkAtmosphere(i);
//					String s = String.format(getText(R.string.atomosphereMeter).toString(), score);
//					text.setText(Html.fromHtml(s));
//				}
//				//面接時間をセット
//					TextView text = (TextView)findViewById(R.id.timetext);
//					int score = checkCompanyTime();
//					String s = String.format(getText(R.string.companytime).toString(), score);
//					text.setText(Html.fromHtml(s));
//				//面接形式をセット
//					text = (TextView)findViewById(R.id.categorytext);
//					String style = checkCompanyCategory();
//					s = String.format(getText(R.string.companycategory).toString(), style);
//					text.setText(Html.fromHtml(s));
//			}

		}

}
