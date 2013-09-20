package jp.recruit.hps.movie.client.utils;

import java.util.List;

import jp.recruit.hps.movie.client.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.hps_movie.companyEndpoint.model.CompanyV1Dto;

public class CompanyAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CompanyV1Dto> list;

	public CompanyAdapter(Context context, List<CompanyV1Dto> list) {
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

	public String getName(int position) {
		return list.get(position).getName();
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
		Button tv = (Button) convertView.findViewById(R.id.company_name_button);
		tv.setText(getName(position));
		return convertView;
	}
}
