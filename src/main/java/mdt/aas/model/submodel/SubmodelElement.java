package mdt.aas.model.submodel;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import mdt.aas.model.HasDataSpecification;
import mdt.aas.model.HasKind;
import mdt.aas.model.HasSemantics;
import mdt.aas.model.LangStringSet;
import mdt.aas.model.ModelingKind;
import mdt.aas.model.Qualifiable;
import mdt.aas.model.Qualifier;
import mdt.aas.model.Referable;
import mdt.aas.model.Reference;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmodelElement implements Referable, HasKind, HasSemantics, Qualifiable, HasDataSpecification {
	@JsonProperty("semanticIds") @Nullable public Reference m_semanticIds;
	@JsonProperty("supplementalSemanticIds") public List<Reference> m_supplementalSemanticIds;

	@JsonProperty("idShort") @Nullable private String m_idShort;
	@JsonProperty("category") @Nullable private String m_category;
	@JsonProperty("displayName") private LangStringSet m_displayName;
	@JsonProperty("description") private LangStringSet m_description;
	@JsonProperty("checksum") @Nullable private String m_checksum;
	
	@JsonProperty("kind") @Nullable private ModelingKind m_kind;
	@JsonProperty("qualifiers") List<Qualifier> m_qualifiers;
	@JsonProperty("dataSpecifications") private List<Reference> m_dataSpecifications;

	@Override
	@JsonProperty("semanticId")
	public Reference getSemanticId() {
		return m_semanticIds;
	}
	@Override
	@JsonProperty("semanticId")
	public void setSemanticId(Reference semanticId) {
		m_semanticIds = semanticId;
	}
	
	@Override
	@JsonProperty("supplementalSemanticId")
	public List<Reference> getSupplementalSemanticIds() {
		return m_supplementalSemanticIds;
	}
	@Override
	@JsonProperty("supplementalSemanticId")
	public void setSupplementalSemanticIds(List<Reference> supplementalSemanticIds) {
		m_supplementalSemanticIds = supplementalSemanticIds;
	}

	@Override
	@JsonProperty("idShort")
	public @Nullable String getIdShort() {
		return m_idShort;
	}
	@Override
	@JsonProperty("idShort")
	public void setIdShort(@Nullable String idShort) {
		m_idShort = idShort;
	}

	@Override
	@JsonProperty("category")
	public @Nullable String getCategory() {
		return m_category;
	}
	@Override
	@JsonProperty("category")
	public void setCategory(@Nullable String category) {
		m_category = category;
	}

	@JsonProperty("displayName")
	public LangStringSet getDisplayName() {
		return m_displayName;
	}
	@JsonProperty("displayName")
	public void setDisplayName(LangStringSet displayName) {
		m_displayName = displayName;
	}

	@JsonProperty("description")
	public LangStringSet getDescription() {
		return m_description;
	}
	@JsonProperty("description")
	public void setDescription(LangStringSet description) {
		m_description = description;
	}

	@Override
	@JsonProperty("checksum")
	public String getChecksum() {
		return m_checksum;
	}

	@Override
	@JsonProperty("checksum")
	public void setChecksum(String checksum) {
		m_checksum = checksum;
	}
	
	@Override
	@JsonProperty("kind")
	public ModelingKind getKind() {
		return m_kind;
	}
	@Override
	@JsonProperty("kind")
	public void setKind(ModelingKind kind) {
		m_kind = kind;
	}

	@Override
	@JsonProperty("dataSpecification")
	public List<Reference> getDataSpecifications() {
		return m_dataSpecifications;
	}
	@Override
	@JsonProperty("dataSpecification")
	public void setDataSpecifications(List<Reference> dataSpecifications) {
		m_dataSpecifications = dataSpecifications;
	}
	
	@Override
	@JsonProperty("qualifier")
	public List<Qualifier> getQualifiers() {
		return m_qualifiers;
	}
	@Override
	@JsonProperty("qualifier")
	public void setQualifiers(List<Qualifier> qualifier) {
		m_qualifiers = qualifier;
	}
}
