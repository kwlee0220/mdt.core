package mdt.aas.model;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class Qualifier implements HasSemantics {
	@JsonProperty("semanticId") @Nullable private Reference m_semanticId;
	@JsonProperty("supplementalSemanticIds") private List<Reference> m_supplementalSemanticIds;
	
	@JsonProperty("kind") @Nullable private QualifierKind m_kind = QualifierKind.ConceptQualifier;
	@JsonProperty("type") @Nullable private String m_type;
	@JsonProperty("valueType") DataTypeDefXsd m_valueType;
	@JsonProperty("value") @Nullable String m_value;
	@JsonProperty("valueId") @Nullable Reference m_valueId;

	@Override
	@JsonProperty("semanticId")
	public Reference getSemanticId() {
		return m_semanticId;
	}
	@Override
	@JsonProperty("semanticId")
	public void setSemanticId(Reference semanticId) {
		m_semanticId = semanticId;
	}

	@Override
	@JsonProperty("supplementalSemanticIds")
	public List<Reference> getSupplementalSemanticIds() {
		return m_supplementalSemanticIds;
	}
	@Override
	@JsonProperty("supplementalSemanticIds")
	public void setSupplementalSemanticIds(List<Reference> supplementalSemanticIds) {
		m_supplementalSemanticIds = supplementalSemanticIds;
	}
	
	@JsonProperty("kind")
	public QualifierKind getKind() {
		return m_kind;
	}
	@JsonProperty("kind")
	public void setKind(QualifierKind kind) {
		m_kind = kind;
	}
	
	@JsonProperty("type")
	public String getType() {
		return m_type;
	}
	@JsonProperty("type")
	public void setType(String type) {
		m_type = type;
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
	public @Nullable String getValue() {
		return m_value;
	}
	@JsonProperty("value")
	public void setValue(String value) {
		m_value = value;
	}

	@JsonProperty("valueId")
	public @Nullable Reference getValueId() {
		return m_valueId;
	}
	@JsonProperty("valueId")
	public void setValueId(Reference valueId) {
		m_valueId = valueId;
	}
}
