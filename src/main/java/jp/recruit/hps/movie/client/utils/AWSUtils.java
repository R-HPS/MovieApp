package jp.recruit.hps.movie.client.utils;

import java.io.IOException;

import android.util.Log;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class AWSUtils {
	private static final String AWS_CREDENTIALS_PROPERTIES = "/AwsCredentials.properties";
	private static AmazonS3 S3_CLIENT;

	public static final String BUCKET_NAME = "hps-app-bucket";

	private static AmazonS3 getS3Client() throws IOException {
		if (S3_CLIENT == null) {
			S3_CLIENT = new AmazonS3Client(new PropertiesCredentials(
					AWSUtils.class
							.getResourceAsStream(AWS_CREDENTIALS_PROPERTIES)));
			/* Set region to Tokyo */
			S3_CLIENT.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		}
		return S3_CLIENT;
	}

	public static boolean uploadToS3(PutObjectRequest request) {
		try {
			AmazonS3 s3 = getS3Client();
			s3.putObject(request);
		} catch (IOException e) {
			Log.d("Error", e.getMessage());
			return false;
		}
		return true;
	}

	public static S3Object downloadFromS3(String userId, String fileName) {
		GetObjectRequest request = new GetObjectRequest(BUCKET_NAME, getKey(
				userId, fileName));
		return downloadFromS3(request);
	}

	public static S3Object downloadFromS3(GetObjectRequest request) {
		try {
			AmazonS3 s3 = getS3Client();
			return s3.getObject(request);
		} catch (IOException e) {
			Log.d("Error", e.getMessage());
			return null;
		}
	}

	public static String getKey(String userId, String fileName) {
		return String.format("%s/%s", userId, fileName);
	}
}