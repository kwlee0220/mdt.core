package mdt.model;

import java.util.List;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public interface MDTInstance {
	public String getId();
	public String getTag();
	
	public String getAssId();
	public List<String> getSubmodelList();
	
	public String getServiceEndpoint();
	public MDTInstanceStatus getStatus();
}
