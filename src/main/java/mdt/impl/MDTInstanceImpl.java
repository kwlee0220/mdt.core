package mdt.impl;

import java.util.List;

import com.google.common.base.Objects;

import mdt.impl.MDTInstanceStore.Record;
import mdt.model.MDTInstance;
import mdt.model.MDTInstanceStatus;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTInstanceImpl implements MDTInstance {
	private Record m_record;
	private MDTInstanceStatus m_status;
	
	public static final MDTInstanceImpl create(Record rec) {
		return new MDTInstanceImpl(rec, MDTInstanceStatus.STOPPED);
	}
	
	private MDTInstanceImpl(Record record, MDTInstanceStatus status) {
		m_record = record;
		m_status = status;
	}

	@Override
	public String getAssId() {
		return m_record.getAASId();
	}

	@Override
	public String getTag() {
		return m_record.getTag();
	}

	@Override
	public String getId() {
		return m_record.getInstanceId();
	}

	@Override
	public List<String> getSubmodelList() {
		return m_record.getSubModelIdList();
	}

	@Override
	public String getServiceEndpoint() {
		return m_record.getServiceEndpoint();
	}

	@Override
	public MDTInstanceStatus getStatus() {
		return m_status;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		else if ( this == null || getClass() != MDTInstanceImpl.class ) {
			return false;
		}
		
		MDTInstanceImpl other = (MDTInstanceImpl)obj;
		return Objects.equal(getId(), other.getId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}
	
	@Override
	public String toString() {
		return String.format("image=%s, aas=%s, endpoint=%s, status=%s",
								m_record.getInstanceId(), m_record.getAASId(), m_record.getServiceEndpoint(),
								m_status);
	}
}
