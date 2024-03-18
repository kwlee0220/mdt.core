package mdt.model;

import java.util.List;

import utils.stream.FStream;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public final class EnvironmentSummary {
	private final IdPair m_aasId;
	private final List<IdPair> m_submodeIdList;
	
	public EnvironmentSummary(IdPair aasId, List<IdPair> submodelIdList) {
		m_aasId = aasId;
		m_submodeIdList = submodelIdList;
	}
	
	public IdPair getAASId() {
		return m_aasId;
	}
	
	public List<IdPair> getSubmodelIdList() {
		return m_submodeIdList;
	}
	
	@Override
	public String toString() {
		String idShortCsv = FStream.from(m_submodeIdList).map(IdPair::getIdShort).join(", ");
		return String.format("%s: {%s}", m_aasId, idShortCsv);
	}

}
