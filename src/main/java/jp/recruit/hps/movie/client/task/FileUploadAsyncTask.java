package jp.recruit.hps.movie.client.task;

import java.io.File;
import java.io.IOException;

import jp.recruit.hps.movie.client.api.RemoteApi;
import jp.recruit.hps.movie.client.utils.AWSUtils;
import android.content.Context;
import android.util.Log;

import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.appspot.hps_movie.movieEndpoint.MovieEndpoint.MovieV1EndPoint;
import com.appspot.hps_movie.movieEndpoint.MovieEndpoint.MovieV1EndPoint.CreateMovie;

public class FileUploadAsyncTask extends DialogAsyncTask {
	private final String userKey;
	private String fileName;

	public FileUploadAsyncTask(Context context, String userKey) {
		super(context);
		this.userKey = userKey;
	}

	@Override
	protected Boolean doInBackground(Object... files) {
		if (files.length == 0) {
			return false;
		}
		File file = (File) files[0];
		fileName = file.getName();
		PutObjectRequest request = new PutObjectRequest(AWSUtils.BUCKET_NAME,
				AWSUtils.getKey(userKey, fileName), file);
		request.setProgressListener(new FileUploadProgressListener(file
				.length()) {

		});
		Boolean result = AWSUtils.uploadToS3(request);
		if (result) {
			MovieV1EndPoint endPoint = RemoteApi.getMovieEndpoint()
					.movieV1EndPoint();
			try {
				CreateMovie action = endPoint.createMovie(userKey, fileName);
				action.execute();
			} catch (IOException e) {
				Log.d("Error", e.getMessage());
			}
		}
		return result;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
	}

	class FileUploadProgressListener implements ProgressListener {
		private final double fileSize;
		private double totalByteRead = 0.0d;

		FileUploadProgressListener(double fileSize) {
			this.fileSize = fileSize;
		}

		@Override
		public void progressChanged(ProgressEvent event) {
			totalByteRead += event.getBytesTransferred();
			publishProgress((int) ((totalByteRead / fileSize) * 100));
		}
	}
}
