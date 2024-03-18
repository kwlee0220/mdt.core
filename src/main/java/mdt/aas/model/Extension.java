package mdt.aas.model;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import mdt.aas.model.ModelReference.ReferableReference;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class Extension {
	@JsonProperty("name") private String m_name;
	@JsonProperty("valueType") private DataTypeDefXsd m_valueType = DataTypeDefXsd.XS_STRING;
	@JsonProperty("value") @Nullable private String m_value;
	@JsonProperty("refersTo") private List<ReferableReference> m_refersTos;
	
	@JsonProperty("name")
	public String getName() {
		return m_name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		m_name = name;
	}

	@JsonProperty("valueType")
	public DataTypeDefXsd getValueType() {
		return m_valueType;
	}
	@JsonProperty("valueType")
	public void setValueType(DataTypeDefXsd valueType) {
		m_valueType = valueType;
	}

	@JsonProperty("value")
	public String getValue() {
		return m_value;
	}
	@JsonProperty("value")
	public void setValue(String value) {
		m_value = value;
	}
}
