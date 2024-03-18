package mdt.harbor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangxueying on 2016/11/14.
 */
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "project_id", "name", "description", "artifact_count", "pull_count",
					"creation_time", "update_time" })
public class Repository {
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("project_id")
	private Integer projectId;
	
	@JsonProperty("name")
	private String fullName;
	private String projectName;
	private String artifactName;
	
	@JsonProperty("description")
	private String description;
	@JsonProperty("artifact_count")
	private Integer artifactCount;
	@JsonProperty("pull_count")
	private Integer pullCount;
	@JsonProperty("creation_time")
	private String creationTime;
	@JsonProperty("update_time")
	private String updateTime;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("id")
	public Integer getId() {
		return this.id;
	}
	
	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("project_id")
	public Integer getProjectId() {
		return this.projectId;
	}

	@JsonProperty("project_id")
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@JsonProperty("name")
	public String getFullName() {
		return this.fullName;
	}
	public String getProjectName() {
		return this.projectName;
	}
	public String getArtifactName() {
		return this.artifactName;
	}

	@JsonProperty("name")
	public void setFullName(String name) {
		this.fullName = name;
		int idx = name.indexOf('/');
		this.projectName = name.substring(0, idx);
		this.artifactName = name.substring(idx+1);
	}

	@JsonProperty("description")
	public String getDescription() {
		return this.description;
	}

	@JsonProperty("description")
	public void setDescription(String desc) {
		this.description = desc;
	}

	@JsonProperty("artifact_count")
	public Integer getArtifactCount() {
		return this.artifactCount;
	}

	@JsonProperty("artifact_count")
	public void setArtifactCount(Integer desc) {
		this.artifactCount = desc;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return String.format("%s(%d):created=%s", fullName, id, this.creationTime);
	}
}
