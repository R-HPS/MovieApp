package jp.recruit.hps.movie.client.utils;

import java.util.List;

import jp.recruit.hps.movie.client.CompanyInterviewActivity;
import jp.recruit.hps.movie.client.R;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appspot.hps_movie.interviewEndpoint.model.QuestionWithCountV1Dto;

public class InterviewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<QuestionWithCountV1Dto> list;
	private Context context;
	private boolean checked = false;
	

	public InterviewAdapter(Context context, List<QuestionWithCountV1Dto> list) {
		super();
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public Double getPercent(int position){
		return list.get(position).getPercent();
	}
	
	public String getQuestion(int position){
		return list.get(position).getName();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.company_list_inflater, null);
		}
		checked = true;
		FrameLayout v = (FrameLayout)convertView.findViewById(R.id.question_meter);
		TextView tv = (TextView) convertView.findViewById(R.id.interview_list_question);
		tv.setText(getQuestion(position));
		tv = (TextView)convertView.findViewById(R.id.interview_list_percent);
		int percent = (int) (getPercent(position)*100);
		tv.setText(percent+"％");
		DisplayMetrics metrics = new DisplayMetrics();  
		((CompanyInterviewActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int viewWidth = (int)metrics.widthPixels;
		int paintWidth = viewWidth*percent/100;
		MyView myView = new MyView(context,paintWidth,percent);
		v.addView(myView);
		return convertView;
	}
}
