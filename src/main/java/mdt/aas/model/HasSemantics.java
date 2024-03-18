package mdt.aas.model;

import java.util.List;

import javax.annotation.Nullable;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public interface HasSemantics {
	@Nullable public Reference getSemanticId();
	public void setSemanticId(Reference semanticId);
	
	@Nullable public List<Reference> getSupplementalSemanticIds();
	public void setSupplementalSemanticIds(List<Reference> supplementalSemanticIds);
}
