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
	private final String m_project;
	
	HarborProjectImpl(HarborImpl harbor, String project) {
		m_harbor = harbor;
		m_project = project;
	}
	
	public HarborImpl getHarbor() {
		return m_harbor;
	}
	
	public String getUrlPrefix() {
		return String.format("%s/projects/%s", m_harbor.getUrlPrefix(), m_project);
	}
	
	public String getName() {
		return m_project;
	}

	public List<Repository> getRepositoryAll() throws MDTHarborException {
		try {
			String url = String.format("%s/repositories", getUrlPrefix());
			Response response = m_harbor.sendRequest(url);
			
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(response.body().string(),
									new TypeReference<List<Repository>>() {});
		}
		catch ( IOException e ) {
			throw new MDTHarborException("fails to connect Harbor: url=%s", e);
		}
	}
	
	public FOption<Repository> getRepository(String repoName) {
		try {
			String url = String.format("%s/repositories/%s", getUrlPrefix(), repoName);
			Response response = m_harbor.sendRequest(url);
			
			ObjectMapper mapper = new ObjectMapper();
			Repository repo = mapper.readValue(response.body().string(), TYPE_REPOSITORY);
			return repo.getId() != null ? FOption.of(repo) : FOption.empty();
		}
		catch ( IOException e ) {
			throw new MDTHarborException("fails to connect Harbor: url=%s", e);
		}
	}
	
	public void removeRepository(String repoName) {
		try {
			String url = String.format("%s/repositories/%s", getUrlPrefix(), repoName);
			Request req = m_harbor.deleteRequest(url);
			Response response = m_harbor.call(req);
			response.body().string();
		}
		catch ( IOException e ) {
			throw new MDTHarborException("fails to connect Harbor: url=%s", e);
		}
	}
}
