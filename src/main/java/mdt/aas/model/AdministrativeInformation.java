package mdt.aas.model;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class AdministrativeInformation {
	@JsonProperty("version") @Nullable private String m_version;
	@JsonProperty("revision") @Nullable private String m_revision;
	
	@JsonProperty("version")
	public String getVersion() {
		return m_version;
	}
	@JsonProperty("version")
	public void setVersion(String version) {
		m_version = version;
	}

	@JsonProperty("revision")
	public String getValue() {
		return m_revision;
	}
	@JsonProperty("revision")
	public void setValue(String revision) {
		m_revision = revision;
	}
}
