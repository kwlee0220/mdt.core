package mdt.aas.model.submodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class Operation extends SubmodelElement {
	@JsonProperty("inputVariables") List<OperationVariable> m_inputVariables;
	@JsonProperty("outputVariables") List<OperationVariable> m_outputVariables;
	@JsonProperty("inoutputVariables") List<OperationVariable> m_inoutputVariables;
	
	@JsonProperty("inputVariables")
	public List<OperationVariable> getInputVariable() {
		return m_inputVariables;
	}
	@JsonProperty("inputVariables")
	public void setInputVariable(List<OperationVariable> inputVariable) {
		m_inputVariables = inputVariable;
	}

	@JsonProperty("outputVariables")
	public List<OperationVariable> getOutputVariable() {
		return m_outputVariables;
	}
	@JsonProperty("outputVariables")
	public void setOutputVariable(List<OperationVariable> outputVariable) {
		m_inputVariables = outputVariable;
	}

	@JsonProperty("inoutputVariables")
	public List<OperationVariable> getInOutputVariable() {
		return m_inoutputVariables;
	}
	@JsonProperty("inoutputVariables")
	public void setInOutputVariable(List<OperationVariable> inoutputVariable) {
		m_inputVariables = inoutputVariable;
	}
}
