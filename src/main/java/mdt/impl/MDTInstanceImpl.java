package mdt.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import com.google.common.base.Objects;

import utils.func.Unchecked;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import mdt.impl.MDTInstanceStore.Record;
import mdt.kubernetes.KubernetesRemote;
import mdt.model.EnvironmentSummary;
import mdt.model.MDTInstance;
import mdt.model.MDTInstanceManagerException;
import mdt.model.MDTInstanceStatus;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTInstanceImpl implements MDTInstance {
	public static final String NAMESPACE = "mdt-instance";
	
	private final MDTInstanceManagerImpl m_manager;
	private final Record m_record;
	private MDTInstanceStatus m_status;
	
	public static final MDTInstanceImpl create(MDTInstanceManagerImpl manager, Record rec) {
		return new MDTInstanceImpl(manager, rec, MDTInstanceStatus.STOPPED);
	}
	
	private MDTInstanceImpl(MDTInstanceManagerImpl manager, Record record, MDTInstanceStatus status) {
		m_manager = manager;
		m_record = record;
		m_status = status;
	}

	@Override
	public EnvironmentSummary getEnvironmentSummary() {
		return m_record.getEnvironmentSummary();
	}

	@Override
	public String getId() {
		return m_record.getInstanceId();
	}
	
	public String getImageId() {
		return m_record.getImageId();
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
	public String start() {
		try ( KubernetesRemote k8s = m_manager.getKubernetesRemote(); ) {
			Deployment deployment = buildDeploymentResource();
			deployment = k8s.createDeployment(NAMESPACE, deployment);
			
			try {
				Service svc = buildServiceResource();
				
				int svcPort = k8s.createService(NAMESPACE, svc);
				String endpoint = String.format("http://xxx:%d", svcPort);
				
				MDTInstanceStore store = m_manager.getInstanceStore();
				store.updateEndpoint(getId(), endpoint);
				m_record.setServiceEndpoint(endpoint);
				m_status = MDTInstanceStatus.RUNNING;
				
				return endpoint;
			}
			catch ( SQLException e ) {
				Unchecked.runOrIgnore(() -> k8s.deleteService(NAMESPACE, toServiceName(getId())));
				Unchecked.runOrIgnore(() -> k8s.deleteDeployment(NAMESPACE, toDeploymentName(getId())));
				
				throw new MDTInstanceManagerException("fails to update MDTInstance status, cause=" + e);
			}
			catch ( Exception e ) {
				Unchecked.acceptOrIgnore(k8s::deleteDeployment, deployment);
				throw e;
			}
		}
		catch ( IOException e ) {
			throw new MDTInstanceManagerException("fails to connect to Kubernetes, cause=" + e);
		}
	}

	@Override
	public void stop() {
		try ( KubernetesRemote k8s = m_manager.getKubernetesRemote() ) {
			Unchecked.runOrIgnore(() -> k8s.deleteService(NAMESPACE, toServiceName(getId())));
			Unchecked.runOrIgnore(() -> k8s.deleteDeployment(NAMESPACE, toDeploymentName(getId())));
			
			try {
				MDTInstanceStore store = m_manager.getInstanceStore();
				store.updateEndpoint(getId(), null);
			}
			catch ( SQLException e ) {
				throw new MDTInstanceManagerException("fails to update MDTInstance status, cause=" + e);
			}
		}
		catch ( IOException e ) {
			throw new MDTInstanceManagerException("fails to connect to Kubernetes, cause=" + e);
		}
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
								m_record.getInstanceId(), m_record.getEnvironmentSummary().getAASId(),
								m_record.getServiceEndpoint(), m_status);
	}
	
	private static String toDeploymentName(String instanceId) {
		return String.format("deploy-%s", instanceId);
	}
	
	private static String toServiceName(String instanceId) {
		return String.format("service-%s", instanceId);
	}
	
	private static String toPodName(String instanceId) {
		return String.format("pod-%s", instanceId);
	}
	
	private static String toContainerName(String instanceId) {
		return String.format("container-%s", instanceId);
	}
	
	private Pod buildPodResource() {
		return new PodBuilder()
					.withNewMetadata()
						.withName("nginx")
					.endMetadata()
					.withNewSpec()
						.addNewContainer()
							.withName("nginx")
							.withImage("nginx")
							.addNewPort()
								.withContainerPort(80)
							.endPort()
						.endContainer()
					.endSpec()
				.build();
	}
	
	private Deployment buildDeploymentResource() {
        return new DeploymentBuilder()
						.withNewMetadata()
							.withName(toDeploymentName(getId()))
						.endMetadata()
						.withNewSpec()
							.withReplicas(1)
							.withNewSelector()
								.addToMatchLabels("mdt-type", "instance")
								.addToMatchLabels("mdt-instance-id", getId())
							.endSelector()
							.withNewTemplate()
								.withNewMetadata()
									.withName(toPodName(getId()))
									.addToLabels("mdt-type", "instance")
									.addToLabels("mdt-instance-id", getId())
								.endMetadata()
								.withNewSpec()
									.addNewContainer()
										.withName(toContainerName(getId()))
										.withImage(getImageId())
										.addNewPort()
											.withContainerPort(80)
										.endPort()
									.endContainer()
								.endSpec()
							.endTemplate()
						.endSpec()
					.build();
	}
	
	private Service buildServiceResource() {
		return new ServiceBuilder()
					.withNewMetadata()
						.withName(toServiceName(getId()))
					.endMetadata()
					.withNewSpec()
						.withType("NodePort")
						.withSelector(Collections.singletonMap("mdt-instance-id", getId()))
						.addNewPort()
							.withName("service-port")
							.withProtocol("TCP")
							.withPort(80)
						.endPort()
				    .endSpec()
			    .build();
	}
}
