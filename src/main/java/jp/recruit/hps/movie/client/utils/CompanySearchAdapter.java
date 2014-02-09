package jp.recruit.hps.movie.client.utils;

import java.util.List;

import jp.recruit.hps.movie.client.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.appspot.hps_movie.companyEndpoint.model.CompanyV1Dto;

public class CompanySearchAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CompanyV1Dto> list;

	public CompanySearchAdapter(Context context, List<CompanyV1Dto> list) {
		super();
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
	}

	@Override
	public int getCount() {
		
		return list != null?list.size():0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	public String getName(int position) {
		return list.get(position).getName();
	}
	
//	public String getPhase(int position){
//		return list.get(position).getPhase();
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
			convertView = inflater.inflate(R.layout.company_search_list_view_item, null);
		}
		
		TextView text = (TextView)convertView.findViewById(R.id.text_view1);
		text.setText(getName(position));
		RadioButton mRadioButton = (RadioButton)convertView.findViewById(R.id.radio_button);
		mRadioButton.setTag(position);
		return convertView;
	}
}
