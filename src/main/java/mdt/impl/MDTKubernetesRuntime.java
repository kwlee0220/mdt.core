package mdt.impl;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTKubernetesRuntime {
	private final Config config;
	
	public static final String NAMESPACE = "mdt-instance";
	
	private MDTKubernetesRuntime(Config config) {
		this.config = config;
	}
	
	public static MDTKubernetesRuntime connect(String host) {
		return connect(host, 6443);
	}
	
	public static MDTKubernetesRuntime connect(String host, int port) {
		Config config = new ConfigBuilder()
							.withMasterUrl(String.format("https://%s:%d", host, port))
							.withNamespace("mdt-instance")
							.build();
		MDTKubernetesRuntime rt = new MDTKubernetesRuntime(config);
		try ( KubernetesClient client = rt.newClient() ) {
			Namespace ns = rt.getNamespace(client, NAMESPACE);
			if ( ns == null ) {
				ns = new NamespaceBuilder()
							.withNewMetadata()
								.withName(NAMESPACE)
							.endMetadata()
						.build();
				client.namespaces().resource(ns).create();
			}
		}
		
		return rt;
	}
	
	public KubernetesClient newClient() {
		return new KubernetesClientBuilder().withConfig(this.config).build();
	}
	
	public Namespace getNamespace(KubernetesClient client, String name) {
		return client.namespaces().withName(name).get();
	}
	
	public Service getService(KubernetesClient client, String name) {
		return client.services()
					.inNamespace(NAMESPACE)
					.withName(String.format("service-%s", name))
					.get();
	}
	
	public void deleteService(KubernetesClient client, String name) {
		Service svc = getService(client, name);
		if ( svc != null ) {
			deleteService(client, svc);
		}
	}
	
	public void deleteService(KubernetesClient client, Service dep) {
		client.resource(dep).delete();
	}
	
	public Deployment getDeployment(KubernetesClient client, String name) {
		return client.apps()
					.deployments()
					.inNamespace(NAMESPACE)
					.withName(String.format("deployment-%s", name))
					.get();
	}
	
	public void deleteDeployment(KubernetesClient client, String name) {
		Deployment deployment = getDeployment(client, name);
		if ( deployment != null ) {
			deleteDeployment(client, deployment);
		}
	}
	
	public void deleteDeployment(KubernetesClient client, Deployment dep) {
		client.resource(dep).delete();
	}
}
