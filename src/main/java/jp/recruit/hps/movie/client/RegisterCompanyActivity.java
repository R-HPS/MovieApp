package jp.recruit.hps.movie.client;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TimePicker;

public class RegisterCompanyActivity extends Activity{
	//登録するData
	private String mName;
	private String mSection;
	private String mPhase;
	private Long mTime;
	
	// UI references.
	private TextView mNameView;
	private TextView mDateView;
	private TextView mTimeView;
	private Spinner mSectionSpinner;
	private Spinner mPhaseSpinner;
	private ImageView mRegistButton;
	
	private String userKey;
	private String selectionKey;
	
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
				
			}
		});
		//会社部門のspinnerのセット
		mSectionSpinner  = (Spinner)this.findViewById(R.id.register_company_section_spinner);
//		setSpinner(mSectionSpinner, arr);
		//会社の選考段階のspinnerセット
		mPhaseSpinner = (Spinner)this.findViewById(R.id.register_company_phase_spinner);
//		setSpinner(mPhaseSpinner,arr);
		
		//面接受ける日にちのセット
		mDateView = (TextView)findViewById(R.id.register_company_date_text);
		mDateView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				// TODO Auto-generated method stub
				new DatePickerDialog(RegisterCompanyActivity.this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						// 日付が設定されたときの処理
						mDateView.setText( String.format("%04d/%02d/%02d", year, monthOfYear+1, dayOfMonth));
					}
				}
				, 2010, 8, 19)
				.show();
			}
		});
		//面接受ける時間のセット
		mTimeView = (TextView)findViewById(R.id.register_company_time_text);
		mTimeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new TimePickerDialog(RegisterCompanyActivity.this, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						// TODO Auto-generated method stub
						// 時間が設定されたときの処理
						mTimeView.setText( String.format("%02d:%02d", hourOfDay, minute));
					}
				} , 13, 0, true)
				.show();
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
	//	setSelection();
	}
	
	private void setSelection() {
		// TODO 自動生成されたメソッド・スタブ
		// Reset errors.
		//mNameView.setError(null);
		mDateView.setError(null);
		mTimeView.setError(null);
		

		// Store values at the time of the login attempt.		
	//	mName= mNameView.getText().toString();
		String date =mDateView.getText().toString()+" "+mTimeView.getText().toString();
		mTime = setCal(date);
		mSection = mSectionSpinner.getTag().toString();
		mPhase = mPhaseSpinner.getTag().toString();
		
		boolean cancel =false;
		View focusView =null;
		
		// Check for a valid email address.
				if (TextUtils.isEmpty(mName)) {
					mNameView.setError(getString(R.string.error_field_required));
					focusView = mNameView;
					cancel = true;
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
		}
	
}
