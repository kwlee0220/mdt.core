package mdt.model;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nullable;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public interface MDTInstanceManager {
	public static enum MatchKeyType {
		AAS_ID,
		AAS_ID_SHORT,
		SUBMODEL_ID,
		SUBMODEL_ID_SHORT,
	};
	
	/**
	 * 주어진 식별자에 해당하는 {@link MDTInstance} 객체를 반환한다.
	 * 
	 * @param id	검색 대상 식별자.
	 * @return		식별자에 해당하는 {@link MDTInstance} 객체.
	 * 				만일 식별자에 해당하는 MDT instance가 없는 경우는
	 * 				{@link MDTInstanceNotFoundException} 예외가 발생된다.
	 * @throws MDTInstanceManagerException
	 */
	public MDTInstance getInstance(String id) throws MDTInstanceManagerException;

	/**
	 * 주어진 식별자에 해당하는 {@link MDTInstance} 객체를 반환한다.
	 * 만일 식별자에 해당하는 MDT instance가 없는 경우에는 {@code null}을 반환한다.
	 * 
	 * @param id	검색 대상 식별자.
	 * @return		식별자에 해당하는 {@link MDTInstance} 객체.
	 * 				만일 식별자에 해당하는 MDT instance가 없는 경우는 {@code null}.
	 * @throws MDTInstanceManagerException
	 */
	public default @Nullable MDTInstance getInstanceOrNull(String id) throws MDTInstanceManagerException {
		try {
			return getInstance(id);
		}
		catch ( MDTInstanceNotFoundException expected ) {
			return null;
		}
	}
	
	/**
	 * MDT 시스템에 등록된 모든 {@link MDTInstance}를 반환한다.
	 * 
	 * @return	등록된 {@link MDTInstance}들의 리스트.
	 * @throws MDTInstanceManagerException
	 */
	public List<MDTInstance> getInstanceAll() throws MDTInstanceManagerException;

	/**
	 * Docker에 저장된 image를 MDT instace로 등록시킨다.
	 * 
	 * @param id			저장할 MDTInstance의 식별자.
	 * @param dockerImageId		대상 docker image를 지정하기 위한 docker image id.
	 * 						Image id는 '<repo>:<tag>' 형식으로 지정한다. 만일 '<tag>'가 지정되지
	 * 						않은 경우는 'latest'로 간주한다.
	 * 						Tag는 "<repository>:<tag>" 형식으로 제공되어야 한다.
	 * @param aasEnvFile	등록할 asset의 AAS Environment 정보 파일 경로.
	 * @param timeout		등록 소요시간 timeout.
	 * 						등록 소요시간이 주어진 시간보다 길어지는 경우에는
	 * 						{@link TimeoutException} 예외가 발생한다.
	 * 						timeout 값이 <code>null</code>인 경우는 무한 대기를 의미한다.
	 * @return 등록된 MDT Instace에 부여된 식별자.
	 * @throws TimeoutException 등록 소요시간이 초과한 경우.
	 * @throws InterruptedException	등록 과정 중 쓰레드가 중단된 경우.
	 * @throws MDTInstanceManagerException	기타 다른 이유로 MDTInstance 등록에 실패한 경우.
	 */
	public String addInstance(String id, String imageId, File aasEnvFile, @Nullable Duration timeout)
		throws MDTInstanceManagerException, TimeoutException, InterruptedException;
	
	/**
	 * 등록된 MDT Instance을 해제시킨다.
	 * 
	 * @param id	해제시킬 MDT Instance의 식별자.
	 * @throws MDTInstanceManagerException	등록 해제가 실패한 경우.
	 */
	public void removeInstance(String id) throws MDTInstanceManagerException;

	/**
	 * MDT 시스템에 등록된 모든 {@link MDTInstance}를 등록 해제시킨다.
	 * 
	 * @throws MDTInstanceManagerException	등록 해제가 실패한 경우.
	 */
	public void removeInstanceAll() throws MDTInstanceManagerException;
}
