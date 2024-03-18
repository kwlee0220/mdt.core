package mdt.aas.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class SpecificAssetId {
	@JsonProperty("name") private String m_name;
	@JsonProperty("value") private String m_value;
	@JsonProperty("externalSubjectId") private Reference m_externalSubjectId;
	
	@JsonProperty("name")
	public String getName() {
		return m_name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		m_name = name;
	}

	@JsonProperty("value")
	public String getValue() {
		return m_value;
	}
	@JsonProperty("value")
	public void setValue(String value) {
		m_value = value;
	}

	@JsonProperty("externalSubjectId")
	public Reference getExternalSubjectId() {
		return m_externalSubjectId;
	}
	@JsonProperty("externalSubjectId")
	public void setExternalSubjectId(Reference externalSubjectId) {
		m_externalSubjectId = externalSubjectId;
	}
}
