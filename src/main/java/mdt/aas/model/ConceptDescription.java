package mdt.aas.model;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConceptDescription {
	@JsonProperty("id") private String m_id;
	@JsonProperty("idShort") @Nullable private String m_idShort;
	@JsonProperty("category") @Nullable private String m_category;
	@JsonProperty("displayName") @Nullable private LangStringSet m_displayName;
	@JsonProperty("description") @Nullable private LangStringSet m_description;
	@JsonProperty("checksum") @Nullable private String m_checksum;
	@JsonProperty("administration") @Nullable private AdministrativeInformation m_administration;
	
	@JsonProperty("dataSpecification") private List<String> m_dataSpecification;
	
	@JsonProperty("isCaseOfs") private List<Reference> m_isCaseOfs;

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

	@JsonProperty("dataSpecification")
	public List<String> getDataSpecification() {
		return m_dataSpecification;
	}
	@JsonProperty("dataSpecification")
	public void setDataSpecification(List<String> dataSpecification) {
		m_dataSpecification = dataSpecification;
	}

	@JsonProperty("isCaseOfs")
	public List<Reference> getIsCaseOf() {
		return m_isCaseOfs;
	}
	@JsonProperty("isCaseOfs")
	public void setIsCaseOf(List<Reference> isCaseOfs) {
		m_isCaseOfs = isCaseOfs;
	}
}
