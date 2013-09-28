package jp.recruit.hps.movie.client.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class UpdateClockService extends Service {

	public static UpdateClockService activeService;
	
	public UpdateClockService() {
		super();
	}

	private long getIntervalMS() {
		return 1 * 1000;
	}

	protected void execTask() {
		activeService = this;
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction("UPDATE_CLOCK");
		getBaseContext().sendBroadcast(broadcastIntent);
		makeNextPlan();
	}

	protected void makeNextPlan() {
		this.scheduleNextTime();
	}

	protected final IBinder binder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public UpdateClockService startResident(Context context) {
		Intent intent = new Intent(context, this.getClass());
		intent.putExtra("type", "start");
		context.startService(intent);

		return this;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		execTask();
		return START_STICKY;
	}

	public void scheduleNextTime() {
		long now = System.currentTimeMillis();

		PendingIntent alarmSender = PendingIntent.getService(this, 0,
				new Intent(this, this.getClass()), 0);
		AlarmManager am = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC, now + getIntervalMS(), alarmSender);
	}

	public void stopResident(Context context) {
		Intent intent = new Intent(context, this.getClass());

		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

		stopSelf();
	}

	public static void stopResidentIfActive(Context context) {
		if (activeService != null) {
			activeService.stopResident(context);
		}
	}
}
