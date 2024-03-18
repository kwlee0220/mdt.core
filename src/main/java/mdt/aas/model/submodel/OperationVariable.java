package mdt.aas.model.submodel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class OperationVariable {
	@JsonProperty("value") private SubmodelElement m_value;

	@JsonProperty("value")
	public SubmodelElement getValue() {
		return m_value;
	}
	@JsonProperty("value")
	public void setValue(SubmodelElement value) {
		m_value = value;
	}
}
