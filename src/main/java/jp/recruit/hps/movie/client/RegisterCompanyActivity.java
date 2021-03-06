package jp.recruit.hps.movie.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.CompanySearchAdapter;
import jp.recruit.hps.movie.common.CommonConstant;

import com.appspot.hps_movie.companyEndpoint.CompanyEndpoint;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1Dto;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1DtoCollection;
import com.appspot.hps_movie.interviewEndpoint.InterviewEndpoint;
import com.appspot.hps_movie.interviewEndpoint.InterviewEndpoint.InterviewV1EndPoint.InsertInterview;
import com.appspot.hps_movie.interviewEndpoint.model.ResultV1Dto;
import com.google.analytics.tracking.android.EasyTracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TimePicker;

public class RegisterCompanyActivity extends HPSActivity {
	// 登録するData
	private Long mTime;

	// selectionあったとき
	// Map<String, Map<String, SelectionV1Dto>> mInterviewMap;

	CompanySearchAdapter adapter;

	String tmpTime;
	String tmpDate;

	List<String> mNameArray;
	List<String> mSectionArray;
	List<String> mPhaseArray;

	// UI references.
	private TextView mNameView;
	private TextView mDateView;
	private Spinner mNameSpinner;
	private Spinner mDateSpinner;
	// private Spinner mSectionSpinner;
	// private Spinner mPhaseSpinner;
	private ImageView mRegistButton;
	private ImageView mCancelButton;

	private String companyKey;
	String userKey;

	AlertDialog mDateDialog;
	AlertDialog companyDialog;
	Button button;

	CompanyV1Dto company;
	// dialog用
	ListView listView;
	ProgressBar progressBar;
	EditText companytext;
	TextView nothing;
	GetCompanySerchAsyncTask mSearchTask;

	private static final String SUCCESS = CommonConstant.SUCCESS;
	SelectionRegisterTask mAuthTask;

	private View mRegisterFormView;
	private View mRegisterStatusView;
	private TextView mRegisterStatusMessageView;

	private TextView errorText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたコンストラクター・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_company);
		setButtons();
		mRegisterFormView = findViewById(R.id.register_company_form);
		mRegisterStatusView = findViewById(R.id.register_company_status);
		mRegisterStatusMessageView = (TextView) findViewById(R.id.register_company_status_message);
		getUserKey();
	}

	private void getUserKey() {
		// TODO 自動生成されたメソッド・スタブ
		SharedPreferences pref = getSharedPreferences(
				CommonUtils.STRING_PREF_KEY, Activity.MODE_PRIVATE);
		userKey = pref.getString(CommonUtils.STRING_EXTRA_USER_KEY, null);
	}

	private void setButtons() {
		// TODO 自動生成されたメソッド・スタブ
		mNameView = (TextView) findViewById(R.id.register_company_name);
		mNameSpinner = (Spinner) this
				.findViewById(R.id.register_company_name_spinner);
		mNameSpinner.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (companyDialog != null)
					return true;
				setCompanyDialog();
				return false;
			}

		});
		mDateView = (TextView) findViewById(R.id.register_company_time);
		mDateSpinner = (Spinner) this
				.findViewById(R.id.register_company_time_text);
		mDateSpinner.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (mDateDialog != null)
					return true;
				setDial();
				return false;
			}

		});

		// 情報を登録するボタンをセット
		mRegistButton = (ImageView) findViewById(R.id.register_company_registbtn);
		mRegistButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				setSelection();
			}
		});

		// cancelして戻るボタンをセット
		mCancelButton = (ImageView) findViewById(R.id.register_company_cancelbtn);
		mCancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				finish();
			}
		});
	}

	private void setCompanyDialog() {
		// TODO 自動生成されたメソッド・スタブ
		// コンテキストからインフレータを取得
		LayoutInflater inflater = LayoutInflater.from(this);
		// レイアウトXMLからビュー(レイアウト)をインフレート
		final View companyDialogView = inflater.inflate(
				R.layout.register_company_dialog, null);
		// 内容の登録
		listView = (ListView) companyDialogView
				.findViewById(R.id.register_company_name_list);
		companytext = (EditText) companyDialogView
				.findViewById(R.id.register_company_select_text);
		progressBar = (ProgressBar) companyDialogView
				.findViewById(R.id.register_company_search_progress);
		nothing = (TextView) companyDialogView
				.findViewById(R.id.register_company_name_nothing);
		errorText = (TextView) companyDialogView
				.findViewById(R.id.register_company_name_error);

		companyDialog = new AlertDialog.Builder(this)
				.setView(companyDialogView)
				.setTitle(R.string.register_company_name_set)
				.setPositiveButton(R.string.register_company_set_title,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO 自動生成されたメソッド・スタブ
								company = (CompanyV1Dto) adapter
										.getItem(listView
												.getCheckedItemPosition());
								mNameArray = new ArrayList<String>();
								mNameArray.add(company.getName());
								companyKey = company.getKey();
								setSpinner(mNameSpinner, mNameArray);
								// mNameView.setText(company.getName());
								companyDialog = null;
							}
						})
				.setNegativeButton(R.string.register_company_cancel_title,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								companyDialog.dismiss();
								companyDialog = null;
							}
						}).show();
		companyDialog.setCanceledOnTouchOutside(false);
		button = companyDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		button.setEnabled(false);
		companyDialog.findViewById(R.id.register_company_serchbtn)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 自動生成されたメソッド・スタブ
						if (mSearchTask == null) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
							button.setEnabled(false);
							nothing.setVisibility(View.GONE);
							errorText.setVisibility(View.GONE);
							listView.setVisibility(View.GONE);
							progressBar.setVisibility(View.VISIBLE);

							mSearchTask = new GetCompanySerchAsyncTask(
									RegisterCompanyActivity.this);
							mSearchTask.executeOnExecutor(
									AsyncTask.THREAD_POOL_EXECUTOR, companytext
											.getText().toString());
						}
					}
				});

	}

	private void setSelection() {
		if (mAuthTask != null) {
			return;
		}

		// TODO 自動生成されたメソッド・スタブ
		// Reset errors.
		mNameView.setError(null);
		mDateView.setError(null);
		boolean cancel = false;
		// 会社名確認
		if (mNameSpinner.getTag() == null) {
			mNameView.setError(getString(R.string.error_field_required));
			cancel = true;
		}
		// 日付確認
		if (mDateSpinner.getTag() != null) {
			String date = mDateSpinner.getTag().toString();
			mTime = setCal(date);
		} else {
			mDateView.setError(getString(R.string.error_field_required));
			cancel = true;
		}

		if (!cancel) {
			// Show a progress spinner, and kick off a background task to
			// perform the user register attempt.
			// selectionKey = mInterviewMap.get(mSection).get(mPhase).getKey();
			mRegisterStatusMessageView
					.setText(R.string.register_progress_signing_up);
			showProgress(true);
			mAuthTask = new SelectionRegisterTask();
			mAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					(Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the register form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mRegisterStatusView.setVisibility(View.VISIBLE);
			mRegisterStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterStatusView
									.setVisibility(show ? View.VISIBLE
											: View.GONE);
						}
					});

			mRegisterFormView.setVisibility(View.VISIBLE);
			mRegisterFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mRegisterStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous registration task used to authenticate the
	 * user.
	 */
	public class SelectionRegisterTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... args) {

			try {
				InterviewEndpoint endpoint = RemoteApi.getInterviewEndpoint();
				InsertInterview register = endpoint.interviewV1EndPoint()
						.insertInterview(userKey, companyKey, mTime);
				ResultV1Dto result = register.execute();

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
			mAuthTask = null;
			showProgress(false);

			if (success) {
				Log.d("DEBUG", "register success");
				startActivity(new Intent(RegisterCompanyActivity.this,
						TopActivity.class));

				finish();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

	private Long setCal(String strDate) {
		// TODO 自動生成されたメソッド・スタブ

		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);

		int yyyy = Integer.parseInt(strDate.substring(0, 4));
		int MM = Integer.parseInt(strDate.substring(5, 7));
		int dd = Integer.parseInt(strDate.substring(8, 10));
		int HH = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		cal.clear();
		cal.set(yyyy, MM - 1, dd);
		HH = Integer.parseInt(strDate.substring(11, 13));
		mm = Integer.parseInt(strDate.substring(14, 16));
		cal.set(Calendar.HOUR_OF_DAY, HH);
		cal.set(Calendar.MINUTE, mm);
		return cal.getTimeInMillis();
	}

	private void setSpinner(Spinner spinner, List<String> arr) {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			// Spinner�̃h���b�v�_�E���A�C�e�����I�����ꂽ��
			public void onItemSelected(AdapterView<?> parent, View viw,
					int position, long arg3) {
				Spinner spinner = (Spinner) parent;
				String item = (String) spinner.getSelectedItem();
				spinner.setTag(item);
				/*
				 * if (spinner == mSectionSpinner) { mPhaseArray = new
				 * ArrayList<String>(); for (String phase :
				 * mInterviewMap.get(item).keySet()) { mPhaseArray.add(phase); }
				 * Collections.sort(mPhaseArray, new Comparator<String>() {
				 * 
				 * @Override public int compare(String lhs, String rhs) { return
				 * lhs.compareTo(rhs); } }); setSpinner(mPhaseSpinner,
				 * mPhaseArray); }
				 */
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinner.setSelection(0);
	}

	private void setDial() {
		// ダイアログに表示する日時を取得する
		// mDateView = (TextView) findViewById(R.id.register_company_time_text);
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		// 2つのpickerを併せたビューを作る
		DatePicker datePicker = new DatePicker(this); // (1)
		TimePicker timePicker = new TimePicker(this); // (2)

		LinearLayout view = new LinearLayout(this); // (3)
		view.setOrientation(LinearLayout.VERTICAL); // (4)
		view.addView(datePicker); // (5)
		view.addView(timePicker); // (6)

		// 日時を設定するビューを使ったダイアログを生成する
		mDateDialog = new AlertDialog.Builder(this)
				// (7)
				.setView(view)
				// (8)
				.setTitle(
						String.format(getString(R.string.date_time_format),
								calendar, calendar))
				.setPositiveButton(R.string.register_company_set_title,
						new DateSetHandler(view))
				// (9)
				.setNegativeButton(R.string.register_company_cancel_title,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mDateDialog.dismiss();
								mDateDialog = null;
							}
						}).show();
		mDateDialog.setCanceledOnTouchOutside(false);
		// pickerに値が変化したことを検知するハンドラーを設定する
		DateChangedHandler handler = new DateChangedHandler(mDateDialog); // (10)

		datePicker.init(year, monthOfYear, dayOfMonth, handler); // (11)
		timePicker.setCurrentHour(hourOfDay); // (12)
		timePicker.setCurrentMinute(minute); // (13)
		timePicker.setOnTimeChangedListener(handler); // (14)
	}

	/**
	 * 日時が変更されたときの処理をするハンドラー
	 */
	private class DateChangedHandler implements
			DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {
		private AlertDialog dialog;
		private Calendar calendar = Calendar.getInstance();

		public DateChangedHandler(AlertDialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			calendar.set(year, monthOfYear, dayOfMonth);
			dialog.setTitle(String.format(getString(R.string.date_time_format),
					calendar));
		}

		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);

			dialog.setTitle(String.format(getString(R.string.date_time_format),
					calendar));
		}
	}

	/**
	 * 日時が設定されたときの処理をするハンドラー
	 */
	private class DateSetHandler implements DialogInterface.OnClickListener { // (16)
		private ViewGroup view;

		public DateSetHandler(ViewGroup view) {
			this.view = view;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) { // (17)
			DatePicker datePicker = (DatePicker) view.getChildAt(0); // (18)
			TimePicker timePicker = (TimePicker) view.getChildAt(1); // (19)

			Calendar calendar = Calendar.getInstance();

			calendar.set(datePicker.getYear(), datePicker.getMonth(),
					datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
					timePicker.getCurrentMinute());
			List<String> mDateArray = new ArrayList<String>();
			mDateArray.add(String.format(getString(R.string.date_time_format),
					calendar));
			setSpinner(mDateSpinner, mDateArray);
			mDateDialog = null;
			// date.setText(String.format(getString(R.string.date_time_format),
			// calendar));
		}
	}

	public class GetCompanySerchAsyncTask extends
			AsyncTask<String, Integer, Boolean> {
		private final Context context;
		private List<CompanyV1Dto> list;
		private boolean error = false;

		public GetCompanySerchAsyncTask(Context context) {
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
				error = true;
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				if (list != null) {
					progressBar.setVisibility(View.GONE);
					adapter = new CompanySearchAdapter(context, list);
					listView.setVisibility(View.VISIBLE);
					listView.setAdapter(adapter);
					// 単一選択モードにする
					listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
					// デフォルト値をセットする
					listView.setItemChecked(0, true);
					button.setEnabled(true);
				} else {
					if (error) {
						progressBar.setVisibility(View.GONE);
						errorText.setVisibility(View.VISIBLE);
					} else {
						progressBar.setVisibility(View.GONE);
						nothing.setVisibility(View.VISIBLE);
					}
				}

			} else {
				progressBar.setVisibility(View.GONE);
				errorText.setVisibility(View.VISIBLE);
			}
			mSearchTask = null;
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
