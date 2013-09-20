package jp.recruit.hps.movie.client;

import java.io.IOException;
import java.util.List;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.CompanyAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.appspot.hps_movie.companyEndpoint.CompanyEndpoint;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1Dto;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1DtoCollection;

public class TopActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mypage);
		new GetCompanyListAsyncTask(this).execute(CommonUtils.TEST_USER_KEY);
//		findViewById(R.id.search_button).setOnClickListener(
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						onSearchRequested();
//					}
//				});
	}
//	public void setList(List<CompanyV1Dto> list){
//		ListView lv = (ListView) findViewById(R.id.company_list);
//		lv.setAdapter(new CompanyAdapter(this, list));]
//		lv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int number, long arg3) {
//				// TODO 自動生成されたメソッド・スタブ
//				Intent intent = new Intent(TopActivity.this, CompanyInterviewActivity.class);
//				intent.putExtra(CommonUtils.STRING_EXTRA_USER_KEY, list.get(number).getKey());
//				intent.putExtra(CommonUtils.STRING_EXTRA_FILE_NAME, list.get(number).getName());
//				startActivity(intent);
//					
//			}
//		});
//	}
//CommonUtils.sampleUserKey
//	@Override
//	protected void onNewIntent(Intent intent) {
//		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//			String query = intent.getStringExtra(SearchManager.QUERY);
//			new GetCompanyListAsyncTask(this).execute(query);
//		}
//	}

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
			CompanyEndpoint endpoint = RemoteApi.getCompanyEndpoint();
			try {
				CompanyV1DtoCollection collection = endpoint
						.companyV1EndPoint().searchCompany(query).execute();
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
				ListView lv = (ListView) findViewById(R.id.listView1);
				lv.setAdapter(new CompanyAdapter(context, list));
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int number, long arg3) {
						// TODO 自動生成されたメソッド・スタブ
						Intent intent = new Intent(TopActivity.this, CompanyInterviewActivity.class);
						intent.putExtra(CommonUtils.STRING_EXTRA_USER_KEY, list.get(number).getKey());
						intent.putExtra(CommonUtils.STRING_EXTRA_FILE_NAME, list.get(number).getName());
						startActivity(intent);
							
					}
				});
			}
		}
	}
}
