package mdt.model;

import com.google.common.base.Objects;

import io.fabric8.kubernetes.api.model.Pod;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTInstanceRuntimeInfo {
	private final String m_name;
	private final MDTInstanceStatus m_status;
	
	public static final MDTInstanceRuntimeInfo fromPod(Pod pod) {
		MDTInstanceStatus status = null;
		switch ( pod.getStatus().getPhase() ) {
			case "Running":
				status = MDTInstanceStatus.RUNNING;
				break;
			default:
				throw new AssertionError();
		}
		
		return new MDTInstanceRuntimeInfo(pod.getMetadata().getName(), status);
	}
	
	private MDTInstanceRuntimeInfo(String name, MDTInstanceStatus status) {
		this.m_name = name;
		this.m_status = status;
	}
	
	public String getName() {
		return m_name;
	}
	
	public MDTInstanceStatus getStatus() {
		return m_status;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		else if ( this == null || getClass() != MDTInstanceRuntimeInfo.class ) {
			return false;
		}
		
		MDTInstanceRuntimeInfo other = (MDTInstanceRuntimeInfo)obj;
		return Objects.equal(m_name, other.m_name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(m_name);
	}
	
	@Override
	public String toString() {
		return String.format("NAME=%s, STATUS=%s", m_name, m_status);
	}
}
