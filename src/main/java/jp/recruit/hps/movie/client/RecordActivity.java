package jp.recruit.hps.movie.client;

import java.io.File;
import java.io.IOException;

import jp.recruit.hps.movie.client.task.FileUploadAsyncTask;
import jp.recruit.hps.movie.client.utils.CommonUtils;
import jp.recruit.hps.movie.client.utils.FileUtils;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class RecordActivity extends Activity implements OnClickListener {
	private SurfaceView mSurfaceView;
	private SurfaceHolder.Callback mCallback;
	private MediaRecorder mRecorder;
	private Camera mCamera;
	private File mFile;
	private boolean isRecording;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		init();
	}

	private void init() {
		isRecording = false;
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		mSurfaceView.setOnClickListener(this);
		mCallback = new SurfaceHolder.Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				destroyCamera();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				initRecorder(holder);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// Noop
			}
		};
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.addCallback(mCallback);
	}

	public void initRecorder(SurfaceHolder holder) {
		mRecorder = new MediaRecorder();
		mCamera = Camera.open(0);
		setCameraDisplayOrientation(0, mCamera);
		mCamera.unlock();
		mRecorder.setCamera(mCamera);

		mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		mRecorder.setVideoFrameRate(30);

		mRecorder.setOutputFile(getFilePath());
		mRecorder.setPreviewDisplay(holder.getSurface());
	}

	public void onClick(View v) {
		try {
			if (!isRecording) {
				isRecording = true;
				if (mRecorder == null) {
					initRecorder(mSurfaceView.getHolder());
				}
				mRecorder.prepare();
				mRecorder.start();
				((TextView) findViewById(R.id.textViewRec))
						.setVisibility(View.GONE);
				((TextView) findViewById(R.id.textViewStop))
						.setVisibility(View.VISIBLE);
			} else {
				destroyRecorder();
				destroyCamera();
				isRecording = false;
				((TextView) findViewById(R.id.textViewRec))
						.setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.textViewStop))
						.setVisibility(View.GONE);
				new FileUploadAsyncTask(this, CommonUtils.TEST_USER_KEY)
						.execute(mFile);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getFilePath() {
		if (mFile == null) {
			mFile = FileUtils
					.createNewFile(System.currentTimeMillis() + ".3gp");
		}
		return mFile.getAbsolutePath();
	}

	public void destroyRecorder() {
		if (mRecorder != null) {
			mRecorder.reset();
			mRecorder.release();
			mRecorder = null;
		}
	}

	public void destroyCamera() {
		if (mCamera != null) {
			mCamera.lock();
			mCamera.release();
			mCamera = null;
		}
	}

	public void setCameraDisplayOrientation(int cameraId, Camera camera) {
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360;
		} else {
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
