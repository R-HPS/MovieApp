package jp.recruit.hps.movie.client;

import java.util.List;

import jp.recruit.hps.movie.client.utils.CommonUtils;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MovieListActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_movie_list);

		Intent i = getIntent();
		final String targetUserKey = i
				.getStringExtra(CommonUtils.STRING_EXTRA_USER_KEY);
		List<String> fileNameList = i
				.getStringArrayListExtra(CommonUtils.STRING_ARRAY_LIST_EXTRA_FILE_NAME_LIST);

		LinearLayout layout = (LinearLayout) findViewById(R.id.liner_view_movie_list);
		for (final String fileName : fileNameList) {
            TextView tv = new TextView(this);
            tv.setText(fileName);
            tv.setWidth(LayoutParams.MATCH_PARENT);
            tv.setHeight(LayoutParams.WRAP_CONTENT);
            tv.setGravity(Gravity.TOP);
            tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(MovieListActivity.this, VideoActivity.class);
					intent.putExtra(CommonUtils.STRING_EXTRA_USER_KEY, targetUserKey);
					intent.putExtra(CommonUtils.STRING_EXTRA_FILE_NAME, fileName);
					startActivity(intent);
				}
			});
            layout.addView(tv);
		}
	}
}
