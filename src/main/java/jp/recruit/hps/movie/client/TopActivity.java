package jp.recruit.hps.movie.client;

import java.io.IOException;
import java.util.List;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.CompanyAdapter;
import jp.recruit.hps.movie.client.utils.CompanyPriferences;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.appspot.hps_movie.interviewGroupEndpoint.model.CompanyV1Dto;
import com.appspot.hps_movie.interviewGroupEndpoint.model.CompanyV1DtoCollection;
import com.appspot.hps_movie.interviewGroupEndpoint.InterviewGroupEndpoint;

public class TopActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mypage);
		new GetCompanyListAsyncTask(this).execute(CommonUtils.TEST_USER_KEY);
	}

	public class GetCompanyListAsyncTask extends
			AsyncTask<String, Integer, Boolean> {
		private final Context context;
		private List<CompanyV1Dto> list;

		public GetCompanyListAsyncTask(Context context) {
			this.context = context;
		}

		@Override
		protected Boolean doInBackground(String... queries) {
			String query = queries[0];
			InterviewGroupEndpoint endpoint = RemoteApi.getInterviewGroupEndpoint();
			try {
				CompanyV1DtoCollection collection = endpoint
						.interviewGroupV1EndPoint().getInterviewGroups(query).execute();
				if (collection != null && collection.getItems() != null) {
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
				ProgressBar prog = (ProgressBar)findViewById(R.id.mypage_progressBar);
				prog.setVisibility(View.GONE);
				ListView lv = (ListView) findViewById(R.id.mypage_company_list);
				lv.setAdapter(new CompanyAdapter(context, list));
				lv.setVisibility(View.VISIBLE);
				CompanyPriferences company = new CompanyPriferences(context,list);
				company.setCompanyData();
			}
		}
	}
}
