package mdt.aas.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import utils.stream.FStream;

import mdt.aas.model.ModelReference.AssetAdministrationShellReference;
import mdt.aas.model.ModelReference.SubmodelReference;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetAdministrationShell implements Identifiable, HasDataSpecification {
	@JsonProperty("assetInformation") private AssetInformation m_assetInformation;
	@JsonProperty("submodels") private List<SubmodelReference> m_submodels;

	@JsonProperty("id") private String m_id;
	@JsonProperty("idShort") @Nullable private String m_idShort;
	@JsonProperty("category") @Nullable private String m_category;
	@JsonProperty("displayName") @Nullable private LangStringSet m_displayName;
	@JsonProperty("description") @Nullable private LangStringSet m_description;
	@JsonProperty("checksum") @Nullable private String m_checksum;
	@JsonProperty("administration") @Nullable private AdministrativeInformation m_administration;

	@JsonProperty("derivedFrom") private AssetAdministrationShellReference m_derivedFrom;
	@JsonProperty("dataSpecifications") private List<Reference> m_dataSpecifications;
	
	@JsonProperty("submodels")
	public List<SubmodelReference> getSubmodels() {
		return m_submodels;
	}
	@JsonProperty("submodels")
	public void setSubmodels(List<SubmodelReference> submodels) {
		if ( FStream.from(submodels)
					.flatMapIterable(sm -> sm.getKeys())
					.exists(k -> k.getType() != KeyTypes.Submodel) ) {
			throw new MDTAASModelException("AssetAdministrationShell has invalid submodels");
		}
		
		m_submodels = submodels;
	}

	@JsonProperty("assetInformation")
	public @Nullable AssetInformation getAssetInformation() {
		return m_assetInformation;
	}
	@JsonProperty("assetInformation")
	public void setAssetInformation(@Nullable AssetInformation assetInfo) {
		m_assetInformation = assetInfo;
	}

	@Override
	@JsonProperty("id")
	public String getId() {
		return m_id;
	}
	@Override
	@JsonProperty("id")
	public void setId(String id) {
		m_id = id;
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

	@Override
	@JsonProperty("displayName")
	public LangStringSet getDisplayName() {
		return m_displayName;
	}

	@Override
	@JsonProperty("displayName")
	public void setDisplayName(LangStringSet displayName) {
		m_displayName = displayName;
	}

	@Override
	@JsonProperty("description")
	public LangStringSet getDescription() {
		return m_description;
	}

	@Override
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
	@JsonProperty("administration")
	public AdministrativeInformation getAdministration() {
		return m_administration;
	}

	@Override
	@JsonProperty("administration")
	public void setAdministration(AdministrativeInformation administration) {
		m_administration = administration;
	}
	
	@JsonProperty("derivedFrom")
	public AssetAdministrationShellReference getDerivedFrom() {
		return m_derivedFrom;
	}
	@JsonProperty("derivedFrom")
	public void setDerivedFrom(AssetAdministrationShellReference ref) {
		m_derivedFrom = ref;
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
	
	
	
	
	@JsonProperty("identification") @Nullable private IdentificationV1 m_identification;
	@JsonProperty("identification")
	public IdentificationV1 getIdentification() {
		return m_identification;
	}
	@JsonProperty("identification")
	public void setIdentification(IdentificationV1 id) {
		m_identification = id;
	}

	private Map<String, Object> m_additionalProperties = new HashMap<String, Object>();
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.m_additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.m_additionalProperties.put(name, value);
	}
}
