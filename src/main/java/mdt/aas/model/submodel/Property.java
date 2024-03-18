package mdt.aas.model.submodel;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import mdt.aas.model.DataTypeDefXsd;
import mdt.aas.model.Reference;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class Property extends DataElement {
	@JsonProperty("valueType") DataTypeDefXsd m_valueType;
	@JsonProperty("valueId") @Nullable Reference m_valueId;
	@JsonProperty("value") @Nullable String m_value;
	
	@JsonProperty("valueType")
	public DataTypeDefXsd getValueType() {
		return m_valueType;
	}
	@JsonProperty("valueType")
	public void setValueType(DataTypeDefXsd valueType) {
		m_valueType = valueType;
	}

	@JsonProperty("valueId")
	public @Nullable Reference getValueId() {
		return m_valueId;
	}
	@JsonProperty("valueId")
	public void setValueId(Reference valueId) {
		m_valueId = valueId;
	}
	
	@JsonProperty("value")
	public @Nullable String getValue() {
		return m_value;
	}
	@JsonProperty("value")
	public void setValue(String value) {
		m_value = value;
	}
}
