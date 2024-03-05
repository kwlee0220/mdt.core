package mdt.impl;

import java.io.File;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.github.dockerjava.api.model.Image;

import utils.func.FOption;
import utils.func.Unchecked;
import utils.stream.FStream;

import mdt.aas.AssetAdministrationShell;
import mdt.docker.DockerImageId;
import mdt.docker.MDTDocker;
import mdt.harbor.HarborImpl;
import mdt.harbor.HarborProjectImpl;
import mdt.harbor.HarborTag;
import mdt.harbor.MDTHarborException;
import mdt.harbor.Repository;
import mdt.impl.MDTInstanceStore.Record;
import mdt.model.MDTInstance;
import mdt.model.MDTInstanceManager;
import mdt.model.MDTInstanceManagerException;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTInstanceManagerImpl implements MDTInstanceManager {
	private final MDTInstanceStore m_instStore;
	private final HarborProjectImpl m_harborProject;
	private final MDTDocker m_docker;
	
	public MDTInstanceManagerImpl(MDTInstanceStore instStore, HarborImpl harbor, MDTDocker docker) {
		m_instStore = instStore;
		m_harborProject = harbor.getMDTInstanceProject();
		m_docker = docker;
	}

	@Override
	public List<MDTInstance> getInstanceAll() throws MDTInstanceManagerException {
		try {
			List<Repository> repositories = m_harborProject.getRepositoryAll();
			Map<String,Record> records = FStream.from(m_instStore.getRecordAll())
												.toMap(rec -> rec.getInstanceId());
			return FStream.from(repositories)
							.map(repo -> repo.getName().split("/")[1])
							.flatMapNullable(art -> records.get(art))
							.map(MDTInstanceImpl::create)
							.cast(MDTInstance.class)
							.toList();
		}
		catch ( MDTHarborException | SQLException e ) {
			throw new MDTInstanceManagerException(e);
		}
	}

	@Override
	public String addInstance(DockerImageId imageId, String assetShell, boolean force, FOption<Duration> timeout)
		throws MDTInstanceManagerException, TimeoutException, InterruptedException {
		final HarborTag tag = HarborTag.from(m_harborProject, imageId);

		boolean tagCreated = false;
		try {
			String aasId = null;
			List<String> submodelShortIds = null;
			try {
				AssetAdministrationShell shell = AssetAdministrationShell.parseJson(new File(assetShell));
				aasId = shell.getId();
				submodelShortIds = shell.getSubmodelShortIds();
			}
			catch ( Exception e ) {
				throw new MDTInstanceManagerException("fails to register the instance, unable to parse AAS file", e);
			}
			
			// 등록시킬 docker image을 tag를 통해 찾는다.
			FOption<Image> oimage = m_docker.getImage(imageId);
			if ( oimage.isAbsent() ) {
				throw new MDTInstanceManagerException("Docker image not found: image=" + imageId);
			}
			
			if ( m_harborProject.getRepository(tag.getArtifactName()).isAbsent() ) {
				// Harbor 등록하기 위한 tag를 부여하고, push 시킨다.
				DockerImageId harborImageId = DockerImageId.parse(tag.getFullNameWithTag());
				boolean tagExists = m_docker.getImage(harborImageId).isPresent();
				if ( !tagExists ) {
					m_docker.tagImage(oimage.get(), tag);
					tagCreated = true;
				}
				m_docker.pushToHarbor(tag, timeout);
			}
			else if ( !force ) {
				throw new MDTInstanceManagerException("MDTInstance already exists: tag=" + tag);
			}
			
			// 등록된 image 및 AAS 정보를 instance database에 저장한다.
			try {
				Record rec = new Record(tag.getArtifactName(), tag.getTag(), aasId,
										submodelShortIds, null);
				if ( force ) {
					m_instStore.addOrReplaceRecord(rec);
				}
				else {
					m_instStore.addRecord(rec);
				}
			}
			catch ( SQLException e ) {
				m_harborProject.removeRepository(tag.getArtifactName());
				throw new MDTInstanceManagerException("fails to register an instance: image=" + imageId, e);
			}
		}
		finally {
			// Harbor 등록을 위해 임시로 부여한 tag를 삭제한다.
			// Tag 삭제 과정에서 오류가 발생한 경우는 무시한다.
			if ( tagCreated ) {
				Unchecked.runOrIgnore(() -> m_docker.removeTag(tag));
			}
		}
		
		return tag.getArtifactName();
	}

	@Override
	public void removeInstance(String instanceId) {
		Unchecked.runOrIgnore(() -> m_instStore.deleteRecord(instanceId));
		Unchecked.runOrIgnore(() -> m_harborProject.removeRepository(instanceId));
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
