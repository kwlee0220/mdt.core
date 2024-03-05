package mdt.harbor;

import mdt.docker.DockerImageId;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class HarborTag {
	private final String m_endpoint;
	private final String m_project;
	private final String m_artifactName;
	private final String m_tag;
	
	public static HarborTag from(HarborProjectImpl project, String repo, String tag) {
		return new HarborTag(project.getHarbor().getEndpoint(), project.getName(), repo, tag);
	}
	
	public static HarborTag from(HarborProjectImpl project, DockerImageId imageId) {
		return new HarborTag(project.getHarbor().getEndpoint(), project.getName(),
							imageId.repository(), imageId.tag());
	}
	
	public static HarborTag from(HarborImpl harbor,DockerImageId imageId) {
		return new HarborTag(harbor.getEndpoint(), harbor.getMDTInstanceProject().getName(),
							imageId.repository(), imageId.tag());
	}
	
	public static HarborTag parse(String dockerTag) {
		String[] parts = dockerTag.split("/");
		String harborEndpoint = parts[0];
		String project = parts[1];
		String artifact = parts[2];
		String[] parts2 = artifact.split(":");
		
		return new HarborTag(harborEndpoint, project, parts2[0], parts2[1]);
	}
	
	private HarborTag(String endpoint, String project, String artifactName, String tag) {
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
	
	@Override
	public String toString() {
		return getFullNameWithTag();
	}
}
