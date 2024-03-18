package mdt.aas.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class IdentificationV1 {
	@JsonProperty("id") private String m_id;
	
	@JsonProperty("id")
	public String getName() {
		return m_id;
	}
	@JsonProperty("id")
	public void setName(String id) {
		m_id = id;
	}
}
