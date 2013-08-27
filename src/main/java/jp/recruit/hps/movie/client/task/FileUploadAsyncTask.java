package jp.recruit.hps.movie.client.task;

import java.io.File;

import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;

import jp.recruit.hps.movie.client.utils.AWSUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public class FileUploadAsyncTask extends AsyncTask<File, Integer, Boolean>
		implements OnCancelListener {
	ProgressDialog dialog;
	Context context;
	double totalByteRead = 0.0d;
	double fileSize = 0.0d;

	public FileUploadAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(context);
		dialog.setTitle("Please wait");
		dialog.setMessage("Uploading file...");
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
	protected Boolean doInBackground(File... files) {
		if (files.length == 0 || files[0].length() == 0l) {
			return false;
		}
		fileSize = files[0].length();
		PutObjectRequest request = new PutObjectRequest(AWSUtils.BUCKET_NAME,
				AWSUtils.getKey("test", files[0].getName()), files[0]);
		request.setProgressListener(new ProgressListener() {

			@Override
			public void progressChanged(ProgressEvent event) {
				totalByteRead += event.getBytesTransferred();
				publishProgress((int)((totalByteRead / fileSize) * 100));
			}
		});
		return AWSUtils.uploadToS3(request);
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
