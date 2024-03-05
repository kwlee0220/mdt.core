package mdt.tool;

import java.nio.file.Path;
import java.nio.file.Paths;

import utils.func.FOption;

import mdt.docker.MDTDocker;
import mdt.harbor.HarborImpl;
import mdt.impl.MDTConfig;
import mdt.impl.MDTInstanceManagerImpl;
import mdt.impl.MDTInstanceStore;
import mdt.model.MDTInstanceManager;
import picocli.CommandLine.Option;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public abstract class MDTCommand extends HomeDirPicocliCommand {
	private static final String ENVVAR_HOME = "MDT_HOME";
	
	@Option(names={"--config"}, paramLabel="path", description={"MDT management configuration file path"})
	protected String m_configPath = "mdt_configs.yaml";
	
	abstract protected void run(MDTConfig configs) throws Exception;
	
	public MDTCommand() {
		super(FOption.of(ENVVAR_HOME));
	}
	
	protected MDTInstanceManager createMDTInstanceManager(MDTConfig config) {
		HarborImpl harbor = HarborImpl.builder(config.getHarborConfig()).build();
		MDTInstanceStore store = new MDTInstanceStore(config.newJdbcProcessor());
		MDTDocker docker = MDTDocker.get(config.getDockerConfig(), config.getHarborConfig());
		
		return new MDTInstanceManagerImpl(store, harbor, docker);
	}
	
	@Override
	protected final void run(Path homeDir) throws Exception {
		Path configPath = Paths.get(m_configPath);
		if ( !configPath.isAbsolute() ) {
			configPath = homeDir.resolve(configPath);
		}
		
		run(MDTConfig.load(configPath));
	}
}
