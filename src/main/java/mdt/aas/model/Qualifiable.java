package mdt.aas.model;

import java.util.List;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public interface Qualifiable {
	public List<Qualifier> getQualifiers();
	public void setQualifiers(List<Qualifier> qualifier);
}
