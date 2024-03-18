package mdt.aas.model;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class AssetInformation {
	@JsonProperty("assetKind") private AssetKind m_assetKind;
	@JsonProperty("specificAssetIds") private List<SpecificAssetId> m_specificAssetIds;
	@JsonProperty("globalAssetId") @Nullable private GlobalReference m_globalAssetId;
	@JsonProperty("thumbnail") @Nullable private Resource m_thumbnail;
	
	@JsonProperty("assetKind")
	public AssetKind getAssetKind() {
		return m_assetKind;
	}
	@JsonProperty("assetKind")
	public void setAssetKind(AssetKind assetKind) {
		m_assetKind = assetKind;
	}
	
	@JsonProperty("specificAssetId")
	public List<SpecificAssetId> getSpecificAssetIds() {
		return m_specificAssetIds;
	}
	@JsonProperty("specificAssetId")
	public void setSpecificAssetIds(List<SpecificAssetId> specificAssetId) {
		m_specificAssetIds = specificAssetId;
	}
	
	@JsonProperty("globalAssetId")
	public @Nullable GlobalReference getGlobalAssetId() {
		return m_globalAssetId;
	}
	@JsonProperty("globalAssetId")
	public void setGlobalAssetId(@Nullable GlobalReference globalAssetId) {
		m_globalAssetId = globalAssetId;
	}

	@JsonProperty("thumbnail")
	public @Nullable Resource getThumbnail() {
		return m_thumbnail;
	}
	@JsonProperty("thumbnail")
	public void setThumbnail(@Nullable Resource thumbnail) {
		m_thumbnail = thumbnail;
	}
}
