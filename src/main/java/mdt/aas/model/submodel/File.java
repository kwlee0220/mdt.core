package mdt.aas.model.submodel;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class File extends DataElement {
	@JsonProperty("value") @Nullable String m_value;
	@JsonProperty("contentType") String m_contentType;
	
	@JsonProperty("value")
	public @Nullable String getValue() {
		return m_value;
	}
	@JsonProperty("value")
	public void setValue(String value) {
		m_value = value;
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
