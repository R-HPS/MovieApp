package jp.recruit.hps.movie.client.api;

import java.io.IOException;

import jp.recruit.hps.movie.client.utils.CommonUtils;

import com.appspot.hps_movie.companyEndpoint.CompanyEndpoint;
import com.appspot.hps_movie.interviewEndpoint.InterviewEndpoint;
import com.appspot.hps_movie.selectionEndpoint.SelectionEndpoint;
import com.appspot.hps_movie.loginEndpoint.LoginEndpoint;
import com.appspot.hps_movie.registerEndpoint.RegisterEndpoint;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;

public class RemoteApi {
	protected static <B extends AbstractGoogleClient.Builder> B updateBuilder(
			B builder) {
		builder.setRootUrl(CommonUtils.BASE_URL + "_ah/api/");

		final boolean enableGZip = builder.getRootUrl().startsWith("https:");

		builder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
			public void initialize(AbstractGoogleClientRequest<?> request)
					throws IOException {
				if (!enableGZip) {
					request.setDisableGZipContent(true);
				}
			}
		});
		return builder;
	}
	
	public static CompanyEndpoint getCompanyEndpoint() {
		CompanyEndpoint.Builder endpointBuilder = new CompanyEndpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		return updateBuilder(endpointBuilder).build();
	}
	
	public static InterviewEndpoint getInterviewEndpoint() {
		InterviewEndpoint.Builder endpointBuilder = new InterviewEndpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		return updateBuilder(endpointBuilder).build();
	}
	
	public static SelectionEndpoint getSelectionEndpoint() {
		SelectionEndpoint.Builder endpointBuilder = new SelectionEndpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		return updateBuilder(endpointBuilder).build();
	}
	public static LoginEndpoint getLoginEndpoint() {
		LoginEndpoint.Builder endpointBuilder = new LoginEndpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		return updateBuilder(endpointBuilder).build();
	}
	
	public static RegisterEndpoint getRegisterEndpoint() {
		RegisterEndpoint.Builder endpointBuilder = new RegisterEndpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		return updateBuilder(endpointBuilder).build();
	}
}
