package jp.recruit.hps.movie.client.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.recruit.hps.movie.client.CompanyInterviewActivity;
import jp.recruit.hps.movie.client.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appspot.hps_movie.interviewGroupEndpoint.model.CompanyV1Dto;

public class CompanyAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CompanyV1Dto> list;
	private final Context context;

	public CompanyAdapter(Context context, List<CompanyV1Dto> list) {
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

	public String getName(int position) {
		return list.get(position).getName();
	}
	
	public String getPhase(int position){
		return list.get(position).getName();
	}
	
//	public Calendar getDate(int position){
//		long time =0;
//		Calendar cal;
//		cal.setTimeInMillis(list.get(position).getDate());
//		return cal;
//	}

	public String getKey(int position) {
		return list.get(position).getKey();
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.company, null);
		}
		ImageButton imgbtn = (ImageButton) convertView.findViewById(R.id.company_name_button);
		if(position!=0){
			imgbtn.setImageResource(R.drawable.mypage00_schedules_before);
		}else{
			imgbtn.setImageResource(R.drawable.mypage00_schedules_after);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.mypage_company_name);
		tv.setText(getName(position));
		tv = (TextView) convertView.findViewById(R.id.mypage_company_phase);
		tv.setText(getPhase(position));
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,
						CompanyInterviewActivity.class);
				intent.putExtra(CommonUtils.STRING_EXTRA_INTERVIEWGROUP_KEY, getKey(position));
				intent.putExtra(CommonUtils.STRING_EXTRA_FILE_NAME,
						getName(position));
				context.startActivity(intent);				
			}
		});
		return convertView;
	}
}
