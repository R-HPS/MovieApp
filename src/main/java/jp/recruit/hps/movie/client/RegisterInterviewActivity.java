package jp.recruit.hps.movie.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.appspot.hps_movie.questionEndpoint.QuestionEndpoint;
import com.appspot.hps_movie.questionEndpoint.QuestionEndpoint.QuestionV1EndPoint.CreateQuestion;
import com.appspot.hps_movie.questionEndpoint.model.QuestionResultV1Dto;
import com.appspot.hps_movie.questionEndpoint.model.QuestionV1Dto;
import com.appspot.hps_movie.questionEndpoint.model.QuestionV1DtoCollection;
import jp.recruit.hps.movie.client.utils.QuestionAdapter;
import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.common.CommonConstant;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class RegisterInterviewActivity extends Activity {
	private QuestionAdapter adapter;
	String selectionKey;
	String userKey;
	private static String SUCCESS = CommonConstant.SUCCESS;
	
	//データ保存
	int mTime;
	int mCategory;
	int mAtmosphere;
	
	
	
	//UI
	Spinner mCategorySpinner;
	Spinner mTimeSpinner;
	ImageView mEasyAtmosphere;
	ImageView mNormalAtmosphere;
	ImageView mHardAtmosphere;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたコンストラクター・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_companypage);
		Intent i = getIntent();
		selectionKey = i
				.getStringExtra(CommonUtils.STRING_EXTRA_SELECTION_KEY);
		SharedPreferences pref = getSharedPreferences(CommonUtils.STRING_PREF_KEY, Activity.MODE_PRIVATE);
		userKey = pref.getString(CommonUtils.STRING_EXTRA_USER_KEY,null);

		new GetQuestionListAsyncTask(this).execute(selectionKey);
	}
	
	private void setSpinner(Spinner spinner,List<String> arr){
		  
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
	
	public void setButton(){
		mCategorySpinner = (Spinner)findViewById(R.id.register_interview_category_spinner);
		mTimeSpinner = (Spinner)findViewById(R.id.register_interview_time_spinner);
		mEasyAtmosphere = (ImageView)findViewById(R.id.register_interview_atmosphre_easy);
		mNormalAtmosphere = (ImageView)findViewById(R.id.register_interview_atmosphre_normal);
		mHardAtmosphere = (ImageView)findViewById(R.id.register_interview_atmosphre_hard);
		Button btn = (Button)findViewById(R.id.add_list_btn);
		btn.setVisibility(View.VISIBLE);
		btn.setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {

						findViewById(R.id.add_list_btn).setVisibility(View.GONE);
						findViewById(R.id.register_add_text_layout)
						.setVisibility(View.VISIBLE);
						findViewById(R.id.add_question_send_btn).setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO 自動生成されたメソッド・スタブ
								findViewById(R.id.add_question_send_btn)
								.setVisibility(View.GONE);
								ProgressBar prog =(ProgressBar)findViewById
										(R.id.register_question_progressBar);
								prog.setVisibility(View.VISIBLE);
							}	
						});
					}
				});
		
		
	}

	public class SetQuestionAsyncTask extends
	AsyncTask<String,Integer,Boolean> {
		QuestionV1Dto question;
		@Override
		protected Boolean doInBackground(String... params) {

			// TODO 自動生成されたメソッド・スタブ
			try {
				QuestionEndpoint endpoint = RemoteApi.getQuestionEndpoint();
				CreateQuestion createQuestion = endpoint.questionV1EndPoint().createQuestion(userKey, selectionKey, params[0]);
				QuestionResultV1Dto result =createQuestion.execute();
				if (SUCCESS.equals(result.getResult())) {
					question = result.getQuestion();
					return true;
				} else {
					return false;
				}

			} catch (Exception e) {
				return false;
			}

		}
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				ProgressBar prog =(ProgressBar)findViewById
						(R.id.register_question_progressBar);
				prog.setVisibility(View.GONE);
				adapter.addList(question);
				//アダプタに対してデータが変更したことを知らせる
				adapter.notifyDataSetChanged();
				setButton();
			}
		}

	}

	public class GetQuestionListAsyncTask extends
	AsyncTask<String, Integer, Boolean> {
		private final Context context;
		private List<QuestionV1Dto> list;

		public GetQuestionListAsyncTask(Context context) {
			this.context = context;
		}

		@Override
		protected Boolean doInBackground(String... queries) {
			String query = queries[0];
			QuestionEndpoint endpoint = RemoteApi.getQuestionEndpoint();
			try {
				QuestionV1DtoCollection collection = endpoint
						.questionV1EndPoint().getQuestions(query).execute();
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
				findViewById(R.id.register_question_progress_layout).setVisibility(View.GONE);
				
				ListView lv = (ListView)findViewById(R.id.register_question_list);
				lv.setVisibility(View.VISIBLE);
				findViewById(R.id.register_add_list_layout).setVisibility(View.VISIBLE);
				adapter = new QuestionAdapter(context,list);
				lv.setAdapter(adapter);
				setButton();
			}
		}
	}
	
	 @Override
	  public boolean dispatchKeyEvent(KeyEvent event) {
	    // TODO Auto-generated method stub
	    if(event.getAction() == KeyEvent.ACTION_DOWN) {
	      if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {  // BACKキー
	        return true;
	      }
	    }
	    return super.dispatchKeyEvent(event);
	  }
	
}


