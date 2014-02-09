package jp.recruit.hps.movie.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.services.UpdateClockService;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.CompanyAdapter;
import jp.recruit.hps.movie.client.utils.CompanyPreferences;
import jp.recruit.hps.movie.client.utils.UpdateClockReceiver;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.hps_movie.companyEndpoint.CompanyEndpoint;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1Dto;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1DtoCollection;
import com.appspot.hps_movie.userEndpoint.UserEndpoint;
import com.appspot.hps_movie.userEndpoint.model.PointV1Dto;

public class TopActivity extends HPSActivity {
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy年MM月dd日HH時mm分", Locale.JAPAN);
	GetCompanyListAsyncTask mLoadSelectionTask;
	String userKey;
	CompanyAdapter adapter;
	TextView pointText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UpdateClockReceiver receiver = new UpdateClockReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("UPDATE_CLOCK");
		registerReceiver(receiver, intentFilter);

		setContentView(R.layout.activity_mypage);
		pointText = (TextView)findViewById(R.id.nowpoint);
		findViewById(R.id.newregistbtn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 自動生成されたメソッド・スタブ
						Intent i = new Intent(TopActivity.this,
								RegisterCompanyActivity.class);
						startActivity(i);
					}
				});
	}

	@Override
	protected void onStop() {
		super.onStop();
		UpdateClockService.stopResidentIfActive(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		new UpdateClockService().startResident(this);

		setNowTime();
		checkCompanyTime();
		getUserKey();
		if (userKey == null) {
			startActivity(new Intent(this, LoginActivity.class));
			Toast.makeText(this, R.string.mypage_userkey_error,
					Toast.LENGTH_SHORT).show();
			finish();
		}
		new GetPointListAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		findViewById(R.id.mypage_company_null_text).setVisibility(View.GONE);
		findViewById(R.id.mypage_progressBar).setVisibility(View.VISIBLE);
		mLoadSelectionTask = new GetCompanyListAsyncTask(TopActivity.this);
		mLoadSelectionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,userKey);
	}

	private void getUserKey() {
		// TODO 自動生成されたメソッド・スタブ
		SharedPreferences pref = getSharedPreferences(
				CommonUtils.STRING_PREF_KEY, Activity.MODE_PRIVATE);
		userKey = pref.getString(CommonUtils.STRING_EXTRA_USER_KEY, null);
	}

	private void checkCompanyTime() {
		// TODO 自動生成されたメソッド・スタブ
		// SharedPrefernces の取得
		SharedPreferences pref = getSharedPreferences(
				CommonUtils.STRING_PREF_KEY, MODE_PRIVATE);
		Calendar cal = Calendar.getInstance();
		if (pref != null) {
			Long l = pref.getLong(CommonUtils.STRING_COMPANY_DATE_FIRST, -1);
			if (l != -1) {
				cal.setTimeInMillis(l);

			}
		}
	}

	public void setNowTime() {
		TextView dateText = (TextView) findViewById(R.id.nowtime);
		dateText.setText(sdf.format(new Date()));
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
			CompanyEndpoint endpoint = RemoteApi.getCompanyEndpoint();
			try {
				CompanyV1DtoCollection collection = endpoint
						.companyV1EndPoint().getCompanyList(query).execute();
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

				CompanyPreferences.setCompanyData(TopActivity.this, list);
				ProgressBar prog = (ProgressBar) findViewById(R.id.mypage_progressBar);
				prog.setVisibility(View.GONE);
				ListView lv = (ListView) findViewById(R.id.mypage_company_list);
				adapter = new CompanyAdapter(context, list);
				lv.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				lv.setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.mypage_progressBar).setVisibility(View.GONE);
				findViewById(R.id.mypage_company_null_text).setVisibility(
						View.VISIBLE);
			}
			
		}
	}

	public class GetPointListAsyncTask extends
			AsyncTask<String, Integer, Boolean> {
		private PointV1Dto point;

		@Override
		protected Boolean doInBackground(String... queries) {
			UserEndpoint endpoint = RemoteApi.getUserEndpoint();
			try {
				PointV1Dto collection = endpoint
						.userV1Endpoint().login(userKey).execute();
				if (collection != null) {
					point = collection;
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
					
					pointText.setText(point.getValue().toString());

			} else {
			}
		}
	}
}
