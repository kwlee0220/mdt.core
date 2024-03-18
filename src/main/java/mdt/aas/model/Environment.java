package mdt.aas.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import mdt.aas.model.submodel.Submodel;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class Environment {
	@JsonProperty("assetAdministrationShells") private List<AssetAdministrationShell> m_assetAdministrationShells;
	@JsonProperty("submodels") private List<Submodel> m_submodels;
	@JsonProperty("conceptDescriptions") private List<ConceptDescription> m_conceptDescriptions;
	
	@JsonProperty("assetAdministrationShells")
	public List<AssetAdministrationShell> getAssetAdministrationShells() {
		return m_assetAdministrationShells;
	}
	@JsonProperty("assetAdministrationShells")
	public void setAssetAdministrationShells(List<AssetAdministrationShell> assetAdministrationShells) {
		m_assetAdministrationShells = assetAdministrationShells;
	}

	@JsonProperty("submodels")
	public List<Submodel> getSubmodel() {
		return m_submodels;
	}
	@JsonProperty("submodels")
	public void setSubmodel(List<Submodel> submodels) {
		m_submodels = submodels;
	}

	@JsonProperty("conceptDescriptions")
	public List<ConceptDescription> getConceptDescriptions() {
		return m_conceptDescriptions;
	}
	@JsonProperty("conceptDescriptions")
	public void setConceptDescriptions(List<ConceptDescription> conceptDescriptions) {
		m_conceptDescriptions = conceptDescriptions;
	}
}
