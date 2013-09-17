package jp.recruit.hps.movie.client.api;

import java.io.IOException;

import jp.recruit.hps.movie.client.utils.CommonUtils;

import com.appspot.hps_movie.companyEndpoint.CompanyEndpoint;
import com.appspot.hps_movie.movieEndpoint.MovieEndpoint;
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

	public static MovieEndpoint getMovieEndpoint() {
		MovieEndpoint.Builder endpointBuilder = new MovieEndpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest httpRequest) {
					}
				});
		return updateBuilder(endpointBuilder).build();
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
}
