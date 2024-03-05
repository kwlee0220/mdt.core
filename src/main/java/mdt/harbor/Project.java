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


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project {
	@JsonProperty("project_id")
	private Integer projectId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("owner_id")
	private Integer ownerId;
	@JsonProperty("owner_name")
	private String ownerName;
	@JsonProperty("repo_count")
	private Integer repoCount;
	@JsonProperty("deleted")
	private Boolean deleted;
	@JsonProperty("creation_time")
	private String creationTime;
	@JsonProperty("update_time")
	private String updateTime;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("project_id")
	public Integer getProjectId() {
		return this.projectId;
	}

	@JsonProperty("project_id")
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@JsonProperty("name")
	public String getName() {
		return this.name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("owner_id")
	public Integer getOwnerId() {
		return this.ownerId;
	}

	@JsonProperty("owner_id")
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	@JsonProperty("owner_name")
	public String getOwnerName() {
		return this.ownerName;
	}

	@JsonProperty("owner_name")
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@JsonProperty("repo_count")
	public Integer getRepoCount() {
		return this.repoCount;
	}

	@JsonProperty("repo_count")
	public void setRepoCount(Integer count) {
		this.repoCount = count;
	}

	@JsonProperty("deleted")
	public Boolean getDeleted() {
		return this.deleted;
	}

	@JsonProperty("deleted")
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@JsonProperty("creation_time")
	public String getCreationTime() {
		return this.creationTime;
	}

	@JsonProperty("creation_time")
	public void setCreationTime(String timeStr) {
		this.creationTime = timeStr;
	}

	@JsonProperty("update_time")
	public String getUpdateTime() {
		return this.updateTime;
	}

	@JsonProperty("update_time")
	public void setUpdateTime(String timeStr) {
		this.updateTime = timeStr;
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
		return String.format("%s(%d): owner=%s, repos=%s, created=%s",
								name, projectId, ownerName, this.repoCount, creationTime);
	}
}
