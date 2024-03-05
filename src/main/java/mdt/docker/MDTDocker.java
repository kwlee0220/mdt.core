package mdt.docker;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.PushImageResultCallback;

import utils.func.FOption;
import utils.stream.FStream;

import mdt.harbor.HarborConfig;
import mdt.harbor.HarborTag;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTDocker implements Closeable {
	private final DockerClient m_dockerClient;
	
	private MDTDocker(DockerClient docker) {
		m_dockerClient = docker;
	}
	
	public static MDTDocker get(DockerConfig dockerConfig, HarborConfig harborConfig) {
		DefaultDockerClientConfig clientConfig
			= DefaultDockerClientConfig.createDefaultConfigBuilder()
					.withDockerHost(dockerConfig.getHost())
					.withRegistryUsername(harborConfig.getUserId())
					.withRegistryPassword(harborConfig.getPassword())
					.withRegistryEmail(harborConfig.getEmail())
					.withRegistryUrl(harborConfig.getEndpoint())
					.build();
		DockerClient client = DockerClientBuilder.getInstance(clientConfig).build();
		return new MDTDocker(client);
	}
	
	@Override
	public void close() throws IOException {
		m_dockerClient.close();
	}
	
	public List<Image> getImageAll() {
		return m_dockerClient.listImagesCmd().exec();
	}
	
	public FOption<Image> getImage(DockerImageId imageId) {
		String key = imageId.toString();
		return FStream.from(getImageAll())
						.findFirst(img -> FStream.of(img.getRepoTags()).exists(key::equals));
	}
	
	public void tagImage(Image image, HarborTag path) {
		m_dockerClient.tagImageCmd(image.getId(), path.getFullName(), path.getTag()).exec();
	}
	
	public void removeTag(HarborTag path) {
		m_dockerClient.removeImageCmd(path.getFullNameWithTag()).exec();
	}
	
	public void pushToHarbor(HarborTag path, FOption<Duration> timeout)
		throws TimeoutException, InterruptedException {
		PushImageResultCallback cb = m_dockerClient.pushImageCmd(path.getFullNameWithTag())
													.exec(new PushImageResultCallback());
		if ( timeout.isPresent() ) {
			if ( !cb.awaitCompletion(timeout.get().getSeconds(), TimeUnit.SECONDS) ) {
				throw new TimeoutException();
			}
		}
		cb.awaitSuccess();
	}
}
