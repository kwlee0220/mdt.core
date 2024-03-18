package mdt.harbor;

import java.io.IOException;
import java.util.Base64;

import mdt.impl.SSLUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class HarborImpl {
	private final String m_authHeader;
	private final String m_endpoint;
	private final OkHttpClient.Builder m_clientBuilder;
	
	private HarborImpl(Builder builder) throws MDTHarborException {
		String authStr = String.format("%s:%s", builder.m_harborUserId, builder.m_harborPassword);
		m_authHeader = "Basic " + Base64.getEncoder().encodeToString(authStr.getBytes());
		m_endpoint = builder.m_harborEndpoint;
		
		try {
			m_clientBuilder = SSLUtils.newTrustAllOkHttpClientBuilder();
		}
		catch ( Exception e ) {
			throw new MDTHarborException("fails to create a RESTful client, cause=" + e);
		}
	}
	
	public String getEndpoint() {
		return m_endpoint;
	}
	
	public String getUrlPrefix() {
		return String.format("https://%s/api/v2.0", m_endpoint);
	}
	
	public HarborProjectImpl getProject(String prj) {
		return new HarborProjectImpl(this, prj);
	}
	
	public Request newGetRequest(String url) {
		return new Request.Builder()
							.url(url)
							.addHeader("Authorization", m_authHeader)
							.get()
							.build();
	}
	
	public Request newDeleteRequest(String url) {
		return new Request.Builder()
							.url(url)
							.addHeader("Authorization", m_authHeader)
							.delete()
							.build();
	}
	
	public Response call(Request request) {
		try {
			OkHttpClient okhttpClient = m_clientBuilder.build();
			return okhttpClient.newCall(request).execute();
		}
		catch ( IOException e ) {
			throw new MDTHarborException("fails to connect Harbor: url=" + request.url() + ", cause=" + e);
		}
	}

	public static Builder builder() {
		return new Builder();
	}
	public static Builder builder(HarborConfig config) {
		return new Builder()
					.setHarborEndpoint(config.getEndpoint())
					.setHarborUserId(config.getUserId())
					.setHarborPassword(config.getPassword());
	}
	public static final class Builder {
		private String m_harborEndpoint;
		private String m_harborUserId;
		private String m_harborPassword;
		
		private Builder() { }
		
		public HarborImpl build() {
			return new HarborImpl(this);
		}
		
		public Builder setHarborEndpoint(String ep) {
			m_harborEndpoint = ep;
			return this;
		}
		
		public Builder setHarborUserId(String userId) {
			m_harborUserId = userId;
			return this;
		}
		
		public Builder setHarborPassword(String password) {
			m_harborPassword = password;
			return this;
		}
	}
}
