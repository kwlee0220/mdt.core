package mdt.harbor;

import java.util.List;

import com.google.common.collect.Lists;

import utils.stream.FStream;

import mdt.docker.DockerImageId;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class HarborImageId {
	private final String m_endpoint;
	private final String m_project;
	private final String m_artifactName;
	private final String m_tag;
	
	public static HarborImageId parse(String imageId) {
		List<String> parts = Lists.newArrayList(imageId.split("/"));
		String endpoint = parts.remove(0);
		String artifactName = parts.remove(parts.size()-1);
		String project = FStream.from(parts).join('/');
		
		String tag = null;
		String[] subParts = artifactName.split(":");
		if ( subParts.length == 2 ) {
			artifactName = subParts[0];
			tag = subParts[1];
		}
		else if ( subParts.length != 1 ) {
			throw new IllegalArgumentException("invalid image-id: " + imageId);
		}
		
		return new HarborImageId(endpoint, project, artifactName, tag);
	}
	
	public static HarborImageId from(HarborProjectImpl project, DockerImageId imageId) {
		return new HarborImageId(project.getHarbor().getEndpoint(), project.getName(),
							imageId.repository(), imageId.tag());
	}
	
	private HarborImageId(String endpoint, String project, String artifactName, String tag) {
		m_endpoint = endpoint;
		m_project = project;
		m_artifactName = artifactName;
		m_tag = tag;
	}
	
	public String getFullNameWithTag() {
		return getFullName() + ":" + m_tag;
	}
	
	public String getFullName() {
		return m_endpoint + "/" + getName();
	}
	
	public String getEndpoint() {
		return m_endpoint;
	}
	
	public String getProjectName() {
		return m_project;
	}
	
	public String getName() {
		return m_project + "/" + m_artifactName;
	}
	
	public String getArtifactName() {
		return m_artifactName;
	}
	
	public String getTag() {
		return m_tag;
	}
	
	public DockerImageId toDockerImageId() {
		return new DockerImageId(m_endpoint + "/" + m_project + "/" + m_artifactName, m_tag);
	}
	
	@Override
	public String toString() {
		return getFullNameWithTag();
	}
}
