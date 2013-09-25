package jp.recruit.hps.movie.client;

import java.io.IOException;
import java.util.List;

import com.appspot.hps_movie.loginEndpoint.LoginEndpoint;
import com.appspot.hps_movie.loginEndpoint.LoginEndpoint.LoginV1Endpoint.Login;
import com.appspot.hps_movie.questionEndpoint.QuestionEndpoint;
import com.appspot.hps_movie.questionEndpoint.model.QuestionV1Dto;
import com.appspot.hps_movie.questionEndpoint.model.QuestionV1DtoCollection;
import com.appspot.hps_movie.selectionEndpoint.SelectionEndpoint;
import com.appspot.hps_movie.selectionEndpoint.model.CompanyV1Dto;
import com.appspot.hps_movie.selectionEndpoint.model.CompanyV1DtoCollection;

import jp.recruit.hps.movie.client.utils.QuestionAdapter;
import jp.recruit.hps.movie.client.CompanyInterviewActivity.GetInterviewListAsyncTask;
import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.CompanyAdapter;
import jp.recruit.hps.movie.client.utils.CompanyPriferences;
import jp.recruit.hps.movie.common.CommonConstant;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

public class RegisterInterviewActivity extends Activity {
	private QuestionAdapter adapter;
	String selectionKey;
	String userKey;
	private static String SUCCESS = CommonConstant.SUCCESS;
	
	public RegisterInterviewActivity() {
		// TODO 自動生成されたコンストラクター・スタブ
		setContentView(R.layout.activity_companypage);
		Intent i = getIntent();
		selectionKey = i
				.getStringExtra(CommonUtils.STRING_EXTRA_SELECTION_KEY);
		userKey = i
				.getStringExtra(CommonUtils.STRING_EXTRA_USER_KEY);
		
		new GetQuestionListAsyncTask(this).execute(selectionKey);
	}
	
//public class SetQuestionAsyncTask extends
//	AsyncTask<String,Integer,Boolean> {
//
//	@Override
//	protected Boolean doInBackground(String... params) {
//		// TODO 自動生成されたメソッド・スタブ
//		try {
//			QuestionEndpoint endpoint = RemoteApi.getQuestionEndpoint();
//			QuestionResultV1Dto result = endpoint.questionV1EndPoint().
//					createQuestion(userKey, selectionKey, params[0]);
//			if (SUCCESS.equals(result.getResult())) {
//				return true;
//			} else {
//				return false;
//			}
//
//		} catch (Exception e) {
//			return false;
//		}
//		
//	}
//	
//}

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
				ListView lv = (ListView)findViewById(R.id.register_question_list);
				adapter = new QuestionAdapter(context,list);
				lv.setAdapter(adapter);
				findViewById(R.id.add_list_btn).setOnClickListener(
						new View.OnClickListener() {
							public void onClick(View view) {
								
								findViewById(view.getId()).setVisibility(View.GONE);
								findViewById(R.id.register_add_text_layout)
									.setVisibility(View.VISIBLE);
								findViewById(R.id.add_question_send_btn).setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO 自動生成されたメソッド・スタブ
										
									}
								});
							}
						});
		}
	}
}
}


