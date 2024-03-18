package mdt.kubernetes;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class KubernetesRemote implements Closeable {
	private final KubernetesClient m_client;
	
	public static KubernetesRemote connect(KubernetesConfig kconfig) {
		KubernetesClient client = new KubernetesClientBuilder().build();
		return new KubernetesRemote(client);
	}
	
	private KubernetesRemote(KubernetesClient client) {
		m_client = client;
	}

	@Override
	public void close() throws IOException {
		m_client.close();
	}
	
	public List<Pod> listPods() {
		List<Pod> plist = m_client.pods().inNamespace("default").list().getItems();
		for ( Pod pod: plist ) {
			System.out.println(pod.getMetadata().getName());
		}
		
		return plist;
	}
	
	public Namespace createNamespace(String name) {
		Namespace ns = new NamespaceBuilder()
							.withNewMetadata()
								.withName(name)
							.endMetadata()
						.build();
		return m_client.namespaces().resource(ns).create();
	}
	
	public Namespace getNamespace(String name) {
		return m_client.namespaces().withName(name).get();
	}
	
	public Pod createPod(String ns, Pod resource) {
		return m_client.pods().inNamespace(ns).resource(resource).create();
	}
	
	public int createService(String ns, Service svc) {
		Service service = m_client.services()
								.inNamespace(ns)
								.resource(svc)
								.create();
		return service.getSpec().getPorts().get(0).getNodePort();
	}
	
	public Service getService(String ns, String svcName) {
		return m_client.services()
						.inNamespace(ns)
						.withName(svcName)
						.get();
	}
	
	public void deleteService(String ns, String name) {
		Service svc = getService(ns, name);
		if ( svc != null ) {
			deleteService(svc);
		}
	}
	
	public void deleteService(Service svc) {
		m_client.resource(svc).delete();
	}
	
	public Deployment getDeployment(String ns, String deploymentName) {
		return m_client.apps()
						.deployments()
						.inNamespace(ns)
						.withName(deploymentName)
						.get();
	}
	
	public Deployment createDeployment(String ns, Deployment deployment) {
		return m_client.apps()
						.deployments()
						.inNamespace(ns)
						.resource(deployment)
						.create();
	}
	
	public void deleteDeployment(String ns, String deploymentName) {
		Deployment deployment = getDeployment(ns, deploymentName);
		if ( deployment != null ) {
			deleteDeployment(deployment);
		}
	}
	
	public void deleteDeployment(Deployment dep) {
		m_client.resource(dep).delete();
	}
}
