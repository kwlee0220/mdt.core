package mdt.aas.model;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {
	@JsonProperty("path") private String m_path;
	@JsonProperty("contentType") @Nullable private String m_contentType;
	
	@JsonProperty("path")
	public String getPath() {
		return m_path;
	}
	@JsonProperty("path")
	public void setPath(String path) {
		m_path = path;
	}

	@JsonProperty("contentType")
	public String getContentType() {
		return m_contentType;
	}
	@JsonProperty("contentType")
	public void setContentType(String contentType) {
		m_contentType = contentType;
	}
}
