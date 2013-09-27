package jp.recruit.hps.movie.client;

import jp.recruit.hps.movie.client.utils.CommonUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(sharedCheck()){
			Intent intent = new Intent(this, TopActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
		finish();
	}
	
	//SharedPreferencesにUserKeyがあるかを調べる。（あったときはログインをSKIP）
	private boolean sharedCheck() {
		// TODO 自動生成されたメソッド・スタブ
		SharedPreferences pref = getSharedPreferences(CommonUtils.STRING_PREF_KEY, Activity.MODE_PRIVATE);
		String key = pref.getString(CommonUtils.STRING_EXTRA_USER_KEY,null);
		if(key!=null){
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
