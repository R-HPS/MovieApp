package jp.recruit.hps.movie.client;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CompanySearchAdapter;
import com.appspot.hps_movie.companyEndpoint.CompanyEndpoint;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1Dto;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1DtoCollection;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

public class RegisterCompanyActivity extends Activity{
	//登録するData
	private String mName;
	private String mSection;
	private String mPhase;
	private String mDate;
	private Long mTime;
	
	CompanySearchAdapter adapter;
	
	String tmpTime;
	String tmpDate;
	
	// UI references.
	private TextView mNameView;
	private TextView mDateView;
	private Spinner mSectionSpinner;
	private Spinner mPhaseSpinner;
	private ImageView mRegistButton;
	private ImageView mCancelButton;

	private String selectionKey;

	AlertDialog mDateDialog;
	AlertDialog companyDialog;
	Button button;
	
	CompanyV1Dto company;
	//dialog用
	ListView listView;
	ProgressBar progressBar;
	EditText companytext;
	TextView nothing;
	GetCompanySerchAsyncTask mSearchTask;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO 自動生成されたコンストラクター・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_company);
		setButtons();
	}

	private void setButtons() {
		// TODO 自動生成されたメソッド・スタブ
		mNameView  = (TextView)findViewById(R.id.register_company_name);
		mNameView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				setCompanyDialog();
			}

			
		});
		//会社部門のspinnerのセット
		mSectionSpinner  = (Spinner)this.findViewById(R.id.register_company_section_spinner);
		String arr[]={"a","b","c"};
		setSpinner(mSectionSpinner,arr);
		//会社の選考段階のspinnerセット
		mPhaseSpinner = (Spinner)this.findViewById(R.id.register_company_phase_spinner);
//		setSpinner(mPhaseSpinner,arr);
		
		//面接受ける日にちのセット
		mDateView = (TextView)findViewById(R.id.register_company_time_text);
		mDateView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setDial();
			}
		});
		//情報を登録するボタンをセット
		mRegistButton = (ImageView)findViewById(R.id.register_company_registbtn);
		mRegistButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				setSelection();
			}
		});
		
		//cancelして戻るボタンをセット
				mCancelButton = (ImageView)findViewById(R.id.register_company_cancelbtn);
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
		//コンテキストからインフレータを取得
		LayoutInflater inflater = LayoutInflater.from(this);
		//レイアウトXMLからビュー(レイアウト)をインフレート
		final View companyDialogView = inflater.inflate(R.layout.register_company_dialog, null);
		//内容の登録
		listView = (ListView)companyDialogView.findViewById(R.id.register_company_name_list);
		companytext = (EditText)companyDialogView.findViewById(R.id.register_company_select_text);
		progressBar = (ProgressBar)companyDialogView.findViewById(R.id.register_company_search_progress);
		nothing = (TextView)companyDialogView.findViewById(R.id.register_company_name_nothing);
		
		companyDialog = new AlertDialog.Builder(this)
		.setView(companyDialogView)
		.setTitle(R.string.register_company_name_set)
		.setPositiveButton(R.string.register_company_set_title, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自動生成されたメソッド・スタブ
						company=(CompanyV1Dto) adapter.getItem(listView.getCheckedItemPosition());
						mNameView.setText(company.getName());
					}
				})
		.setNegativeButton(R.string.register_company_cancel_title, null).show();
		button = companyDialog.getButton(DialogInterface.BUTTON_POSITIVE);
	    button.setEnabled(false);
		companyDialog.findViewById(R.id.register_company_serchbtn)
			.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自動生成されたメソッド・スタブ
					button.setEnabled(false);
					nothing.setVisibility(View.GONE);
					listView.setVisibility(View.GONE);
					progressBar.setVisibility(View.VISIBLE);
					mSearchTask = new GetCompanySerchAsyncTask(RegisterCompanyActivity.this);
					mSearchTask.execute(companytext.getText().toString());
				}
			});

	}
	
	private void setSelection() {
		// TODO 自動生成されたメソッド・スタブ
		// Reset errors.
		mNameView.setError(null);
		mDateView.setError(null);
		

		// Store values at the time of the login attempt.		
	//	mName= mNameView.getText().toString();
		mName = mNameView.getText().toString();
		mDate = mDateView.getText().toString();
		boolean cancel =false;
		View focusView =null;
		
		if(TextUtils.isEmpty(mName)){
			mNameView.setError(getString(R.string.error_field_required));
			focusView = mNameView;
			cancel = true;
		}
		
		if(TextUtils.isEmpty(mDate)){
			mDateView.setError(getString(R.string.error_field_required));
			cancel = true;
		}else{
			String date =mDateView.getText().toString();
			mTime = setCal(date);
		}

		if(mSectionSpinner.getTag().toString()!=null){
			mSection = mSectionSpinner.getTag().toString();
		}else {
			cancel=true;
		}
		if(mPhaseSpinner.getTag().toString()!=null){		
			mPhase = mPhaseSpinner.getTag().toString();
		}else{
			cancel=true;					
		}

	}

	private Long setCal(String strDate) {
		// TODO 自動生成されたメソッド・スタブ

		    Calendar cal = Calendar.getInstance();
		    cal.setLenient(false);

		    int yyyy = Integer.parseInt(strDate.substring(0,4));
		    int MM = Integer.parseInt(strDate.substring(5,7));
		    int dd = Integer.parseInt(strDate.substring(8,10));
		    int HH = cal.get(Calendar.HOUR_OF_DAY);
		    int mm = cal.get(Calendar.MINUTE);
		    cal.clear();
		    cal.set(yyyy,MM-1,dd);
		            HH = Integer.parseInt(strDate.substring(11,13));
		            mm = Integer.parseInt(strDate.substring(14,16));
		            cal.set(Calendar.HOUR_OF_DAY,HH);
		            cal.set(Calendar.MINUTE,mm);
		    return cal.getTimeInMillis();
	}

	private void setSpinner(Spinner spinner,String[] arr){
		  ArrayAdapter<String> adapter =
		  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
		  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  spinner.setAdapter(adapter);
		  spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
	            //Spinner�̃h���b�v�_�E���A�C�e�����I�����ꂽ��
	            public void onItemSelected(AdapterView<?> parent, View viw, int position, long arg3) {
	                Spinner spinner = (Spinner)parent;
	                String item = (String)spinner.getSelectedItem();
	                spinner.setTag(item);
	            }
	            public void onNothingSelected(AdapterView<?> parent) {
	            }});
		  spinner.setSelection(0);
		}
	private void setDial(){
		// ダイアログに表示する日時を取得する
		mDateView =(TextView)findViewById(R.id.register_company_time_text);
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		// 2つのpickerを併せたビューを作る
		DatePicker datePicker = new DatePicker(this);                   // (1)
		TimePicker timePicker = new TimePicker(this);                   // (2)

		LinearLayout view = new LinearLayout(this);                     // (3)
		view.setOrientation(LinearLayout.VERTICAL);                     // (4)
		view.addView(datePicker);                                       // (5)
		view.addView(timePicker);                                       // (6)

		// 日時を設定するビューを使ったダイアログを生成する
		mDateDialog = new AlertDialog.Builder(this)              // (7)
		.setView(view)                                          // (8)
		.setTitle(
				String.format(getString(R.string.date_time_format),
						calendar, calendar))
						.setPositiveButton(R.string.register_company_set_title, new DateSetHandler(view,
								mDateView))                                  // (9)
								.setNegativeButton(R.string.register_company_cancel_title, null).show();
	
		// pickerに値が変化したことを検知するハンドラーを設定する
		DateChangedHandler handler = new DateChangedHandler(mDateDialog);    // (10)

		datePicker.init(year, monthOfYear, dayOfMonth, handler);        // (11)
		timePicker.setCurrentHour(hourOfDay);                           // (12)
		timePicker.setCurrentMinute(minute);                            // (13)
		timePicker.setOnTimeChangedListener(handler);                   // (14)
		timePicker.setIs24HourView(true);                               // (15)
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
		private TextView date;

		public DateSetHandler(ViewGroup view, TextView date) {
			this.view = view;
			this.date = date;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {        // (17)
			DatePicker datePicker = (DatePicker) view.getChildAt(0);    // (18)
			TimePicker timePicker = (TimePicker) view.getChildAt(1);    // (19)

			Calendar calendar = Calendar.getInstance();
			calendar.set(datePicker.getYear(), datePicker.getMonth(),
					datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
					timePicker.getCurrentMinute());


			date.setText(String.format(getString(R.string.date_time_format),
					calendar));
		}
	}
	
	public class GetCompanySerchAsyncTask extends
	AsyncTask<String, Integer, Boolean> {
		private final Context context;
		private List<CompanyV1Dto> list;

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
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				progressBar.setVisibility(View.GONE);
				adapter = new CompanySearchAdapter(context,list);
				listView.setVisibility(View.VISIBLE);
				listView.setAdapter(adapter);
		        // 単一選択モードにする
		        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		        // デフォルト値をセットする
		        listView.setItemChecked(0, true);
			    button.setEnabled(true);
		        
			}else{
				progressBar.setVisibility(View.GONE);
				nothing.setVisibility(View.VISIBLE);
			}
			
		}
	}
}
