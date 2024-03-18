package mdt.aas.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public interface HasExtentions {
	@JsonProperty("extension")
	public List<Extension> getExtension();
	@JsonProperty("extension")
	public void setExtension(List<Extension> extension);
	
}
