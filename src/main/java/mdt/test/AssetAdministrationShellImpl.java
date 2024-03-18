package mdt.test;

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
public class AssetAdministrationShellImpl {
	@JsonProperty("id") private String m_id;
	@JsonProperty("idShort") private String m_idShort;
	@JsonProperty("assetInformation") private AssetInformation m_assetInformation;
	@JsonProperty("submodels") private List<Submodel> m_submodels;
	
	@JsonIgnore
	private Map<String, Object> m_additionalProperties = new HashMap<String, Object>();
	
	public static AssetAdministrationShellImpl parseJson(File jsonFile)
		throws StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonFile, AssetAdministrationShellImpl.class);
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
	public String getIdShort() {
		return m_idShort;
	}
	@JsonProperty("idShort")
	public void setIdShort(String idShort) {
		m_idShort = idShort;
	}

	@JsonProperty("assetInformation")
	public AssetInformation getAssetInformation() {
		return m_assetInformation;
	}
	
	@JsonProperty("assetInformation")
	public void setAssetInformation(AssetInformation assetInfo) {
		this.m_assetInformation = assetInfo;
	}
	
	public String getAssertKind() {
		return m_assetInformation.assetKind();
	}
	
	public String getGlobalAssetId() {
		return m_assetInformation.globalAssetId();
	}
	
	public List<String> getSubmodelIds() {
		return FStream.from(m_submodels)
						.flatMap(sm -> FStream.from(sm.keys))
						.filter(modelKey -> modelKey.type.equals("Submodel"))
						.map(SubmodelKey::value)
						.toList();
	}
	
	public List<String> getSubmodelShortIds(String prefix) {
		int prefixLen = prefix.length();
		return FStream.from(getSubmodelIds())
						.map(id -> id.startsWith(prefix) ? id.substring(prefixLen) : id)
						.toList();
	}

	@JsonProperty("submodels")
	public List<Submodel> getSubmodels() {
		return m_submodels;
	}
	
	@JsonProperty("submodels")
	public void setSubmodels(List<Submodel> subModels) {
		this.m_submodels = subModels;
	}

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
		String submodelIds = FStream.from(getSubmodelShortIds(getId() + "/")).join(", ", "[", "]");
		return String.format("id=%s (%s), submodels=%s", getId(), getIdShort(), submodelIds);
	}

	public record Identification(String idType, String id) { }
	public record AssetInformation(String assetKind, String globalAssetId) { }

	public static record SubmodelKey(String type, String value) { }
	public record Submodel(List<SubmodelKey> keys, String type) { }
}
