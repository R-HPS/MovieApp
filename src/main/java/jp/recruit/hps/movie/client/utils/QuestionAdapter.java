package jp.recruit.hps.movie.client.utils;

import java.util.List;

import jp.recruit.hps.movie.client.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appspot.hps_movie.questionEndpoint.model.QuestionV1Dto;

public class QuestionAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<QuestionV1Dto> list;
	private final Context context;


	public QuestionAdapter(Context context, List<QuestionV1Dto> list) {
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
			imgbtn.setBackgroundResource(R.drawable.mypage00_schedules_before);
		}else{
			imgbtn.setBackgroundResource(R.drawable.mypage00_schedules_after);
		}
		if(getName(position)!=null){
			TextView tv = (TextView) convertView.findViewById(R.id.mypage_company_name);
			tv.setText(getName(position));
		}

		imgbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			
			}
		});
		return convertView;
	}
	
	public void addList(QuestionV1Dto question){
		list.add(question);
	}
}
