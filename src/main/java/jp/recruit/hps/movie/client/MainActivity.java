package jp.recruit.hps.movie.client;

import com.appspot.hps_movie.registerEndpoint.RegisterEndpoint;
import com.appspot.hps_movie.registerEndpoint.RegisterEndpoint.RegisterV1Endpoint.Register;
import com.google.analytics.tracking.android.EasyTracker;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.CompanyPreferences;
import jp.recruit.hps.movie.client.utils.Installation;
import jp.recruit.hps.movie.common.CommonConstant;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;

public class MainActivity extends Activity {
	String mPassword = "hpsmovies";
	private UserRegisterTask mAuthTask = null;
	private String mEmail;
	private static String SUCCESS = CommonConstant.SUCCESS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//待ち時間にlogoを表示
		FrameLayout fv = new FrameLayout(this);
		ImageView image = new ImageView(this);
		image.setImageResource(R.drawable.logo);
		image.setAdjustViewBounds(true);
		fv.addView(image, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(fv);
		//ここからログイン開始
		new CompanyPreferences();
		//以前に端末がログイン済みかを調べる
		if (sharedCheck()) {
			Intent intent = new Intent(this, TopActivity.class);
			startActivity(intent);
		} else {
			new Installation();
			mEmail = Installation.id(this);
			mEmail += "@test.ac.jp";
			mAuthTask = new UserRegisterTask();
			mAuthTask.execute((Void) null);
		}
	}

	// SharedPreferencesにUserKeyがあるかを調べる。（あったときはログインをSKIP）
	private boolean sharedCheck() {
		// TODO 自動生成されたメソッド・スタブ
		SharedPreferences pref = getSharedPreferences(
				CommonUtils.STRING_PREF_KEY, Activity.MODE_PRIVATE);
		String key = pref.getString(CommonUtils.STRING_EXTRA_USER_KEY, null);
		if (key != null) {
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
	 * Represents an asynchronous registration task used to authenticate the
	 * user.
	 */
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... args) {

			try {
				RegisterEndpoint endpoint = RemoteApi.getRegisterEndpoint();
				Register register = endpoint.registerV1Endpoint().register(
						mEmail, mPassword, mPassword);
				com.appspot.hps_movie.registerEndpoint.model.LoginResultV1Dto result = register
						.execute();

				if (SUCCESS.equals(result.getResult())) {
					// ログイン中のユーザー情報をpreferenceに格納して用いることができるようにする
					SharedPreferences pref = getSharedPreferences(
							CommonUtils.STRING_PREF_KEY, Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					editor.putString(CommonUtils.STRING_EXTRA_USER_KEY,
							result.getKey());
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
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MainActivity.this);
				// アラートダイアログのタイトルを設定します
				alertDialogBuilder.setTitle("接続エラー");
				// アラートダイアログのメッセージを設定します
				alertDialogBuilder.setMessage("ネット接続環境を確かめた後" + "\n" +"もう一度起動してください。");
				// アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
				alertDialogBuilder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				// アラートダイアログのキャンセルが可能かどうかを設定します
				alertDialogBuilder.setCancelable(true);
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
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
