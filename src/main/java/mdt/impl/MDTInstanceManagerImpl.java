package mdt.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nullable;

import com.github.dockerjava.api.model.Image;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import utils.func.FOption;
import utils.func.NoSuchValueException;
import utils.func.Unchecked;
import utils.stream.FStream;

import mdt.docker.DockerImageId;
import mdt.docker.MDTDocker;
import mdt.harbor.HarborImageId;
import mdt.harbor.HarborImpl;
import mdt.harbor.HarborProjectImpl;
import mdt.harbor.MDTHarborException;
import mdt.impl.MDTInstanceStore.Record;
import mdt.kubernetes.KubernetesRemote;
import mdt.model.EnvironmentSummary;
import mdt.model.IdPair;
import mdt.model.MDTInstance;
import mdt.model.MDTInstanceExistsException;
import mdt.model.MDTInstanceManager;
import mdt.model.MDTInstanceManagerException;
import mdt.model.MDTInstanceNotFoundException;
import mdt.model.SubmodelExistsException;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTInstanceManagerImpl implements MDTInstanceManager {
	private static final String EMPTY_ENDPOINT = null;
	private static final int DEFAULT_K8S_PORT = 6443;
	
	private final MDTInstanceStore m_instStore;
	private final HarborProjectImpl m_instanceProject;
	private final MDTDocker m_docker;
	private final KubernetesRemote m_k8s;
	
	public MDTInstanceManagerImpl(MDTInstanceStore instStore, HarborImpl harbor, MDTDocker docker,
									KubernetesRemote k8s, MDTConfig config) {
		m_instStore = instStore;
		m_instanceProject = harbor.getProject(config.getInstanceProject());
		m_docker = docker;
		m_k8s = k8s;
	}
	
//	public static void format(MDTConfig config) throws MDTInstanceManagerException {
//		try {
//			JdbcProcessor jdbc = config.newJdbcProcessor();
//			try ( Connection conn = jdbc.connect() ) {
//				MDTInstanceStore.dropTable(conn);
//				MDTInstanceStore.createTable(conn);
//			}
//		}
//		catch ( SQLException e ) {
//			throw new MDTInstanceManagerException("fails to format MDTInstance, cause=" + e);
//		}
//	}
	
	MDTInstanceStore getInstanceStore() {
		return m_instStore;
	}

	@Override
	public MDTInstanceImpl getInstance(String instanceId) throws MDTInstanceManagerException {
		Objects.requireNonNull(instanceId);
		
		try {
			Record rec = readInstanceRecord(instanceId);
			return MDTInstanceImpl.create(this, rec);
		}
		catch ( NoSuchValueException e ) {
			throw new MDTInstanceNotFoundException(instanceId);
		}
		catch ( MDTHarborException | SQLException e ) {
			throw new MDTInstanceManagerException("fails to get an instance, cause=" + e);
		}
	}

	@Override
	public List<MDTInstance> getInstanceAll() throws MDTInstanceManagerException {
		try {
			return FStream.from(m_instStore.getRecordAll())
							.map(rec -> MDTInstanceImpl.create(this, rec))
							.cast(MDTInstance.class)
							.toList();
		}
		catch ( SQLException e ) {
			throw new MDTInstanceManagerException("fails to get instance list, cause=" + e);
		}
	}
	
	public @Nullable String getHarborTaggedImage(String localImageId) {
		final DockerImageId dockerId = DockerImageId.parse(localImageId);
		
		// 등록시킬 docker image을 tag를 통해 찾는다.
		FOption<Image> oimage = m_docker.getImage(dockerId);
		if ( oimage.isAbsent() ) {
			throw new MDTInstanceManagerException("Docker image not found: image=" + localImageId);
		}
		
		// Harbor에 등록시킬 tag가 부여된 image가 Docker에 존재하는지 확인하여
		// 존재하면 해당 image id를 반환하고, 그렇지 않다면 null을 반환한다.
		final HarborImageId tag = HarborImageId.from(m_instanceProject, dockerId);
		DockerImageId harborImageId = DockerImageId.parse(tag.getFullNameWithTag());
		if ( m_docker.getImage(harborImageId).isPresent() ) {
			return tag.getFullNameWithTag();
		}
		else {
			return null;
		}
	}
	
	public String tagImageForHarbor(String localImageId) {
		final DockerImageId dockerId = DockerImageId.parse(localImageId);
		
		// 등록시킬 docker image을 tag를 통해 찾는다.
		FOption<Image> oimage = m_docker.getImage(dockerId);
		if ( oimage.isAbsent() ) {
			throw new MDTInstanceManagerException("Docker image not found: image=" + localImageId);
		}
		
		// Harbor에 등록시킬 tag가 부여된 image가 Docker에 존재하는지 확인하여
		// 존재하지 않는다면, tag를 부여한다.
		final HarborImageId tag = HarborImageId.from(m_instanceProject, dockerId);
		DockerImageId harborImageId = DockerImageId.parse(tag.getFullNameWithTag());
		if ( m_docker.getImage(harborImageId).isPresent() ) {
			throw new IllegalArgumentException("tagged image already exists: id=" + tag);
		}

		m_docker.tagImage(oimage.get(), tag);
		return tag.getFullNameWithTag();
	}
	
	public void removeTag(String id) {
		m_docker.removeTag(id);
	}

	@Override
	public String addInstance(String id, String imageId, File aasEnvFile, @Nullable Duration timeout)
		throws MDTInstanceManagerException, TimeoutException, InterruptedException {
		final HarborImageId harborId = HarborImageId.parse(imageId);
		
		// Harbor에 등록시킬 tag가 부여된 image가 Harbor에 이미 존재하는 확인하여
		// 존재하는 경우 예외를 발생시킨다.
		if ( m_instanceProject.getRepository(harborId.getArtifactName()).isPresent() ) {
			throw new MDTInstanceExistsException(""+harborId + " at Harbor");
		}

		try {
			EnvironmentSummary envSummary = readEnvironmentSummary(aasEnvFile);
			
			// Harbor에 등록시킬 tag가 부여된 image가 Docker에 존재하는지 확인하여
			// 존재하지 않는다면, tag를 부여한다.
			final DockerImageId dockerId = harborId.toDockerImageId();
			if ( m_docker.getImage(dockerId).isAbsent() ) {
				throw new MDTInstanceNotFoundException("image-id=" + imageId);
			}
			
			// Tag된 image를 Harbor에 등록시킨다.
			m_docker.pushToHarbor(harborId, timeout);
			
			// 등록된 image 및 AAS 정보를 instance database에 저장한다.
			try {
				Record rec = new Record(id, dockerId.getId(), envSummary,
										EMPTY_ENDPOINT);
				m_instStore.addRecord(rec);
			}
			catch ( SQLException e ) {
				Unchecked.runOrIgnore(() -> m_instanceProject.removeRepository(harborId.getArtifactName()));
				throw new MDTInstanceManagerException("fails to register an instance: image=" + imageId, e);
			}
		}
		catch ( IOException e ) {
			String params = String.format("id=%s, aas=%s", imageId, aasEnvFile);
			throw new MDTInstanceManagerException("fails to register an instance: " + params + ", cause=" + e);
		}
		
		return harborId.getArtifactName();
	}

	@Override
	public void removeInstance(String id) {
		MDTInstanceImpl inst = getInstance(id);
		
		Unchecked.runOrIgnore(() -> m_instStore.deleteRecord(id));
		
		HarborImageId harborImageId = HarborImageId.parse(inst.getImageId());
		Unchecked.runOrIgnore(() -> m_instanceProject.removeRepository(harborImageId.getArtifactName()));
	}

	@Override
	public void removeInstanceAll() {
		Unchecked.runOrIgnore(() -> m_instStore.deleteRecordAll());
		Unchecked.runOrIgnore(() -> m_instanceProject.removeRepositoryAll());
	}
	
	KubernetesRemote getKubernetesRemote() {
		return m_k8s;
	}
	
	Record readInstanceRecord(String instanceId) throws SQLException {
		return m_instStore.getRecordByInstanceId(instanceId);
	}
	
	private EnvironmentSummary readEnvironmentSummary(File aasEnvFile)
		throws IOException, SubmodelExistsException {
		DocumentContext ctxt = JsonPath.parse(aasEnvFile);

		DocumentContext top = JsonPath.parse((Object)ctxt.read("$.assetAdministrationShells[0]"));
		IdPair assIdPair = IdPair.of(getId(top), getIdShort(top));
		
		// Environment의 submodels section에서 등록된 submodel들의 id와 idShort를 수집한다.
		// AssetAdministrationShell에서 정의 submodel들에서는 idShort가 정의되지 않기 때문에
		// Environment의 submodels section에서 idShort 값을 획득한다.
		Map<String,String> submodelMap = Maps.newLinkedHashMap();
		List<Object> submodels = ctxt.read("$.submodels[*]", List.class);
		for ( Object submodelJson: submodels ) {
			DocumentContext submodelCtxt = JsonPath.parse(submodelJson);
			String submodelId = getId(submodelCtxt);
			String prevIdShort = submodelMap.put(submodelId, getIdShort(submodelCtxt));
			if ( prevIdShort != null ) {
				throw new SubmodelExistsException(submodelId + ", aas_id=" + assIdPair.getId());
			}
		}
		
		@SuppressWarnings("unchecked")
		List<String> submodeIdRefs = top.read("$.submodels[*].keys[*].value", List.class);
		List<IdPair> submodelIdPairs = Lists.newArrayList();
		for ( String id: submodeIdRefs ) {
			if ( FStream.from(submodelIdPairs).exists(pair -> id.equals(pair.getId())) ) {
				throw new SubmodelExistsException("ref=" + id + ", aas_id=" + assIdPair.getId());
			}
			submodelIdPairs.add(IdPair.of(id, submodelMap.get(id)));
		}
		return new EnvironmentSummary(assIdPair, submodelIdPairs);
	}
	
	private String getId(DocumentContext parent) {
		try {
			return parent.read("$.id", String.class);
		}
		catch ( PathNotFoundException expected ) {
			return parent.read("$.identification.id", String.class);
		}
	}
	
	private @Nullable String getIdShort(DocumentContext parent) {
		try {
			return parent.read("$.idShort", String.class);
		}
		catch ( PathNotFoundException expected ) {
			return null;
		}
	}

//	@Override
//	public List<Tuple<Image,HarborDockerTag>> listLocalInstanceAll() throws MDTInstanceManagerException {
//		List<Image> images = m_docker.listImagesCmd().exec();
//		String prefix = String.format("%s/%s", m_harbor.getEndpoint(), m_harborProject.getName());
//		
//		return FStream.from(images)
//						.flatMapFOption(img -> getDockerTag(img, prefix).map(dt -> Tuple.of(img, dt)))
//						.toList();
//	}
//	
//	private FOption<HarborDockerTag> getDockerTag(Image image, String prefix) {
//		return FStream.of(image.getRepoTags())
//						.filter(tag -> tag.startsWith(prefix))
//						.map(HarborDockerTag::parse)
//						.findFirst();
//	}
}
