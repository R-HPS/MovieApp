package jp.recruit.hps.movie.client.utils;

import java.util.List;

import jp.recruit.hps.movie.client.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appspot.hps_movie.companyEndpoint.model.CompanyV1Dto;
import com.appspot.hps_movie.interviewEndpoint.model.InterviewV1Dto;

public class InterviewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<InterviewV1Dto> list;

	public InterviewAdapter(Context context, List<InterviewV1Dto> list) {
		super();
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	public String getCategory(int position) {
		return list.get(position).getCategory();
	}
	
	public String getQuestion(int position) {
		return list.get(position).getQuestion();
	}
	
	public Integer getAtmosphere(int position) {
		return list.get(position).getAtmosphere();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.company, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.company_name);
		//tv.setText(getName(position));
		return convertView;
	}
}
