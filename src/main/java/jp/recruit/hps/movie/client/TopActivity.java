package jp.recruit.hps.movie.client;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.services.UpdateClockService;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.CompanyAdapter;
import jp.recruit.hps.movie.client.utils.CompanyPreferences;
import jp.recruit.hps.movie.client.utils.PopularCompanyAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.hps_movie.companyEndpoint.CompanyEndpoint;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1Dto;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1DtoCollection;
import com.google.analytics.tracking.android.EasyTracker;

public class TopActivity extends HPSActivity {
	GetCompanyListAsyncTask mLoadSelectionTask;
	GetNewCompanyListAsyncTask mLoadPopularTask;
	String userKey;
	CompanyAdapter adapter;
	PopularCompanyAdapter newAdapter;
	TextView pointText;
	boolean newList = false;
	boolean isLoaded = false;
	boolean isNewLoaded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mypage);
		findViewById(R.id.mypage_mail).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 自動生成されたメソッド・スタブ
						String Model = Build.MODEL;
						int VERSION_SDK = Build.VERSION.SDK_INT;

						Uri uri = Uri.parse("mailto:" + getResources().getString(R.string.mail_to));
						Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
						intent.putExtra(Intent.EXTRA_SUBJECT,
								getResources().getString(R.string.title));
						intent.putExtra(Intent.EXTRA_TEXT,
								getResources().getString(R.string.sendrequest_app)
										+ "VERSION.SDK:" + VERSION_SDK + "\nModel:"
										+ Model);
						startActivity(intent);
					}
				});
		
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

		findViewById(R.id.changebtn).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 自動生成されたメソッド・スタブ
						if (newList) {
							newList = false;
							if (isLoaded) {
								findViewById(R.id.mypage_new_company_list)
										.setVisibility(View.GONE);
								findViewById(R.id.mypage_company_list)
										.setVisibility(View.VISIBLE);
								ImageView img = (ImageView) findViewById(R.id.changebtn);
								img.setImageResource(R.drawable.design_btn_poplar);
							}
						} else {
							newList = true;
							if (isNewLoaded) {
								findViewById(R.id.mypage_company_list)
										.setVisibility(View.GONE);
								findViewById(R.id.mypage_new_company_list)
										.setVisibility(View.VISIBLE);
								ImageView img = (ImageView) findViewById(R.id.changebtn);
								img.setImageResource(R.drawable.design_btn_myphase);

							}
						}

					}
				});

	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
		UpdateClockService.stopResidentIfActive(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		new UpdateClockService().startResident(this);

		// setNowTime();
		checkCompanyTime();
		getUserKey();
		if (userKey == null) {
			startActivity(new Intent(this, MainActivity.class));
			Toast.makeText(this, R.string.mypage_userkey_error,
					Toast.LENGTH_SHORT).show();
			finish();
		}
		// new GetPointListAsyncTask()
		// .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		findViewById(R.id.mypage_company_null_text).setVisibility(View.GONE);
		findViewById(R.id.mypage_progressBar).setVisibility(View.VISIBLE);
		mLoadSelectionTask = new GetCompanyListAsyncTask(TopActivity.this);
		mLoadSelectionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				userKey);
		mLoadPopularTask = new GetNewCompanyListAsyncTask(TopActivity.this);
		mLoadPopularTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				userKey);
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

	// public void setNowTime() {
	// TextView dateText = (TextView) findViewById(R.id.nowtime);
	// dateText.setText(sdf.format(new Date()));
	// }

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
				isLoaded = true;
				ListView lv = (ListView) findViewById(R.id.mypage_company_list);
				adapter = new CompanyAdapter(context, list);
				lv.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				CompanyPreferences.setCompanyData(TopActivity.this, list);
				if (!newList) {
					ProgressBar prog = (ProgressBar) findViewById(R.id.mypage_progressBar);
					prog.setVisibility(View.GONE);
					lv.setVisibility(View.VISIBLE);
				}

			} else {
				findViewById(R.id.mypage_progressBar).setVisibility(View.GONE);
				findViewById(R.id.mypage_company_null_text).setVisibility(
						View.VISIBLE);
				newList = true;
				findViewById(R.id.changebtn).setVisibility(View.INVISIBLE);
			}

		}
	}

	public class GetNewCompanyListAsyncTask extends
			AsyncTask<String, Integer, Boolean> {
		private final Context context;
		private List<CompanyV1Dto> list;

		public GetNewCompanyListAsyncTask(Context context) {
			this.context = context;
		}

		@Override
		protected Boolean doInBackground(String... queries) {
			CompanyEndpoint endpoint = RemoteApi.getCompanyEndpoint();
			try {
				CompanyV1DtoCollection collection = endpoint
						.companyV1EndPoint().getPopularCompanyList().execute();
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
				isNewLoaded = true;
				ListView lv = (ListView) findViewById(R.id.mypage_new_company_list);
				newAdapter = new PopularCompanyAdapter(context, list);
				lv.setAdapter(newAdapter);
				newAdapter.notifyDataSetChanged();
				if (newList) {
					ProgressBar prog = (ProgressBar) findViewById(R.id.mypage_progressBar);
					prog.setVisibility(View.GONE);
					lv.setVisibility(View.VISIBLE);
				}
			} else {
				findViewById(R.id.mypage_progressBar).setVisibility(View.GONE);
				findViewById(R.id.mypage_company_null_text).setVisibility(
						View.VISIBLE);
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

}
