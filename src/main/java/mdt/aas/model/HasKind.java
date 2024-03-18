package mdt.aas.model;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public interface HasKind {
	@JsonProperty("kind") @Nullable
	public ModelingKind getKind();
	
	@JsonProperty("kind")
	public void setKind(ModelingKind kind);
}
