package mdt.aas.model;

import java.util.Set;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class DataSpecification {
	@JsonProperty("administration") @Nullable private String m_administration;
	@JsonProperty("id") private String m_id;
	@JsonProperty("dataSpecificationContent") private String m_dataSpecificationContent;
	@JsonProperty("description") @Nullable private Set<LangString> m_description;
	
	@JsonProperty("id")
	public String getId() {
		return m_id;
	}
	@JsonProperty("id")
	public void setId(String id) {
		m_id = id;
	}

	@JsonProperty("dataSpecificationContent")
	public String getDataSpecificationContent() {
		return m_dataSpecificationContent;
	}
	@JsonProperty("dataSpecificationContent")
	public void setDataSpecificationContent(String dataSpecificationContent) {
		m_dataSpecificationContent = dataSpecificationContent;
	}

	@JsonProperty("administration")
	public String getAdministration() {
		return m_administration;
	}
	@JsonProperty("administration")
	public void setAdministration(String administration) {
		m_administration = administration;
	}

	@JsonProperty("description")
	public Set<LangString> getDescription() {
		return m_description;
	}
	@JsonProperty("description")
	public void setDescription(Set<LangString> description) {
		m_description = description;
	}
}
