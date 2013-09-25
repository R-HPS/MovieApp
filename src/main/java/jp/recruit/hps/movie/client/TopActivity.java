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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appspot.hps_movie.selectionEndpoint.model.CompanyV1Dto;
import com.appspot.hps_movie.selectionEndpoint.model.CompanyV1DtoCollection;
import com.appspot.hps_movie.selectionEndpoint.SelectionEndpoint;

public class TopActivity extends Activity {
	private final String PREF_KEY = "comapanydate1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_mypage);
		
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		setNowTime();
		checkCompanyTime();
		new GetCompanyListAsyncTask(this).execute(CommonUtils.TEST_USER_KEY);
	}

	private void checkCompanyTime() {
		// TODO 自動生成されたメソッド・スタブ
	    // SharedPrefernces の取得
		SharedPreferences pref = getSharedPreferences(CommonUtils.STRING_PREF_KEY, MODE_PRIVATE);
		Calendar cal =Calendar.getInstance();
		if(pref!=null){
			Long l = pref.getLong(PREF_KEY, -1);
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
//		Time time = new Time("Asia/Tokyo");
//	    time.setToNow();
//	    String date = time.year + "年" + (time.month+1) + "月" + time.monthDay + "日 " +
//	            time.hour + "時" + time.minute + "分";
//	    dateText.setText(date);
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
			}
		}
	}
}
