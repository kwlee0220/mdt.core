package mdt.aas.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class Key {
	@JsonProperty("type") KeyTypes m_type;
	@JsonProperty("value") String m_value;
	
	public static Key Identifiable(String id) {
		return new Key(KeyTypes.Identifiable, id);
	}
	public static Key Referable(String id) {
		return new Key(KeyTypes.Referable, id);
	}
	
	public Key() { }
	public Key(KeyTypes type, String value) {
		m_type = type;
		m_value = value;
	}
	
	@JsonProperty("type")
	public KeyTypes getType() {
		return m_type;
	}
	@JsonProperty("type")
	public void setType(KeyTypes type) {
		m_type = type;
	}
	
	@JsonProperty("value")
	public String getValue() {
		return m_value;
	}
	@JsonProperty("value")
	public void setValue(String value) {
		m_value = value;
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s", m_type, m_value);
	}
}
