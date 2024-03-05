package mdt.aas;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.stream.FStream;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class AssetAdministrationShell {
	@JsonProperty("modelType")
	private ModelType m_modelType;
	@JsonProperty("identification")
	private Identification m_identification;
	@JsonProperty("assetInformation")
	private AssetInformation m_assetInformation;
	@JsonProperty("submodels")
	private List<Submodel> m_submodels;
	@JsonProperty("idShort")
	private String m_idShort;
	
	public static AssetAdministrationShell parseJson(File jsonFile)
		throws StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonFile, AssetAdministrationShell.class);
	}
	
	public String getId() {
		return m_identification.id;
	}

	@JsonProperty("idShort")
	public String getIdShort() {
		return m_idShort;
	}
	@JsonProperty("idShort")
	public void setIdShort(String idShort) {
		m_idShort = idShort;
	}
	
	public List<String> getSubmodelIds() {
		return FStream.from(m_submodels)
						.flatMap(sm -> FStream.from(sm.keys))
						.filter(modelKey -> modelKey.type.equals("Submodel"))
						.map(SubmodelKey::value)
						.toList();
	}
	
	public List<String> getSubmodelShortIds() {
		int prefixLen = getId().length() + 1;
		return FStream.from(getSubmodelIds())
						.map(id -> id.substring(prefixLen))
						.toList();
	}

	@JsonProperty("identification")
	public Identification getIdentification() {
		return m_identification;
	}
	
	@JsonProperty("identification")
	public void setIdentification(Identification id) {
		this.m_identification = id;
	}

	@JsonProperty("modelType")
	public ModelType getModelType() {
		return m_modelType;
	}
	
	@JsonProperty("modelType")
	public void setModelType(ModelType type) {
		this.m_modelType = type;
	}

	@JsonProperty("assetInformation")
	public AssetInformation getAssetInformation() {
		return m_assetInformation;
	}
	
	@JsonProperty("assetInformation")
	public void setAssetInformation(AssetInformation assetInfo) {
		this.m_assetInformation = assetInfo;
	}

	@JsonProperty("submodels")
	public List<Submodel> getSubmodels() {
		return m_submodels;
	}
	
	@JsonProperty("submodels")
	public void setSubmodels(List<Submodel> subModels) {
		this.m_submodels = subModels;
	}
	
	@JsonIgnore
	private Map<String, Object> m_additionalProperties = new HashMap<String, Object>();

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.m_additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.m_additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		String submodelIds = FStream.from(getSubmodelShortIds()).join(", ", "[", "]");
		return String.format("id=%s (%s), submodels=%s", getId(), getIdShort(), submodelIds);
	}

	public record Identification(String idType, String id) { }
	public record ModelType(String name) { }
	public record GlobalAssetId(List<String> keys) { }
	public record AssetInformation(String assetKind, GlobalAssetId globalAssetId) { }

	public static record SubmodelKey(String idType, String type, String value) { }
	public record Submodel(List<SubmodelKey> keys) { }
}
