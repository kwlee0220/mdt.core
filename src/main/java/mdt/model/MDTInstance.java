package mdt.model;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public interface MDTInstance {
	public String getId();
	
	public EnvironmentSummary getEnvironmentSummary();
	
	public String getServiceEndpoint();
	public MDTInstanceStatus getStatus();
	
	public default boolean isRunning() {
		return getStatus() == MDTInstanceStatus.RUNNING;
	}
	public default boolean isStopped() {
		return getStatus() == MDTInstanceStatus.STOPPED;
	}
	
	public String start();
	public void stop();
}
