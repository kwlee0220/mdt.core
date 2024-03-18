package mdt.harbor;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.func.FOption;

import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class HarborProjectImpl {
	private static final TypeReference<Repository> TYPE_REPOSITORY = new TypeReference<Repository>() {};
	
	private final HarborImpl m_harbor;
	private final String m_projectName;
	
	HarborProjectImpl(HarborImpl harbor, String projectName) {
		m_harbor = harbor;
		m_projectName = projectName;
	}
	
	public HarborImpl getHarbor() {
		return m_harbor;
	}
	
	public String getUrlPrefix() {
		return String.format("%s/projects/%s", m_harbor.getUrlPrefix(), m_projectName);
	}
	
	public String getName() {
		return m_projectName;
	}

	public List<Repository> getRepositoryAll() throws MDTHarborException {
		try {
			String url = String.format("%s/repositories", getUrlPrefix());
			Request req = m_harbor.newGetRequest(url);
			Response response = m_harbor.call(req);
			if ( response.isSuccessful() ) {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(response.body().string(),
										new TypeReference<List<Repository>>() {});
			}
			handleHttpStatusCode(response.code(), "all");
			throw new AssertionError();
			
		}
		catch ( IOException e ) {
			throw new MDTHarborException("fails to connect Harbor: url=%s", e);
		}
	}
	
	public FOption<Repository> getRepository(String repoName) {
		try {
			String url = String.format("%s/repositories/%s", getUrlPrefix(), repoName);
			Request req = m_harbor.newGetRequest(url);
			Response response = m_harbor.call(req);
			if ( response.isSuccessful() ) {
				ObjectMapper mapper = new ObjectMapper();
				Repository repo = mapper.readValue(response.body().string(), TYPE_REPOSITORY);
				return repo.getId() != null ? FOption.of(repo) : FOption.empty();
			}
			else if ( response.code() == 404 ) {
				return FOption.empty();
			}
			handleHttpStatusCode(response.code(), repoName);
			throw new AssertionError();
		}
		catch ( IOException e ) {
			throw new MDTHarborException("fails to connect Harbor: url=%s", e);
		}
	}
	
	public void removeRepository(String repoName) {
		String url = String.format("%s/repositories/%s", getUrlPrefix(), repoName);
		Request req = m_harbor.newDeleteRequest(url);
		Response response = m_harbor.call(req);
		if ( response.isSuccessful() ) { }
		else if ( response.code() == 404 ) {
			throw new MDTHarborException("Not found Harbor repository: " + repoName);
		}
		else {
			throw new MDTHarborException("Failed to remove repository: " + repoName);
		}
	}
	
	public void removeRepositoryAll() {
		for ( Repository repo: getRepositoryAll() ) {
			removeRepository(repo.getArtifactName());
		}
	}
	
	private void handleHttpStatusCode(int code, String rscId) throws MDTHarborException {
		switch ( code ) {
			case 404:
				throw new MDTHarborException("MDTInstance not found: " + rscId);
		}
	}
}
