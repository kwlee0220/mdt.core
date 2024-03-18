package mdt.tool;

import java.io.File;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.UnitUtils;
import utils.func.FOption;

import mdt.impl.MDTConfig;
import mdt.impl.MDTInstanceManagerImpl;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@Command(name = "add", description = "Register an MDT instance.")
public class AddMDTInstanceCommand extends MDTCommand {
	private static final Logger s_logger = LoggerFactory.getLogger(AddMDTInstanceCommand.class);

	@Parameters(index="0", paramLabel="id", description="target MDTInstance id")
	private String m_id;
	
	@Option(names={"--image"}, paramLabel="repo:tag", required=true, description="Docker image name")
	private String m_imageId;
	
	@Option(names={"--aas"}, paramLabel="path", required=true, description="AAS Json file path")
	private File m_aasFile;
	
	@Option(names={"--timeout"}, paramLabel="seconds", required=false, description="registration timeout")
	private String m_timeout = null;

	public AddMDTInstanceCommand() {
		setLogger(s_logger);
	}

	@Override
	public void run(MDTConfig configs) throws Exception {
		MDTInstanceManagerImpl mgr = this.createMDTInstanceManager(configs);
		
		Duration timeout = FOption.ofNullable(m_timeout)
									.map(to -> UnitUtils.parseDuration(to))
									.getOrNull();

		// Harbor에 등록시킬 tag가 부여된 image가 Docker에 존재하는지 확인하여
		// 존재하지 않는다면, tag를 부여한다.
		boolean tagCreated = false;
		String mdtInstanceImageId = mgr.getHarborTaggedImage(m_imageId);
		if ( mdtInstanceImageId == null ) {
			mdtInstanceImageId = mgr.tagImageForHarbor(m_imageId);
			tagCreated = true;
		}
		
		try {
			// MDT Instance image를 등록시킨다.
			mgr.addInstance(m_id, mdtInstanceImageId, m_aasFile, timeout);
		}
		finally {
			if ( tagCreated ) {
				mgr.removeTag(mdtInstanceImageId);
			}
		}
	}

	public static final void main(String... args) throws Exception {
		runCommand(new AddMDTInstanceCommand(), args);
	}
}
