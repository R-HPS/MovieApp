package jp.recruit.hps.movie.client.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public abstract class DialogAsyncTask extends AsyncTask<Object, Integer, Boolean>
		implements OnCancelListener {
	protected ProgressDialog dialog;
	protected String title;
	protected String message;

	protected final Context context;
	
	public DialogAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(this);
		dialog.setMax(100);
		dialog.setProgress(0);
		dialog.show();
	}

	protected void onProgressUpdate(Integer... values) {
		dialog.setProgress(values[0]);
	}

	@Override
	protected void onCancelled() {
		dialog.dismiss();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		dialog.dismiss();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		this.cancel(true);
	}
}
