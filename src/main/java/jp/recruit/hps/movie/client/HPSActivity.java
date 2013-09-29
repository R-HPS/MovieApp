package jp.recruit.hps.movie.client;

import java.util.Calendar;

import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.CompanyPreferences;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class HPSActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		int current = -1;
		
	for (int i = 0; i < 3; i++) {
			if (CompanyPreferences.getTime(i) != -1l
					&& CompanyPreferences.getTime(i) < System
							.currentTimeMillis()) {
				current = i;
			}
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(cal.getTimeInMillis() + 24 * 3600 * 1000);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (current != -1
				&& CompanyPreferences.getTime(current) < cal.getTimeInMillis()) {
			Intent intent = new Intent(this, RegisterInterviewActivity.class);
			intent.putExtra(CommonUtils.STRING_EXTRA_SELECTION_KEY,
					CompanyPreferences.getKey(current));
			intent.putExtra(CommonUtils.STRING_EXTRA_COMPANY_NAME, 
					CompanyPreferences.getName(current));
			CompanyPreferences.cleanPref(current);
			startActivity(intent);
			finish();
		}
	}

}
