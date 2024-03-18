package mdt.aas.model;

import java.util.List;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public interface HasDataSpecification {
	public List<Reference> getDataSpecifications();
	public void setDataSpecifications(List<Reference> dataSpecifications);
}
