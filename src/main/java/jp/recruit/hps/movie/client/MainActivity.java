package jp.recruit.hps.movie.client;

import com.appspot.hps_movie.loginEndpoint.LoginEndpoint;
import com.appspot.hps_movie.loginEndpoint.LoginEndpoint.LoginV1Endpoint.Login;
import com.appspot.hps_movie.loginEndpoint.model.LoginResultV1Dto;
import com.appspot.hps_movie.registerEndpoint.RegisterEndpoint;
import com.appspot.hps_movie.registerEndpoint.RegisterEndpoint.RegisterV1Endpoint.Register;
import com.appspot.hps_movie.registerEndpoint.model.RegisterResultV1Dto;
import com.google.analytics.tracking.android.EasyTracker;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.CompanyPreferences;
import jp.recruit.hps.movie.client.utils.Installation;
import jp.recruit.hps.movie.common.CommonConstant;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	String mPassword = "hpsmovies";
	private UserLoginTask mAuthTask = null;
	private UserRegisterTask mAuthTask2 = null;
	private String mEmail;
	private static String SUCCESS = CommonConstant.SUCCESS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new CompanyPreferences();
		if(sharedCheck()){
			Intent intent = new Intent(this,TopActivity.class);
			startActivity(intent);
		}else{
			new Installation();
			mEmail = Installation.id(this);
			mEmail += "@test.ac.jp";
			mAuthTask2 = new UserRegisterTask();
			mAuthTask2.execute((Void) null);
		}
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

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		

		@Override
		protected Boolean doInBackground(Void... args) {

			try {
				LoginEndpoint endpoint = RemoteApi.getLoginEndpoint();
				Login login = endpoint.loginV1Endpoint().login(mEmail,
						mPassword);
				
				LoginResultV1Dto result = login.execute();
			

				if (SUCCESS.equals(result.getResult())) {
					//ログイン中のユーザー情報をpreferenceに格納して用いることができるようにする
					SharedPreferences pref = getSharedPreferences(CommonUtils.STRING_PREF_KEY, Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					editor.putString(CommonUtils.STRING_EXTRA_USER_KEY,result.getKey());  
				    editor.commit();
					return true;
				} else {
					return false;
				}

			} catch (Exception e) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			

			if (success) {
				Log.d("DEBUG", "ログイン成功");
				startActivity(new Intent(MainActivity.this, TopActivity.class));
				finish();
			} else {
				
			}
		}
	}
	
	/**
	 * Represents an asynchronous registration task used to authenticate
	 * the user.
	 */
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... args) {

			try {
				RegisterEndpoint endpoint = RemoteApi.getRegisterEndpoint();
				Register register = endpoint.registerV1Endpoint().register(
						mEmail, mPassword, mPassword);
				RegisterResultV1Dto result = register.execute();

				
				if (SUCCESS.equals(result.getResult())) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask2 = null;

			if (success) {
				Log.d("DEBUG", "register success");
				mAuthTask = new UserLoginTask();
				mAuthTask.execute((Void) null);
				finish();
			}
		}
	}
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
}
