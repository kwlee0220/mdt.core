package mdt.test;

import java.util.Collections;

import com.google.common.base.Preconditions;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import mdt.model.MDTInstance;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTK8sInstance {
	private final MDTKubernetesRuntime runtime;
	
	public MDTK8sInstance(MDTKubernetesRuntime runtime) {
		this.runtime = runtime;
	}
	
	public String start(String name, int port, String image) {
		return start(name, port, image, 1);
	}

	public String start(String name, int port, String image, int replicas) {
		Preconditions.checkArgument(replicas >= 1);
		
		Deployment deployment = null;
		try ( KubernetesClient client = this.runtime.newClient() ) {
			deployment = deploy(client, name, port, image, replicas);
			try {
				return service(client, name, port);
			}
			catch ( Exception e ) {
				this.runtime.deleteDeployment(client, deployment);
				throw e;
			}
		}
	}

	public void stop(String name) {
		try ( KubernetesClient client = this.runtime.newClient() ) {
			try {
				this.runtime.deleteService(client, name);
			}
			catch ( Exception ignored ) { }
			
			this.runtime.deleteDeployment(client, name);
		}
	}
	
	private Deployment deploy(KubernetesClient client, String name, int port, String image, int replicas) {
		Preconditions.checkArgument(replicas >= 1);
		
        Deployment deployment = new DeploymentBuilder()
									.withNewMetadata()
										.withName(String.format("deployment-%s", name))
									.endMetadata()
									.withNewSpec()
										.withReplicas(replicas)
										.withNewSelector()
											.addToMatchLabels("mdt-instance", name)
										.endSelector()
										.withNewTemplate()
											.withNewMetadata()
												.addToLabels("mdt-instance", name)
											.endMetadata()
											.withNewSpec()
												.addNewContainer()
													.withName(String.format("pod-%s", name))
													.withImage(image)
													.addNewPort()
														.withContainerPort(port)
													.endPort()
												.endContainer()
											.endSpec()
										.endTemplate()
									.endSpec()
								.build();
		return client.apps()
					.deployments()
					.inNamespace(MDTKubernetesRuntime.NAMESPACE)
					.resource(deployment)
					.create();
	}
	
	private String service(KubernetesClient client, String name, int port) {
		Service service = new ServiceBuilder()
								.withNewMetadata()
									.withName(String.format("service-%s", name))
								.endMetadata()
								.withNewSpec()
									.withSelector(Collections.singletonMap("mdt-instance", name))
									.addNewPort()
										.withName("service-port")
										.withProtocol("TCP")
										.withPort(port)
//										.withTargetPort(new IntOrString(port))
									.endPort()
									.withType("NodePort")
							    .endSpec()
						    .build();
		
		service = client.services()
						.inNamespace(MDTKubernetesRuntime.NAMESPACE)
						.resource(service)
						.create();
		return client.services().inNamespace(MDTKubernetesRuntime.NAMESPACE)
						.withName(service.getMetadata().getName())
						.getURL("service-port");
	}
}
