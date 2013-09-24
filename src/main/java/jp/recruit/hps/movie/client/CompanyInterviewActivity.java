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
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appspot.hps_movie.interviewEndpoint.InterviewEndpoint;
import com.appspot.hps_movie.interviewEndpoint.model.InterviewV1Dto;
import com.appspot.hps_movie.interviewEndpoint.model.InterviewV1DtoCollection;


public class CompanyInterviewActivity extends Activity {
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_companypage);
			Intent i = getIntent();
			final String interviewGroupKey = i
					.getStringExtra(CommonUtils.STRING_EXTRA_INTERVIEWGROUP_KEY);
			new GetInterviewListAsyncTask(this).execute(interviewGroupKey);
			final String companyName = i
					.getStringExtra(CommonUtils.STRING_EXTRA_FILE_NAME);
			final String companyPhase = i
					.getStringExtra(CommonUtils.String_EXTRA_COMPANY_PHASE);
			TextView tv = (TextView)findViewById(R.id.companyname);
			tv.setText(companyName);
			tv = (TextView)findViewById(R.id.companyphase);
			tv.setText(companyPhase);
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
					FrameLayout layout =(FrameLayout) findViewById(R.id.companystatus);
					layout.setVisibility(View.VISIBLE);
					setText();
					
					ListView lv = (ListView) findViewById(R.id.listView2);
					lv.setAdapter(new InterviewAdapter(context, list));
					lv.setVisibility(View.VISIBLE);
					
				}
			}
			final int TEXT_VIEWS[]={
				R.id.odayakapt,R.id.futuupt,R.id.appakupt
			};
			
			//Textにセット
			private void setText() {
				// TODO 自動生成されたメソッド・スタブ
				//Atmosphereをセット
				for(int i=0;i<TEXT_VIEWS.length;i++){
					TextView text = (TextView)findViewById(TEXT_VIEWS[i]);
					int score = checkAtmosphere(i);
					String s = String.format(getText(R.string.atomosphereMeter).toString(), score);
					text.setText(Html.fromHtml(s));
				}
				//面接時間をセット
					TextView text = (TextView)findViewById(R.id.timetext);
					int score = checkCompanyTime();
					String s = String.format(getText(R.string.companytime).toString(), score);
					text.setText(Html.fromHtml(s));
				//面接形式をセット
					text = (TextView)findViewById(R.id.categorytext);
					String style = checkCompanyCategory();
					s = String.format(getText(R.string.companycategory).toString(), style);
					text.setText(Html.fromHtml(s));
			}
			private String checkCompanyCategory() {
				// TODO 自動生成されたメソッド・スタブ
//				int scoreGroup =0;
//				int scoreIndivisual=0;
//				for(int i=0;i<list.size();i++){
//					if(list.get(i).getCategory().equals()){
//						scoreGroup++;
//					}else{
//						scoreIndivisual++;
//					}
//				}
				String s ="a";
				return s;
			}

			private int checkCompanyTime() {
				// TODO 自動生成されたメソッド・スタブ
				int score =0;
				for(int i=0;i<list.size();i++){
					int tmp = 1;//本来はここで面接時間の平均を書く
					
//					 score=score+list.get(i).getStartDate()
					 
					score = score+tmp;
				}
//				score = score/list.size();
				return score;
			}
			private final String ATMOSPHERES[]={
					"SUNNY", "CLOUDY", "RAINY"
			};
			//Atmosphereのそれぞれの値を取得
			private int checkAtmosphere(int i) {
				// TODO 自動生成されたメソッド・スタブ
				int score=0;
				for(int j=0;j<list.size();j++){
					if(list.get(j).getAtmosphere()!=null&&list.get(j).getAtmosphere().equals(ATMOSPHERES[i])==true){
					score++;
					}
				}
				return score;
			}
		}

}
