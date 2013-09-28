package jp.recruit.hps.movie.client.utils;

import java.util.List;

import jp.recruit.hps.movie.client.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.appspot.hps_movie.questionEndpoint.model.QuestionV1Dto;

public class QuestionAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<QuestionV1Dto> list;

	public QuestionAdapter(Context context, List<QuestionV1Dto> list) {
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
			convertView = inflater.inflate(R.layout.question_list_view_item,
					null);
		}
		if (getName(position) != null) {
			TextView tv = (TextView) convertView
					.findViewById(R.id.question_text_view1);
			tv.setText(getName(position));
		}
		CheckBox check = (CheckBox) convertView.findViewById(R.id.check_box);
		check.getTag(position);
		return convertView;
	}

	public void addList(QuestionV1Dto question) {
		list.add(question);
	}
}
