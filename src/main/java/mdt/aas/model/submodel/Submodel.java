package mdt.aas.model.submodel;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import mdt.aas.model.AdministrativeInformation;
import mdt.aas.model.LangStringSet;
import mdt.aas.model.ModelingKind;
import mdt.aas.model.Qualifier;
import mdt.aas.model.Reference;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Submodel {
	@JsonProperty("submodelElements") private List<SubmodelElement> m_submodelElements;
	
	@JsonProperty("semanticId") @Nullable public Reference m_semanticId;
	@JsonProperty("supplementalSemanticIds") public String m_supplementalSemanticIds;

	@JsonProperty("id") private String m_id;
	@JsonProperty("idShort") @Nullable private String m_idShort;
	@JsonProperty("category") @Nullable private String m_category;
	@JsonProperty("displayName") private LangStringSet m_displayName;
	@JsonProperty("description") private LangStringSet m_description;
	@JsonProperty("checksum") @Nullable private String m_checksum;
	@JsonProperty("administration") @Nullable private AdministrativeInformation m_administration;
	
	@JsonProperty("kind") @Nullable private ModelingKind m_kind;
	@JsonProperty("qualifier") private List<Qualifier> m_qualifier;
	@JsonProperty("dataSpecification") private List<String> m_dataSpecification;

	@JsonProperty("submodelElements")
	public List<SubmodelElement> getSubmodelElements() {
		return m_submodelElements;
	}
	@JsonProperty("submodelElements")
	public void setSubmodelElements(List<SubmodelElement> submodelElements) {
		m_submodelElements = submodelElements;
	}

	@JsonProperty("semanticId")
	public Reference getSemanticId() {
		return m_semanticId;
	}
	@JsonProperty("semanticId")
	public void setSemanticId(Reference semanticId) {
		m_semanticId = semanticId;
	}
	
	@JsonProperty("supplementalSemanticIds")
	public String getSupplementalSemanticId() {
		return m_supplementalSemanticIds;
	}
	@JsonProperty("supplementalSemanticIds")
	public void setSupplementalSemanticId(String supplementalSemanticIds) {
		m_supplementalSemanticIds = supplementalSemanticIds;
	}

	@JsonProperty("id")
	public String getId() {
		return m_id;
	}
	@JsonProperty("id")
	public void setId(String id) {
		m_id = id;
	}

	@JsonProperty("idShort")
	public @Nullable String getIdShort() {
		return m_idShort;
	}
	@JsonProperty("idShort")
	public void setIdShort(@Nullable String idShort) {
		m_idShort = idShort;
	}

	@JsonProperty("category")
	public @Nullable String getCategory() {
		return m_category;
	}
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

	@JsonProperty("checksum")
	public String getChecksum() {
		return m_checksum;
	}

	@JsonProperty("checksum")
	public void setChecksum(String checksum) {
		m_checksum = checksum;
	}

	@JsonProperty("administration")
	public AdministrativeInformation getAdministration() {
		return m_administration;
	}
	@JsonProperty("administration")
	public void setAdministration(AdministrativeInformation administration) {
		m_administration = administration;
	}
	
	@JsonProperty("kind")
	public ModelingKind getKind() {
		return m_kind;
	}
	@JsonProperty("kind")
	public void setKind(ModelingKind kind) {
		m_kind = kind;
	}
	
	@JsonProperty("qualifier")
	public List<Qualifier> getQualifier() {
		return null;
	}
	@JsonProperty("qualifier")
	public void setQualifier(List<Qualifier> qualifier) {
	}

	@JsonProperty("dataSpecification")
	public List<String> getDataSpecification() {
		return m_dataSpecification;
	}
	@JsonProperty("dataSpecification")
	public void setDataSpecification(List<String> dataSpecification) {
		m_dataSpecification = dataSpecification;
	}
	
	@Override
	public String toString() {
		return String.format("(%s): %s[%s]", m_kind, m_idShort, m_id);
	}
}
