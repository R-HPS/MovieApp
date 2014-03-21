package jp.recruit.hps.movie.client.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.appspot.hps_movie.companyEndpoint.model.CompanyV1Dto;

import jp.recruit.hps.movie.client.CompanyInterviewActivity;
import jp.recruit.hps.movie.client.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class PopularCompanyAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CompanyV1Dto> list;
	private final Context context;
	@SuppressLint("SimpleDateFormat")
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	String[] week_name = { "日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日" };

	public PopularCompanyAdapter(Context context, List<CompanyV1Dto> list) {
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

//	public String getPhase(int position) {
//		return list.get(position).getPhase();
//	}

	public Calendar getDate(int position) {
		Calendar cal = Calendar.getInstance();
		if (list.get(position).getStartDate() != null) {
			cal.setTimeInMillis(list.get(position).getStartDate());
			return cal;
		} else {
			return null;
		}
	}

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
			convertView = inflater.inflate(R.layout.popularcompany, null);
		}
		ImageButton imgbtn = (ImageButton) convertView
				.findViewById(R.id.company_name_button);
		if (position != 0) {
			imgbtn.setBackgroundResource(R.drawable.cn_back);
		} else {
			imgbtn.setBackgroundResource(R.drawable.cn_back);
		}
		if (getName(position) != null) {
			TextView tv = (TextView) convertView
					.findViewById(R.id.mypage_company_name);
			tv.setText(getName(position));
		}
			TextView tv = (TextView) convertView
					.findViewById(R.id.mypage_company_date);
			
			tv.setText("人気ランキング第"+(position+1)+"位");
		imgbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,
						CompanyInterviewActivity.class);
				intent.putExtra(CommonUtils.STRING_EXTRA_SELECTION_KEY,
						getKey(position));
				intent.putExtra(CommonUtils.STRING_EXTRA_FILE_NAME,
						getName(position));
				context.startActivity(intent);
			}
		});
		return convertView;
	}
}
