package mdt.aas.model.submodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class SubmodelElementCollection extends SubmodelElement {
	@JsonProperty("values") List<SubmodelElement> m_values;
	
	@JsonProperty("values")
	public List<SubmodelElement> getValues() {
		return m_values;
	}
	@JsonProperty("values")
	public void setValues(List<SubmodelElement> values) {
		m_values = values;
	}
}
