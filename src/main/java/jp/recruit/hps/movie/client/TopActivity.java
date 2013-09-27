package jp.recruit.hps.movie.client;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.CompanyAdapter;
import jp.recruit.hps.movie.client.utils.CompanyPriferences;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.hps_movie.selectionEndpoint.model.CompanyV1Dto;
import com.appspot.hps_movie.selectionEndpoint.model.CompanyV1DtoCollection;
import com.appspot.hps_movie.selectionEndpoint.SelectionEndpoint;

public class TopActivity extends Activity {
	GetCompanyListAsyncTask mLoadSelectionTask;
	String userkey;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_mypage);
		findViewById(R.id.newregistbtn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				Intent i =  new Intent(TopActivity.this,RegisterCompanyActivity.class);
				startActivity(i);
			}
		});
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		setNowTime();
		checkCompanyTime();
		String userKey =getUserKey();
		if(userKey==null){
			startActivity(new Intent(this,LoginActivity.class));
			Toast.makeText( this, R.string.mypage_userkey_error, Toast.LENGTH_SHORT ).show();
			finish();
		}
		mLoadSelectionTask = new GetCompanyListAsyncTask(this);
		mLoadSelectionTask.execute(CommonUtils.TEST_USER_KEY);//userKey
	}

	private String getUserKey() {
		// TODO 自動生成されたメソッド・スタブ
		SharedPreferences pref = getSharedPreferences(CommonUtils.STRING_PREF_KEY, Activity.MODE_PRIVATE);
		return pref.getString(CommonUtils.STRING_EXTRA_USER_KEY,null);
	}

	private void checkCompanyTime() {
		// TODO 自動生成されたメソッド・スタブ
	    // SharedPrefernces の取得
		SharedPreferences pref = getSharedPreferences(CommonUtils.STRING_PREF_KEY, MODE_PRIVATE);
		Calendar cal =Calendar.getInstance();
		if(pref!=null){
			Long l = pref.getLong(CommonUtils.STRING_COMPANY_DATE_FIRST, -1);
			if(l!=-1){
				cal.setTimeInMillis(l);
				
			}
		}
	}

	private void setNowTime() {
		// TODO 自動生成されたメソッド・スタブ
		TextView dateText = (TextView)findViewById(R.id.nowtime);
		// デフォルトのCalendarオブジェクト
	    Calendar cal = Calendar.getInstance();
	 // Calendarクラスによる現在時表示
	    String tmp =cal.get(Calendar.YEAR) + "年" 
	            + (cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DATE)
	            + "日 " + cal.get(Calendar.HOUR_OF_DAY) + "時"
	            + cal.get(Calendar.MINUTE) + "分";
	    dateText.setText(tmp);
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
			SelectionEndpoint endpoint = RemoteApi.getSelectionEndpoint();
			try {
				CompanyV1DtoCollection collection = endpoint
						.selectionV1EndPoint().getSelections(query).execute();
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
			}else {
				findViewById(R.id.mypage_progressBar).setVisibility(View.GONE);
				findViewById(R.id.mypage_company_null_text).setVisibility(View.VISIBLE);
			}
		}
	}
}
