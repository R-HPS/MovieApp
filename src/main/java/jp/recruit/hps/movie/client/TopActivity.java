package jp.recruit.hps.movie.client;

import java.io.IOException;
import java.util.List;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.CompanyAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.appspot.hps_movie.companyEndpoint.CompanyEndpoint;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1Dto;
import com.appspot.hps_movie.companyEndpoint.model.CompanyV1DtoCollection;

public class TopActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_top);

		findViewById(R.id.search_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						onSearchRequested();
					}
				});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			CompanyEndpoint endpoint = RemoteApi.getCompanyEndpoint();
			try {
				CompanyV1DtoCollection collection = endpoint
						.companyV1EndPoint().searchCompany(query).execute();
				if (collection != null) {
					List<CompanyV1Dto> list = collection.getItems();
					ListView lv = (ListView) findViewById(R.id.company_list);
					lv.setAdapter(new CompanyAdapter(this, list));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
