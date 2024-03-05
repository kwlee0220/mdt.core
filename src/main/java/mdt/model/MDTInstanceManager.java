package mdt.model;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;

import utils.func.FOption;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public interface MDTInstanceManager {
	public List<MDTInstance> getInstanceAll() throws MDTInstanceManagerException;

	/**
	 * Docker에 저장된 image를 MDT instace로 등록시킨다.
	 * 
	 * @param dockerTag		대상 docker image를 지정하기 위한 docker tag.
	 * 						Tag는 "<repository>:<tag>" 형식으로 제공되어야 한다.
	 * @param assetShell	등록할 asset의 AAS 정보 파일 경로.
	 * @param force 		등록할 image가 이미 등록된 경우에는 기존의 것을 삭제하고 등록한다.
	 * @param timeout		등록 소요시간 timeout.
	 * 						등록 소요시간이 주어진 시간보다 길어지는 경우에는
	 * 						{@link TimeoutException} 예외가 발생한다.
	 * @return 등록된 MDT Instace에 부여된 식별자.
	 * @throws TimeoutException 등록 소요시간이 초과한 경우.
	 * @throws InterruptedException	등록 과정 중 쓰레드가 중단된 경우.
	 * @throws MDTInstanceManagerException	기타 다른 이유로 MDTInstance 등록에 실패한 경우.
	 */
	public String addInstance(String dockerTag, String assetShell, boolean force,
							FOption<Duration> timeout)
		throws MDTInstanceManagerException, TimeoutException, InterruptedException;
	
	/**
	 * 등록된 MDT Instance을 해제시킨다.
	 * 
	 * @param instanceId	해제시킬 MDT Instance의 식별자.
	 */
	public void removeInstance(String instanceId);
}
