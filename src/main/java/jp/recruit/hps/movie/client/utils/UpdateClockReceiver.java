package jp.recruit.hps.movie.client.utils;

import jp.recruit.hps.movie.client.TopActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateClockReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (context.getClass() == TopActivity.class) {
			((TopActivity) context).setNowTime();
		}
	}
}
